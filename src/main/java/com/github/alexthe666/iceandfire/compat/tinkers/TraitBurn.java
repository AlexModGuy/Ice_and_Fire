package com.github.alexthe666.iceandfire.compat.tinkers;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.FrozenEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitBurn extends AbstractTrait {

    private int level;

    public TraitBurn(int level) {
        super("burn" + level, TextFormatting.GOLD);
        this.level = level;
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        target.attackEntityFrom(IceAndFire.dragonFire, Math.max(1, level) * 2F);
        target.setFire(15);
        if(level >= 2){
            target.knockBack(target, 1F, player.posX - target.posX, player.posZ - target.posZ);
        }
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }

}
