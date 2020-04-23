package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAITargetSheepPlayers;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateCyclops;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDragon;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class EntityCyclops extends EntityMob implements IAnimatedEntity, IBlacklistedFromStatues, IVillagerFear, IHumanoid {

    public static final ResourceLocation LOOT = LootTableList.register(new ResourceLocation("iceandfire", "cyclops"));
    private static final DataParameter<Boolean> BLINDED = EntityDataManager.createKey(EntityCyclops.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityCyclops.class, DataSerializers.VARINT);
    public static Animation ANIMATION_STOMP;
    public static Animation ANIMATION_EATPLAYER;
    public static Animation ANIMATION_KICK;
    public static Animation ANIMATION_ROAR;
    public PartEntity eyeEntity;
    private int animationTick;
    private Animation currentAnimation;

    public EntityCyclops(World worldIn) {
        super(worldIn);
        this.setSize(1.95F, 7.4F);
        this.stepHeight = 2.5F;
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.FENCE, 0.0F);

        eyeEntity = new EntityCyclopsEye(this, 0.2F, 0, 7.4F, 1.2F, 0.5F, 1);
        ANIMATION_STOMP = Animation.create(27);
        ANIMATION_EATPLAYER = Animation.create(40);
        ANIMATION_KICK = Animation.create(20);
        ANIMATION_ROAR = Animation.create(30);

    }

    protected PathNavigate createNavigator(World worldIn) {
        return new PathNavigateCyclops(this, world);
    }


    protected int getExperiencePoints(EntityPlayer player) {
        return 40;
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new CyclopsAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, true, true, new Predicate<EntityLivingBase>() {
            @Override
            public boolean apply(@Nullable EntityLivingBase entity) {
                return !EntityGorgon.isStoneMob(entity) && DragonUtils.isAlive(entity) && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityPlayer) && !(entity instanceof EntityCyclops) && !ServerEvents.isAnimaniaSheep(entity) && !(entity instanceof EntityAnimal && !(entity instanceof EntityWolf || entity instanceof EntityPolarBear || entity instanceof EntityDragonBase)) || entity instanceof EntityGorgon || entity instanceof EntityVillager;
            }
        }));
        this.targetTasks.addTask(3, new CyclopsAITargetSheepPlayers(this, EntityPlayer.class, 0, true, true, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return true;
            }
        }));
    }

    protected void collideWithEntity(Entity entityIn) {
        if (!ServerEvents.isAnimaniaSheep(entityIn)) {
            entityIn.applyEntityCollision(this);
        }
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        int attackDescision = this.getRNG().nextInt(3);
        if (attackDescision == 0) {
            this.setAnimation(ANIMATION_STOMP);
            return true;
        } else if (attackDescision == 1) {
            if (!entityIn.isPassenger(this) && entityIn.width < 1.95F && !(entityIn instanceof EntityDragonBase)) {
                this.setAnimation(ANIMATION_EATPLAYER);
                entityIn.dismountRidingEntity();
                entityIn.startRiding(this, true);
            } else {
                this.setAnimation(ANIMATION_STOMP);
            }
            return true;
        } else {
            this.setAnimation(ANIMATION_KICK);
            return true;
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.cyclopsAttackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.cyclopsMaxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);

    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(BLINDED, Boolean.valueOf(false));
        this.dataManager.register(VARIANT, Integer.valueOf(0));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Blind", this.isBlinded());
        compound.setInteger("Variant", this.getVariant());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setBlinded(compound.getBoolean("Blind"));
        this.setVariant(compound.getInteger("Variant"));
    }

    public int getVariant() {
        return Integer.valueOf(this.dataManager.get(VARIANT).intValue());
    }

    public void setVariant(int variant) {
        this.dataManager.set(VARIANT, Integer.valueOf(variant));
    }

    public boolean isBlinded() {
        return Boolean.valueOf(this.dataManager.get(BLINDED).booleanValue());
    }

    public void setBlinded(boolean blind) {
        this.dataManager.set(BLINDED, Boolean.valueOf(blind));
    }

    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger)) {
            passenger.motionX = 0;
            passenger.motionZ = 0;
            this.setAnimation(ANIMATION_EATPLAYER);
            double raiseUp = this.getAnimationTick() < 10 ? 0 : Math.min((this.getAnimationTick() * 3 - 30) * 0.2, 5.2F);
            float pullIn = this.getAnimationTick() < 15 ? 0 : Math.min((this.getAnimationTick() - 15) * 0.15F, 0.75F);
            renderYawOffset = rotationYaw;
            this.rotationYaw *= 0;
            float radius = -2.75F + pullIn;
            float angle = (0.01745329251F * this.renderYawOffset) + 3.15F;
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraZ = (double) (radius * MathHelper.cos(angle));
            double extraY = raiseUp;
            passenger.setPosition(this.posX + extraX, this.posY + extraY, this.posZ + extraZ);
            if (this.getAnimationTick() == 32) {
                passenger.attackEntityFrom(DamageSource.causeMobDamage(this), passenger instanceof EntityPlayer ? (float) IceAndFire.CONFIG.cyclopsBiteStrength : passenger instanceof EntityLivingBase ? ((EntityLivingBase) passenger).getMaxHealth() * 2F : (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 2F);
                passenger.dismountRidingEntity();
            }
        }
    }

    @Override
    public void travel(float strafe, float forward, float vertical) {
        if (this.getAnimation() == ANIMATION_EATPLAYER) {
            strafe = 0;
            forward = 0;
            vertical = 0;
            super.travel(strafe, forward, vertical);
            return;
        }
        super.travel(strafe, forward, vertical);
    }

    public boolean canPassengerSteer() {
        return false;
    }

    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }


    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getAttackTarget() instanceof EntityPlayer){
            this.setAttackTarget(null);
        }
        if (this.isBlinded() && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) > 6) {
            this.setAttackTarget(null);
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.CYCLOPS_BLINDED, 1, 1);
        }
        if (this.getAnimation() == ANIMATION_EATPLAYER && this.getAnimationTick() == 25) {
            this.playSound(IafSoundRegistry.CYCLOPS_BITE, 1, 1);
        }
        if (this.getAnimation() == ANIMATION_STOMP && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 12D && this.getAnimationTick() == 14) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }
        if (this.getAnimation() == ANIMATION_KICK && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 14D && this.getAnimationTick() == 12) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
            this.getAttackTarget().knockBack(this, 2, 1, 1);

        }
        if (this.getAnimation() != ANIMATION_EATPLAYER && this.getAttackTarget() != null && !this.getPassengers().isEmpty() && this.getPassengers().contains(this.getAttackTarget())) {
            this.setAnimation(ANIMATION_EATPLAYER);
        }
        if (this.getAnimation() == NO_ANIMATION && this.getAttackTarget() != null && this.getRNG().nextInt(100) == 0) {
            this.setAnimation(ANIMATION_ROAR);
        }
        if (this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() == 14) {
            for (int i1 = 0; i1 < 20; i1++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float radius = 0.75F * -2F;
                float angle = (0.01745329251F * this.renderYawOffset) + i1 * 1F;
                double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
                double extraY = 0.8F;
                double extraZ = (double) (radius * MathHelper.cos(angle));

                IBlockState iblockstate = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX + extraX), MathHelper.floor(this.posY + extraY) - 1, MathHelper.floor(this.posZ + extraZ)));
                if (iblockstate.getMaterial() != Material.AIR) {
                    if (world.isRemote) {
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, this.posX + extraX, this.posY + extraY, this.posZ + extraZ, motionX, motionY, motionZ, Block.getStateId(iblockstate));
                    }
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        eyeEntity.onUpdate();
        breakBlock();
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setVariant(this.getRNG().nextInt(4));
        return livingdata;
    }

    public void breakBlock() {
        if (IceAndFire.CONFIG.cyclopsGriefing) {
            for (int a = (int) Math.round(this.getEntityBoundingBox().minX) - 1; a <= (int) Math.round(this.getEntityBoundingBox().maxX) + 1; a++) {
                for (int b = (int) Math.round(this.getEntityBoundingBox().minY) + 1; (b <= (int) Math.round(this.getEntityBoundingBox().maxY) + 2) && (b <= 127); b++) {
                    for (int c = (int) Math.round(this.getEntityBoundingBox().minZ) - 1; c <= (int) Math.round(this.getEntityBoundingBox().maxZ) + 1; c++) {
                        BlockPos pos = new BlockPos(a, b, c);
                        IBlockState state = world.getBlockState(pos);
                        Block block = state.getBlock();
                        if (state.getMaterial() != Material.AIR && !(block instanceof BlockBush) && !(block instanceof BlockLiquid) && block != Blocks.BEDROCK && (state.getBlock().isLeaves(state, world, pos) || state.getBlock().canSustainLeaves(state, world, pos))) {
                            this.motionX *= 0.6D;
                            this.motionZ *= 0.6D;
                            if (MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, a, b, c))) continue;
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
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    public void setDead() {
        if (eyeEntity != null) {
            world.removeEntityDangerously(eyeEntity);
        }
        super.setDead();
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
        return new Animation[]{NO_ANIMATION, ANIMATION_STOMP, ANIMATION_EATPLAYER, ANIMATION_KICK, ANIMATION_ROAR};
    }

    public boolean isBlinking() {
        return this.ticksExisted % 50 > 40 && !this.isBlinded();
    }


    public void onHitEye(DamageSource source, float damage) {
        if (!this.isBlinded()) {
            this.setBlinded(true);
            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(6F);
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
            this.setAnimation(ANIMATION_ROAR);
            this.attackEntityFrom(source, damage * 3);
        }
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.CYCLOPS_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return IafSoundRegistry.CYCLOPS_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.CYCLOPS_DIE;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return !this.isBlinded();
    }

    @Override
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

}
