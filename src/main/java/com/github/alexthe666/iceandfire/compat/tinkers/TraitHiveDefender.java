package com.github.alexthe666.iceandfire.compat.tinkers;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.potion.TinkerPotion;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitHiveDefender extends AbstractTrait {

    public TraitHiveDefender() {
        super("hive_defender", TextFormatting.GOLD);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        if(target instanceof EntityDeathWorm){
            newDamage += 8;
        }else if (target.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD) {
            newDamage += 4;
        }
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }
}
