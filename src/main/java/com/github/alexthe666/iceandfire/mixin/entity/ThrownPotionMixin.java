package com.github.alexthe666.iceandfire.mixin.entity;

import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ThrownPotion.class)
public abstract class ThrownPotionMixin extends ThrowableItemProjectile {
    public ThrownPotionMixin(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(
            method = "applySplash",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private void $applySplash(List<MobEffectInstance> pEffectInstances, Entity pTarget, CallbackInfo ci) {
        roadblock$applySplash(pEffectInstances, pTarget);
        ci.cancel();
    }

    private void roadblock$applySplash(List<MobEffectInstance> pEffectInstances, @Nullable Entity pTarget) {
        ThrownPotion potion = (ThrownPotion)(Object) this;

        AABB aabb = potion.getBoundingBox().inflate(4.0d, 2.0d, 4.0d);
        Vec3 potionPos = potion.getPosition(1.0f);
        List<Entity> entityList = potion.level.getEntitiesOfClass(Entity.class, aabb, entity -> (
                (entity instanceof EntityMutlipartPart dragonPart && dragonPart.getParent() != null)
                        || entity instanceof LivingEntity
        ));
        if (!entityList.isEmpty()) {
            Entity thrower = potion.getEffectSource();

            for (Entity effectedEntity : entityList) {
                if (effectedEntity instanceof EntityMutlipartPart dragonPart) {
                    LivingEntity parent = dragonPart.getRootParent();
                    if (parent == null) {
                        break;
                    }

                    if (parent.isAffectedByPotions()) {
                        double d0 = dragonPart.getBoundingBox().clip(potionPos, dragonPart.getPosition(1.0f)).orElse(potionPos).subtract(potionPos).length();
                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                            if (dragonPart == pTarget) {
                                d1 = 1.0D;
                            }

                            for (MobEffectInstance mobeffectinstance : pEffectInstances) {
                                MobEffect mobeffect = mobeffectinstance.getEffect();
                                if (mobeffect.isInstantenous()) {
                                    mobeffect.applyInstantenousEffect(potion, potion.getOwner(), parent, mobeffectinstance.getAmplifier(), d1);
                                } else {
                                    int i = (int)(d1 * (double)mobeffectinstance.getDuration() + 0.5D);
                                    if (i > 20) {
                                        parent.addEffect(new MobEffectInstance(mobeffect, i, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), thrower);
                                    }
                                }
                            }
                        }
                    }
                }
                if (effectedEntity instanceof LivingEntity livingEntity) {
                    if (livingEntity.isAffectedByPotions()) {
                        double d0 = livingEntity.getBoundingBox().clip(potionPos, livingEntity.getPosition(1.0f)).orElse(potionPos).subtract(potionPos).length();
                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;
                            if (livingEntity == pTarget) {
                                d1 = 1.0D;
                            }

                            for(MobEffectInstance mobeffectinstance : pEffectInstances) {
                                MobEffect mobeffect = mobeffectinstance.getEffect();
                                if (mobeffect.isInstantenous()) {
                                    mobeffect.applyInstantenousEffect(potion, potion.getOwner(), livingEntity, mobeffectinstance.getAmplifier(), d1);
                                } else {
                                    int i = (int)(d1 * (double)mobeffectinstance.getDuration() + 0.5D);
                                    if (i > 20) {
                                        livingEntity.addEffect(new MobEffectInstance(mobeffect, i, mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()), thrower);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
