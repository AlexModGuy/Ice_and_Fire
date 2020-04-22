package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixieCharge;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPixieWand extends Item {

    public ItemPixieWand() {
        super();
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.pixie_wand");
        this.setRegistryName(IceAndFire.MODID, "pixie_wand");
        this.maxStackSize = 1;
        this.setMaxDamage(500);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        boolean flag = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, itemStackIn) > 0;
        ItemStack itemstack = this.findAmmo(playerIn);
        playerIn.setActiveHand(hand);
        playerIn.swingArm(hand);
        if (!itemstack.isEmpty() || flag) {
            boolean flag1 = playerIn.capabilities.isCreativeMode || this.isInfinite(itemstack, itemStackIn, playerIn);
            if (!flag1) {
                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    playerIn.inventory.deleteStack(itemstack);
                }
            }
            double d2 = playerIn.getLookVec().x;
            double d3 = playerIn.getLookVec().y;
            double d4 = playerIn.getLookVec().z;
            float inaccuracy = 1.0F;
            d2 = d2 + playerIn.getRNG().nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
            d3 = d3 + playerIn.getRNG().nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
            d4 = d4 + playerIn.getRNG().nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
            EntityPixieCharge charge = new EntityPixieCharge(worldIn, playerIn, d2, d3, d4);
            charge.setPosition(playerIn.posX, playerIn.posY + 1, playerIn.posZ);
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(charge);
            }
            playerIn.playSound(IafSoundRegistry.PIXIE_WAND, 1F, 0.75F + 0.5F * playerIn.getRNG().nextFloat());
            itemStackIn.damageItem(1, playerIn);
            playerIn.getCooldownTracker().setCooldown(this, 5);
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.EntityPlayer player) {
        int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, bow);
        return enchant > 0 && stack.getItem() == IafItemRegistry.pixie_dust;
    }

    private ItemStack findAmmo(EntityPlayer player) {
        if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isAmmo(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    protected boolean isAmmo(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == IafItemRegistry.pixie_dust;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("item.iceandfire.legendary_weapon.desc"));
        tooltip.add(I18n.format("item.iceandfire.pixie_wand.desc_0"));
        tooltip.add(I18n.format("item.iceandfire.pixie_wand.desc_1"));
    }
}
