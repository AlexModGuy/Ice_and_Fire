package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.GorgonAIStareAttack;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;
import com.github.alexthe666.iceandfire.entity.util.*;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.message.MessageStoneStatue;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import net.minecraft.entity.*;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityGorgon extends MonsterEntity implements IAnimatedEntity, IVillagerFear, IAnimalFear, IHumanoid {

    public static Animation ANIMATION_SCARE;
    public static Animation ANIMATION_HIT;
    private int animationTick;
    private Animation currentAnimation;
    private GorgonAIStareAttack aiStare;
    private MeleeAttackGoal aiMelee;
    private int playerStatueCooldown;

    public EntityGorgon(EntityType type, World worldIn) {
        super(type, worldIn);
        ANIMATION_SCARE = Animation.create(30);
        ANIMATION_HIT = Animation.create(10);
    }

    public static boolean isEntityLookingAt(LivingEntity looker, LivingEntity seen, double degree) {
        degree *= 1 + (looker.getDistance(seen) * 0.1);
        Vec3d vec3d = looker.getLook(1.0F).normalize();
        Vec3d vec3d1 = new Vec3d(seen.getPosX() - looker.getPosX(), seen.getBoundingBox().minY + (double) seen.getEyeHeight() - (looker.getPosY() + (double) looker.getEyeHeight()), seen.getPosZ() - looker.getPosZ());
        double d0 = vec3d1.length();
        vec3d1 = vec3d1.normalize();
        double d1 = vec3d.dotProduct(vec3d1);
        return d1 > 1.0D - degree / d0 && (looker.canEntityBeSeen(seen) && !isStoneMob(seen));
    }

    public static boolean isStoneMob(LivingEntity mob) {
        try {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(mob, StoneEntityProperties.class);
            return properties != null && properties.isStone();
        } catch (Exception e) {
            IceAndFire.LOGGER.warn("stone entity properties do not exist for " + mob.getName());
        }
        return false;
    }

    public static boolean isBlindfolded(LivingEntity attackTarget) {
        return attackTarget != null && attackTarget.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == IafItemRegistry.BLINDFOLD;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(3, aiStare = new GorgonAIStareAttack(this, 1.0D, 0, 15.0F));
        this.goalSelector.addGoal(3, aiMelee = new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new GorgonAIStareAttack(this, 1.0D, 0, 15.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D) {
            public boolean shouldExecute() {
                executionChance = 20;
                return super.shouldExecute();
            }
        });
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F, 1.0F) {
            public boolean shouldContinueExecuting() {
                if (this.closestEntity != null && this.closestEntity instanceof PlayerEntity && ((PlayerEntity) this.closestEntity).isCreative()) {
                    return false;
                }
                return super.shouldContinueExecuting();
            }
        });
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, LivingEntity.class, 0, true, false, new Predicate<Entity>() {
            @Override
            public boolean apply(@Nullable Entity entity) {
                StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
                return entity instanceof LivingEntity && DragonUtils.isAlive((LivingEntity) entity) && !(entity instanceof EntityMutlipartPart) && (properties == null || properties != null && !properties.isStone()) || (entity instanceof IBlacklistedFromStatues && ((IBlacklistedFromStatues) entity).canBeTurnedToStone());
            }
        }));
        this.goalSelector.removeGoal(aiMelee);
    }

    public void attackEntityWithRangedAttack(LivingEntity entity) {
        if (!(entity instanceof MobEntity) && entity instanceof LivingEntity) {
            forcePreyToLook((MobEntity) entity);
        }
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        boolean blindness = this.isPotionActive(Effects.BLINDNESS) || this.getAttackTarget() != null && this.getAttackTarget().isPotionActive(Effects.BLINDNESS) || this.getAttackTarget() != null && this.getAttackTarget() instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) this.getAttackTarget()).canBeTurnedToStone();
        if (blindness && this.deathTime == 0) {
            if (this.getAnimation() != ANIMATION_HIT) {
                this.setAnimation(ANIMATION_HIT);
            }
            if (entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).addPotionEffect(new EffectInstance(Effects.POISON, 100, 2, false, true));
            }
        }
        return super.attackEntityAsMob(entityIn);
    }

    public void setAttackTarget(@Nullable LivingEntity LivingEntityIn) {
        super.setAttackTarget(LivingEntityIn);
        if (LivingEntityIn != null && !world.isRemote) {

            boolean blindness = this.isPotionActive(Effects.BLINDNESS) || LivingEntityIn.isPotionActive(Effects.BLINDNESS) || LivingEntityIn instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) LivingEntityIn).canBeTurnedToStone() || isBlindfolded(LivingEntityIn);
            if (blindness && this.deathTime == 0) {
                this.goalSelector.removeGoal(aiStare);
                this.goalSelector.addGoal(3, aiMelee);
            } else {
                this.goalSelector.removeGoal(aiMelee);
                this.goalSelector.addGoal(3, aiStare);
            }
        }
    }

    protected int getExperiencePoints(PlayerEntity player) {
        return 30;
    }

    protected void onDeathUpdate() {
        ++this.deathTime;
        this.livingSoundTime = 20;
        if (this.world.isRemote) {
            for (int k = 0; k < 5; ++k) {
                double d2 = 0.4;
                double d0 = 0.1;
                double d1 = 0.1;
                IceAndFire.PROXY.spawnParticle("blood", this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY(), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
        }
        if (this.deathTime >= 200) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = ExperienceOrbEntity.getXPSplit(i);
                    i -= j;
                    this.world.addEntity(new ExperienceOrbEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), j));
                }
            }
            this.remove();

            for (int k = 0; k < 20; ++k) {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.CLOUD, this.getPosX() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getPosY() + (double) (this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double) (this.rand.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d2, d0, d1);
            }
        }
    }

    public void livingTick() {
        super.livingTick();
        if (playerStatueCooldown > 0) {
            playerStatueCooldown--;
        }
        if (this.getAttackTarget() != null) {
            boolean blindness = this.isPotionActive(Effects.BLINDNESS) || this.getAttackTarget().isPotionActive(Effects.BLINDNESS);
            this.getLookController().setLookPosition(this.getAttackTarget().getPosX(), this.getAttackTarget().getPosY() + (double) this.getAttackTarget().getEyeHeight(), this.getAttackTarget().getPosZ(), (float) this.getHorizontalFaceSpeed(), (float) this.getVerticalFaceSpeed());
            if (!blindness && this.deathTime == 0 && this.getAttackTarget() instanceof MobEntity && !(this.getAttackTarget() instanceof PlayerEntity)) {
                forcePreyToLook((MobEntity) this.getAttackTarget());
            }
        }

        if (this.getAttackTarget() != null && isEntityLookingAt(this, this.getAttackTarget(), 0.4) && isEntityLookingAt(this.getAttackTarget(), this, 0.4) && !isBlindfolded(this.getAttackTarget())) {
            boolean blindness = this.isPotionActive(Effects.BLINDNESS) || this.getAttackTarget().isPotionActive(Effects.BLINDNESS) || this.getAttackTarget() instanceof IBlacklistedFromStatues && !((IBlacklistedFromStatues) this.getAttackTarget()).canBeTurnedToStone();
            if (!blindness && this.deathTime == 0) {
                if (this.getAnimation() != ANIMATION_SCARE) {
                    this.playSound(IafSoundRegistry.GORGON_ATTACK, 1, 1);
                    this.setAnimation(ANIMATION_SCARE);
                }
                if (this.getAnimation() == ANIMATION_SCARE) {
                    if (this.getAnimationTick() > 10) {
                        if (this.getAttackTarget() instanceof PlayerEntity) {
                            if (!world.isRemote) {
                                this.getAttackTarget().attackEntityFrom(IafDamageRegistry.GORGON_DMG, Integer.MAX_VALUE);
                                if (!this.getAttackTarget().isAlive() && playerStatueCooldown == 0) {
                                    EntityStoneStatue statue = new EntityStoneStatue(IafEntityRegistry.STONE_STATUE, world);
                                    statue.setPositionAndRotation(this.getAttackTarget().getPosX(), this.getAttackTarget().getPosY(), this.getAttackTarget().getPosZ(), this.getAttackTarget().rotationYaw, this.getAttackTarget().rotationPitch);
                                    statue.smallArms = true;
                                    if (!world.isRemote) {
                                        world.addEntity(statue);
                                    }
                                    statue.prevRotationYaw = this.getAttackTarget().rotationYaw;
                                    statue.rotationYaw = this.getAttackTarget().rotationYaw;
                                    statue.rotationYawHead = this.getAttackTarget().rotationYaw;
                                    statue.renderYawOffset = this.getAttackTarget().rotationYaw;
                                    statue.prevRenderYawOffset = this.getAttackTarget().rotationYaw;
                                    playerStatueCooldown = 40;
                                }
                                this.setAttackTarget(null);
                            }
                        } else {
                            if (this.getAttackTarget() instanceof LivingEntity && !(this.getAttackTarget() instanceof IBlacklistedFromStatues) || this.getAttackTarget() instanceof IBlacklistedFromStatues && ((IBlacklistedFromStatues) this.getAttackTarget()).canBeTurnedToStone()) {
                                StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this.getAttackTarget(), StoneEntityProperties.class);
                                LivingEntity attackTarget = this.getAttackTarget();
                                if (properties != null && !properties.isStone()) {
                                    properties.setStone(true);
                                    if (world.isRemote) {
                                        IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageStoneStatue(attackTarget.getEntityId(), true));
                                    } else {
                                        IceAndFire.sendMSGToAll(new MessageStoneStatue(attackTarget.getEntityId(), true));
                                    }
                                    this.playSound(IafSoundRegistry.GORGON_TURN_STONE, 1, 1);
                                    this.setAttackTarget(null);
                                }

                                if (attackTarget instanceof EntityDragonBase) {
                                    EntityDragonBase dragon = (EntityDragonBase) attackTarget;
                                    dragon.setFlying(false);
                                    dragon.setHovering(false);
                                }
                                if (attackTarget instanceof EntityHippogryph) {
                                    EntityHippogryph dragon = (EntityHippogryph) attackTarget;
                                    dragon.setFlying(false);
                                    dragon.setHovering(false);
                                    dragon.airTarget = null;
                                }
                            }
                        }
                    }
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public int getVerticalFaceSpeed() {
        return 10;
    }

    public int getHorizontalFaceSpeed() {
        return 10;
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }

    public void forcePreyToLook(MobEntity mob) {
        mob.getLookController().setLookPosition(this.getPosX(), this.getPosY() + (double) this.getEyeHeight(), this.getPosZ(), (float) mob.getHorizontalFaceSpeed(), (float) mob.getVerticalFaceSpeed());
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IafConfig.gorgonMaxHealth);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
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
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
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
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }
}
