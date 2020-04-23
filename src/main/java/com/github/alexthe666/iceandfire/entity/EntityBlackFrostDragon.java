package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityBlackFrostDragon extends EntityIceDragon implements IDreadMob {

    protected static final DataParameter<Optional<UUID>> COMMANDER_UNIQUE_ID = EntityDataManager.createKey(EntityBlackFrostDragon.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    public EntityBlackFrostDragon(World worldIn) {
        super(worldIn);
        this.maximumArmor = 70D;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(COMMANDER_UNIQUE_ID, Optional.absent());
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        EntityDreadQueen queen = this.getRidingQueen();
        if(queen != null && queen.getAttackTarget() != null){
            this.setAttackTarget(queen.getAttackTarget());
        }
    }
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new DreadAIDragonFindQueen(this));
        this.tasks.addTask(1, this.aiSit = new EntityAISit(this));
        this.tasks.addTask(2, new DragonAIEscort(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.5D, false));
        this.tasks.addTask(4, new AquaticAITempt(this, 1.0D, IafItemRegistry.frost_stew, false));
        this.tasks.addTask(6, new DragonAIWander(this, 1.0D));
        this.tasks.addTask(7, new DragonAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(7, new DragonAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(4, new DreadAITargetNonDread(this, EntityLivingBase.class, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof EntityLivingBase && DragonUtils.canHostilesTarget(entity);
            }
        }));
        this.targetTasks.addTask(5, new DragonAITargetItems(this, false));
    }

    @Nullable
    public Entity getControllingPassenger() {
        Entity commander = getCommander();
        if (commander != null) {
            for (Entity passenger : this.getPassengers()) {
                if (passenger.getUniqueID().equals(commander.getUniqueID())) {
                    return passenger;
                }
            }
        }
        return super.getControllingPassenger();
    }

    @Override
    public boolean canBeSteered() {
        return false;
    }

    @Override
    public boolean isAllowedToTriggerFlight() {
        return this.hasFlightClearance() && !this.isSitting() && !this.isChild() && !this.isSleeping() && this.canMove() && this.onGround;
    }

    public boolean canMove() {
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        if (properties != null && properties.isStone) {
            return false;
        }
        return !this.isSitting() && !this.isSleeping() && !this.isModelDead() && sleepProgress == 0 && this.getAnimation() != ANIMATION_SHAKEPREY;
    }


    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            passenger.setPosition(this.posX, this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ);
        }
        if (this.isPassenger(passenger)) {
            if (!(passenger instanceof EntityDreadQueen) && (this.getControllingPassenger() == null || !this.getControllingPassenger().getUniqueID().equals(passenger.getUniqueID()))) {
                updatePreyInMouth(passenger);
            } else {
                if (this.isModelDead()) {
                    passenger.dismountRidingEntity();
                }
                if(passenger instanceof EntityDreadQueen){
                    passenger.rotationYaw = this.rotationYaw;
                    ((EntityDreadQueen)passenger).renderYawOffset = rotationYaw;
                }else{
                    renderYawOffset = rotationYaw;
                    this.rotationYaw = passenger.rotationYaw;
                }

                Vec3d riderPos = this.getRiderPosition();
                passenger.setPosition(riderPos.x, riderPos.y + passenger.height, riderPos.z);
                this.stepHeight = 1;
            }
        }
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

    public boolean shouldRiderSit() {
        return this.getControllingPassenger() != null || getRidingQueen() != null;
    }

    public EntityDreadQueen getRidingQueen(){
        for(Entity passenger : this.getPassengers()){
            if(passenger instanceof EntityDreadQueen){
                return (EntityDreadQueen) passenger;
            }
        }
        return null;
    }
    @Nullable
    public UUID getCommanderId() {
        return (UUID) ((Optional) this.dataManager.get(COMMANDER_UNIQUE_ID)).orNull();
    }

    public void setCommanderId(@Nullable UUID uuid) {
        this.dataManager.set(COMMANDER_UNIQUE_ID, Optional.fromNullable(uuid));
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
                        return entity;
                    }
                }
            }
        } catch (IllegalArgumentException var2) {
            return null;
        }
        return null;
    }

    @Override
    public String getVariantName(int variant) {
        return "blue_";
    }

    public Item getVariantScale(int variant) {
        return IafItemRegistry.dragonscales_blue;
    }

    public Item getVariantEgg(int variant) {
        return IafItemRegistry.dragonegg_blue;
    }

    public boolean isBreedingItem(@Nullable ItemStack stack) {
        return false;
    }

    public int getDragonStage() {
        return 5;
    }

    public int getAgeInDays() {
        return 125;
    }

    public int getArmorOrdinal(ItemStack stack) {
        return 0;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public boolean isMale() {
        return false;
    }

    public boolean isDaytime() {
        return true;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setGender(this.getRNG().nextBoolean());
        this.setSleeping(false);
        this.updateAttributes();
        this.growDragon(125);
        this.heal((float) maximumHealth);
        this.usingGroundAttack = true;
        this.setHunger(50);
        return livingdata;
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    protected int getFlightChancePerTick(){
        return 15;
    }
}
