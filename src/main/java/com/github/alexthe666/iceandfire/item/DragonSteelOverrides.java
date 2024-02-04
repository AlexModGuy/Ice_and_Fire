package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;


public interface DragonSteelOverrides<T extends TieredItem> {

    /**
     * Kept for compatibility
     *
     * @deprecated use data pack overrides instead
     */
    @Deprecated
    Multimap<Attribute, AttributeModifier> bakeDragonsteel();

    default float getAttackDamage(T item) {
        if (item instanceof SwordItem) {
            return ((SwordItem) item).getDamage();
        }
        if (item instanceof DiggerItem) {
            return ((DiggerItem) item).getAttackDamage();
        }
        return item.getTier().getAttackDamageBonus();
        //return item.getDamage(item.asItem().getDefaultInstance())
    }

    default boolean isDragonsteel(Tier tier) {
        return tier.getTag() == DragonSteelTier.DRAGONSTEEL_TIER_TAG;
    }

    default boolean isDragonsteelFire(Tier tier) {
        return tier == DragonSteelTier.DRAGONSTEEL_TIER_FIRE;
    }

    default boolean isDragonsteelIce(Tier tier) {
        return tier == DragonSteelTier.DRAGONSTEEL_TIER_ICE;
    }

    default boolean isDragonsteelLightning(Tier tier) {
        return tier == DragonSteelTier.DRAGONSTEEL_TIER_LIGHTNING;
    }

    default void hurtEnemy(T item, ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (item.getTier() == IafItemRegistry.SILVER_TOOL_MATERIAL) {
            if (target.getMobType() == MobType.UNDEAD) {
                target.hurt(attacker.level().damageSources().magic(), getAttackDamage(item) + 3.0F);
            }
        }

        if (item.getTier() == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            if (target.getMobType() != MobType.ARTHROPOD) {
                target.hurt(attacker.level().damageSources().generic(), getAttackDamage(item) + 5.0F);
            }
            if (target instanceof EntityDeathWorm) {
                target.hurt(attacker.level().damageSources().generic(), getAttackDamage(item) + 5.0F);
            }
        }
        if (isDragonsteelFire(item.getTier()) && IafConfig.dragonWeaponFireAbility) {
            target.setSecondsOnFire(15);
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (isDragonsteelIce(item.getTier()) && IafConfig.dragonWeaponIceAbility) {
            EntityDataProvider.getCapability(target).ifPresent(data -> data.frozenData.setFrozen(target, 300));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 2));
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (isDragonsteelLightning(item.getTier()) && IafConfig.dragonWeaponLightningAbility) {
            boolean flag = true;
            if (attacker instanceof Player) {
                if (attacker.attackAnim > 0.2) {
                    flag = false;
                }
            }
            if (!attacker.level().isClientSide && flag) {
                LightningBolt lightningboltentity = EntityType.LIGHTNING_BOLT.create(target.level());
                lightningboltentity.getTags().add(ServerEvents.BOLT_DONT_DESTROY_LOOT);
                lightningboltentity.getTags().add(attacker.getStringUUID());
                lightningboltentity.moveTo(target.position());
                if (!target.level().isClientSide) {
                    target.level().addFreshEntity(lightningboltentity);
                }
            }
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }

    }

    default void appendHoverText(Tier tier, ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (tier == IafItemRegistry.SILVER_TOOL_MATERIAL) {
            tooltip.add(Component.translatable("silvertools.hurt").withStyle(ChatFormatting.GREEN));
        }
        if (tier == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            tooltip.add(Component.translatable("myrmextools.hurt").withStyle(ChatFormatting.GREEN));
        }
        if (isDragonsteelFire(tier) && IafConfig.dragonWeaponFireAbility) {
            tooltip.add(Component.translatable("dragon_sword_fire.hurt2").withStyle(ChatFormatting.DARK_RED));
        }
        if (isDragonsteelIce(tier) && IafConfig.dragonWeaponIceAbility) {
            tooltip.add(Component.translatable("dragon_sword_ice.hurt2").withStyle(ChatFormatting.AQUA));
        }
        if (isDragonsteelLightning(tier) && IafConfig.dragonWeaponLightningAbility) {
            tooltip.add(Component.translatable("dragon_sword_lightning.hurt2").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
