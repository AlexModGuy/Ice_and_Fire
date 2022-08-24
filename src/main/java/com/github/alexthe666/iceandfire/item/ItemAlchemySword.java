package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.props.FrozenProperties;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAlchemySword extends SwordItem {

    public ItemAlchemySword(IItemTier toolmaterial, String name) {
        super(toolmaterial, 3, -2.4F, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, name);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this == IafItemRegistry.DRAGONBONE_SWORD_FIRE && IafConfig.dragonWeaponFireAbility) {
            if (target instanceof EntityIceDragon) {
                target.hurt(DamageSource.IN_FIRE, 13.5F);
            }
            target.setSecondsOnFire(5);
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_ICE && IafConfig.dragonWeaponIceAbility) {
            if (target instanceof EntityFireDragon) {
                target.hurt(DamageSource.DROWN, 13.5F);
            }
            FrozenProperties.setFrozenFor(target, 200);
            target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 2));
            target.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 100, 2));
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING && IafConfig.dragonWeaponLightningAbility) {
            boolean flag = true;
            if (attacker instanceof PlayerEntity) {
                if (attacker.attackAnim > 0.2) {
                    flag = false;
                }
            }
            if (!attacker.level.isClientSide && flag) {
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(target.level);
                lightningboltentity.moveTo(target.position());
                if (!target.level.isClientSide) {
                    target.level.addFreshEntity(lightningboltentity);
                }
            }
            if (target instanceof EntityFireDragon || target instanceof EntityIceDragon) {
                target.hurt(DamageSource.LIGHTNING_BOLT, 9.5F);
            }
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").withStyle(TextFormatting.GRAY));
        if (this == IafItemRegistry.DRAGONBONE_SWORD_FIRE) {
            tooltip.add(new TranslationTextComponent("dragon_sword_fire.hurt1").withStyle(TextFormatting.GREEN));
            if (IafConfig.dragonWeaponFireAbility)
                tooltip.add(new TranslationTextComponent("dragon_sword_fire.hurt2").withStyle(TextFormatting.DARK_RED));
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_ICE) {
            tooltip.add(new TranslationTextComponent("dragon_sword_ice.hurt1").withStyle(TextFormatting.GREEN));
            if (IafConfig.dragonWeaponIceAbility)
                tooltip.add(new TranslationTextComponent("dragon_sword_ice.hurt2").withStyle(TextFormatting.AQUA));
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING) {
            tooltip.add(new TranslationTextComponent("dragon_sword_lightning.hurt1").withStyle(TextFormatting.GREEN));
            if (IafConfig.dragonWeaponLightningAbility)
                tooltip.add(new TranslationTextComponent("dragon_sword_lightning.hurt2").withStyle(TextFormatting.DARK_PURPLE));
        }
    }

    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
