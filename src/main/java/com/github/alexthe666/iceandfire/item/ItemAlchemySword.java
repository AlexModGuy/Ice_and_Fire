package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAlchemySword extends SwordItem {

    public ItemAlchemySword(Tier toolmaterial) {
        super(toolmaterial, 3, -2.4F, new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (this == IafItemRegistry.DRAGONBONE_SWORD_FIRE.get() && IafConfig.dragonWeaponFireAbility) {
            if (target instanceof EntityIceDragon) {
                target.hurt(attacker.level().damageSources().inFire(), 13.5F);
            }
            target.setSecondsOnFire(5);
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_ICE.get() && IafConfig.dragonWeaponIceAbility) {
            if (target instanceof EntityFireDragon) {
                target.hurt(attacker.level().damageSources().drown(), 13.5F);
            }

            EntityDataProvider.getCapability(target).ifPresent(data -> data.frozenData.setFrozen(target, 200));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING.get() && IafConfig.dragonWeaponLightningAbility) {
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
            if (target instanceof EntityFireDragon || target instanceof EntityIceDragon) {
                target.hurt(attacker.level().damageSources().lightningBolt(), 9.5F);
            }
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        if (this == IafItemRegistry.DRAGONBONE_SWORD_FIRE.get()) {
            tooltip.add(Component.translatable("dragon_sword_fire.hurt1").withStyle(ChatFormatting.GREEN));
            if (IafConfig.dragonWeaponFireAbility)
                tooltip.add(Component.translatable("dragon_sword_fire.hurt2").withStyle(ChatFormatting.DARK_RED));
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_ICE.get()) {
            tooltip.add(Component.translatable("dragon_sword_ice.hurt1").withStyle(ChatFormatting.GREEN));
            if (IafConfig.dragonWeaponIceAbility)
                tooltip.add(Component.translatable("dragon_sword_ice.hurt2").withStyle(ChatFormatting.AQUA));
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING.get()) {
            tooltip.add(Component.translatable("dragon_sword_lightning.hurt1").withStyle(ChatFormatting.GREEN));
            if (IafConfig.dragonWeaponLightningAbility)
                tooltip.add(Component.translatable("dragon_sword_lightning.hurt2").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
}
