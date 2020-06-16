package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.props.FrozenEntityProperties;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
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
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAlchemySword extends SwordItem {

    public ItemAlchemySword(IItemTier toolmaterial, String name) {
        super(toolmaterial, 3, -2.4F, new Item.Properties().group(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, name);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this == IafItemRegistry.DRAGONBONE_SWORD_FIRE) {
            if (target instanceof EntityIceDragon) {
                target.attackEntityFrom(DamageSource.IN_FIRE, 13.5F);
            }
            target.setFire(5);
            target.knockBack(target, 1F, attacker.getPosX() - target.getPosX(), attacker.getPosZ() - target.getPosZ());
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_ICE) {
            if (target instanceof EntityFireDragon) {
                target.attackEntityFrom(DamageSource.DROWN, 13.5F);
            }
            FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(target, FrozenEntityProperties.class);
            frozenProps.setFrozenFor(200);
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 2));
            target.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 100, 2));
            target.knockBack(target, 1F, attacker.getPosX() - target.getPosX(), attacker.getPosZ() - target.getPosZ());
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING) {
            if(!attacker.world.isRemote){
                ((ServerWorld)attacker.world).addLightningBolt(new LightningBoltEntity(attacker.world, (double)target.getPosX(), (double)target.getPosY(), (double)target.getPosZ(), false));
            }
            if (target instanceof EntityFireDragon || target instanceof EntityIceDragon) {
                target.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 9.5F);
            }
            target.knockBack(target, 1F, attacker.getPosX() - target.getPosX(), attacker.getPosZ() - target.getPosZ());
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").applyTextStyle(TextFormatting.GRAY));
        if (this == IafItemRegistry.DRAGONBONE_SWORD_FIRE) {
            tooltip.add(new TranslationTextComponent("dragon_sword_fire.hurt1").applyTextStyle(TextFormatting.GREEN));
            tooltip.add(new TranslationTextComponent("dragon_sword_fire.hurt2").applyTextStyle(TextFormatting.DARK_RED));
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_ICE) {
            tooltip.add(new TranslationTextComponent("dragon_sword_ice.hurt1").applyTextStyle(TextFormatting.GREEN));
            tooltip.add(new TranslationTextComponent("dragon_sword_ice.hurt2").applyTextStyle(TextFormatting.AQUA));
        }
        if (this == IafItemRegistry.DRAGONBONE_SWORD_LIGHTNING) {
            tooltip.add(new TranslationTextComponent("dragon_sword_lightning.hurt1").applyTextStyle(TextFormatting.GREEN));
            tooltip.add(new TranslationTextComponent("dragon_sword_lightning.hurt2").applyTextStyle(TextFormatting.DARK_PURPLE));
        }
    }

    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
