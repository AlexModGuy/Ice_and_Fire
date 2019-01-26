package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.ai.CyclopsAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAITargetSheepPlayers;
import com.github.alexthe666.iceandfire.entity.ai.PathNavigateFlyingCreature;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.pathfinding.PathNavigateFlying;
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
import java.util.Random;

public class EntityAmphithere extends EntityTameable implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityAmphithere.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.<Boolean>createKey(EntityAmphithere.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> FLAP_TICKS = EntityDataManager.<Integer>createKey(EntityAmphithere.class, DataSerializers.VARINT);
    public float flapProgress;
    private int flapTicks = 0;
    public float groundProgress;
    private boolean isFlying;
    private boolean isLandNavigator;
    @SideOnly(Side.CLIENT)
    public RollBuffer roll_buffer;
    @SideOnly(Side.CLIENT)
    public ChainBuffer tail_buffer;
    @SideOnly(Side.CLIENT)
    public ChainBuffer pitch_buffer;
    protected FlightBehavior flightBehavior = FlightBehavior.WANDER;
    private boolean changedFlightBehavior = false;
    @Nullable
    public BlockPos orbitPos = null;
    public float orbitRadius = 0.0F;

    public EntityAmphithere(World worldIn) {
        super(worldIn);
        this.setSize(2.5F, 1.25F);
        this.stepHeight = 1;
        if (FMLCommonHandler.instance().getSide().isClient()) {
            roll_buffer = new RollBuffer();
            pitch_buffer = new ChainBuffer();
            tail_buffer = new ChainBuffer();
        }
    }

    public float getBlockPathWeight(BlockPos pos) {
        if(this.isFlying()){
            if(world.isAirBlock(pos)){
                return 10F;
            }else{
                return 0F;
            }
        }else{
            return super.getBlockPathWeight(pos);
        }
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit = new EntityAISit(this));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(3, new AIFlyWander());
        this.tasks.addTask(3, new AIFlyCircle());
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new PathNavigateClimber(this, world);
            this.isLandNavigator = true;
        } else {
            this.moveHelper = new EntityAmphithere.FlyMoveHelper(this);
            this.navigator = new PathNavigateFlyingCreature(this, world);
            this.isLandNavigator = false;
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        boolean flapping = this.isFlapping();
        boolean flying = this.isFlying() && !this.onGround;
        if(this.onGround && this.isFlying()){
            //this.setFlying(false);
        }
        if(!this.isFlying()  && this.onGround){
            this.motionY += 0.5F;
            this.flapWings();
            this.setFlying(true);
        }
        if (flying && groundProgress > 0.0F) {
            groundProgress -= 1F;
        } else if (!flying && groundProgress < 20.0F) {
            groundProgress -= 1F;
        }
        if(this.isFlying()){
            this.motionY += 0.08D;

        }

        if (flying && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (!flying && !this.isLandNavigator) {
            switchNavigator(true);
        }
        if (flapping && flapProgress < 10.0F) {
            flapProgress += 1F;
        } else if (!flapping && flapProgress > 0.0F) {
            flapProgress -= 1F;
        }
        if(flapping){
            flapTicks--;
        }
        renderYawOffset = rotationYaw;
        if (world.isRemote) {
            roll_buffer.calculateChainFlapBuffer(90, 10, 10, this);
            tail_buffer.calculateChainSwingBuffer(90, 30, 13F, this);
            pitch_buffer.calculateChainWaveBuffer(90, 20, 2.5F, this);
        }
        if(changedFlightBehavior){
            changedFlightBehavior = false;
        }
        if(orbitRadius > 1.0){
           // orbitRadius = 0;
        }
    }

    public boolean isFlapping() {
        if (world.isRemote) {
            return (this.flapTicks = this.dataManager.get(FLAP_TICKS).intValue()) > 0;
        }
        return flapTicks > 0;
    }

    public void flapWings() {
        this.dataManager.set(FLAP_TICKS, 20);
        if (!world.isRemote) {
            this.flapTicks = 20;
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(FLYING, false);
        this.dataManager.register(FLAP_TICKS, Integer.valueOf(0));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setBoolean("Flying", this.isFlying());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setFlying(compound.getBoolean("Flying"));
    }

    public boolean isFlying() {
        if (world.isRemote) {
            return this.isFlying = this.dataManager.get(FLYING).booleanValue();
        }
        return isFlying;
    }

    public void setFlying(boolean flying) {
        this.dataManager.set(FLYING, flying);
        if (!world.isRemote) {
            this.isFlying = flying;
        }
    }

    public int getVariant() {

        return Integer.valueOf(this.dataManager.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, Integer.valueOf(variant));
    }


    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
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
        return new Animation[0];
    }

    public boolean isBlinking() {
        return this.ticksExisted % 50 > 40;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setVariant(this.getRNG().nextInt(5));
        return livingdata;
    }

    public void fall(float distance, float damageMultiplier) {
    }


    public static BlockPos getPositionRelativetoGround(Entity entity, World world, double x, double z, Random rand) {
        BlockPos pos = new BlockPos(x, entity.posY, z);
        for (int yDown = 0; yDown < 6 + rand.nextInt(6); yDown++) {
            if (!world.isAirBlock(pos.down(yDown))) {
                return pos.up(yDown);
            }
        }
        return pos;
    }

    public static BlockPos getPositionInOrbit(EntityAmphithere entity, World world, BlockPos orbit, Random rand) {
        float possibleOrbitRadius = (entity.orbitRadius + 10.0F);
        float radius = 10;
        float angle = (0.01745329251F * possibleOrbitRadius);
        double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
        double extraZ = (double) (radius * MathHelper.cos(angle));
        BlockPos radialPos = new BlockPos(entity.posX + extraX, orbit.getY(), entity.posZ + extraZ);
        //world.setBlockState(radialPos.down(4), Blocks.QUARTZ_BLOCK.getDefaultState());
        //world.setBlockState(orbit.down(4), Blocks.GOLD_BLOCK.getDefaultState());
        entity.orbitRadius = possibleOrbitRadius;
        return radialPos;
    }

    public enum FlightBehavior{
        CIRCLE,
        WANDER;
    }

    class AIFlyWander extends EntityAIBase {
        BlockPos target;

        public AIFlyWander() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            if(EntityAmphithere.this.flightBehavior != FlightBehavior.WANDER){
                return false;
            }
            if (EntityAmphithere.this.isFlying()) {
                target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.world, EntityAmphithere.this.posX + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.posZ + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.rand);
                EntityAmphithere.this.orbitPos = null;
                return isDirectPathBetweenPoints(EntityAmphithere.this.getPosition(), target) && !EntityAmphithere.this.getMoveHelper().isUpdating();
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            RayTraceResult raytraceresult = EntityAmphithere.this.world.rayTraceBlocks(new Vec3d(posVec31.getX() + 0.5D, posVec31.getY() + 0.5D, posVec31.getZ() + 0.5D), new Vec3d(posVec32.getX() + 0.5D, posVec32.getY() + (double) EntityAmphithere.this.height * 0.5D, posVec32.getZ() + 0.5D), false, true, false);
            return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            if (!isDirectPathBetweenPoints(EntityAmphithere.this.getPosition(), target)) {
                target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.world, EntityAmphithere.this.posX + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.posZ + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.rand);
            }
            if (EntityAmphithere.this.world.isAirBlock(target)) {
                EntityAmphithere.this.moveHelper.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntityAmphithere.this.getAttackTarget() == null) {
                    EntityAmphithere.this.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class AIFlyCircle extends EntityAIBase {
        BlockPos target;

        public AIFlyCircle() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            if(EntityAmphithere.this.flightBehavior != FlightBehavior.CIRCLE){
                return false;
            }
            if (EntityAmphithere.this.isFlying()) {
                EntityAmphithere.this.orbitPos = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.world, EntityAmphithere.this.posX + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.posZ + EntityAmphithere.this.rand.nextInt(30) - 15, EntityAmphithere.this.rand);
                target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, world, EntityAmphithere.this.orbitPos, EntityAmphithere.this.rand);
                return true;
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            RayTraceResult raytraceresult = EntityAmphithere.this.world.rayTraceBlocks(new Vec3d(posVec31.getX() + 0.5D, posVec31.getY() + 0.5D, posVec31.getZ() + 0.5D), new Vec3d(posVec32.getX() + 0.5D, posVec32.getY() + (double) EntityAmphithere.this.height * 0.5D, posVec32.getZ() + 0.5D), false, true, false);
            return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
        }

        public boolean shouldContinueExecuting() {
            return EntityAmphithere.this.getAttackTarget() == null && EntityAmphithere.this.flightBehavior == FlightBehavior.CIRCLE;
        }

        public void updateTask() {
            if(EntityAmphithere.this.getDistance(target.getX(), target.getY(), target.getZ()) < 5){
                target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, world, EntityAmphithere.this.orbitPos, EntityAmphithere.this.rand);
            }
            if (EntityAmphithere.this.world.isAirBlock(target)) {
                EntityAmphithere.this.moveHelper.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntityAmphithere.this.getAttackTarget() == null) {
                    EntityAmphithere.this.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class FlyMoveHelper extends EntityMoveHelper {
        public FlyMoveHelper(EntityAmphithere entity) {
            super(entity);
            this.speed = 1.75F;
        }

        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - EntityAmphithere.this.posX;
                double d1 = this.posY - EntityAmphithere.this.posY;
                double d2 = this.posZ - EntityAmphithere.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);
                if(d3 < 6){
                    if(!EntityAmphithere.this.changedFlightBehavior && EntityAmphithere.this.flightBehavior == FlightBehavior.WANDER && EntityAmphithere.this.rand.nextInt(10) == 0){
                        EntityAmphithere.this.flightBehavior = FlightBehavior.CIRCLE;
                        EntityAmphithere.this.changedFlightBehavior = true;
                    }
                    if(!EntityAmphithere.this.changedFlightBehavior && EntityAmphithere.this.flightBehavior == FlightBehavior.CIRCLE && EntityAmphithere.this.rand.nextInt(8) == 0){
                        EntityAmphithere.this.flightBehavior = FlightBehavior.WANDER;
                        EntityAmphithere.this.changedFlightBehavior = true;
                    }
                }
                if (d3 < EntityAmphithere.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityAmphithere.this.motionX *= 0.5D;
                    EntityAmphithere.this.motionY *= 0.5D;
                    EntityAmphithere.this.motionZ *= 0.5D;
                } else {
                    EntityAmphithere.this.motionX += d0 / d3 * 0.5D * this.speed;
                    EntityAmphithere.this.motionY += d1 / d3 * 0.5D * this.speed;
                    EntityAmphithere.this.motionZ += d2 / d3 * 0.5D * this.speed;
                    if(EntityAmphithere.this.posY + 5 < this.posY && !EntityAmphithere.this.isFlapping()){
                        EntityAmphithere.this.flapWings();
                    }
                    if (EntityAmphithere.this.getAttackTarget() == null) {
                        EntityAmphithere.this.rotationYaw = -((float) MathHelper.atan2(EntityAmphithere.this.motionX, EntityAmphithere.this.motionZ)) * (180F / (float) Math.PI);
                        EntityAmphithere.this.rotationPitch = ((float) MathHelper.clamp(EntityAmphithere.this.motionY * 3, -2.0, 2.0) * (180F / (float) Math.PI));
                        EntityAmphithere.this.renderYawOffset = EntityAmphithere.this.rotationYaw;
                    } else {
                        double d4 = EntityAmphithere.this.getAttackTarget().posX - EntityAmphithere.this.posX;
                        double d5 = EntityAmphithere.this.getAttackTarget().posZ - EntityAmphithere.this.posZ;
                        EntityAmphithere.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityAmphithere.this.renderYawOffset = EntityAmphithere.this.rotationYaw;
                        EntityAmphithere.this.rotationPitch = ((float) MathHelper.clamp(EntityAmphithere.this.motionY * 3, -2.0, 2.0) * (180F / (float) Math.PI));
                    }
                }
            }
        }
    }
}
