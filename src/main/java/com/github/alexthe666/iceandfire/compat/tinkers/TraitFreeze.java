package com.github.alexthe666.iceandfire.compat.tinkers;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.FrozenEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitFreeze extends AbstractTrait {

    private int level;

    public TraitFreeze(int level) {
        super("freeze" + level, TextFormatting.GOLD);
        this.level = level;
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(target, FrozenEntityProperties.class);
        if(frozenProps != null){
            frozenProps.setFrozenFor(150 * (level));
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 150 * (level), 2));
            if(level >= 2){
                target.knockBack(target, 1F, player.posX - target.posX, player.posZ - target.posZ);
            }
        }
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }

}
