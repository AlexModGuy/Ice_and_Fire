package com.github.alexthe666.iceandfire.compat.tinkers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitSurfWaxAmerica extends AbstractTrait {

    public TraitSurfWaxAmerica() {
        super("surf_wax_america", TextFormatting.BLUE);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        if(player.isRiding()){
            newDamage += 5.0F;
        }
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }
}
