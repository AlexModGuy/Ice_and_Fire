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
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityDreadMob extends MonsterEntity implements IDreadMob {
    protected static final DataParameter<Optional<UUID>> COMMANDER_UNIQUE_ID = EntityDataManager.defineId(EntityDreadMob.class, DataSerializers.OPTIONAL_UUID);

    public EntityDreadMob(EntityType<? extends MonsterEntity> t, World worldIn) {
        super(t, worldIn);
    }

    public static Entity necromancyEntity(LivingEntity entity) {
        Entity lichSummoned = null;
        if (entity.getMobType() == CreatureAttribute.ARTHROPOD) {
            lichSummoned = new EntityDreadScuttler(IafEntityRegistry.DREAD_SCUTTLER.get(), entity.level);
            float readInScale = (entity.getBbWidth() / 1.5F);
            if (entity.level instanceof IServerWorld) {
                ((EntityDreadScuttler) lichSummoned).finalizeSpawn((IServerWorld) entity.level, entity.level.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
            }
            ((EntityDreadScuttler) lichSummoned).setSize(readInScale);
            return lichSummoned;
        }
        if (entity instanceof ZombieEntity || entity instanceof IHumanoid) {
            lichSummoned = new EntityDreadGhoul(IafEntityRegistry.DREAD_GHOUL.get(), entity.level);
            float readInScale = (entity.getBbWidth() / 0.6F);
            if (entity.level instanceof IServerWorld) {
                ((EntityDreadGhoul) lichSummoned).finalizeSpawn((IServerWorld) entity.level, entity.level.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
            }
            ((EntityDreadGhoul) lichSummoned).setSize(readInScale);
            return lichSummoned;
        }
        if (entity.getMobType() == CreatureAttribute.UNDEAD || entity instanceof AbstractSkeletonEntity || entity instanceof PlayerEntity) {
            lichSummoned = new EntityDreadThrall(IafEntityRegistry.DREAD_THRALL.get(), entity.level);
            EntityDreadThrall thrall = (EntityDreadThrall) lichSummoned;
            if (entity.level instanceof IServerWorld) {
                thrall.finalizeSpawn((IServerWorld) entity.level, entity.level.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
            }
            thrall.setCustomArmorHead(false);
            thrall.setCustomArmorChest(false);
            thrall.setCustomArmorLegs(false);
            thrall.setCustomArmorFeet(false);
            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                thrall.setItemSlot(slot, entity.getItemBySlot(slot));
            }
            return thrall;
        }
        if (entity instanceof AbstractHorseEntity) {
            lichSummoned = new EntityDreadHorse(IafEntityRegistry.DREAD_HORSE.get(), entity.level);
            return lichSummoned;
        }
        if (entity instanceof AnimalEntity) {
            lichSummoned = new EntityDreadBeast(IafEntityRegistry.DREAD_BEAST.get(), entity.level);
            float readInScale = (entity.getBbWidth() / 1.2F);
            if (entity.level instanceof IServerWorld) {
                ((EntityDreadBeast) lichSummoned).finalizeSpawn((IServerWorld) entity.level, entity.level.getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
            }
            ((EntityDreadBeast) lichSummoned).setSize(readInScale);
            return lichSummoned;
        }
        return lichSummoned;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COMMANDER_UNIQUE_ID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.getCommanderId() != null) {
            compound.putUUID("CommanderUUID", this.getCommanderId());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("CommanderUUID")) {
            uuid = compound.getUUID("CommanderUUID");
        } else {
            String s = compound.getString("CommanderUUID");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setCommanderId(uuid);
            } catch (Throwable throwable) {
            }
        }

    }


    @Override
    public boolean isAlliedTo(Entity entityIn) {
        return entityIn instanceof IDreadMob || super.isAlliedTo(entityIn);
    }

    @Nullable
    public UUID getCommanderId() {
        return (UUID) ((Optional) this.entityData.get(COMMANDER_UNIQUE_ID)).orElse(null);
    }

    public void setCommanderId(@Nullable UUID uuid) {
        this.entityData.set(COMMANDER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide && this.getCommander() instanceof EntityDreadLich) {
            EntityDreadLich lich = (EntityDreadLich) this.getCommander();
            if (lich.getTarget() != null && lich.getTarget().isAlive()) {
                this.setTarget(lich.getTarget());
            }
        }
    }

    @Override
    public Entity getCommander() {
        try {
            UUID uuid = this.getCommanderId();
            LivingEntity player = uuid == null ? null : this.level.getPlayerByUUID(uuid);
            if (player != null) {
                return player;
            } else {
                if (!level.isClientSide) {
                    Entity entity = level.getServer().getLevel(this.level.dimension()).getEntity(uuid);
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
                summoned.copyPosition(LivingEntityIn);
                if (!level.isClientSide) {
                    level.addFreshEntity(summoned);
                }
                if (commander instanceof EntityDreadLich) {
                    ((EntityDreadLich) commander).setMinionCount(((EntityDreadLich) commander).getMinionCount() + 1);
                }
                if (summoned instanceof EntityDreadMob) {
                    ((EntityDreadMob) summoned).setCommanderId(commander.getUUID());
                }
            }
        }

    }

    @Override
    public void remove() {
        if (!removed && this.getCommander() != null && this.getCommander() instanceof EntityDreadLich) {
            EntityDreadLich lich = (EntityDreadLich) this.getCommander();
            lich.setMinionCount(lich.getMinionCount() - 1);
        }
        super.remove();
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }
}
