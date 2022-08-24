package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.google.common.collect.Lists;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class DreadSpawnerBaseLogic extends AbstractSpawner {

    private final List<WeightedSpawnerEntity> potentialSpawns = Lists.newArrayList();
    private short spawnDelay = 20;
    private WeightedSpawnerEntity spawnData = new WeightedSpawnerEntity();
    private double mobRotation;
    private double prevMobRotation;
    private short minSpawnDelay = 200;
    private short maxSpawnDelay = 800;
    private short spawnCount = 4;
    private Entity cachedEntity;
    private short maxNearbyEntities = 6;
    private short activatingRangeFromPlayer = 16;
    private short spawnRange = 4;

    @Nullable
    private ResourceLocation getEntityId() {
        String s = this.spawnData.getTag().getString("id");
        return StringUtils.isNullOrEmpty(s) ? null : new ResourceLocation(s);
    }

    public void setEntityId(@Nullable ResourceLocation id) {
        if (id != null) {
            this.spawnData.getTag().putString("id", id.toString());
        }
    }

    /**
     * Returns true if there's a player close enough to this mob spawner to activate it.
     */
    private boolean isActivated() {
        BlockPos blockpos = this.getPos();
        return this.getLevel().hasNearbyAlivePlayer(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D,
            this.activatingRangeFromPlayer);
    }

    public void updateSpawner() {
        if (!this.isActivated()) {
            this.prevMobRotation = this.mobRotation;
        } else {
            World world = this.getLevel();
            BlockPos blockpos = this.getPos();

            if (this.getLevel().isClientSide) {
                double d3 = blockpos.getX() + this.getLevel().random.nextFloat();
                double d4 = blockpos.getY() + this.getLevel().random.nextFloat();
                double d5 = blockpos.getZ() + this.getLevel().random.nextFloat();
                this.getLevel().addParticle(ParticleTypes.SMOKE, d3, d4, d5, 0.0D, 0.0D, 0.0D);
                IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, d3, d4, d5, 0.0D, 0.0D, 0.0D);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }

                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + 1000.0F / (this.spawnDelay + 200.0F)) % 360.0D;
            } else {
                if (this.spawnDelay == -1) {
                    this.resetTimer();
                }

                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.spawnCount; ++i) {
                    CompoundNBT compoundnbt = this.spawnData.getTag();
                    Optional<EntityType<?>> optional = EntityType.by(compoundnbt);
                    if (!optional.isPresent()) {
                        this.resetTimer();
                        return;
                    }

                    ListNBT listnbt = compoundnbt.getList("Pos", 6);
                    final int j = listnbt.size();
                    final double d0 = j >= 1 ? listnbt.getDouble(0) : blockpos.getX() + (world.random.nextDouble() - world.random.nextDouble()) * this.spawnRange + 0.5D;
                    final double d1 = j >= 2 ? listnbt.getDouble(1) : (double) (blockpos.getY() + world.random.nextInt(3) - 1);
                    final double d2 = j >= 3 ? listnbt.getDouble(2) : blockpos.getZ() + (world.random.nextDouble() - world.random.nextDouble()) * this.spawnRange + 0.5D;
                    if (world.noCollision(optional.get().getAABB(d0, d1, d2)) && EntitySpawnPlacementRegistry.checkSpawnRules(optional.get(), (IServerWorld) world, SpawnReason.SPAWNER, new BlockPos(d0, d1, d2), world.getRandom())) {
                        ServerWorld serverworld = (ServerWorld) world;
                        Entity entity = EntityType.loadEntityRecursive(compoundnbt, world, (p_221408_6_) -> {
                            p_221408_6_.moveTo(d0, d1, d2, p_221408_6_.yRot, p_221408_6_.xRot);
                            return p_221408_6_;
                        });
                        if (entity == null) {
                            this.resetTimer();
                            return;
                        }

                        final int k = world.getEntitiesOfClass(entity.getClass(), (new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() + 1, blockpos.getY() + 1, blockpos.getZ() + 1)).inflate(this.spawnRange)).size();
                        if (k >= this.maxNearbyEntities) {
                            this.resetTimer();
                            return;
                        }

                        entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), world.random.nextFloat() * 360.0F, 0.0F);
                        if (entity instanceof MobEntity) {
                            MobEntity mobentity = (MobEntity) entity;
                            if (!net.minecraftforge.event.ForgeEventFactory.canEntitySpawnSpawner(mobentity, world, (float) entity.getX(), (float) entity.getY(), (float) entity.getZ(), this)) {
                                continue;
                            }

                            if (this.spawnData.getTag().size() == 1 && this.spawnData.getTag().contains("id", 8)) {
                                mobentity.finalizeSpawn(serverworld,
                                    world.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.SPAWNER, null,
                                    null);
                            }

                            this.spawnMobs(entity);
                            world.levelEvent(2004, blockpos, 0);
                            mobentity.spawnAnim();
                        }

                        flag = true;
                    }
                }

                if (flag) {
                    this.resetTimer();
                }
            }
        }
    }

    private void spawnMobs(Entity entityIn) {
        if (this.getLevel().addFreshEntity(entityIn)) {
            for (Entity entity : entityIn.getPassengers()) {
                this.spawnMobs(entity);
            }

        }
    }

    private void resetTimer() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = (short) (this.minSpawnDelay + getLevel().random.nextInt(i));
        }

        if (!this.potentialSpawns.isEmpty()) {
            this.setNextSpawnData(spawnData);
        }

        this.broadcastEvent(1);
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.spawnDelay = nbt.getShort("Delay");
        this.potentialSpawns.clear();
        if (nbt.contains("SpawnPotentials", 9)) {
            ListNBT listnbt = nbt.getList("SpawnPotentials", 10);

            for (int i = 0; i < listnbt.size(); ++i) {
                this.potentialSpawns.add(new WeightedSpawnerEntity(listnbt.getCompound(i)));
            }
        }

        if (nbt.contains("SpawnData", 10)) {
            this.setNextSpawnData(new WeightedSpawnerEntity(1, nbt.getCompound("SpawnData")));
        } else if (!this.potentialSpawns.isEmpty()) {
            this.setNextSpawnData(WeightedRandom.getRandomItem(this.getLevel().random, this.potentialSpawns));
        }

        if (nbt.contains("MinSpawnDelay", 99)) {
            this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
            this.spawnCount = nbt.getShort("SpawnCount");
        }

        if (nbt.contains("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
        }

        if (nbt.contains("SpawnRange", 99)) {
            this.spawnRange = nbt.getShort("SpawnRange");
        }

        if (this.getLevel() != null) {
            this.cachedEntity = null;
        }

    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        ResourceLocation resourcelocation = this.getEntityId();
        if (resourcelocation == null) {
            return compound;
        } else {
            compound.putShort("Delay", this.spawnDelay);
            compound.putShort("MinSpawnDelay", this.minSpawnDelay);
            compound.putShort("MaxSpawnDelay", this.maxSpawnDelay);
            compound.putShort("SpawnCount", this.spawnCount);
            compound.putShort("MaxNearbyEntities", this.maxNearbyEntities);
            compound.putShort("RequiredPlayerRange", this.activatingRangeFromPlayer);
            compound.putShort("SpawnRange", this.spawnRange);
            compound.put("SpawnData", this.spawnData.getTag().copy());
            ListNBT listnbt = new ListNBT();
            if (this.potentialSpawns.isEmpty()) {
                listnbt.add(this.spawnData.save());
            } else {
                for (WeightedSpawnerEntity weightedspawnerentity : this.potentialSpawns) {
                    listnbt.add(weightedspawnerentity.save());
                }
            }

            compound.put("SpawnPotentials", listnbt);
            return compound;
        }
    }

    /**
     * Sets the delay to minDelay if parameter given is 1, else return false.
     */
    @Override
    public boolean onEventTriggered(int delay) {
        if (delay == 1 && this.getLevel().isClientSide) {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Entity getOrCreateDisplayEntity() {
        if (this.cachedEntity == null) {
            this.cachedEntity = EntityType.loadEntityRecursive(this.spawnData.getTag(), this.getLevel(), Function.identity());
            if (this.spawnData.getTag().size() == 1 && this.spawnData.getTag().contains("id", 8) && this.cachedEntity instanceof MobEntity) {
            }
        }

        return this.cachedEntity;
    }

    @Override
    public void setNextSpawnData(WeightedSpawnerEntity nextSpawnData) {
        this.spawnData = nextSpawnData;
    }

    @Override
    public abstract void broadcastEvent(int id);

    @Override
    public abstract BlockPos getPos();

    @Override
    public double getSpin() {
        return this.mobRotation;
    }

    @Override
    public double getoSpin() {
        return this.prevMobRotation;
    }

}
