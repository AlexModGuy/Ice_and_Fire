package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemModHoe extends HoeItem implements DragonSteelOverrides<ItemModHoe> {

    private Multimap<Attribute, AttributeModifier> dragonsteelModifiers;

    public ItemModHoe(Tier toolmaterial) {
        super(toolmaterial, 5, -3.0F, new Item.Properties());
    }

    @Override
    @Deprecated
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND && isDragonsteel(getTier()) ? this.bakeDragonsteel() : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    @Deprecated
    public Multimap<Attribute, AttributeModifier> bakeDragonsteel() {
        if (getTier().getAttackDamageBonus() != IafConfig.dragonsteelBaseDamage || dragonsteelModifiers == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 1F, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3F, AttributeModifier.Operation.ADDITION));
            this.dragonsteelModifiers = builder.build();
            return this.dragonsteelModifiers;
        } else {
            return dragonsteelModifiers;
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return isDragonsteel(getTier()) ? IafConfig.dragonsteelBaseDurability : getTier().getUses();
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        hurtEnemy(this, stack, target, attacker);
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        appendHoverText(getTier(), stack, worldIn, tooltip, flagIn);
    }
}
