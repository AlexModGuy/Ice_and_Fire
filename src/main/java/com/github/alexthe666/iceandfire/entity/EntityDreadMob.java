package com.github.alexthe666.iceandfire.entity;

import com.google.common.base.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityDreadMob extends EntityMob implements IDreadMob {

    protected static final DataParameter<Optional<UUID>> COMMANDER_UNIQUE_ID = EntityDataManager.createKey(EntityDreadMob.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityDreadMob(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(COMMANDER_UNIQUE_ID, Optional.absent());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.getCommanderId() == null) {
            compound.setString("CommanderUUID", "");
        } else {
            compound.setString("CommanderUUID", this.getCommanderId().toString());
        }

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        String s;
        if (compound.hasKey("CommanderUUID", 8)) {
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
        return (UUID) ((Optional) this.dataManager.get(COMMANDER_UNIQUE_ID)).orNull();
    }

    public void setCommanderId(@Nullable UUID uuid) {
        this.dataManager.set(COMMANDER_UNIQUE_ID, Optional.fromNullable(uuid));
    }

    @Override
    public void onLivingUpdate(){
        super.onLivingUpdate();
        if (!world.isRemote && this.getCommander() instanceof EntityDreadLich) {
            EntityDreadLich lich = (EntityDreadLich) this.getCommander();
            if (lich.getAttackTarget() != null && lich.getAttackTarget().isEntityAlive()) {
                this.setAttackTarget(lich.getAttackTarget());
            }
        }
    }

    @Override
    public Entity getCommander() {
        try {
            UUID uuid = this.getCommanderId();
            EntityLivingBase player = uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
            if (player != null) {
                return player;
            } else {
                if (!world.isRemote) {
                    Entity entity = world.getMinecraftServer().getWorld(this.dimension).getEntityFromUuid(uuid);
                    if (entity instanceof EntityLivingBase) {
                        return (EntityLivingBase) entity;
                    }
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    public void onKillEntity(EntityLivingBase entityLivingIn) {
        Entity commander = this instanceof EntityDreadLich ? this : this.getCommander();
        if(commander != null && !(entityLivingIn instanceof EntityDragonBase)){// zombie dragons!!!!
            Entity summoned = necromancyEntity(entityLivingIn);
            if(summoned != null){
                summoned.copyLocationAndAnglesFrom(entityLivingIn);
                if(!world.isRemote){
                    world.spawnEntity(summoned);
                }
                if(commander instanceof EntityDreadLich){
                    ((EntityDreadLich) commander).setMinionCount(((EntityDreadLich) commander).getMinionCount() + 1);
                }
                if(summoned instanceof EntityDreadMob){
                    ((EntityDreadMob) summoned).setCommanderId(commander.getUniqueID());
                }
            }
        }

    }

    public static Entity necromancyEntity(EntityLivingBase entity) {
        Entity lichSummoned = null;
        if(entity.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD){
            lichSummoned = new EntityDreadScuttler(entity.world);
            float readInScale = (entity.width / 1.5F);
            ((EntityDreadScuttler)lichSummoned).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
            ((EntityDreadScuttler)lichSummoned).setScale(readInScale);
            return lichSummoned;
        }
        if(entity instanceof EntityZombie || entity instanceof IHumanoid) {
            lichSummoned = new EntityDreadGhoul(entity.world);
            float readInScale = (entity.width / 0.6F);
            ((EntityDreadGhoul)lichSummoned).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
            ((EntityDreadGhoul)lichSummoned).setScale(readInScale);
            return lichSummoned;
        }
        if(entity.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD || entity instanceof AbstractSkeleton || entity instanceof EntityPlayer) {
            lichSummoned = new EntityDreadThrall(entity.world);
            EntityDreadThrall thrall = (EntityDreadThrall)lichSummoned;
            thrall.onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
            thrall.setCustomArmorHead(false);
            thrall.setCustomArmorChest(false);
            thrall.setCustomArmorLegs(false);
            thrall.setCustomArmorFeet(false);
            for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
                thrall.setItemStackToSlot(slot, entity.getItemStackFromSlot(slot));
            }
            return thrall;
        }
        if(entity instanceof AbstractHorse) {
            lichSummoned = new EntityDreadHorse(entity.world);
            return lichSummoned;
        }
        if(entity instanceof EntityAnimal) {
            lichSummoned = new EntityDreadBeast(entity.world);
            float readInScale = (entity.width / 1.2F);
            ((EntityDreadBeast)lichSummoned).onInitialSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), null);
            ((EntityDreadBeast)lichSummoned).setScale(readInScale);
            return lichSummoned;
        }
        return lichSummoned;
    }


    public void setDead() {
        if (!isDead && this.getCommander() != null && this.getCommander() instanceof EntityDreadLich) {
            EntityDreadLich lich = (EntityDreadLich) this.getCommander();
            lich.setMinionCount(lich.getMinionCount() - 1);
        }
        this.isDead = true;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }
}
