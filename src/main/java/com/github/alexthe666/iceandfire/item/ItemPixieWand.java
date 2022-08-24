package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityPixieCharge;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPixieWand extends Item {

    public ItemPixieWand() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1).durability(500));
        this.setRegistryName(IceAndFire.MODID, "pixie_wand");
    }


    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);
        boolean flag = playerIn.isCreative() || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, itemStackIn) > 0;
        ItemStack itemstack = this.findAmmo(playerIn);
        playerIn.startUsingItem(hand);
        playerIn.swing(hand);
        if (!itemstack.isEmpty() || flag) {
            boolean flag1 = playerIn.isCreative() || this.isInfinite(itemstack, itemStackIn, playerIn);
            if (!flag1) {
                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    playerIn.inventory.removeItem(itemstack);
                }
            }
            double d2 = playerIn.getLookAngle().x;
            double d3 = playerIn.getLookAngle().y;
            double d4 = playerIn.getLookAngle().z;
            float inaccuracy = 1.0F;
            d2 = d2 + playerIn.getRandom().nextGaussian() * 0.007499999832361937D * inaccuracy;
            d3 = d3 + playerIn.getRandom().nextGaussian() * 0.007499999832361937D * inaccuracy;
            d4 = d4 + playerIn.getRandom().nextGaussian() * 0.007499999832361937D * inaccuracy;
            EntityPixieCharge charge = new EntityPixieCharge(IafEntityRegistry.PIXIE_CHARGE.get(), worldIn, playerIn,
                d2, d3, d4);
            charge.setPos(playerIn.getX(), playerIn.getY() + 1, playerIn.getZ());
            if (!worldIn.isClientSide) {
                worldIn.addFreshEntity(charge);
            }
            playerIn.playSound(IafSoundRegistry.PIXIE_WAND, 1F, 0.75F + 0.5F * playerIn.getRandom().nextFloat());
            itemstack.hurtAndBreak(1, playerIn, (player) -> {
                player.broadcastBreakEvent(playerIn.getUsedItemHand());
            });
            playerIn.getCooldowns().addCooldown(this, 5);
        }
        return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.entity.player.PlayerEntity player) {
        int enchant = net.minecraft.enchantment.EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow);
        return enchant > 0 && stack.getItem() == IafItemRegistry.PIXIE_DUST;
    }

    private ItemStack findAmmo(PlayerEntity player) {
        if (this.isAmmo(player.getItemInHand(Hand.OFF_HAND))) {
            return player.getItemInHand(Hand.OFF_HAND);
        } else if (this.isAmmo(player.getItemInHand(Hand.MAIN_HAND))) {
            return player.getItemInHand(Hand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = player.inventory.getItem(i);

                if (this.isAmmo(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    protected boolean isAmmo(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == IafItemRegistry.PIXIE_DUST;
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.pixie_wand.desc_0").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.pixie_wand.desc_1").withStyle(TextFormatting.GRAY));
    }
}
