package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDreadLichSkull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemLichStaff extends ItemGeneric {

    public ItemLichStaff() {
        super("lich_staff", "iceandfire.lich_staff");
        this.maxStackSize = 1;
        this.setMaxDamage(100);
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == IafItemRegistry.dread_shard || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        playerIn.swingArm(hand);
        double d2 = playerIn.getLookVec().x;
        double d3 = playerIn.getLookVec().y;
        double d4 = playerIn.getLookVec().z;
        float inaccuracy = 1.0F;
        d2 = d2 + playerIn.getRNG().nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        d3 = d3 + playerIn.getRNG().nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        d4 = d4 + playerIn.getRNG().nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
        EntityDreadLichSkull charge = new EntityDreadLichSkull(worldIn, playerIn, 6);
        charge.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 7.0F, 1.0F);
        charge.setPosition(playerIn.posX, playerIn.posY + 1, playerIn.posZ);
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(charge);
        }
        charge.shoot(d2, d3, d4, 1, 1);
        playerIn.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1F, 0.75F + 0.5F * playerIn.getRNG().nextFloat());
        itemStackIn.damageItem(1, playerIn);
        playerIn.getCooldownTracker().setCooldown(this, 4);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }
}