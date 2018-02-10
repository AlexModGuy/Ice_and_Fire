package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class EntitySiren extends EntityMob implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    private static final DataParameter<Integer> HAIR_COLOR = EntityDataManager.<Integer>createKey(EntitySiren.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> AGGRESSIVE = EntityDataManager.<Boolean>createKey(EntitySiren.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> SING_POSE = EntityDataManager.<Integer>createKey(EntitySiren.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> SINGING = EntityDataManager.<Boolean>createKey(EntitySiren.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SWIMMING = EntityDataManager.<Boolean>createKey(EntitySiren.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CHARMED = EntityDataManager.<Boolean>createKey(EntitySiren.class, DataSerializers.BOOLEAN);
    @SideOnly(Side.CLIENT)
    public ChainBuffer tail_buffer;
    public float singProgress;
    private boolean isSinging;
    private boolean isSwimming;
    public float swimProgress;
    public static final int SEARCH_RANGE = 32;
    private boolean isLandNavigator;
    private int ticksAgressive;
    public static Animation ANIMATION_BITE = Animation.create(20);
    public static Animation ANIMATION_PULL = Animation.create(20);
    public EntitySiren(World worldIn) {
        super(worldIn);
        this.setSize(1.6F, 0.9F);
        this.switchNavigator(true);
        this.tasks.addTask(0, new SirenAIFindWaterTarget(this));
        this.tasks.addTask(1, new SirenAIGetInWater(this, 1.0D));
        this.tasks.addTask(2, new SirenAIWander(this, 1));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, new Predicate<EntityPlayer>() {
            @Override
            public boolean apply(@Nullable EntityPlayer entity) {
                return EntitySiren.this.isAgressive() && !entity.isCreative();
            }
        }));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, true, false, new Predicate<EntityVillager>() {
            @Override
            public boolean apply(@Nullable EntityVillager entity) {
                return EntitySiren.this.isAgressive();
            }
        }));
        if (FMLCommonHandler.instance().getSide().isClient()) {
            tail_buffer = new ChainBuffer();
        }
    }

    public float getBlockPathWeight(BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.WATER ? 10F : super.getBlockPathWeight(pos);
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if(this.getRNG().nextInt(2) == 0){
            this.setAnimation(ANIMATION_PULL);
        }else{
            this.setAnimation(ANIMATION_BITE);
        }
        return true;
    }

    public boolean isDirectPathBetweenPoints(Vec3d vec1, Vec3d vec2) {
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec1, new Vec3d(vec2.x, vec2.y + (double) this.height * 0.5D, vec2.z), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    public float getPathPriority(PathNodeType nodeType) {
        return nodeType == PathNodeType.WATER ? 0F : super.getPathPriority(nodeType);
    }

    private void switchNavigator(boolean onLand){
        if(onLand){
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new PathNavigateAmphibious(this, world);
            this.isLandNavigator = true;
        }else{
            this.moveHelper = new EntitySiren.SwimmingMoveHelper();
            this.navigator = new PathNavigateSwimmer(this, world);
            this.isLandNavigator = false;
        }
    }

    private boolean isPathOnHighGround(){
        if(this.navigator != null && this.navigator.getPath() != null && this.navigator.getPath().getTarget() != null) {
            BlockPos target = new BlockPos(this.navigator.getPath().getTarget().x, this.navigator.getPath().getTarget().y, this.navigator.getPath().getTarget().z);
            BlockPos siren = new BlockPos(this);
            if (world.isAirBlock(siren.up()) && world.isAirBlock(target.up()) && target.getY() >= siren.getY()) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isInWater() {
        return super.isInWater() || this.isInsideOfMaterial(Material.WATER) || this.isInsideOfMaterial(Material.CORAL);
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if(this.getAnimation()  == ANIMATION_BITE && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 4D && this.getAnimationTick() == 5){
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }
        if(this.getAnimation()  == ANIMATION_PULL && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 12D && this.getAnimationTick() == 5){
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
            this.getAttackTarget().motionX += (Math.signum(this.posX - this.getAttackTarget().posX) * 0.5D - this.getAttackTarget().motionX) * 0.100000000372529 * 5;
            this.getAttackTarget().motionY += (Math.signum(this.posY - this.getAttackTarget().posY + 1) * 0.5D - this.getAttackTarget().motionY) * 0.100000000372529 * 5;
            this.getAttackTarget().motionZ += (Math.signum(this.posZ - this.getAttackTarget().posZ) * 0.5D - this.getAttackTarget().motionZ) * 0.100000000372529 * 5;
            float angle = (float) (Math.atan2(this.getAttackTarget().motionZ, this.getAttackTarget().motionX) * 180.0D / Math.PI) - 90.0F;
            //entity.moveForward = 0.5F;
            double d0 = this.posX - this.getAttackTarget().posX;
            double d2 = this.posZ - this.getAttackTarget().posZ;
            double d1 = this.posY - 1 - this.getAttackTarget().posY;
            double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
            float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
            this.getAttackTarget().rotationPitch = EventLiving.updateRotation(this.getAttackTarget().rotationPitch, f1, 30F);
            this.getAttackTarget().rotationYaw = EventLiving.updateRotation(this.getAttackTarget().rotationYaw, f, 30F);
        }
        if (world.isRemote) {
            tail_buffer.calculateChainSwingBuffer(40, 10, 2.5F, this);
        }
        if(this.isAgressive()){
            ticksAgressive++;
        }else{
            ticksAgressive = 0;
        }
        if(ticksAgressive > 300 && this.isAgressive() && this.getAttackTarget() == null){
            this.setAggressive(false);
            this.ticksAgressive = 0;
            this.setSinging(!this.isCharmed());

        }
        if((this.getAttackTarget() != null && !this.getAttackTarget().isInWater() || this.isPathOnHighGround()) && this.collidedHorizontally && this.isInWater()){
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= (double)(MathHelper.sin(f) * 0.2F);
            this.motionZ += (double)(MathHelper.cos(f) * 0.2F);
            this.motionY +=  0.35F;
        }
        if(this.isInWater() && !this.isSwimming()){
            this.setSwimming(true);
        }
        if(!this.isInWater() && this.isSwimming()){
            this.setSwimming(false);
        }
        if(this.isInWater() && this.isLandNavigator){
            switchNavigator(false);
        }
        if(!this.isInWater() && !this.isLandNavigator){
            switchNavigator(true);
        }
        if(this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer) this.getAttackTarget()).isCreative()){
            this.setAttackTarget(null);
        }
        if(this.getAttackTarget() != null && !this.isAgressive()){
            this.setAggressive(true);
        }
        boolean singing = isSinging() && !wantsToSing() && !this.isAgressive() && !this.isInWater() && onGround;
        if (singing && singProgress < 20.0F) {
            singProgress += 1F;
        } else if (!singing && singProgress > 0.0F) {
            singProgress -= 1F;
        }
        boolean swimming = isSwimming();
        if (swimming && swimProgress < 20.0F) {
            swimProgress += 1F;
        } else if (!swimming && swimProgress > 0.0F) {
            swimProgress -= 0.5F;
        }
        checkForPrey();
        if(singing && !this.wantsToSing() && !this.isInWater()){
            if(this.getRNG().nextInt(3) == 0){
                this.renderYawOffset = 0;
                renderYawOffset = rotationYaw;
                float radius = -0.9F;
                float angle = (0.01745329251F * this.renderYawOffset) - 3F;
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
                double extraY = 1.2F;
                double extraZ = (double) (radius * MathHelper.cos(angle));
                IceAndFire.PROXY.spawnParticle("siren_music", this.world, this.posX + extraX + this.rand.nextFloat() - 0.5, this.posY + extraY + this.rand.nextFloat() - 0.5, this.posZ + extraZ + this.rand.nextFloat() - 0.5, 0, 0, 0);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void checkForPrey(){
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(SEARCH_RANGE, SEARCH_RANGE, SEARCH_RANGE));
        for(Entity entity : entities){
            if(entity instanceof EntityLivingBase && isDrawnToSong(entity)){
                SirenEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, SirenEntityProperties.class);
                if(this.isSinging()){
                    this.setAggressive(false);
                    if(!this.wantsToSing() && !this.isInWater()) {
                        if (properties != null && !properties.isCharmed) {
                            properties.isCharmed = true;
                        }
                        if (getDistance(entity) < 5D) {
                            if (properties != null && properties.isCharmed) {
                                properties.isCharmed = false;
                            }
                            if(!world.isRemote){
                                this.setSinging(false);
                                this.setAttackTarget((EntityLivingBase) entity);
                                this.setAggressive(true);
                                this.triggerOtherSirens((EntityLivingBase) entity);
                            }
                        }
                    }
                }else if(getDistance(entity) >= 5D && !this.isAgressive()){
                    if(!world.isRemote) {
                        this.setSinging(true);
                    }
                }
            }
        }
    }

    public void triggerOtherSirens(EntityLivingBase aggressor) {
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(12, 12, 12));
        for(Entity entity : entities) {
            if (entity instanceof EntitySiren) {
                ((EntitySiren) entity).setAttackTarget(aggressor);
                ((EntitySiren) entity).setAggressive(true);
                ((EntitySiren) entity).setSinging(false);

            }
        }
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("HairColor", this.getHairColor());
        tag.setBoolean("Aggressive", this.isAgressive());
        tag.setInteger("SingingPose", this.getSingingPose());
        tag.setBoolean("Singing", this.isSinging());
        tag.setBoolean("Swimming", this.isSwimming());
        tag.setBoolean("Passive", this.isCharmed());

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setHairColor(tag.getInteger("HairColor"));
        this.setAggressive(tag.getBoolean("Aggressive"));
        this.setSingingPose(tag.getInteger("SingingPose"));
        this.setSinging(tag.getBoolean("Singing"));
        this.setSwimming(tag.getBoolean("Swimming"));
        this.setCharmed(tag.getBoolean("Passive"));

    }

    public boolean isSinging() {
        if (world.isRemote) {
            return this.isSinging = this.dataManager.get(SINGING);
        }
        return isSinging;
    }

    public boolean wantsToSing(){
        return this.isSinging() && this.isInWater() && !this.isAgressive();
    }

    public void setSinging(boolean singing) {
        this.dataManager.set(SINGING, singing);
        if (!world.isRemote) {
            this.isSinging = singing;
        }
    }

    public boolean isSwimming() {
        if (world.isRemote) {
            return this.isSwimming = this.dataManager.get(SWIMMING);
        }
        return isSwimming;
    }

    public void setSwimming(boolean swimming) {
        this.dataManager.set(SWIMMING, swimming);
        if (!world.isRemote) {
            this.isSwimming = swimming;
        }
    }

    public void setAggressive(boolean aggressive) {
        this.dataManager.set(AGGRESSIVE, aggressive);
    }

    public boolean isAgressive() {
        return this.dataManager.get(AGGRESSIVE);
    }

    public void setCharmed(boolean aggressive) {
        this.dataManager.set(CHARMED, aggressive);
    }

    public boolean isCharmed() {
        return this.dataManager.get(CHARMED);
    }

    public void setHairColor(int hairColor) {
        this.dataManager.set(HAIR_COLOR, hairColor);
    }

    public int getHairColor() {
        return this.dataManager.get(HAIR_COLOR);
    }

    public void setSingingPose(int pose) {
        this.dataManager.set(SING_POSE, MathHelper.clamp(pose, 0, 2));
    }

    public int getSingingPose() {
        return this.dataManager.get(SING_POSE);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.sirenMaxHealth);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(HAIR_COLOR, 0);
        this.dataManager.register(SING_POSE, 0);
        this.dataManager.register(AGGRESSIVE, false);
        this.dataManager.register(SINGING, false);
        this.dataManager.register(SWIMMING, false);
        this.dataManager.register(CHARMED, false);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setHairColor(this.getRNG().nextInt(3));
        this.setSingingPose(this.getRNG().nextInt(3));
        return livingdata;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_BITE, ANIMATION_PULL};
    }

    public static boolean isDrawnToSong(Entity entity){
        return entity instanceof EntityPlayer && !((EntityPlayer) entity).isCreative() || entity instanceof EntityVillager || entity instanceof IHearsSiren;
    }

    @Override
    public void travel(float strafe, float forward, float vertical) {
        if (this.isServerWorld()) {
            float f4;
            float f5;
            if (this.isInWater()) {
                this.moveRelative(strafe, forward, vertical,0.1F);
                f4 = 0.8F;
                float d0 = (float) EnchantmentHelper.getDepthStriderModifier(this);
                if (d0 > 3.0F) {
                    d0 = 3.0F;
                }
                if (!this.onGround) {
                    d0 *= 0.5F;
                }
                if (d0 > 0.0F) {
                    f4 += (0.54600006F - f4) * d0 / 3.0F;
                }
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double) f4;
                this.motionX *= 0.900000011920929D;
                this.motionY *= 0.900000011920929D;
                this.motionY *= (double) f4;
                this.motionZ *= 0.900000011920929D;
                this.motionZ *= (double) f4;
            } else {
                super.travel(strafe, forward, vertical);
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double deltaX = this.posX - this.prevPosX;
        double deltaZ = this.posZ - this.prevPosZ;
        float delta = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 4.0F;
        if (delta > 1.0F) {
            delta = 1.0F;
        }
        this.limbSwingAmount += (delta - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    class SwimmingMoveHelper extends EntityMoveHelper {
        private EntitySiren siren = EntitySiren.this;

        public SwimmingMoveHelper() {
            super(EntitySiren.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.siren.getNavigator().noPath()) {
                double distanceX = this.posX - this.siren.posX;
                double distanceY = this.posY - this.siren.posY;
                double distanceZ = this.posZ - this.siren.posZ;
                double distance = Math.abs(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distance = (double) MathHelper.sqrt(distance);
                float angle = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.siren.rotationYaw = this.limitAngle(this.siren.rotationYaw, angle, 30.0F);
                this.siren.setAIMoveSpeed((float) 0.5F);
                this.siren.motionY += (double) distanceY * 0.1D;
                if ( distanceX * distanceX + distanceZ * distanceZ < (double)Math.max(1.0F, this.entity.width)) {
                    float f = this.siren.rotationYaw * 0.017453292F;
                    this.siren.motionX -= (double)(MathHelper.sin(f) * 0.35F);
                    this.siren.motionZ += (double)(MathHelper.cos(f) * 0.35F);
                    this.siren.motionY +=  0.42F;
                }
            }else if (this.action == EntityMoveHelper.Action.JUMPING) {
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));

                if (this.entity.onGround) {
                    this.action = EntityMoveHelper.Action.WAIT;
                }
            } else {
                this.siren.setAIMoveSpeed(0.0F);
            }
        }
    }
}
