package com.github.alexthe666.iceandfire.compat.tinkers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.events.TinkerToolEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.traits.TraitSplinters;

public class TraitSplitting2 extends AbstractTrait {

    private static final float DOUBLESHOT_CHANCE = 0.65f;

    public TraitSplitting2() {
        super("splitting2", TextFormatting.GREEN);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBowShooting(TinkerToolEvent.OnBowShoot event) {
        if(TinkerUtil.hasTrait(TagUtil.getTagSafe(event.ammo), this.getModifierIdentifier()) && random.nextFloat() < DOUBLESHOT_CHANCE) {
            event.setProjectileCount(3);
            event.setConsumeAmmoPerProjectile(false);
            event.setConsumeDurabilityPerProjectile(false);
            event.setBonusInaccuracy(3f);
        }
    }
}