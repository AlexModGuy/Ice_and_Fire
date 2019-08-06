package com.github.alexthe666.iceandfire.compat.tinkers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.tools.traits.TraitSplinters;

public class TraitSplintersII extends AbstractTrait {

    private static int chance = 75; // 1/X chance of getting the effect

    public TraitSplintersII() {
        super("splinters2", TextFormatting.GREEN);
    }

    @Override
    public void afterBlockBreak(ItemStack tool, World world, IBlockState state, BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        splinter(player);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        splinter(player);
    }

    private void splinter(EntityLivingBase player) {
        // SPLINTERS!
        if(!player.getEntityWorld().isRemote && random.nextInt(chance) == 0) {
            int oldTime = player.hurtResistantTime;
            attackEntitySecondary(TraitSplinters.splinter, 0.2f, player, true, true);
            player.hurtResistantTime = oldTime; // keep old invulv time
        }
    }
}