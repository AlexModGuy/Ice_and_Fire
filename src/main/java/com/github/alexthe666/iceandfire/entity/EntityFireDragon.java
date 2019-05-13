package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityFireDragon extends EntityDragonBase {

    public static Animation ANIMATION_FIRECHARGE;
    public static final float[] growth_stage_1 = new float[]{1F, 3F};
    public static final float[] growth_stage_2 = new float[]{3F, 7F};
    public static final float[] growth_stage_3 = new float[]{7F, 12.5F};
    public static final float[] growth_stage_4 = new float[]{12.5F, 20F};
    public static final float[] growth_stage_5 = new float[]{20F, 30F};
    public static final ResourceLocation FEMALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/fire_dragon_female"));
    public static final ResourceLocation MALE_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/fire_dragon_male"));
    public static final ResourceLocation SKELETON_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "dragon/fire_dragon_skeleton"));

    public EntityFireDragon(World worldIn) {
        super(worldIn, 1, 1 + IceAndFire.CONFIG.dragonAttackDamage, IceAndFire.CONFIG.dragonHealth * 0.04, IceAndFire.CONFIG.dragonHealth, 0.15F, 0.4F);
        this.setSize(0.78F, 1.2F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
        this.isImmuneToFire = true;
        ANIMATION_SPEAK = Animation.create(20);
        ANIMATION_BITE = Animation.create(35);
        ANIMATION_SHAKEPREY = Animation.create(65);
        ANIMATION_TAILWHACK = Animation.create(40);
        ANIMATION_FIRECHARGE = Animation.create(25);
        ANIMATION_WINGBLAST = Animation.create(50);
        ANIMATION_ROAR = Animation.create(40);
        this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
        this.stepHeight = 1;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, this.aiSit = new EntityAISit(this));
        this.tasks.addTask(2, new EntityAISwimming(this));
        this.tasks.addTask(3, new DragonAIMate(this, 1.0D));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.5D, false));
        this.tasks.addTask(5, new AquaticAITempt(this, 1.0D, ModItems.fire_stew, false));
        this.tasks.addTask(6, new DragonAIAirTarget(this));
        this.tasks.addTask(7, new DragonAIWander(this, 1.0D));
        this.tasks.addTask(8, new DragonAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(8, new DragonAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(4, new DragonAITarget(this, EntityLivingBase.class, true, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof EntityLivingBase && DragonUtils.isAlive((EntityLivingBase) entity);
            }
        }));
        this.targetTasks.addTask(5, new DragonAITargetItems(this, false));
    }

    public String getVariantName(int variant) {
        switch (variant) {
            default:
                return "red_";
            case 1:
                return "green_";
            case 2:
                return "bronze_";
            case 3:
                return "gray_";
        }
    }

    public Item getVariantScale(int variant) {
        switch (variant) {
            default:
                return ModItems.dragonscales_red;
            case 1:
                return ModItems.dragonscales_green;
            case 2:
                return ModItems.dragonscales_bronze;
            case 3:
                return ModItems.dragonscales_gray;
        }
    }

    public Item getVariantEgg(int variant) {
        switch (variant) {
            default:
                return ModItems.dragonegg_red;
            case 1:
                return ModItems.dragonegg_green;
            case 2:
                return ModItems.dragonegg_bronze;
            case 3:
                return ModItems.dragonegg_gray;
        }
    }

    public boolean canBeSteered() {
        return true;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getAnimation() == ANIMATION_WINGBLAST) {
            return false;
        }
        switch (new Random().nextInt(4)) {
            case 0:
                if (this.getAnimation() != this.ANIMATION_BITE) {
                    this.setAnimation(this.ANIMATION_BITE);
                    return false;
                } else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
                    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
                    this.attackDecision = this.getRNG().nextBoolean();
                    return flag;
                }
                break;
            case 1:
                if (new Random().nextInt(2) == 0 && isDirectPathBetweenPoints(this, this.getPositionVector(), entityIn.getPositionVector()) && entityIn.width < this.width * 0.5F && this.getControllingPassenger() == null && this.getDragonStage() > 1 && !(entityIn instanceof EntityDragonBase) && !DragonUtils.isAnimaniaMob(entityIn)) {
                    if (this.getAnimation() != this.ANIMATION_SHAKEPREY) {
                        this.setAnimation(this.ANIMATION_SHAKEPREY);
                        entityIn.startRiding(this);
                        this.attackDecision = this.getRNG().nextBoolean();
                        return true;
                    }
                } else {
                    if (this.getAnimation() != this.ANIMATION_BITE) {
                        this.setAnimation(this.ANIMATION_BITE);
                        return false;
                    } else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
                        boolean flag1 = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
                        this.attackDecision = this.getRNG().nextBoolean();
                        return flag1;
                    }
                }
                break;
            case 2:
                if (this.getAnimation() != this.ANIMATION_TAILWHACK) {
                    this.setAnimation(this.ANIMATION_TAILWHACK);
                    return false;
                } else if (this.getAnimationTick() > 20 && this.getAnimationTick() < 25) {
                    boolean flag2 = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
                    if (entityIn instanceof EntityLivingBase) {
                        ((EntityLivingBase) entityIn).knockBack(entityIn, 1, 1, 1);
                    }
                    this.attackDecision = this.getRNG().nextBoolean();
                    return flag2;
                }
                break;
            case 3:
                if (this.onGround && !this.isHovering() && !this.isFlying() && this.getDragonStage() > 2) {
                    if (this.getAnimation() != this.ANIMATION_WINGBLAST) {
                        this.setAnimation(this.ANIMATION_WINGBLAST);
                        return true;
                    }
                } else {
                    if (this.getAnimation() != this.ANIMATION_BITE) {
                        this.setAnimation(this.ANIMATION_BITE);
                        return false;
                    } else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
                        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
                        this.attackDecision = this.getRNG().nextBoolean();
                        return flag;
                    }
                }

                break;
            default:
                if (this.getAnimation() != this.ANIMATION_BITE) {
                    this.setAnimation(this.ANIMATION_BITE);
                    return false;
                } else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
                    boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
                    this.attackDecision = this.getRNG().nextBoolean();
                    return flag;
                }
                break;
        }

        return false;
    }

    public void moveEntityTowards(Entity entity, double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x = x / (double) f;
        y = y / (double) f;
        z = z / (double) f;
        x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        x = x * (double) velocity;
        y = y * (double) velocity;
        z = z * (double) velocity;
        entity.motionX = x;
        entity.motionY = y;
        entity.motionZ = z;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!world.isRemote) {
            if (this.getAttackTarget() != null && !this.isSleeping() && this.getAnimation() != ANIMATION_SHAKEPREY) {
                if ((!attackDecision || this.isFlying()) && !isTargetBlocked(new Vec3d(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ))) {
                    shootFireAtMob(this.getAttackTarget());
                } else {
                    if (this.getEntityBoundingBox().grow(this.getRenderSize() * 0.5F, this.getRenderSize() * 0.5F, this.getRenderSize() * 0.5F).intersects(this.getAttackTarget().getEntityBoundingBox())) {
                        attackEntityAsMob(this.getAttackTarget());
                    }

                }
            } else {
                //this.setBreathingFire(false);
            }
        }

    }

    @Override
    protected void breathFireAtPos(BlockPos burningTarget) {
        if (this.isBreathingFire()) {
            if (this.isActuallyBreathingFire()) {
                rotationYaw = renderYawOffset;
                if(this.ticksExisted % 5 == 0){
                    this.playSound(ModSounds.FIREDRAGON_BREATH, 4, 1);
                }
                stimulateFire(burningTarget.getX() + 0.5F, burningTarget.getY() + 0.5F, burningTarget.getZ() + 0.5F, 0);
            }
        } else {
            this.setBreathingFire(true);
        }
    }

    public void riderShootFire(Entity controller) {
        if (this.getRNG().nextInt(5) == 0 && !this.isChild()) {
            if (this.getAnimation() != this.ANIMATION_FIRECHARGE) {
                this.setAnimation(this.ANIMATION_FIRECHARGE);
            } else if (this.getAnimationTick() == 15) {
                rotationYaw = renderYawOffset;
                float headPosX = (float) (posX + 1.8F * getRenderSize() * 0.3F * Math.cos((rotationYaw + 90) * Math.PI / 180));
                float headPosZ = (float) (posZ + 1.8F * getRenderSize() * 0.3F * Math.sin((rotationYaw + 90) * Math.PI / 180));
                float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);
                this.playSound(ModSounds.FIREDRAGON_BREATH, 4, 1);
                double d2 = controller.getLookVec().x;
                double d3 = controller.getLookVec().y;
                double d4 = controller.getLookVec().z;
                float inaccuracy = 1.0F;
                d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                EntityDragonFireCharge entitylargefireball = new EntityDragonFireCharge(world, this, d2, d3, d4);
                float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
                entitylargefireball.setSizes(size, size);
                entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
                if (!world.isRemote) {
                    world.spawnEntity(entitylargefireball);
                }
            }
        } else {
            if (this.isBreathingFire()) {
                if (this.isActuallyBreathingFire()) {
                    rotationYaw = renderYawOffset;
                    if(this.ticksExisted % 5 == 0){
                        this.playSound(ModSounds.FIREDRAGON_BREATH, 4, 1);
                    }
                    RayTraceResult mop = rayTraceRider(controller, 10 * this.getDragonStage(), 1.0F);
                    if(mop != null){
                        stimulateFire( mop.hitVec.x,  mop.hitVec.y,  mop.hitVec.z, 0);
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

    private void shootFireAtMob(EntityLivingBase entity) {
        if (!this.attackDecision) {
            if (this.getRNG().nextInt(5) == 0) {
                if (this.getAnimation() != this.ANIMATION_FIRECHARGE) {
                    this.setAnimation(this.ANIMATION_FIRECHARGE);
                } else if (this.getAnimationTick() == 15) {
                    rotationYaw = renderYawOffset;
                    float headPosX = (float) (posX + 1.8F * getRenderSize() * 0.3F * Math.cos((rotationYaw + 90) * Math.PI / 180));
                    float headPosZ = (float) (posZ + 1.8F * getRenderSize() * 0.3F * Math.sin((rotationYaw + 90) * Math.PI / 180));
                    float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);
                    double d2 = entity.posX - headPosX;
                    double d3 = entity.posY - headPosY;
                    double d4 = entity.posZ - headPosZ;
                    float inaccuracy = 1.0F;
                    d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                    d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                    d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
                    this.playSound(ModSounds.FIREDRAGON_BREATH, 4, 1);
                    EntityDragonFireCharge entitylargefireball = new EntityDragonFireCharge(world, this, d2, d3, d4);
                    float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
                    entitylargefireball.setSizes(size, size);
                    entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
                    if (!world.isRemote) {
                        world.spawnEntity(entitylargefireball);
                    }
                    if (entity.isDead || entity == null) {
                        this.setBreathingFire(false);
                        this.attackDecision = this.getRNG().nextBoolean();
                    }
                }
            } else {
                if (this.isBreathingFire()) {
                    if (this.isActuallyBreathingFire()) {
                        rotationYaw = renderYawOffset;
                        if(this.ticksExisted % 5 == 0){
                            this.playSound(ModSounds.FIREDRAGON_BREATH, 4, 1);
                        }
                        stimulateFire(entity.posX, entity.posY, entity.posZ, 0);
                        if (entity.isDead || entity == null) {
                            this.setBreathingFire(false);
                            this.attackDecision = this.getRNG().nextBoolean();
                        }
                    }
                } else {
                    this.setBreathingFire(true);
                }
            }
        }
        this.faceEntity(entity, 360, 360);
    }

    public void stimulateFire(double burnX, double burnY, double burnZ, float overrideDistance) {
        this.getNavigator().clearPath();
        float headPosX = (float) (posX + 1.8F * getRenderSize() * 0.3F * Math.cos((rotationYaw + 90) * Math.PI / 180));
        float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);
        float headPosZ = (float) (posZ + 1.8F * getRenderSize() * 0.3F * Math.sin((rotationYaw + 90) * Math.PI / 180));
        double d2 = burnX - headPosX;
        double d3 = burnY - headPosY;
        double d4 = burnZ - headPosZ;
        float particleScale = MathHelper.clamp(this.getRenderSize() * 0.08F, 0.55F, 3F);
        double distance = Math.max(5 * this.getDistance(burnX, burnY, burnZ), overrideDistance);
        double conqueredDistance = burnProgress / 30D * distance;
        float inaccuracy = 0.5F;
        d2 = d2 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        d3 = d3 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        d4 = d4 + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        for (int i = 0; i < conqueredDistance; i++) {
            double progressX = headPosX + d2 * (i / (float) distance);
            double progressY = headPosY + d3 * (i / (float) distance);
            double progressZ = headPosZ + d4 * (i / (float) distance);
            if (canPositionBeSeen(progressX, progressY, progressZ)) {
                if(world.isRemote && ticksExisted % 3 == 0){
                    IceAndFire.PROXY.spawnParticle("dragonfire", world, progressX, progressY, progressZ, 0, 0.15F, 0, particleScale);
                }
                for (EntityLivingBase entity : world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(progressX - 0.75D, progressY - 0.75D, progressZ - 0.75D, progressX + 0.75D, progressY + 0.75D, progressZ + 0.75D))) {
                    if (!this.isOnSameTeam(entity) && entity != this) {
                        entity.attackEntityFrom(IceAndFire.dragonFire, 0.5F);
                        entity.setFire(5);
                    }
                }
            } else {
                RayTraceResult result = this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double) this.getEyeHeight(), this.posZ), new Vec3d(progressX, progressY, progressZ), false, true, false);
                BlockPos pos = result.getBlockPos();
                if (pos != null) {
                    FireExplosion explosion = new FireExplosion(world, this, pos.getX(), pos.getY(), pos.getZ(), Math.max(0.35F, this.getDragonStage() * 0.35F), true);
                    explosion.doExplosionA();
                    explosion.doExplosionB(false);
                    double spawnX = burnX + (rand.nextFloat() * 3.0) - 1.5;
                    double spawnY = burnY + (rand.nextFloat() * 3.0) - 1.5;
                    double spawnZ = burnZ + (rand.nextFloat() * 3.0) - 1.5;
                    for (int k = 0; k < 7; k++) {
                        IceAndFire.PROXY.spawnParticle("dragonfire", world, spawnX, spawnY, spawnZ, 0, -0.1F, 0, particleScale * 2.75F);
                    }
                }
            }

        }
        if (burnProgress >= 30D && canPositionBeSeen(burnX, burnY, burnZ)) {
            double spawnX = burnX + (rand.nextFloat() * 3.0) - 1.5;
            double spawnY = burnY + (rand.nextFloat() * 3.0) - 1.5;
            double spawnZ = burnZ + (rand.nextFloat() * 3.0) - 1.5;
            for (int j = 0; j < 7; j++) {
                IceAndFire.PROXY.spawnParticle("dragonfire", world, spawnX, spawnY, spawnZ, 0, -0.1F, 0, particleScale * 2.75F);
            }
            FireExplosion explosion = new FireExplosion(world, this, spawnX, spawnY, spawnZ, Math.max(0.35F, this.getDragonStage() * 0.35F), true);
            explosion.doExplosionA();
            explosion.doExplosionB(false);

        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTeen() ? ModSounds.FIREDRAGON_TEEN_IDLE : this.isAdult() ? ModSounds.FIREDRAGON_ADULT_IDLE : ModSounds.FIREDRAGON_CHILD_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isTeen() ? ModSounds.FIREDRAGON_TEEN_HURT : this.isAdult() ? ModSounds.FIREDRAGON_ADULT_HURT : ModSounds.FIREDRAGON_CHILD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTeen() ? ModSounds.FIREDRAGON_TEEN_DEATH : this.isAdult() ? ModSounds.FIREDRAGON_ADULT_DEATH : ModSounds.FIREDRAGON_CHILD_DEATH;
    }

    @Override
    public SoundEvent getRoarSound() {
        return this.isTeen() ? ModSounds.FIREDRAGON_TEEN_ROAR : this.isAdult() ? ModSounds.FIREDRAGON_ADULT_ROAR : ModSounds.FIREDRAGON_CHILD_ROAR;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT, EntityDragonBase.ANIMATION_SPEAK, EntityDragonBase.ANIMATION_BITE, EntityDragonBase.ANIMATION_SHAKEPREY, EntityFireDragon.ANIMATION_TAILWHACK, EntityFireDragon.ANIMATION_FIRECHARGE, EntityFireDragon.ANIMATION_WINGBLAST, EntityFireDragon.ANIMATION_ROAR};
    }

    public boolean isBreedingItem(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() != null && stack.getItem() == ModItems.fire_stew;
    }


}
