package com.github.alexthe666.iceandfire.entity.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockBreakExplosion extends Explosion {
    private final Level world;
    private final double x;
    private final double y;
    private final double z;
    private final Mob exploder;
    private final float size;
    private final List<BlockPos> affectedBlockPositions;
    private final Map<Player, Vec3> playerKnockbackMap;
    private final Vec3 position;
    private BlockInteraction mode;

    public BlockBreakExplosion(Level world, Mob entity, double x, double y, double z, float size) {
        super(world, entity, null, null, x, y, z, size, false, BlockInteraction.DESTROY);
        this.affectedBlockPositions = Lists.newArrayList();
        this.playerKnockbackMap = Maps.newHashMap();
        this.world = world;
        this.exploder = entity;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.position = new Vec3(x, y, z);
    }

    private static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos) {
        int i = dropPositionArray.size();

        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = dropPositionArray.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.areMergable(itemstack, stack)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, stack, 16);
                dropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        dropPositionArray.add(Pair.of(stack, pos));
    }

    @Override
    public void explode() {
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (float) j / 15.0F * 2.0F - 1.0F;
                        double d1 = (float) k / 15.0F * 2.0F - 1.0F;
                        double d2 = (float) l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.size * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            BlockState blockstate = this.world.getBlockState(blockpos);
                            FluidState ifluidstate = this.world.getFluidState(blockpos);
                            if (!blockstate.isAir() || !ifluidstate.isEmpty()) {
                                float f2 = Math.max(blockstate.getExplosionResistance(this.world, blockpos, this), ifluidstate.getExplosionResistance(this.world, blockpos, this));
                                if (this.exploder != null) {
                                    f2 = this.exploder.getBlockExplosionResistance(this, this.world, blockpos, blockstate, ifluidstate, f2);
                                }

                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && (this.exploder == null || this.exploder.shouldBlockExplode(this, this.world, blockpos, blockstate, f))) {
                                set.add(blockpos);
                            }

                            d4 += d0 * (double) 0.3F;
                            d6 += d1 * (double) 0.3F;
                            d8 += d2 * (double) 0.3F;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(set);
        float f3 = this.size * 2.0F;
        int k1 = Mth.floor(this.x - (double) f3 - 1.0D);
        int l1 = Mth.floor(this.x + (double) f3 + 1.0D);
        int i2 = Mth.floor(this.y - (double) f3 - 1.0D);
        int i1 = Mth.floor(this.y + (double) f3 + 1.0D);
        int j2 = Mth.floor(this.z - (double) f3 - 1.0D);
        int j1 = Mth.floor(this.z + (double) f3 + 1.0D);
        List<Entity> list = this.world.getEntities(this.exploder, new AABB(k1, i2, j2, l1, i1, j1));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, list, f3);
        Vec3 Vector3d = new Vec3(this.x, this.y, this.z);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);
            if (!entity.ignoreExplosion() && !(entity instanceof ItemEntity)) {
                double d12 = Math.sqrt(entity.distanceToSqr(Vector3d)) / f3;
                if (d12 <= 1.0D) {
                    double d5 = entity.getX() - this.x;
                    double d7 = entity.getEyeY() - this.y;
                    double d9 = entity.getZ() - this.z;
                    double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = getSeenPercent(Vector3d, entity);
                        double d10 = (1.0D - d12) * d14;
                        entity.hurt(this.getDamageSource(), (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        double d11 = d10;
                        if (entity instanceof LivingEntity) {
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, d10);
                        }

                        entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                        if (entity instanceof Player) {
                            Player playerentity = (Player) entity;
                            if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.getAbilities().flying)) {
                                this.playerKnockbackMap.put(playerentity, new Vec3(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    @Override
    public void finalizeExplosion(boolean spawnParticles) {
        if (this.world.isClientSide) {
            this.world.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean flag = this.mode != Explosion.BlockInteraction.NONE;
        if (spawnParticles) {
            if (!(this.size < 2.0F) && flag) {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            }
        }

        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            Collections.shuffle(this.affectedBlockPositions, this.world.random);

            for (BlockPos blockpos : this.affectedBlockPositions) {
                BlockState blockstate = this.world.getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (!blockstate.isAir()) {
                    BlockPos blockpos1 = blockpos.immutable();
                    this.world.getProfiler().push("explosion_blocks");
                    if (blockstate.canDropFromExplosion(this.world, blockpos, this) && this.world instanceof ServerLevel) {
                        BlockEntity tileentity = blockstate.hasBlockEntity() ? this.world.getBlockEntity(blockpos) : null;
                        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.world)).withRandom(this.world.random).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, tileentity).withOptionalParameter(LootContextParams.THIS_ENTITY, this.exploder);
                        if (this.mode == Explosion.BlockInteraction.DESTROY) {
                            lootcontext$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.size);
                        }

                        blockstate.getDrops(lootcontext$builder).forEach((stack) -> {
                            handleExplosionDrops(objectarraylist, stack, blockpos1);
                        });
                    }

                    blockstate.onBlockExploded(this.world, blockpos, this);
                    this.world.getProfiler().pop();
                }
            }

            for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                Block.popResource(this.world, pair.getSecond(), pair.getFirst());
            }
        }
    }

    @Override
    public @NotNull Map<Player, Vec3> getHitPlayers() {
        return this.playerKnockbackMap;
    }

    @Override
    public LivingEntity getSourceMob() {
        return exploder;
    }

    @Override
    public void clearToBlow() {
        this.affectedBlockPositions.clear();
    }

    @Override
    public @NotNull List<BlockPos> getToBlow() {
        return this.affectedBlockPositions;
    }

    @Override
    public @NotNull Vec3 getPosition() {
        return this.position;
    }
}