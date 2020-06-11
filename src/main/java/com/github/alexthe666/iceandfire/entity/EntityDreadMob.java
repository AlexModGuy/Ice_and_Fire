package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import com.github.alexthe666.iceandfire.entity.util.IHumanoid;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityDreadMob extends MonsterEntity implements IDreadMob {

    protected static final DataParameter<java.util.Optional<UUID>> COMMANDER_UNIQUE_ID = EntityDataManager.createKey(EntityDreadMob.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityDreadMob(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    public static Entity necromancyEntity(LivingEntity entity) {
        Entity lichSummoned = null;
        if (entity.getCreatureAttribute() == CreatureAttribute.ARTHROPOD) {
            lichSummoned = new EntityDreadScuttler(IafEntityRegistry.DREAD_SCUTTLER, entity.world);
            float readInScale = (entity.getWidth() / 1.5F);
            ((EntityDreadScuttler) lichSummoned).onInitialSpawn(entity.world, entity.world.getDifficultyForLocation(new BlockPos(entity)), SpawnReason.MOB_SUMMONED, null, null);
            ((EntityDreadScuttler) lichSummoned).setScale(readInScale);
            return lichSummoned;
        }
        if (entity instanceof ZombieEntity || entity instanceof IHumanoid) {
            lichSummoned = new EntityDreadGhoul(IafEntityRegistry.DREAD_GHOUL, entity.world);
            float readInScale = (entity.getWidth() / 0.6F);
            ((EntityDreadGhoul) lichSummoned).onInitialSpawn(entity.world, entity.world.getDifficultyForLocation(new BlockPos(entity)), SpawnReason.MOB_SUMMONED, null, null);
            ((EntityDreadGhoul) lichSummoned).setScale(readInScale);
            return lichSummoned;
        }
        if (entity.getCreatureAttribute() == CreatureAttribute.UNDEAD || entity instanceof AbstractSkeletonEntity || entity instanceof PlayerEntity) {
            lichSummoned = new EntityDreadThrall(IafEntityRegistry.DREAD_THRALL, entity.world);
            EntityDreadThrall thrall = (EntityDreadThrall) lichSummoned;
            thrall.onInitialSpawn(entity.world, entity.world.getDifficultyForLocation(new BlockPos(entity)), SpawnReason.MOB_SUMMONED, null, null);
            thrall.setCustomArmorHead(false);
            thrall.setCustomArmorChest(false);
            thrall.setCustomArmorLegs(false);
            thrall.setCustomArmorFeet(false);
            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                thrall.setItemStackToSlot(slot, entity.getItemStackFromSlot(slot));
            }
            return thrall;
        }
        if (entity instanceof AbstractHorseEntity) {
            lichSummoned = new EntityDreadHorse(IafEntityRegistry.DREAD_HORSE, entity.world);
            return lichSummoned;
        }
        if (entity instanceof AnimalEntity) {
            lichSummoned = new EntityDreadBeast(IafEntityRegistry.DREAD_BEAST, entity.world);
            float readInScale = (entity.getWidth() / 1.2F);
            ((EntityDreadBeast) lichSummoned).onInitialSpawn(entity.world, entity.world.getDifficultyForLocation(new BlockPos(entity)), SpawnReason.MOB_SUMMONED, null, null);
            ((EntityDreadBeast) lichSummoned).setScale(readInScale);
            return lichSummoned;
        }
        return lichSummoned;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(COMMANDER_UNIQUE_ID, null);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.getCommanderId() == null) {
            compound.putString("CommanderUUID", "");
        } else {
            compound.putString("CommanderUUID", this.getCommanderId().toString());
        }

    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        String s;
        if (compound.contains("CommanderUUID", 8)) {
            s = compound.getString("CommanderUUID");
        } else {
            String s1 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s1);
        }
        if (!s.isEmpty()) {
            try {
                this.setCommanderId(UUID.fromString(s));
            } catch (Throwable var4) {
            }
        }
    }

    @Override
    public boolean isOnSameTeam(Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isOnSameTeam(entityIn);
    }

    @Nullable
    public UUID getCommanderId() {
        return this.dataManager.get(COMMANDER_UNIQUE_ID).orElse(null);
    }

    public void setCommanderId(@Nullable UUID uuid) {
        this.dataManager.set(COMMANDER_UNIQUE_ID, Optional.of(uuid));
    }

    @Override
    public void livingTick() {
        super.livingTick();
        if (!world.isRemote && this.getCommander() instanceof EntityDreadLich) {
            EntityDreadLich lich = (EntityDreadLich) this.getCommander();
            if (lich.getAttackTarget() != null && lich.getAttackTarget().isAlive()) {
                this.setAttackTarget(lich.getAttackTarget());
            }
        }
    }

    @Override
    public Entity getCommander() {
        try {
            UUID uuid = this.getCommanderId();
            LivingEntity player = uuid == null ? null : this.world.getPlayerByUuid(uuid);
            if (player != null) {
                return player;
            } else {
                if (!world.isRemote) {
                    Entity entity = world.getServer().getWorld(this.dimension).getEntityByUuid(uuid);
                    if (entity instanceof LivingEntity) {
                        return entity;
                    }
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    public void onKillEntity(LivingEntity LivingEntityIn) {
        Entity commander = this instanceof EntityDreadLich ? this : this.getCommander();
        if (commander != null && !(LivingEntityIn instanceof EntityDragonBase)) {// zombie dragons!!!!
            Entity summoned = necromancyEntity(LivingEntityIn);
            if (summoned != null) {
                summoned.copyLocationAndAnglesFrom(LivingEntityIn);
                if (!world.isRemote) {
                    world.addEntity(summoned);
                }
                if (commander instanceof EntityDreadLich) {
                    ((EntityDreadLich) commander).setMinionCount(((EntityDreadLich) commander).getMinionCount() + 1);
                }
                if (summoned instanceof EntityDreadMob) {
                    ((EntityDreadMob) summoned).setCommanderId(commander.getUniqueID());
                }
            }
        }

    }

    public void remove() {
        if (!removed && this.getCommander() != null && this.getCommander() instanceof EntityDreadLich) {
            EntityDreadLich lich = (EntityDreadLich) this.getCommander();
            lich.setMinionCount(lich.getMinionCount() - 1);
        }
        super.remove();
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }
}
