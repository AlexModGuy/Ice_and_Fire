package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.message.MessageDeathWormHitbox;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateSwimmer;
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

public class EntitySeaSerpent extends EntityAnimal implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear {

    private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntitySeaSerpent.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SCALE = EntityDataManager.<Float>createKey(EntitySeaSerpent.class, DataSerializers.FLOAT);
    private int animationTick;
    private Animation currentAnimation;
    private EntityMutlipartPart[] segments = new EntityMutlipartPart[9];
    private float lastScale;
    private boolean isLandNavigator;
    private SwimBehavior swimBehavior = SwimBehavior.WANDER;
    private boolean changedSwimBehavior = false;
    private int ticksCircling;
    public float orbitRadius = 0.0F;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer roll_buffer;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer tail_buffer;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer head_buffer;
    @SideOnly(Side.CLIENT)
    public IFChainBuffer pitch_buffer;
    @Nullable
    public BlockPos orbitPos = null;

    public EntitySeaSerpent(World worldIn) {
        super(worldIn);
        switchNavigator(true);
        this.setSize(0.5F, 0.5F);
        this.ignoreFrustumCheck = true;
        resetParts(1.0F);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            roll_buffer = new IFChainBuffer();
            pitch_buffer = new IFChainBuffer();
            tail_buffer = new IFChainBuffer();
            head_buffer = new IFChainBuffer();
        }
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new SeaSerpentAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(2, new EntitySeaSerpent.AISwimWander());
        this.tasks.addTask(2, new  EntitySeaSerpent.AISwimCircle());
        this.tasks.addTask(3, new AquaticAIGetInWater(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosestIgnoreRider(this, EntityLivingBase.class, 6.0F));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new PathNavigateAmphibious(this, world);
            this.isLandNavigator = true;
        } else {
            this.moveHelper = new EntitySeaSerpent.SwimmingMoveHelper();
            this.navigator = new PathNavigateSwimmer(this, world);
            this.isLandNavigator = false;
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
    }

    public void resetParts(float scale) {
        clearParts();
        segments = new EntityMutlipartPart[9];
        for (int i = 0; i < segments.length; i++) {
            if(i > 3){
                segments[i] = new EntityMutlipartPart(this, (2F - ((i + 1) * 0.55F)) * scale, 0, 0, 0.5F * scale, 0.5F * scale, 1);
            }else{
                segments[i] = new EntityMutlipartPart(this, (1.8F - (i * 0.5F)) * scale, 0, 0, 0.45F * scale, 0.4F * scale, 1);
            }
        }
    }

    public void onUpdateParts() {
        for (Entity entity : segments) {
            if (entity != null) {
                entity.onUpdate();
            }
        }
    }

    private void clearParts() {
        for (Entity entity : segments) {
            if (entity != null) {
                world.removeEntityDangerously(entity);
            }
        }
    }

    public void setDead() {
        clearParts();
        super.setDead();
    }

    @Override
    public void setScaleForAge(boolean par1) {
        this.setScale(this.getSeaSerpentScale());
        if (this.getSeaSerpentScale() != lastScale) {
            resetParts(this.getSeaSerpentScale());
        }
        lastScale = this.getSeaSerpentScale();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setScaleForAge(true);
        onUpdateParts();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(SCALE, Float.valueOf(0F));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setFloat("Scale", this.getSeaSerpentScale());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setSeaSerpentScale(compound.getFloat("Scale"));
    }

    private void setSeaSerpentScale(float scale) {
        this.dataManager.set(SCALE, Float.valueOf(scale));
        this.updateAttributes();
    }

    private void updateAttributes() {
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Math.min(0.25D, 0.15D * this.getSeaSerpentScale()));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Math.max(10, 10 * this.getSeaSerpentScale()));
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.max(2.5D, 2.5D * this.getSeaSerpentScale()));
        this.heal(30F * this.getSeaSerpentScale());
    }

    public float getSeaSerpentScale() {
        return Float.valueOf(this.dataManager.get(SCALE).floatValue());
    }

    public int getVariant() {
        return Integer.valueOf(this.dataManager.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, Integer.valueOf(variant));
    }

    public void onLivingUpdate(){
        super.onLivingUpdate();
        if (this.isInWater() && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (!this.isInWater() && !this.isLandNavigator) {
            switchNavigator(true);
        }
        renderYawOffset = rotationYaw;
        if (world.isRemote) {
            if (this.isInWater()) {
                roll_buffer.calculateChainFlapBuffer(this.isBeingRidden() ? 55 : 90, 3, 10F, 0.5F, this);
                pitch_buffer.calculateChainWaveBuffer(90, 10, 10F, 0.5F, this);
            }
            tail_buffer.calculateChainSwingBuffer(70, 20, 5F, this);
            head_buffer.calculateChainSwingBuffer(70, 20, 5F, this);
        }
        if (changedSwimBehavior) {
            changedSwimBehavior = false;
        }
        if (!world.isRemote) {
            if (this.swimBehavior == SwimBehavior.CIRCLE) {
                ticksCircling++;
            } else {
                ticksCircling = 0;
            }
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
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setVariant(this.getRNG().nextInt(7));
        this.setSeaSerpentScale(1.5F + this.getRNG().nextFloat() * 4.0F);
        return livingdata;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
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

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    public boolean isBlinking() {
        return this.ticksExisted % 50 > 43;
    }

    private boolean canMove() {
        return true;
    }

    public static BlockPos getPositionRelativeToSeafloor(Entity entity, World world, double x, double z, Random rand) {
        BlockPos pos;
        for(int tries = 0; tries < 5; tries++){
            int y = rand.nextInt(8) - 4;
            pos = new BlockPos(x, entity.posY + y, z);
            if(isWaterBlock(world, pos)){
                return pos;
            }
        }
        return entity.getPosition();
    }

    public static BlockPos getPositionInOrbit(EntitySeaSerpent entity, World world, BlockPos orbit, Random rand) {
        float possibleOrbitRadius = (entity.orbitRadius + 10.0F);
        float radius = 10;
        float angle = (0.01745329251F * possibleOrbitRadius);
        double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
        double extraZ = (double) (radius * MathHelper.cos(angle));
        BlockPos radialPos = new BlockPos(orbit.getX() + extraX, orbit.getY(), orbit.getZ() + extraZ);
        //world.setBlockState(radialPos.down(4), Blocks.QUARTZ_BLOCK.getDefaultState());
        // world.setBlockState(orbit.down(4), Blocks.GOLD_BLOCK.getDefaultState());
        entity.orbitRadius = possibleOrbitRadius;
        return radialPos;
    }

    public static boolean isWaterBlock(World world, BlockPos pos){
        return world.getBlockState(pos).getMaterial() == Material.WATER;
    }

    enum SwimBehavior{
        CIRCLE,
        WANDER,
        NONE;
    }

    public class SwimmingMoveHelper extends EntityMoveHelper {
        private EntitySeaSerpent serpent = EntitySeaSerpent.this;

        public SwimmingMoveHelper() {
            super(EntitySeaSerpent.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.serpent.getNavigator().noPath() && !this.serpent.isBeingRidden()) {
                double d0 = this.posX - EntitySeaSerpent.this.posX;
                double d1 = this.posY - EntitySeaSerpent.this.posY;
                double d2 = this.posZ - EntitySeaSerpent.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);
                if (d3 < 6 && EntitySeaSerpent.this.getAttackTarget() == null) {
                    if (!EntitySeaSerpent.this.changedSwimBehavior && EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.WANDER && EntitySeaSerpent.this.rand.nextInt(20) == 0) {
                        EntitySeaSerpent.this.swimBehavior = EntitySeaSerpent.SwimBehavior.CIRCLE;
                        EntitySeaSerpent.this.changedSwimBehavior = true;
                    }
                    if (!EntitySeaSerpent.this.changedSwimBehavior && EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.CIRCLE && EntitySeaSerpent.this.rand.nextInt(10) == 0 && ticksCircling > 150) {
                        EntitySeaSerpent.this.swimBehavior = EntitySeaSerpent.SwimBehavior.WANDER;
                        EntitySeaSerpent.this.changedSwimBehavior = true;
                    }
                }
                if (d3 < 1 && EntitySeaSerpent.this.getAttackTarget() == null) {
                    //this.action = EntityMoveHelper.Action.WAIT;
                    EntitySeaSerpent.this.motionX *= 0.5D;
                    EntitySeaSerpent.this.motionY *= 0.5D;
                    EntitySeaSerpent.this.motionZ *= 0.5D;
                } else {
                    EntitySeaSerpent.this.motionX += d0 / d3 * 0.5D * this.speed;
                    EntitySeaSerpent.this.motionY += d1 / d3 * 0.5D * this.speed;
                    EntitySeaSerpent.this.motionZ += d2 / d3 * 0.5D * this.speed;
                    float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
                    this.entity.rotationPitch = f1;
                    if (EntitySeaSerpent.this.getAttackTarget() == null) {
                        EntitySeaSerpent.this.rotationYaw = -((float) MathHelper.atan2(EntitySeaSerpent.this.motionX, EntitySeaSerpent.this.motionZ)) * (180F / (float) Math.PI);
                        EntitySeaSerpent.this.renderYawOffset = EntitySeaSerpent.this.rotationYaw;
                    } else {
                        double d4 = EntitySeaSerpent.this.getAttackTarget().posX - EntitySeaSerpent.this.posX;
                        double d5 = EntitySeaSerpent.this.getAttackTarget().posZ - EntitySeaSerpent.this.posZ;
                        EntitySeaSerpent.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntitySeaSerpent.this.renderYawOffset = EntitySeaSerpent.this.rotationYaw;
                    }
                }
            }
        }
    }

    public class AISwimWander extends EntityAIBase {
        BlockPos target;

        public AISwimWander() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            if (EntitySeaSerpent.this.swimBehavior != EntitySeaSerpent.SwimBehavior.WANDER || !EntitySeaSerpent.this.canMove()) {
                return false;
            }
            if (EntitySeaSerpent.this.isInWater()) {
                target = EntitySeaSerpent.getPositionRelativeToSeafloor(EntitySeaSerpent.this, EntitySeaSerpent.this.world, EntitySeaSerpent.this.posX + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.posZ + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.rand);
                EntitySeaSerpent.this.orbitPos = null;
                return !EntitySeaSerpent.this.getMoveHelper().isUpdating();
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            return true;
            //RayTraceResult raytraceresult = EntitySeaSerpent.this.world.rayTraceBlocks(new Vec3d(posVec31.getX() + 0.5D, posVec31.getY() + 0.5D, posVec31.getZ() + 0.5D), new Vec3d(posVec32.getX() + 0.5D, posVec32.getY() + (double) EntitySeaSerpent.this.height * 0.5D, posVec32.getZ() + 0.5D), false, true, false);
            //return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            target = EntitySeaSerpent.getPositionRelativeToSeafloor(EntitySeaSerpent.this, EntitySeaSerpent.this.world, EntitySeaSerpent.this.posX + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.posZ + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.rand);

            if (EntitySeaSerpent.isWaterBlock(world, target)) {
                EntitySeaSerpent.this.moveHelper.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntitySeaSerpent.this.getAttackTarget() == null) {
                    EntitySeaSerpent.this.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class AISwimCircle extends EntityAIBase {
        BlockPos target;

        public AISwimCircle() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            if (EntitySeaSerpent.this.swimBehavior != EntitySeaSerpent.SwimBehavior.CIRCLE || !EntitySeaSerpent.this.canMove()) {
                return false;
            }
            if (EntitySeaSerpent.this.isInWater()) {
                EntitySeaSerpent.this.orbitPos = EntitySeaSerpent.getPositionRelativeToSeafloor(EntitySeaSerpent.this, EntitySeaSerpent.this.world, EntitySeaSerpent.this.posX + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.posZ + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.rand);
                target = EntitySeaSerpent.getPositionInOrbit(EntitySeaSerpent.this, world, EntitySeaSerpent.this.orbitPos, EntitySeaSerpent.this.rand);
                return true;
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            RayTraceResult raytraceresult = EntitySeaSerpent.this.world.rayTraceBlocks(new Vec3d(posVec31.getX() + 0.5D, posVec31.getY() + 0.5D, posVec31.getZ() + 0.5D), new Vec3d(posVec32.getX() + 0.5D, posVec32.getY() + (double) EntitySeaSerpent.this.height * 0.5D, posVec32.getZ() + 0.5D), false, true, false);
            return raytraceresult == null || raytraceresult.typeOfHit == RayTraceResult.Type.MISS;
        }

        public boolean shouldContinueExecuting() {
            return EntitySeaSerpent.this.getAttackTarget() == null && EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.CIRCLE;
        }

        public void updateTask() {
            if (EntitySeaSerpent.this.getDistance(target.getX(), target.getY(), target.getZ()) < 5) {
                target = EntitySeaSerpent.getPositionInOrbit(EntitySeaSerpent.this, world, EntitySeaSerpent.this.orbitPos, EntitySeaSerpent.this.rand);
            }
            if (EntitySeaSerpent.isWaterBlock(world, target)) {
                EntitySeaSerpent.this.moveHelper.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntitySeaSerpent.this.getAttackTarget() == null) {
                    EntitySeaSerpent.this.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

}
