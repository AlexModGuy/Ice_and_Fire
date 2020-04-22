package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIWatchClosestIgnoreRider;
import com.github.alexthe666.iceandfire.entity.ai.FlyingAITarget;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.SeaSerpentAIGetInWater;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.multipart.IMultipartEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class EntitySeaSerpent extends EntityAnimal implements IAnimatedEntity, IMultipartEntity, IVillagerFear, IAnimalFear {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_SPEAK = Animation.create(15);
    public static final Animation ANIMATION_ROAR = Animation.create(40);
    public static final int TIME_BETWEEN_JUMPS = 170;
    public static final int TIME_BETWEEN_ROARS = 300;
    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "sea_serpent"));
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.VARINT);
    private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> JUMPING = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BREATHING = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> ANCIENT = EntityDataManager.createKey(EntitySeaSerpent.class, DataSerializers.BOOLEAN);
    private static final Predicate NOT_SEA_SERPENT = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity entity) {
            return entity instanceof EntityLivingBase && !(entity instanceof EntitySeaSerpent) && DragonUtils.isAlive((EntityLivingBase) entity);
        }
    };
    public int swimCycle;
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
    public float jumpProgress = 0.0F;
    public float wantJumpProgress = 0.0F;
    public float jumpRot = 0.0F;
    public float breathProgress = 0.0F;
    //true  = melee, false = ranged
    public boolean attackDecision = false;
    private int animationTick;
    private Animation currentAnimation;
    private EntityMutlipartPart[] segments = new EntityMutlipartPart[9];
    private float lastScale;
    private boolean isLandNavigator;
    private SwimBehavior swimBehavior = SwimBehavior.WANDER;
    private boolean changedSwimBehavior = false;
    private int ticksCircling;
    private boolean isArcing = false;
    private float arcingYAdditive = 0F;
    private int ticksSinceJump = 0;
    private int ticksSinceRoar = 0;
    private int ticksJumping = 0;
    private boolean isBreathing;

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

    public static BlockPos getPositionRelativeToSeafloor(EntitySeaSerpent entity, World world, double x, double z, Random rand) {
        BlockPos pos;
        BlockPos topY = new BlockPos(x, entity.posY, z);
        BlockPos bottomY = new BlockPos(x, entity.posY, z);
        while (isWaterBlock(world, topY) && topY.getY() < world.getHeight()) {
            topY = topY.up();
        }
        while (isWaterBlock(world, bottomY) && bottomY.getY() > 0) {
            bottomY = bottomY.down();
        }
        if (entity.ticksSinceJump > TIME_BETWEEN_JUMPS) {
            return topY.up((int) Math.ceil(3 * entity.getSeaSerpentScale()));

        }
        if (entity.ticksSinceRoar > TIME_BETWEEN_ROARS || entity.getAnimation() == ANIMATION_ROAR) {
            return topY.down();
        }
        for (int tries = 0; tries < 5; tries++) {
            pos = new BlockPos(x, bottomY.getY() + 1 + rand.nextInt(Math.max(1, topY.getY() - bottomY.getY() - 2)), z);
            if (isWaterBlock(world, pos)) {
                return pos;
            }
        }
        return entity.getPosition();
    }

    public static BlockPos getPositionInOrbit(EntitySeaSerpent entity, World world, BlockPos orbit, Random rand) {
        float possibleOrbitRadius = (entity.orbitRadius + 10.0F);
        float radius = 5 * entity.getSeaSerpentScale();
        float angle = (0.01745329251F * possibleOrbitRadius);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        BlockPos radialPos = new BlockPos(orbit.getX() + extraX, orbit.getY(), orbit.getZ() + extraZ);
        entity.orbitRadius = possibleOrbitRadius;
        return radialPos;
    }

    public static BlockPos getPositionPreyArc(EntitySeaSerpent entity, BlockPos target, World world) {
        float radius = 10 * entity.getSeaSerpentScale();
        entity.renderYawOffset = entity.rotationYaw;
        float angle = (0.01745329251F * entity.renderYawOffset);
        double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
        double extraZ = radius * MathHelper.cos(angle);
        double signum = Math.signum(target.getY() + 0.5F - entity.posY);
        BlockPos pos = new BlockPos(target.getX() + extraX, target.getY() + entity.rand.nextInt(5) * signum, target.getZ() + extraZ);
        entity.isArcing = true;
        return clampBlockPosToWater(entity, world, pos);
    }

    private static BlockPos clampBlockPosToWater(Entity entity, World world, BlockPos pos) {
        BlockPos topY = new BlockPos(pos.getX(), entity.posY, pos.getZ());
        BlockPos bottomY = new BlockPos(pos.getX(), entity.posY, pos.getZ());
        while (isWaterBlock(world, topY) && topY.getY() < world.getHeight()) {
            topY = topY.up();
        }
        while (isWaterBlock(world, bottomY) && bottomY.getY() > 0) {
            bottomY = bottomY.down();
        }
        return new BlockPos(pos.getX(), MathHelper.clamp(pos.getY(), bottomY.getY() + 1, topY.getY() - 1), pos.getZ());
    }

    public static boolean isWaterBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial() == Material.WATER;
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new SeaSerpentAIGetInWater(this, 1.0D));
        this.tasks.addTask(1, new EntitySeaSerpent.AISwimBite());
        this.tasks.addTask(1, new EntitySeaSerpent.AISwimWander());
        this.tasks.addTask(1, new EntitySeaSerpent.AISwimCircle());
        this.tasks.addTask(2, new SeaSerpentAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(3, new EntityAIWatchClosestIgnoreRider(this, EntityLivingBase.class, 6.0F));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new FlyingAITarget(this, EntityLivingBase.class, 0, true, false, NOT_SEA_SERPENT));
    }

    protected int getExperiencePoints(EntityPlayer player) {
        return this.isAncient() ? 30 : 15;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveHelper = new EntityMoveHelper(this);
            this.navigator = new PathNavigateGround(this, world);
            ((PathNavigateGround) this.navigator).setCanSwim(true);
            this.isLandNavigator = true;
        } else {
            this.moveHelper = new EntitySeaSerpent.SwimmingMoveHelper();
            this.navigator = new PathNavigateSwimmer(this, world);
            this.isLandNavigator = false;
        }
    }

    public boolean isDirectPathBetweenPoints(BlockPos pos) {
        RayTraceResult movingobjectposition = world.rayTraceBlocks(this.getPositionVector().add(0, this.height * 0.5, 0), new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit != RayTraceResult.Type.BLOCK;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.15D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IceAndFire.CONFIG.dragonTargetSearchLength));
    }

    public void resetParts(float scale) {
        clearParts();
        segments = new EntityMutlipartPart[9];
        for (int i = 0; i < segments.length; i++) {
            if (i > 3) {
                segments[i] = new EntityMutlipartPart(this, (2F - ((i + 1) * 0.55F)) * scale, 0, 0, 0.5F * scale, 0.5F * scale, 1);
            } else {
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
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(ANIMATION_BITE);
            return true;
        }
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setScaleForAge(true);
        onUpdateParts();
        if (this.isInWater()) {
            spawnParticlesAroundEntity(EnumParticleTypes.WATER_BUBBLE, this, (int) this.getSeaSerpentScale());
            for (Entity entity : segments) {
                spawnParticlesAroundEntity(EnumParticleTypes.WATER_BUBBLE, entity, (int) this.getSeaSerpentScale());
            }
        }
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void spawnParticlesAroundEntity(EnumParticleTypes type, Entity entity, int count) {
        for (int i = 0; i < count; i++) {
            double x = entity.posX + (double) (this.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width;
            double y = entity.posY + 0.5D + (double) (this.rand.nextFloat() * entity.height);
            double z = entity.posZ + (double) (this.rand.nextFloat() * entity.width * 2.0F) - (double) entity.width;
            if (this.world.getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.WATER) {
                this.world.spawnParticle(type, x, y, z, 0, 0, 0);
            }
        }
    }

    private void spawnSlamParticles(EnumParticleTypes type) {
        for (int i = 0; i < this.getSeaSerpentScale() * 3; i++) {
            for (int i1 = 0; i1 < 20; i1++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float radius = 1.25F * getSeaSerpentScale();
                float angle = (0.01745329251F * this.renderYawOffset) + i1 * 1F;
                double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = radius * MathHelper.cos(angle);
                if (world.isRemote) {
                    world.spawnParticle(type, true, this.posX + extraX, this.posY + extraY, this.posZ + extraZ, motionX, motionY, motionZ, 0);
                }
            }
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, Integer.valueOf(0));
        this.dataManager.register(SCALE, Float.valueOf(0F));
        this.dataManager.register(JUMPING, false);
        this.dataManager.register(BREATHING, false);
        this.dataManager.register(ANCIENT, false);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("TicksSinceRoar", ticksSinceRoar);
        compound.setFloat("Scale", this.getSeaSerpentScale());
        compound.setBoolean("JumpingOutOfWater", this.isJumpingOutOfWater());
        compound.setBoolean("AttackDecision", attackDecision);
        compound.setBoolean("Breathing", this.isBreathing());
        compound.setBoolean("Ancient", this.isAncient());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        ticksSinceRoar = compound.getInteger("TicksSinceRoar");
        this.setSeaSerpentScale(compound.getFloat("Scale"));
        this.setJumpingOutOfWater(compound.getBoolean("JumpingOutOfWater"));
        attackDecision = compound.getBoolean("AttackDecision");
        this.setBreathing(compound.getBoolean("Breathing"));
        this.setAncient(compound.getBoolean("Ancient"));
    }

    private void updateAttributes() {
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(Math.min(0.25D, 0.15D * this.getSeaSerpentScale() * this.getAncientModifier()));
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Math.max(4, IceAndFire.CONFIG.seaSerpentAttackStrength * this.getSeaSerpentScale() * this.getAncientModifier()));
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.max(10, IceAndFire.CONFIG.seaSerpentBaseHealth * this.getSeaSerpentScale() * this.getAncientModifier()));
        this.heal(30F * this.getSeaSerpentScale());
    }

    private float getAncientModifier() {
        return this.isAncient() ? 1.5F : 1.0F;
    }

    public float getSeaSerpentScale() {
        return Float.valueOf(this.dataManager.get(SCALE).floatValue());
    }

    private void setSeaSerpentScale(float scale) {
        this.dataManager.set(SCALE, Float.valueOf(scale));
        this.updateAttributes();
    }

    public int getVariant() {
        return Integer.valueOf(this.dataManager.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, Integer.valueOf(variant));
    }

    public boolean isJumpingOutOfWater() {
        return this.dataManager.get(JUMPING).booleanValue();
    }

    public void setJumpingOutOfWater(boolean jump) {
        this.dataManager.set(JUMPING, jump);
    }

    public boolean isAncient() {
        return this.dataManager.get(ANCIENT).booleanValue();
    }

    public void setAncient(boolean ancient) {
        this.dataManager.set(ANCIENT, ancient);
    }

    public boolean isBreathing() {
        if (world.isRemote) {
            boolean breathing = this.dataManager.get(BREATHING).booleanValue();
            this.isBreathing = breathing;
            return breathing;
        }
        return isBreathing;
    }

    public void setBreathing(boolean breathing) {
        this.dataManager.set(BREATHING, breathing);
        if (!world.isRemote) {
            this.isBreathing = breathing;
        }
    }

    public void fall(float distance, float damageMultiplier) {
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!world.isRemote) {
            if (isJumpingOutOfWater() && swimBehavior == SwimBehavior.WANDER && shouldStopJumping()) {
                motionY -= 0.25D;
                if (this.isInWater()) {
                    this.setJumpingOutOfWater(false);
                    this.ticksSinceJump = 0;
                }
            }
            if (world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getAttackTarget() instanceof EntityPlayer) {
                this.setAttackTarget(null);
            }
        }
        boolean breathing = isBreathing() && this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_ROAR;
        boolean jumping = !this.isInWater() && !this.onGround && this.motionY >= 0;
        boolean wantJumping = false; //(ticksSinceJump > TIME_BETWEEN_JUMPS) && this.isInWater();
        boolean ground = !isInWater() && this.onGround;
        boolean prevJumping = this.isJumpingOutOfWater();
        this.ticksSinceRoar++;
        this.ticksSinceJump++;
        if (this.ticksSinceRoar > TIME_BETWEEN_ROARS && isAtSurface() && this.getAnimation() != ANIMATION_BITE && jumpProgress == 0 && !isJumpingOutOfWater()) {
            this.setAnimation(ANIMATION_ROAR);
            this.ticksSinceRoar = 0;
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 1) {
            this.playSound(IafSoundRegistry.SEA_SERPENT_ROAR, this.getSoundVolume() + 1, 1);
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.SEA_SERPENT_BITE, this.getSoundVolume(), 1);
        }
        if (isJumpingOutOfWater()) {
            ticksJumping++;
        } else {
            ticksJumping = 0;
        }
        if (isJumpingOutOfWater() && isWaterBlock(world, new BlockPos(this).up(2))) {
            setJumpingOutOfWater(false);
        }
        if (!isJumpingOutOfWater() && !isWaterBlock(world, new BlockPos(this).up()) && (ticksSinceJump > TIME_BETWEEN_JUMPS || this.getAttackTarget() != null)) {
            ticksSinceJump = 0;
            setJumpingOutOfWater(true);
        }
        if (this.swimCycle < 38) {
            this.swimCycle += 2;
        } else {
            this.swimCycle = 0;
        }
        if (breathing && breathProgress < 20.0F) {
            breathProgress += 0.5F;
        } else if (!breathing && breathProgress > 0.0F) {
            breathProgress -= 0.5F;
        }
        if (jumping && jumpProgress < 10.0F) {
            jumpProgress += 0.5F;
        } else if (!jumping && jumpProgress > 0.0F) {
            jumpProgress -= 0.5F;
        }
        if (wantJumping && wantJumpProgress < 10.0F) {
            wantJumpProgress += 2F;
        } else if (!wantJumping && wantJumpProgress > 0.0F) {
            wantJumpProgress -= 2F;
        }
        if (this.isJumpingOutOfWater() && jumpRot < 1.0F) {
            jumpRot += 0.1F;
        } else if (!this.isJumpingOutOfWater() && jumpRot > 0.0F) {
            jumpRot -= 0.1F;
        }
        if (prevJumping != this.isJumpingOutOfWater() && !this.isJumpingOutOfWater()) {
            this.playSound(IafSoundRegistry.SEA_SERPENT_SPLASH, 5F, 0.75F);
            spawnSlamParticles(EnumParticleTypes.FIREWORKS_SPARK);
            spawnSlamParticles(EnumParticleTypes.WATER_BUBBLE);
            spawnSlamParticles(EnumParticleTypes.WATER_BUBBLE);
            spawnSlamParticles(EnumParticleTypes.WATER_BUBBLE);
            this.doSplashDamage();
        }
        if (!ground && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (ground && !this.isLandNavigator) {
            switchNavigator(true);
        }
        renderYawOffset = rotationYaw;
        rotationPitch = MathHelper.clamp((float) motionY * 20F, -90, 90);
        if (world.isRemote) {
            pitch_buffer.calculateChainWaveBuffer(90, 10, 10F, 0.5F, this);

            if (!jumping) {
                tail_buffer.calculateChainSwingBuffer(70, 20, 5F, this);
                head_buffer.calculateChainSwingBuffer(70, 20, 5F, this);
            }
        }
        if (changedSwimBehavior) {
            changedSwimBehavior = false;
        }
        if (!world.isRemote) {
            if (attackDecision) {
                this.setBreathing(false);
            }
            if (this.getAttackTarget() != null && this.getAnimation() != ANIMATION_ROAR) {
                if (!attackDecision) {
                    if (!this.getAttackTarget().isInWater() || !this.isDirectPathBetweenPoints(this.getAttackTarget().getPosition()) || this.getDistanceSq(this.getAttackTarget()) < 60 * this.getSeaSerpentScale()) {
                        attackDecision = true;
                    }
                    if (!attackDecision) {
                        shoot(this.getAttackTarget());
                    }
                } else {
                    if (this.getDistanceSq(this.getAttackTarget()) > 500 * this.getSeaSerpentScale()) {
                        attackDecision = false;
                    }
                }
            } else {
                this.setBreathing(false);
            }
            if (this.swimBehavior == SwimBehavior.CIRCLE) {
                ticksCircling++;
            } else {
                ticksCircling = 0;
            }
            if (this.getAttackTarget() != null) {
                if (this.isInWater()) {
                    if (this.attackDecision) {
                        if (isPreyAtSurface() && this.getDistanceSq(this.getAttackTarget()) < 200 * getSeaSerpentScale()) {
                            this.swimBehavior = SwimBehavior.JUMP;
                        } else {
                            this.swimBehavior = SwimBehavior.ATTACK;
                        }
                    } else {
                        this.swimBehavior = SwimBehavior.ATTACK;
                    }
                } else if (this.onGround) {
                    this.swimBehavior = SwimBehavior.ATTACK;
                } else {
                    this.swimBehavior = SwimBehavior.WANDER;
                }
            } else {
                if (this.swimBehavior == SwimBehavior.JUMP && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) > 200 * getSeaSerpentScale()) {
                    this.swimBehavior = SwimBehavior.WANDER;
                    this.ticksSinceJump = 0;
                    setJumpingOutOfWater(false);
                }
            }
            if (this.swimBehavior != SwimBehavior.JUMP && this.swimBehavior != SwimBehavior.ATTACK && this.ticksSinceJump > TIME_BETWEEN_JUMPS) {
                this.swimBehavior = SwimBehavior.JUMP;
            }
            if (swimBehavior != SwimBehavior.ATTACK) {
                arcingYAdditive = 0;
            }
            if (swimBehavior == SwimBehavior.JUMP && this.motionY < 0 && !this.isInWater()) {
                this.swimBehavior = SwimBehavior.WANDER;
                this.ticksSinceJump = 0;
                setJumpingOutOfWater(false);
            }
            if (this.swimBehavior == SwimBehavior.ATTACK && this.getAttackTarget() != null && !this.getAttackTarget().isInWater()) {
                this.swimBehavior = SwimBehavior.WANDER;
                this.faceEntity(this.getAttackTarget(), 360, 360);
            }
            if (this.swimBehavior == SwimBehavior.ATTACK && (this.getAttackTarget() == null || !isDirectPathBetweenPoints(new BlockPos(this.getAttackTarget())))) {
                this.swimBehavior = SwimBehavior.WANDER;
            }
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && (this.isTouchingMob(this.getAttackTarget()) || this.getDistanceSq(this.getAttackTarget()) < 50)) {
            this.hurtMob(this.getAttackTarget());
        }
        breakBlock();
        if (!world.isRemote && this.isRiding() && this.getLowestRidingEntity() instanceof EntityBoat) {
            EntityBoat boat = (EntityBoat) this.getLowestRidingEntity();
            this.dismountRidingEntity();
            boat.setDead();
        }
    }

    private void doSplashDamage() {
        double width = 2D * this.getSeaSerpentScale();
        List<Entity> list = world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(width, width * 0.5D, width), NOT_SEA_SERPENT);
        for (Entity entity : list) {
            if (entity instanceof EntityLivingBase) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
                destroyBoat(entity);
                double xRatio = this.posX - entity.posX;
                double zRatio = this.posZ - entity.posZ;
                float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
                float strength = 0.3F * this.getSeaSerpentScale();
                entity.motionX /= 2.0D;
                entity.motionZ /= 2.0D;
                entity.motionX -= xRatio / (double) f * (double) strength;
                entity.motionZ -= zRatio / (double) f * (double) strength;
                entity.motionY += strength;
                if (this.motionY > 0.4000000059604645D) {
                    this.motionY = 0.4000000059604645D;
                }
            }
        }

    }

    public void destroyBoat(Entity sailor) {
        if (sailor.getRidingEntity() != null && sailor.getRidingEntity() instanceof EntityBoat && !world.isRemote) {
            EntityBoat boat = (EntityBoat) sailor.getRidingEntity();
            boat.setDead();
            if (this.world.getGameRules().getBoolean("doEntityDrops")) {
                int meta;
                try {
                    meta = boat.getBoatType().getMetadata();
                } catch (Exception e) {
                    meta = 0;
                }
                for (int i = 0; i < 3; ++i) {
                    boat.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, meta), 0.0F);
                }
                for (int j = 0; j < 2; ++j) {
                    boat.dropItemWithOffset(Items.STICK, 1, 0.0F);
                }
            }
        }
    }

    private boolean isPreyAtSurface() {
        if (this.getAttackTarget() != null) {
            BlockPos pos = new BlockPos(this.getAttackTarget());
            return !isWaterBlock(world, pos.up((int) Math.ceil(this.getAttackTarget().height)));
        }
        return false;
    }

    private void hurtMob(EntityLivingBase entity) {
        if (this.getAnimation() == ANIMATION_BITE && entity != null && this.getAnimationTick() > 6) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
            EntitySeaSerpent.this.attackDecision = this.getRNG().nextBoolean();
        }
    }

    public void moveJumping() {
        float velocity = 0.5F;
        double x = -MathHelper.sin(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F);
        double z = MathHelper.cos(this.rotationYaw * 0.017453292F) * MathHelper.cos(this.rotationPitch * 0.017453292F);
        float f = MathHelper.sqrt(x * x + z * z);
        x = x / (double) f;
        z = z / (double) f;
        x = x * (double) velocity;
        z = z * (double) velocity;
        this.motionX = x;
        this.motionZ = z;
    }

    private boolean isTouchingMob(Entity entity) {
        if (this.getEntityBoundingBox().expand(this.getSeaSerpentScale() * 3, this.getSeaSerpentScale() * 3, this.getSeaSerpentScale() * 3).intersects(entity.getEntityBoundingBox())) {
            return true;
        }
        for (Entity segment : segments) {
            if (segment.getEntityBoundingBox().expand(this.getSeaSerpentScale() * 2, this.getSeaSerpentScale() * 2, this.getSeaSerpentScale() * 2).intersects(entity.getEntityBoundingBox())) {
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

    public void breakBlock() {
        if (IceAndFire.CONFIG.seaSerpentGriefing) {
            for (int a = (int) Math.round(this.getEntityBoundingBox().minX) - 2; a <= (int) Math.round(this.getEntityBoundingBox().maxX) + 2; a++) {
                for (int b = (int) Math.round(this.getEntityBoundingBox().minY) - 1; (b <= (int) Math.round(this.getEntityBoundingBox().maxY) + 2) && (b <= 127); b++) {
                    for (int c = (int) Math.round(this.getEntityBoundingBox().minZ) - 2; c <= (int) Math.round(this.getEntityBoundingBox().maxZ) + 2; c++) {
                        BlockPos pos = new BlockPos(a, b, c);
                        IBlockState state = world.getBlockState(pos);
                        Block block = state.getBlock();
                        if (state.getMaterial() != Material.AIR && !(block instanceof BlockLiquid) && (state.getMaterial() == Material.PLANTS || state.getMaterial() == Material.LEAVES)) {
                            if (block != Blocks.AIR) {
                                if (!world.isRemote) {
                                    world.destroyBlock(pos, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setVariant(this.getRNG().nextInt(7));
        boolean ancient = this.getRNG().nextInt(16) == 1;
        if (ancient) {
            this.setAncient(true);
            this.setSeaSerpentScale(6.0F + this.getRNG().nextFloat() * 3.0F);

        } else {
            this.setSeaSerpentScale(1.5F + this.getRNG().nextFloat() * 4.0F);
        }
        return livingdata;
    }

    public void onWorldSpawn(Random random) {
        this.setVariant(random.nextInt(6));
        boolean ancient = random.nextInt(15) == 1;
        if (ancient) {
            this.setAncient(true);
            this.setSeaSerpentScale(6.0F + random.nextFloat() * 3.0F);

        } else {
            this.setSeaSerpentScale(1.5F + random.nextFloat() * 4.0F);
        }
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
        return new Animation[]{ANIMATION_BITE, ANIMATION_ROAR, ANIMATION_SPEAK};
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.SEA_SERPENT_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return IafSoundRegistry.SEA_SERPENT_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.SEA_SERPENT_DIE;
    }

    public void playLivingSound() {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playLivingSound();
    }

    protected void playHurtSound(DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
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

    public boolean wantsToJump() {
        return this.ticksSinceJump > TIME_BETWEEN_JUMPS && this.swimBehavior == SwimBehavior.JUMP;
    }

    private boolean shouldStopJumping() {
        return ticksJumping > 30 || !isWaterBlock(world, world.getHeight(new BlockPos(this)).down(1));
    }

    public boolean isAtSurface() {
        BlockPos pos = new BlockPos(this);
        return isWaterBlock(world, pos.down()) && !isWaterBlock(world, pos.up());
    }

    private void shoot(EntityLivingBase entity) {
        if (!this.attackDecision) {
            if (!this.isInWater()) {
                this.setBreathing(false);
                this.attackDecision = true;
            }
            if (this.isBreathing()) {
                if (this.ticksExisted % 40 == 0) {
                    this.playSound(IafSoundRegistry.SEA_SERPENT_BREATH, 4, 1);
                }
                if (this.ticksExisted % 5 == 0) {
                    rotationYaw = renderYawOffset;
                    float f1 = 0;
                    float f2 = 0;
                    float f3 = 0;
                    float headPosX = f1 + (float) (this.segments[0].posX + 0.3F * getSeaSerpentScale() * Math.cos((rotationYaw + 90) * Math.PI / 180));
                    float headPosZ = f2 + (float) (this.segments[0].posZ + 0.3F * getSeaSerpentScale() * Math.sin((rotationYaw + 90) * Math.PI / 180));
                    float headPosY = f3 + (float) (this.segments[0].posY + 0.2F * getSeaSerpentScale());
                    double d2 = entity.posX - headPosX;
                    double d3 = entity.posY - headPosY;
                    double d4 = entity.posZ - headPosZ;
                    EntitySeaSerpentBubbles entitylargefireball = new EntitySeaSerpentBubbles(world, this, d2, d3, d4);
                    float size = 0.8F;
                    entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
                    if (!world.isRemote && !entity.isDead) {
                        world.spawnEntity(entitylargefireball);
                    }
                    entitylargefireball.setSizes(size, size);
                    if (entity.isDead || entity == null) {
                        this.setBreathing(false);
                        this.attackDecision = this.getRNG().nextBoolean();
                    }
                }
            } else {
                this.setBreathing(true);
            }
        }
        this.faceEntity(entity, 360, 360);
    }

    public EnumSeaSerpent getEnum() {
        switch (this.getVariant()) {
            default:
                return EnumSeaSerpent.BLUE;
            case 1:
                return EnumSeaSerpent.BRONZE;
            case 2:
                return EnumSeaSerpent.DEEPBLUE;
            case 3:
                return EnumSeaSerpent.GREEN;
            case 4:
                return EnumSeaSerpent.PURPLE;
            case 5:
                return EnumSeaSerpent.RED;
            case 6:
                return EnumSeaSerpent.TEAL;
        }
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.swimBehavior == SwimBehavior.JUMP && this.isJumpingOutOfWater() && this.getAttackTarget() == null) {
            // moveJumping();
        }
        float f4;
        if (this.isServerWorld()) {
            float f5;
            if (this.isInWater()) {
                this.moveRelative(strafe, vertical, forward, 0.1F);
                f4 = 0.6F;
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
                this.motionX *= f4;
                this.motionX *= 0.900000011920929D;
                this.motionY *= 0.900000011920929D;
                this.motionY *= f4;
                this.motionZ *= 0.900000011920929D;
                this.motionZ *= f4;
            } else {
                super.travel(strafe, vertical, forward);
            }
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double deltaX = this.posX - this.prevPosX;
        double deltaZ = this.posZ - this.prevPosZ;
        double deltaY = this.posY - this.prevPosY;
        float delta = MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 4.0F;
        if (delta > 1.0F) {
            delta = 1.0F;
        }
        this.limbSwingAmount += (delta - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public void onKillEntity(EntityLivingBase entity) {
        super.onKillEntity(entity);
        this.attackDecision = this.getRNG().nextBoolean();
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    public int getMaxFallHeight() {
        return 1000;
    }

    enum SwimBehavior {
        CIRCLE,
        WANDER,
        ATTACK,
        JUMP,
        NONE
    }

    public class SwimmingMoveHelper extends EntityMoveHelper {
        private EntitySeaSerpent serpent = EntitySeaSerpent.this;

        public SwimmingMoveHelper() {
            super(EntitySeaSerpent.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - EntitySeaSerpent.this.posX;
                double d1 = this.posY - EntitySeaSerpent.this.posY;
                double d2 = this.posZ - EntitySeaSerpent.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = MathHelper.sqrt(d3);
                if (d3 < 6 && EntitySeaSerpent.this.getAttackTarget() == null) {
                    if (!EntitySeaSerpent.this.changedSwimBehavior && EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.WANDER && EntitySeaSerpent.this.rand.nextInt(20) == 0) {
                        EntitySeaSerpent.this.swimBehavior = EntitySeaSerpent.SwimBehavior.CIRCLE;
                        EntitySeaSerpent.this.changedSwimBehavior = true;
                    }
                    if (!EntitySeaSerpent.this.changedSwimBehavior && EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.CIRCLE && EntitySeaSerpent.this.rand.nextInt(5) == 0 && ticksCircling > 150) {
                        EntitySeaSerpent.this.swimBehavior = EntitySeaSerpent.SwimBehavior.WANDER;
                        EntitySeaSerpent.this.changedSwimBehavior = true;
                    }
                }
                if (EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP && !EntitySeaSerpent.this.isInWater() && !onGround) {
                    EntitySeaSerpent.this.ticksSinceJump = 0;
                }
                int dist = EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP ? 10 : 3;
                if (d3 < dist && EntitySeaSerpent.this.getAttackTarget() == null || EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP && EntitySeaSerpent.this.shouldStopJumping() && EntitySeaSerpent.this.getAttackTarget() == null) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntitySeaSerpent.this.motionX *= 0.5D;
                    EntitySeaSerpent.this.motionZ *= 0.5D;
                    if (EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                        EntitySeaSerpent.this.swimBehavior = SwimBehavior.WANDER;
                        EntitySeaSerpent.this.ticksSinceJump = 0;
                        EntitySeaSerpent.this.setJumpingOutOfWater(false);
                    }
                } else {
                    EntitySeaSerpent.this.motionX += d0 / d3 * 0.5D * this.speed;
                    EntitySeaSerpent.this.motionY += d1 / d3 * 0.5D * this.speed;
                    EntitySeaSerpent.this.motionZ += d2 / d3 * 0.5D * this.speed;
                    float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
                    this.entity.rotationPitch = f1;
                    if (!EntitySeaSerpent.this.isArcing) {
                        if (EntitySeaSerpent.this.getAttackTarget() == null) {
                            EntitySeaSerpent.this.rotationYaw = -((float) MathHelper.atan2(EntitySeaSerpent.this.motionX, EntitySeaSerpent.this.motionZ)) * (180F / (float) Math.PI);
                            EntitySeaSerpent.this.renderYawOffset = EntitySeaSerpent.this.rotationYaw;
                        } else if (EntitySeaSerpent.this.swimBehavior != SwimBehavior.JUMP) {
                            double d4 = EntitySeaSerpent.this.getAttackTarget().posX - EntitySeaSerpent.this.posX;
                            double d5 = EntitySeaSerpent.this.getAttackTarget().posZ - EntitySeaSerpent.this.posZ;
                            EntitySeaSerpent.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                            EntitySeaSerpent.this.renderYawOffset = EntitySeaSerpent.this.rotationYaw;
                        }
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
            if (EntitySeaSerpent.this.swimBehavior != EntitySeaSerpent.SwimBehavior.WANDER && EntitySeaSerpent.this.swimBehavior != EntitySeaSerpent.SwimBehavior.JUMP || !EntitySeaSerpent.this.canMove() || EntitySeaSerpent.this.getAttackTarget() != null) {
                return false;
            }
            if (EntitySeaSerpent.this.isInWater()) {
                BlockPos gen = generateTarget();
                if (gen != null) {
                    target = gen;
                    EntitySeaSerpent.this.orbitPos = null;
                    return EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP || !EntitySeaSerpent.this.getMoveHelper().isUpdating();
                }
            }
            return false;
        }

        protected BlockPos generateTarget() {
            if (EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                BlockPos pos = EntitySeaSerpent.getPositionRelativeToSeafloor(EntitySeaSerpent.this, EntitySeaSerpent.this.world, EntitySeaSerpent.this.posX, EntitySeaSerpent.this.posZ, EntitySeaSerpent.this.rand);
                return pos.up(3 * (int) Math.ceil(EntitySeaSerpent.this.getSeaSerpentScale()));
            }
            for (int i = 0; i < 5; i++) {
                BlockPos pos = EntitySeaSerpent.getPositionRelativeToSeafloor(EntitySeaSerpent.this, EntitySeaSerpent.this.world, EntitySeaSerpent.this.posX + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.posZ + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.rand);
                if (EntitySeaSerpent.isWaterBlock(world, pos) && EntitySeaSerpent.this.isDirectPathBetweenPoints(pos) || EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                    return pos;
                }
            }
            return null;
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            return true;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            if (target == null)
                target = generateTarget();
            if (target != null && (EntitySeaSerpent.isWaterBlock(world, target) || EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.JUMP) && EntitySeaSerpent.this.isDirectPathBetweenPoints(target)) {
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
            if (!EntitySeaSerpent.this.getMoveHelper().isUpdating()) {
                EntitySeaSerpent.this.swimBehavior = SwimBehavior.WANDER;
                return false;
            }
            if (EntitySeaSerpent.this.isInWater() && !EntitySeaSerpent.this.isJumpingOutOfWater()) {
                BlockPos gen = generateTarget();
                if (gen != null) {
                    EntitySeaSerpent.this.orbitPos = gen;
                    target = EntitySeaSerpent.getPositionInOrbit(EntitySeaSerpent.this, world, EntitySeaSerpent.this.orbitPos, EntitySeaSerpent.this.rand);
                    return EntitySeaSerpent.this.isDirectPathBetweenPoints(target);
                } else {
                    EntitySeaSerpent.this.swimBehavior = SwimBehavior.WANDER;
                }
            }
            return false;
        }

        protected BlockPos generateTarget() {
            for (int i = 0; i < 5; i++) {
                BlockPos pos = EntitySeaSerpent.getPositionRelativeToSeafloor(EntitySeaSerpent.this, EntitySeaSerpent.this.world, EntitySeaSerpent.this.posX + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.posZ + EntitySeaSerpent.this.rand.nextInt(30) - 15, EntitySeaSerpent.this.rand);
                if (EntitySeaSerpent.isWaterBlock(world, pos) && EntitySeaSerpent.this.isDirectPathBetweenPoints(pos)) {
                    return pos;
                }
            }
            return null;
        }


        public boolean shouldContinueExecuting() {
            if (target != null && !EntitySeaSerpent.this.isDirectPathBetweenPoints(target)) {
                return false;
            }
            return EntitySeaSerpent.this.getAttackTarget() == null && EntitySeaSerpent.this.swimBehavior == EntitySeaSerpent.SwimBehavior.CIRCLE;
        }

        public void updateTask() {
            if (EntitySeaSerpent.this.getDistance(target.getX(), target.getY(), target.getZ()) < 5) {
                target = EntitySeaSerpent.getPositionInOrbit(EntitySeaSerpent.this, world, EntitySeaSerpent.this.orbitPos, EntitySeaSerpent.this.rand);
            }
            if (EntitySeaSerpent.isWaterBlock(world, target) && EntitySeaSerpent.this.isDirectPathBetweenPoints(target)) {
                EntitySeaSerpent.this.moveHelper.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.25D);
                if (EntitySeaSerpent.this.getAttackTarget() == null) {
                    EntitySeaSerpent.this.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                }
            }
        }
    }

    class AISwimBite extends EntityAIBase {
        public int max_distance = 1000;
        BlockPos target;
        boolean secondPhase = false;
        boolean isOver = false;

        public AISwimBite() {
            this.setMutexBits(1);
        }

        public void resetTask() {
            target = null;
            secondPhase = false;
            isOver = false;
            EntitySeaSerpent.this.isArcing = false;
            EntitySeaSerpent.this.arcingYAdditive = 0;

        }

        public boolean shouldExecute() {
            if (!EntitySeaSerpent.this.attackDecision && EntitySeaSerpent.this.getAttackTarget() != null && EntitySeaSerpent.this.getDistanceSq(EntitySeaSerpent.this.getAttackTarget()) < 300) {
                return false;
            }
            if (EntitySeaSerpent.this.swimBehavior != SwimBehavior.ATTACK && EntitySeaSerpent.this.swimBehavior != SwimBehavior.JUMP || !EntitySeaSerpent.this.canMove() || EntitySeaSerpent.this.getAttackTarget() == null) {
                return false;
            }
            if (EntitySeaSerpent.this.isInWater() && EntitySeaSerpent.this.getAttackTarget() != null) {
                target = new BlockPos(EntitySeaSerpent.this.getAttackTarget());
                EntitySeaSerpent.this.orbitPos = null;
                secondPhase = false;
                EntitySeaSerpent.this.isArcing = false;
                return true;
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
            return true;
        }

        public boolean shouldContinueExecuting() {
            if (isOver) {
                return false;
            }
            if (!EntitySeaSerpent.this.attackDecision && EntitySeaSerpent.this.getAttackTarget() != null && EntitySeaSerpent.this.getDistanceSq(EntitySeaSerpent.this.getAttackTarget()) < 300) {
                EntitySeaSerpent.this.moveHelper.action = EntityMoveHelper.Action.WAIT;
                resetTask();
                return false;
            }
            if (secondPhase) {
                if (EntitySeaSerpent.this.getAttackTarget() == null || EntitySeaSerpent.this.getDistanceSq(target) > max_distance || isOver) {
                    EntitySeaSerpent.this.swimBehavior = SwimBehavior.WANDER;
                    resetTask();
                    return false;
                } else {
                    return true;
                }
            } else {
                return EntitySeaSerpent.this.getAttackTarget() != null;
            }
        }

        public void updateTask() {
            if (EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                if (EntitySeaSerpent.this.getAttackTarget() != null) {
                    if (EntitySeaSerpent.this.isInWater()) {
                        target = new BlockPos(EntitySeaSerpent.this.getAttackTarget()).up((int) Math.ceil(3 * EntitySeaSerpent.this.getSeaSerpentScale()));
                    }
                }
            } else {
                if (EntitySeaSerpent.this.getAttackTarget() == null || EntitySeaSerpent.this.getAttackTarget().isDead) {
                    this.secondPhase = true;
                } else {
                    double d0 = EntitySeaSerpent.this.getAttackTarget().posX - EntitySeaSerpent.this.posX;
                    double d1 = EntitySeaSerpent.this.getAttackTarget().posZ - EntitySeaSerpent.this.posZ;
                    double d2 = d0 * d0 + d1 * d1;
                    d2 = MathHelper.sqrt(d2);
                    EntitySeaSerpent.this.arcingYAdditive = (secondPhase ? 1 : -1) * (float) d2;
                }
                if (!secondPhase) {
                    target = new BlockPos(EntitySeaSerpent.this.getAttackTarget());
                    if (!EntitySeaSerpent.this.attackDecision) {
                        if (EntitySeaSerpent.this.getAttackTarget() != null) {
                            if (EntitySeaSerpent.this.getDistanceSq(target) > 10 * EntitySeaSerpent.this.getSeaSerpentScale()) {
                                EntitySeaSerpent.this.setBreathing(true);
                            } else {
                                EntitySeaSerpent.this.attackDecision = true;
                            }
                        }
                    } else {
                        if (EntitySeaSerpent.this.getAttackTarget() != null && (EntitySeaSerpent.this.getDistanceSq(target) < 30 * EntitySeaSerpent.this.getSeaSerpentScale() || EntitySeaSerpent.this.isTouchingMob(EntitySeaSerpent.this.getAttackTarget()))) {
                            EntitySeaSerpent.this.setAnimation(ANIMATION_BITE);
                        }
                        if (EntitySeaSerpent.this.getAttackTarget() == null || EntitySeaSerpent.this.getDistanceSq(target) < 30 * EntitySeaSerpent.this.getSeaSerpentScale()) {
                            target = null;
                            secondPhase = true;
                        }
                    }
                }
                if (secondPhase) {
                    if (EntitySeaSerpent.this.getAttackTarget() != null) {
                        if (EntitySeaSerpent.this.getDistanceSq(EntitySeaSerpent.this.getAttackTarget()) > max_distance || target != null && EntitySeaSerpent.this.getDistanceSq(target) < 5) {
                            resetTask();
                            isOver = true;
                        } else {
                            target = EntitySeaSerpent.getPositionPreyArc(EntitySeaSerpent.this, new BlockPos(EntitySeaSerpent.this.getAttackTarget()), world);
                        }
                    }
                }
            }

            if (target != null) {
                if (EntitySeaSerpent.isWaterBlock(world, target) || EntitySeaSerpent.this.swimBehavior == SwimBehavior.JUMP) {
                    EntitySeaSerpent.this.moveHelper.setMoveTo((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 0.5D);
                    if (EntitySeaSerpent.this.getAttackTarget() == null) {
                        EntitySeaSerpent.this.getLookHelper().setLookPosition((double) target.getX() + 0.5D, (double) target.getY() + 0.5D, (double) target.getZ() + 0.5D, 180.0F, 20.0F);

                    }
                }
            }

        }
    }
}
