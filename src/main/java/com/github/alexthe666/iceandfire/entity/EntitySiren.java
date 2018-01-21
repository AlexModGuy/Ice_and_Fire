package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.PathNavigateAmphibious;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIFindWaterTarget;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIGetInWater;
import com.github.alexthe666.iceandfire.entity.ai.SirenAIWander;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

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

    public float singProgress;
    private boolean isSinging;
    private boolean isSwimming;
    public float swimProgress;
    public static final int SEARCH_RANGE = 32;
    private boolean isLandNavigator;

    public EntitySiren(World worldIn) {
        super(worldIn);
        this.setSize(1.6F, 0.9F);
        this.switchNavigator(true);
        this.tasks.addTask(0, new SirenAIFindWaterTarget(this));
        this.tasks.addTask(1, new SirenAIGetInWater(this, 1.0D));
        this.tasks.addTask(2, new SirenAIWander(this, 1));
        this.tasks.addTask(3, new EntityAILookIdle(this));

    }

    public float getBlockPathWeight(BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.WATER ? 10F : super.getBlockPathWeight(pos);
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
        this.setSwimming(this.isInWater());
        if(this.isInWater() && this.isLandNavigator){
            switchNavigator(false);
        }
        if(!this.isInWater() && !this.isLandNavigator){
            switchNavigator(true);
        }
        if(this.getAttackTarget() == null && this.isAgressive()){
            this.setAggressive(false);
        }
        if(this.getAttackTarget() != null && !this.isAgressive()){
            this.setAggressive(true);
        }
        boolean singing = isSinging();
        if (singing && singProgress < 20.0F) {
            singProgress += 1F;
        } else if (!singing && singProgress > 0.0F) {
            singProgress -= 1F;
        }
        boolean swimming = isSwimming();
        if (swimming && swimProgress < 20.0F) {
            swimProgress += 1F;
        } else if (!swimming && swimProgress > 0.0F) {
            swimProgress -= 1F;
        }
        if(singing && this.getRNG().nextInt(3) == 0){
            this.renderYawOffset = 0;
            renderYawOffset = rotationYaw;
            float radius = -0.9F;
            float angle = (0.01745329251F * this.renderYawOffset) - 3F;
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraY = 1.2F;
            double extraZ = (double) (radius * MathHelper.cos(angle));
            IceAndFire.PROXY.spawnParticle("siren_music", this.world, this.posX + extraX + this.rand.nextFloat() - 0.5, this.posY + extraY + this.rand.nextFloat() - 0.5, this.posZ + extraZ + this.rand.nextFloat() - 0.5, 0, 0, 0);

        }
        checkForPrey();
    }

    private void checkForPrey(){
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(SEARCH_RANGE, SEARCH_RANGE, SEARCH_RANGE));
        for(Entity entity : entities){
            if(entity instanceof EntityLivingBase && isDrawnToSong(entity)){
                SirenEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, SirenEntityProperties.class);
                if(properties != null && !properties.isCharmed){
                    properties.isCharmed = true;
                }
                if(getDistance(entity) < 10D){
                    if(properties != null && properties.isCharmed){
                        properties.isCharmed = false;
                    }
                    this.setSinging(false);
                    this.setAttackTarget((EntityLivingBase)entity);
                    this.setAggressive(true);
                    this.triggerOtherSirens((EntityLivingBase)entity);
                }
            }
        }
    }

    private void triggerOtherSirens(EntityLivingBase aggressor) {
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(12, 12, 12));
        for(Entity entity : entities) {
            if (entity instanceof EntitySiren) {
                ((EntitySiren) entity).setAttackTarget(aggressor);
                ((EntitySiren) entity).setAggressive(true);
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

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setHairColor(tag.getInteger("HairColor"));
        this.setAggressive(tag.getBoolean("Aggressive"));
        this.setSingingPose(tag.getInteger("SingingPose"));
        this.setSinging(tag.getBoolean("Singing"));
        this.setSwimming(tag.getBoolean("Swimming"));

    }

    public boolean isSinging() {
        if (world.isRemote) {
            return this.isSinging = this.dataManager.get(SINGING);
        }
        return isSinging;
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
        return new Animation[]{NO_ANIMATION};
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
                this.siren.motionY += (double) distanceY * 0.1D * 0.35D;

            } else {
                this.siren.setAIMoveSpeed(0.0F);
            }
        }
    }
}
