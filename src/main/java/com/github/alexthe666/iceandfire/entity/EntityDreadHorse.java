package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IDreadMob;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class EntityDreadHorse extends SkeletonHorseEntity implements IDreadMob {

    protected static final DataParameter<Optional<UUID>> COMMANDER_UNIQUE_ID = EntityDataManager.createKey(EntityDreadHorse.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityDreadHorse(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
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

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData data = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setGrowingAge(24000);
        return data;
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
        this.dataManager.set(COMMANDER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    @Override
    public Entity getCommander() {
        try {
            UUID uuid = this.getCommanderId();
            return uuid == null ? null : this.world.getPlayerByUuid(uuid);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }
}
