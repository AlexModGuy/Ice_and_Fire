package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.DragonFireEvent;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.message.MessageDragonSyncFire;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityIceDragon extends EntityDragonBase {

    public static final float[] growth_stage_1 = new float[]{1F, 3F};
    public static final float[] growth_stage_2 = new float[]{3F, 7F};
    public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
    public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
    public static final float[] growth_stage_5 = new float[]{20F, 30F};
    public static final ResourceLocation FEMALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_female"));
    public static final ResourceLocation MALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_male"));
    public static final ResourceLocation SKELETON_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/ice_dragon_skeleton"));
    private static final DataParameter<Boolean> SWIMMING = EntityDataManager.createKey(EntityIceDragon.class, DataSerializers.BOOLEAN);
    public static Animation ANIMATION_FIRECHARGE;
    public boolean isSwimming;
    public float swimProgress;
    public int ticksSwiming;
    public int swimCycle;

    public EntityIceDragon(World worldIn) {
        super(worldIn, DragonType.ICE, 1, 1 + IceAndFire.CONFIG.dragonAttackDamage, IceAndFire.CONFIG.dragonHealth * 0.04, IceAndFire.CONFIG.dragonHealth, 0.15F, 0.4F);
        this.setSize(0.78F, 1.2F);
        ANIMATION_SPEAK = Animation.create(20);
        ANIMATION_BITE = Animation.create(35);
        ANIMATION_SHAKEPREY = Animation.create(65);
        ANIMATION_TAILWHACK = Animation.create(40);
        ANIMATION_FIRECHARGE = Animation.create(25);
        ANIMATION_WINGBLAST = Animation.create(50);
        ANIMATION_ROAR = Animation.create(40);
        ANIMATION_EPIC_ROAR = Animation.create(60);
        this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SWIMMING, Boolean.valueOf(false));
    }

    public String getVariantName(int variant) {
        switch (variant) {
            default:
                return "blue_";
            case 1:
                return "white_";
            case 2:
                return "sapphire_";
            case 3:
                return "silver_";
        }
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public Item getVariantScale(int variant) {
        switch (variant) {
            default:
                return IafItemRegistry.dragonscales_blue;
            case 1:
                return IafItemRegistry.dragonscales_white;
            case 2:
                return IafItemRegistry.dragonscales_sapphire;
            case 3:
                return IafItemRegistry.dragonscales_silver;
        }
    }

    public Item getVariantEgg(int variant) {
        switch (variant) {
            default:
                return IafItemRegistry.dragonegg_blue;
            case 1:
                return IafItemRegistry.dragonegg_white;
            case 2:
                return IafItemRegistry.dragonegg_sapphire;
            case 3:
                return IafItemRegistry.dragonegg_silver;
        }
    }

    public boolean isPushedByWater() {
        return false;
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Swimming", this.isSwimming());
        compound.setInteger("SwimmingTicks", this.ticksSwiming);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setSwimming(compound.getBoolean("Swimming"));
        this.ticksSwiming = compound.getInteger("SwimmingTicks");
    }

    public boolean canBeSteered() {
        return true;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        this.getLookHelper().setLookPositionWithEntity(entityIn, 30.0F, 30.0F);
        if(!this.isPlayingAttackAnimation()){
            switch (groundAttack) {
                case BITE:
                    this.setAnimation(ANIMATION_BITE);
                    break;
                case TAIL_WHIP:
                    this.setAnimation(ANIMATION_TAILWHACK);
                    break;
                case SHAKE_PREY:
                    boolean flag = false;
                    if (new Random().nextInt(2) == 0 && isDirectPathBetweenPoints(this, this.getPositionVector().add(0, this.height/2, 0), entityIn.getPositionVector().add(0, entityIn.height/2, 0)) &&
                            entityIn.width < this.width * 0.5F && this.getControllingPassenger() == null && this.getDragonStage() > 1 && !(entityIn instanceof EntityDragonBase) && !DragonUtils.isAnimaniaMob(entityIn)) {
                        this.setAnimation(ANIMATION_SHAKEPREY);
                        flag = true;
                        entityIn.startRiding(this);
                    }
                    if(!flag){
                        groundAttack = IafDragonAttacks.Ground.BITE;
                        this.setAnimation(ANIMATION_BITE);
                    }
                    break;
                case WING_BLAST:
                    this.setAnimation(ANIMATION_WINGBLAST);
                    break;
            }
        }
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!isFlying() && !isHovering() && isSwimming() && !world.isRemote) {
            this.flightManager.update();
        }
        if (!world.isRemote && this.isInLava() && this.isAllowedToTriggerFlight() && !this.isModelDead()) {
            this.setHovering(true);
            this.setSleeping(false);
            this.setSitting(false);
            this.flyHovering = 0;
            this.flyTicks = 0;
        }
        if (!world.isRemote && this.getAttackTarget() != null) {
            float growSize = this.isInMaterialWater() ? 1.0F : 0.5F;
            if (this.getEntityBoundingBox().grow(2.5F + this.getRenderSize() * 0.33F, 2.5F + this.getRenderSize() * 0.33F, 2.5F + this.getRenderSize() * 0.33F).intersects(this.getAttackTarget().getEntityBoundingBox())) {
                attackEntityAsMob(this.getAttackTarget());
            }
            if (this.groundAttack == IafDragonAttacks.Ground.FIRE && (usingGroundAttack || this.onGround)) {
                shootIceAtMob(this.getAttackTarget());
            }
            if (this.airAttack == IafDragonAttacks.Air.TACKLE && !usingGroundAttack && this.getDistanceSq(this.getAttackTarget()) < 100) {
                double difX = this.getAttackTarget().posX - this.posX;
                double difY = this.getAttackTarget().posY + this.getAttackTarget().height - this.posY;
                double difZ = this.getAttackTarget().posZ - this.posZ;
                this.motionX += difX * 0.1D;
                this.motionY += difY * 0.1D;
                this.motionZ += difZ * 0.1D;
                if (this.getEntityBoundingBox().grow(1 + this.getRenderSize() * 0.5F, 1 + this.getRenderSize() * 0.5F, 1 + this.getRenderSize() * 0.5F).intersects(this.getAttackTarget().getEntityBoundingBox())) {
                    attackEntityAsMob(this.getAttackTarget());
                    usingGroundAttack = true;
                    randomizeAttacks();
                    setFlying(false);
                    setHovering(false);
                }
            }
        }
        boolean swimming = isSwimming() && !isHovering() && !isFlying() && ridingProgress == 0;
        if (swimming && swimProgress < 20.0F) {
            swimProgress += 0.5F;
        } else if (!swimming && swimProgress > 0.0F) {
            swimProgress -= 0.5F;
        }
        if (this.isInMaterialWater() && !this.isSwimming() && (!this.isFlying() && !this.isHovering() || this.flyTicks > 100)) {
            this.setSwimming(true);
            this.setHovering(false);
            this.setFlying(false);
            this.flyTicks = 0;
            this.ticksSwiming = 0;
        }
        if ((!this.isInMaterialWater() || this.isHovering() || this.isFlying()) && this.isSwimming()) {
            this.setSwimming(false);
            this.ticksSwiming = 0;
        }
        if (this.isSwimming() && !this.isModelDead()) {
            ticksSwiming++;
            if ((this.isInMaterialWater() || this.isOverWater()) && (ticksSwiming > 4000 || this.getAttackTarget() != null && this.isInWater() != this.getAttackTarget().isInWater()) && !this.isChild() && !this.isHovering() && !this.isFlying()) {
                this.setHovering(true);
                this.jump();
                this.motionY += 0.8D;
                this.setSwimming(false);
            }
        }
        if (!world.isRemote && (this.isHovering() && !this.isFlying() && (this.isInMaterialWater() || this.isOverWater()))) {
            this.motionY += 0.2D;
        }
        if (swimCycle < 48) {
            swimCycle += 2;
        } else {
            swimCycle = 0;
        }
        if (this.isModelDead() && swimCycle != 0) {
            swimCycle = 0;
        }
    }

    public boolean isInsideWaterBlock() {
        return this.isInsideOfMaterial(Material.WATER);
    }


    public void riderShootFire(Entity controller) {
        if (this.getRNG().nextInt(5) == 0 && !this.isChild()) {
            if (this.getAnimation() != ANIMATION_FIRECHARGE) {
                this.setAnimation(ANIMATION_FIRECHARGE);
            } else if (this.getAnimationTick() == 15) {
                rotationYaw = renderYawOffset;
                Vec3d headVec = this.getHeadPosition();
                this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                double d2 = controller.getLookVec().x;
                double d3 = controller.getLookVec().y;
                double d4 = controller.getLookVec().z;
                float inaccuracy = 1.0F;
                d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                EntityDragonIceCharge entitylargefireball = new EntityDragonIceCharge(world, this, d2, d3, d4);
                float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
                entitylargefireball.setSizes(size, size);
                entitylargefireball.setPosition(headVec.x, headVec.y, headVec.z);
                if (!world.isRemote) {
                    world.spawnEntity(entitylargefireball);
                }

            }
        } else {
            if (this.isBreathingFire()) {
                if (this.isActuallyBreathingFire()) {
                    rotationYaw = renderYawOffset;
                    if (this.ticksExisted % 5 == 0) {
                        this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                    }
                    RayTraceResult mop = rayTraceRider(controller, 10 * this.getDragonStage(), 1.0F);
                    if (mop != null) {
                        stimulateFire(mop.hitVec.x, mop.hitVec.y, mop.hitVec.z, 1);
                    }
                }
            } else {
                this.setBreathingFire(true);
            }
        }
    }

    @Override
    public ResourceLocation getDeadLootTable() {
        if (this.getDeathStage() >= (this.getAgeInDays() / 5) / 2) {
            return SKELETON_LOOT;
        } else {
            return isMale() ? MALE_LOOT : FEMALE_LOOT;
        }
    }

    public boolean isInMaterialWater() {
        return this.world.isMaterialInBB(this.getEntityBoundingBox().grow(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.WATER);
    }

    private void shootIceAtMob(EntityLivingBase entity) {
        if (this.usingGroundAttack && this.groundAttack == IafDragonAttacks.Ground.FIRE || !this.usingGroundAttack && (this.airAttack == IafDragonAttacks.Air.SCORCH_STREAM || this.airAttack == IafDragonAttacks.Air.HOVER_BLAST)) {
            if (this.usingGroundAttack && this.getRNG().nextInt(5) == 0 || !this.usingGroundAttack && this.airAttack == IafDragonAttacks.Air.HOVER_BLAST) {
                if (this.getAnimation() != ANIMATION_FIRECHARGE) {
                    this.setAnimation(ANIMATION_FIRECHARGE);
                } else if (this.getAnimationTick() == 15) {
                    rotationYaw = renderYawOffset;
                    Vec3d headVec = this.getHeadPosition();
                    double d2 = entity.posX - headVec.x;
                    double d3 = entity.posY - headVec.y;
                    double d4 = entity.posZ - headVec.z;
                    float inaccuracy = 1.0F;
                    d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                    d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                    d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                    this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                    EntityDragonIceCharge entitylargefireball = new EntityDragonIceCharge(world, this, d2, d3, d4);
                    float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
                    entitylargefireball.setSizes(size, size);
                    entitylargefireball.setPosition(headVec.x, headVec.y, headVec.z);
                    if (!world.isRemote) {
                        world.spawnEntity(entitylargefireball);
                    }
                    if (entity.isDead || entity == null) {
                        this.setBreathingFire(false);
                        this.usingGroundAttack = true;
                    }
                }
            } else {
                if (this.isBreathingFire()) {
                    if (this.isActuallyBreathingFire()) {
                        rotationYaw = renderYawOffset;
                        if (this.ticksExisted % 5 == 0) {
                            this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                        }
                        stimulateFire(entity.posX, entity.posY, entity.posZ, 1);
                        if (entity.isDead || entity == null) {
                            this.setBreathingFire(false);
                            this.usingGroundAttack = true;
                        }
                    }
                } else {
                    this.setBreathingFire(true);
                }
            }
        }
        this.faceEntity(entity, 360, 360);
    }

    public void stimulateFire(double burnX, double burnY, double burnZ, int syncType) {
        if (MinecraftForge.EVENT_BUS.post(new DragonFireEvent(this, burnX, burnY, burnZ))) return;
        if (syncType == 1 && !world.isRemote) {
            //sync with client
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageDragonSyncFire(this.getEntityId(), burnX, burnY, burnZ, 0));
        }
        if (syncType == 2 && world.isRemote) {
            //sync with server
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonSyncFire(this.getEntityId(), burnX, burnY, burnZ, 0));
        }
        if (syncType == 3 && !world.isRemote) {
            //sync with client, fire bomb
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageDragonSyncFire(this.getEntityId(), burnX, burnY, burnZ, 5));
        }
        if (syncType == 4 && world.isRemote) {
            //sync with server, fire bomb
            IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonSyncFire(this.getEntityId(), burnX, burnY, burnZ, 5));
        }
        if (syncType > 2 && syncType < 6) {
            if (this.getAnimation() != ANIMATION_FIRECHARGE) {
                this.setAnimation(ANIMATION_FIRECHARGE);
            } else if (this.getAnimationTick() == 20) {
                rotationYaw = renderYawOffset;
                Vec3d headVec = this.getHeadPosition();
                double d2 = burnX - headVec.x;
                double d3 = burnY - headVec.y;
                double d4 = burnZ - headVec.z;
                float inaccuracy = 1.0F;
                d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                this.playSound(IafSoundRegistry.FIREDRAGON_BREATH, 4, 1);
                EntityDragonIceCharge entitylargefireball = new EntityDragonIceCharge(world, this, d2, d3, d4);
                float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
                entitylargefireball.setSizes(size, size);
                entitylargefireball.setPosition(headVec.x, headVec.y, headVec.z);
                if (!world.isRemote) {
                    world.spawnEntity(entitylargefireball);
                }
            }
            return;
        }
        this.getNavigator().clearPath();
        this.burnParticleX = burnX;
        this.burnParticleY = burnY;
        this.burnParticleZ = burnZ;
        Vec3d headPos = getHeadPosition();
        double d2 = burnX - headPos.x;
        double d3 = burnY - headPos.y;
        double d4 = burnZ - headPos.z;
        float particleScale = MathHelper.clamp(this.getRenderSize() * 0.08F, 0.55F, 3F);
        double distance = Math.max(5 * this.getDistance(burnX, burnY, burnZ), 0);
        double conqueredDistance = burnProgress / 40D * distance;
        int increment = (int)Math.ceil(conqueredDistance / 100);
        for (int i = 0; i < conqueredDistance; i += increment) {
            double progressX = headPos.x + d2 * (i / (float) distance);
            double progressY = headPos.y + d3 * (i / (float) distance);
            double progressZ = headPos.z + d4 * (i / (float) distance);
            if (canPositionBeSeen(progressX, progressY, progressZ)) {
                if (world.isRemote && rand.nextInt(5) == 0) {
                    IceAndFire.PROXY.spawnDragonParticle("dragonice", headPos.x, headPos.y, headPos.z, 0, 0, 0, this);
                }
            } else {
                if (!world.isRemote) {
                    RayTraceResult result = this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), new Vec3d(progressX, progressY, progressZ), false, true, false);
                    BlockPos pos = result.getBlockPos();
                    IafDragonDestructionManager.destroyAreaIce(world, pos, this);
                }
            }
        }
        if (burnProgress >= 40D && canPositionBeSeen(burnX, burnY, burnZ)) {
            double spawnX = burnX + (rand.nextFloat() * 3.0) - 1.5;
            double spawnY = burnY + (rand.nextFloat() * 3.0) - 1.5;
            double spawnZ = burnZ + (rand.nextFloat() * 3.0) - 1.5;
            if (!world.isRemote) {
                IafDragonDestructionManager.destroyAreaIce(world, new BlockPos(spawnX, spawnY, spawnZ), this);
            }
        }
    }

    public boolean isSwimming() {
        if (world.isRemote) {
            boolean swimming = this.dataManager.get(SWIMMING).booleanValue();
            this.isSwimming = swimming;
            return swimming;
        }
        return isSwimming;
    }

    public void setSwimming(boolean swimming) {
        this.dataManager.set(SWIMMING, swimming);
        if (!world.isRemote) {
            this.isSwimming = swimming;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_IDLE : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_IDLE : IafSoundRegistry.ICEDRAGON_CHILD_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_HURT : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_HURT : IafSoundRegistry.ICEDRAGON_CHILD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_DEATH : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_DEATH : IafSoundRegistry.ICEDRAGON_CHILD_DEATH;
    }

    @Override
    public SoundEvent getRoarSound() {
        return this.isTeen() ? IafSoundRegistry.ICEDRAGON_TEEN_ROAR : this.isAdult() ? IafSoundRegistry.ICEDRAGON_ADULT_ROAR : IafSoundRegistry.ICEDRAGON_CHILD_ROAR;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT, EntityDragonBase.ANIMATION_SPEAK, EntityDragonBase.ANIMATION_BITE, EntityDragonBase.ANIMATION_SHAKEPREY, EntityIceDragon.ANIMATION_TAILWHACK, EntityIceDragon.ANIMATION_FIRECHARGE, EntityIceDragon.ANIMATION_WINGBLAST, EntityIceDragon.ANIMATION_ROAR};
    }

    public boolean isBreedingItem(@Nullable ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() != null && stack.getItem() == IafItemRegistry.frost_stew;
    }

    @Override
    protected void breathFireAtPos(BlockPos burningTarget) {
        if (this.isBreathingFire()) {
            if (this.isActuallyBreathingFire()) {
                rotationYaw = renderYawOffset;
                if (this.ticksExisted % 5 == 0) {
                    this.playSound(IafSoundRegistry.ICEDRAGON_BREATH, 4, 1);
                }
                stimulateFire(burningTarget.getX(), burningTarget.getY(), burningTarget.getZ(), 1);
            }
        } else {
            this.setBreathingFire(true);
        }
    }

    public double getFlightSpeedModifier() {
        return super.getFlightSpeedModifier() * (this.isInMaterialWater() ? 0.8F : 1F);
    }

    public boolean isAllowedToTriggerFlight() {
        return super.isAllowedToTriggerFlight() && !this.isInWater();
    }

    protected void spawnDeathParticles(){
        if (this.world.isRemote) {
            for (int k = 0; k < 10; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                IceAndFire.PROXY.spawnParticle("snowflake", this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d2, d0, d1);
            }
        }
    }

    protected void spawnBabyParticles() {
        if (this.world.isRemote) {
            for (int i = 0; i < 5; i++) {
                float radiusAdd = i * 0.15F;
                float headPosX = (float) (posX + 1.8F * getRenderSize() * (0.3F + radiusAdd) * Math.cos((rotationYaw + 90) * Math.PI / 180));
                float headPosZ = (float) (posZ + 1.8F * getRenderSize() * (0.3F + radiusAdd) * Math.sin((rotationYaw + 90) * Math.PI / 180));
                float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);
                IceAndFire.PROXY.spawnParticle("dragonice", headPosX, headPosY, headPosZ, 0, 0, 0);
            }
        }
    }

    //Required for proper egg drop
    public int getStartMetaForType(){
        return 4;
    }

    public SoundEvent getBabyFireSound() {
        return SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH;
    }

    protected ItemStack getSkull() {
        return new ItemStack(IafItemRegistry.dragon_skull, 1, 1);
    }

    public boolean useFlyingPathFinder() {
        return this.isFlying() || this.isInMaterialWater();
    }

    @Override
    public Item getSummoningCrystal() {
        return IafItemRegistry.summoning_crystal_ice;
    }
}
