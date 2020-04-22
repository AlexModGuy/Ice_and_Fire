package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBestiary extends Item {

    public ItemBestiary() {
        this.maxStackSize = 1;
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.bestiary");
        this.setRegistryName(IceAndFire.MODID, "bestiary");
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setIntArray("Pages", new int[]{0});

    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
            ItemStack stack = new ItemStack(IafItemRegistry.bestiary);
            stack.setTagCompound(new NBTTagCompound());
            int[] pages = new int[EnumBestiaryPages.values().length];
            for (int i = 0; i < EnumBestiaryPages.values().length; i++) {
                pages[i] = i;
            }
            stack.getTagCompound().setIntArray("Pages", pages);
            items.add(stack);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (worldIn.isRemote) {
            IceAndFire.PROXY.openBestiaryGui(itemStackIn);
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setIntArray("Pages", new int[]{EnumBestiaryPages.INTRODUCTION.ordinal()});

        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        if (stack.getTagCompound() != null) {
            if(IceAndFire.PROXY.shouldSeeBestiaryContents()){
                tooltip.add(StatCollector.translateToLocal("bestiary.contains"));
                List<EnumBestiaryPages> pages = EnumBestiaryPages.containedPages(EnumBestiaryPages.toList(stack.getTagCompound().getIntArray("Pages")));
                for (EnumBestiaryPages page : pages) {
                    tooltip.add(TextFormatting.WHITE + "-" + StatCollector.translateToLocal("bestiary." + EnumBestiaryPages.values()[page.ordinal()].toString().toLowerCase()));
                }
            }else{
                tooltip.add(StatCollector.translateToLocal("bestiary.hold_shift"));

            }


        }
    }

}
