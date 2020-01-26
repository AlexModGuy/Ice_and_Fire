package com.github.alexthe666.iceandfire.compat.tinkers;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitInTheGarage extends AbstractTrait {

    public TraitInTheGarage() {
        super("in_the_garage", TextFormatting.BLUE);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        if (!player.world.isDaytime() || !player.world.canSeeSky(new BlockPos(player))) {
            newDamage += 5.0D;
        }
        for (int i = 0; i < Math.ceil(target.width * 3); i++) {
            target.world.spawnParticle(EnumParticleTypes.NOTE, target.posX + (random.nextFloat() - 0.5) * target.width, target.posY + target.height * random.nextFloat() - 0.5D, target.posZ + (random.nextFloat() - 0.5) * target.width, random.nextFloat(), random.nextFloat(), random.nextFloat());
        }
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }
}
