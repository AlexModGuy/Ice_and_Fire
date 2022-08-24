package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.GorgonAIStareAttack;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class EntityGorgon extends MonsterEntity implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHumanoid, IHasCustomizableAttributes {

    public static Animation ANIMATION_SCARE;
    public static Animation ANIMATION_HIT;
    private int animationTick;
    private Animation currentAnimation;
    private GorgonAIStareAttack aiStare;
    private MeleeAttackGoal aiMelee;
    private int playerStatueCooldown;

    public EntityGorgon(EntityType<EntityGorgon> type, World worldIn) {
        super(type, worldIn);
        IHasCustomizableAttributes.applyAttributesForEntity(type, this);
        ANIMATION_SCARE = Animation.create(30);
        ANIMATION_HIT = Animation.create(10);
    }

    public static boolean isEntityLookingAt(LivingEntity looker, LivingEntity seen, double degree) {
        degree *= 1 + (looker.distanceTo(seen) * 0.1);
        Vector3d Vector3d = looker.getViewVector(1.0F).normalize();
        Vector3d Vector3d1 = new Vector3d(seen.getX() - looker.getX(), seen.getBoundingBox().minY + (double) seen.getEyeHeight() - (looker.getY() + (double) looker.getEyeHeight()), seen.getZ() - looker.getZ());
        double d0 = Vector3d1.length();
        Vector3d1 = Vector3d1.normalize();
        double d1 = Vector3d.dot(Vector3d1);
        return d1 > 1.0D - degree / d0 && (looker.canSee(seen) && !isStoneMob(seen));
    }

    public static boolean isStoneMob(LivingEntity mob) {
        return mob instanceof EntityStoneStatue;
    }

    public static boolean isBlindfolded(LivingEntity attackTarget) {
        return attackTarget != null && (attackTarget.getItemBySlot(EquipmentSlotType.HEAD).getItem() == IafItemRegistry.BLINDFOLD || attackTarget.hasEffect(Effects.BLINDNESS) || ServerEvents.isBlindMob(attackTarget));
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, IafConfig.gorgonMaxHealth)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.25D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, 3.0D)
            //ARMOR
            .add(Attributes.ARMOR, 1.0D);
    }

    @Override
    public AttributeModifierMap.MutableAttribute getConfigurableAttributes() {
        return bakeAttributes();
    }

    public boolean isTargetBlocked(Vector3d target) {
        Vector3d Vector3d = new Vector3d(this.getX(), this.getEyeY(), this.getZ());
        RayTraceResult result = this.level.clip(new RayTraceContext(Vector3d, target, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        return result.getType() != RayTraceResult.Type.MISS;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, aiStare = new GorgonAIStareAttack(this, 1.0D, 0, 15.0F));
        this.goalSelector.addGoal(3, aiMelee = new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D) {
            public boolean canUse() {
                interval = 20;
                return super.canUse();
            }
        });
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F, 1.0F) {
            public boolean canContinueToUse() {
                if (this.lookAt != null && this.lookAt instanceof PlayerEntity && ((PlayerEntity) this.lookAt).isCreative()) {
                    return false;
                }
                return super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, PlayerEntity.class, 10, false, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity.isAlive();
            }
        }));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                return entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) || (entity instanceof IBlacklistedFromStatues && ((IBlacklistedFromStatues) entity).canBeTurnedToStone());
            }
        }));
        this.goalSelector.removeGoal(aiMelee);
    }

    public void attackEntityWithRangedAttack(LivingEntity entity) {
        if (!(entity instanceof MobEntity) && entity instanceof LivingEntity) {
            forcePreyToLook(entity);
        }
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean blindness = this.hasEffect(Effects.BLINDNESS) || this.getTarget() != null && this.getTarget().hasEffect(Effects.BLINDNESS) || this.getTarget() != null && this.getTarget() instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) this.getTarget()).canBeTurnedToStone();
        if (blindness && this.deathTime == 0) {
            if (this.getAnimation() != ANIMATION_HIT) {
                this.setAnimation(ANIMATION_HIT);
            }
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).addEffect(new EffectInstance(Effects.POISON, 100, 2, false, true));
            }
        }
        return super.doHurtTarget(entityIn);
    }

    public void setTarget(@Nullable LivingEntity LivingEntityIn) {
        super.setTarget(LivingEntityIn);
        if (LivingEntityIn != null && !level.isClientSide) {


            boolean blindness = this.hasEffect(Effects.BLINDNESS) || LivingEntityIn.hasEffect(Effects.BLINDNESS) || LivingEntityIn instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) LivingEntityIn).canBeTurnedToStone() || isBlindfolded(LivingEntityIn);
            if (blindness && this.deathTime == 0) {
                this.goalSelector.addGoal(3, aiMelee);
                this.goalSelector.removeGoal(aiStare);
            } else {
                this.goalSelector.addGoal(3, aiStare);
                this.goalSelector.removeGoal(aiMelee);
            }
        }
    }

    protected int getExperienceReward(PlayerEntity player) {
        return 30;
    }

    protected void tickDeath() {
        ++this.deathTime;
        this.ambientSoundTime = 20;
        if (this.level.isClientSide) {
            for (int k = 0; k < 5; ++k) {
                double d2 = 0.4;
                double d0 = 0.1;
                double d1 = 0.1;
                IceAndFire.PROXY.spawnParticle(EnumParticles.Blood, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY(), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d2, d0, d1);
            }
        }
        if (this.deathTime >= 200) {
            if (!this.level.isClientSide && (this.isAlwaysExperienceDropper() || this.lastHurtByPlayerTime > 0 && this.shouldDropExperience() && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS))) {
                int i = this.getExperienceReward(this.lastHurtByPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.lastHurtByPlayer, i);
                while (i > 0) {
                    int j = ExperienceOrbEntity.getExperienceValue(i);
                    i -= j;
                    this.level.addFreshEntity(new ExperienceOrbEntity(this.level, this.getX(), this.getY(), this.getZ(), j));
                }
            }
            this.remove();

            for (int k = 0; k < 20; ++k) {
                double d2 = this.random.nextGaussian() * 0.02D;
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.CLOUD, this.getX() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), this.getY() + (double) (this.random.nextFloat() * this.getBbHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double) this.getBbWidth(), d2, d0, d1);
            }
        }
    }

    public void aiStep() {
        super.aiStep();
        if (playerStatueCooldown > 0) {
            playerStatueCooldown--;
        }
        LivingEntity attackTarget = this.getTarget();
        if (attackTarget != null) {
            boolean blindness = this.hasEffect(Effects.BLINDNESS) || attackTarget.hasEffect(Effects.BLINDNESS);
            if (!blindness && this.deathTime == 0 && attackTarget instanceof MobEntity && !(attackTarget instanceof PlayerEntity)) {
                forcePreyToLook(attackTarget);
            }
            if (isEntityLookingAt(attackTarget, this, 0.4)) {
                this.getLookControl().setLookAt(attackTarget.getX(), attackTarget.getY() + (double) attackTarget.getEyeHeight(), attackTarget.getZ(), (float) this.getMaxHeadYRot(), (float) this.getMaxHeadXRot());
            }
        }


        if (attackTarget != null && isEntityLookingAt(this, attackTarget, 0.4) && isEntityLookingAt(attackTarget, this, 0.4) && !isBlindfolded(attackTarget)) {
            boolean blindness = this.hasEffect(Effects.BLINDNESS) || attackTarget.hasEffect(Effects.BLINDNESS) || attackTarget instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) attackTarget).canBeTurnedToStone();
            if (!blindness && this.deathTime == 0) {
                if (this.getAnimation() != ANIMATION_SCARE) {
                    this.playSound(IafSoundRegistry.GORGON_ATTACK, 1, 1);
                    this.setAnimation(ANIMATION_SCARE);
                }
                if (this.getAnimation() == ANIMATION_SCARE) {
                    if (this.getAnimationTick() > 10) {
                        if (!level.isClientSide) {
                            if (playerStatueCooldown == 0) {
                                EntityStoneStatue statue = EntityStoneStatue.buildStatueEntity(attackTarget);
                                statue.absMoveTo(attackTarget.getX(), attackTarget.getY(), attackTarget.getZ(), attackTarget.yRot, attackTarget.xRot);
                                if (!level.isClientSide) {
                                    level.addFreshEntity(statue);
                                }
                                statue.yRotO = attackTarget.yRot;
                                statue.yRot = attackTarget.yRot;
                                statue.yHeadRot = attackTarget.yRot;
                                statue.yBodyRot = attackTarget.yRot;
                                statue.yBodyRotO = attackTarget.yRot;
                                playerStatueCooldown = 40;
                                if (attackTarget instanceof PlayerEntity) {

                                    attackTarget.hurt(IafDamageRegistry.causeGorgonDamage(this), Integer.MAX_VALUE);
                                } else {
                                    attackTarget.remove();
                                }
                                this.setTarget(null);

                            }
                        }
                    }
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public int getMaxHeadXRot() {
        return 10;
    }

    public int getMaxHeadYRot() {
        return 30;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    public void forcePreyToLook(LivingEntity mob) {
        if (mob instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) mob;
            mobEntity.getLookControl().setLookAt(this.getX(), this.getY() + (double) this.getEyeHeight(), this.getZ(), (float) mobEntity.getMaxHeadYRot(), (float) mobEntity.getMaxHeadXRot());

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
        return new Animation[]{ANIMATION_SCARE, ANIMATION_HIT};
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.GORGON_IDLE;
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return IafSoundRegistry.GORGON_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.GORGON_DIE;
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
