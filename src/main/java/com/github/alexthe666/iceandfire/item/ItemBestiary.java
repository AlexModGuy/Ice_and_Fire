package com.github.alexthe666.iceandfire.item;

import java.util.List;

import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;

public class ItemBestiary extends Item {

    public ItemBestiary() {
        this.maxStackSize = 1;
        this.setCreativeTab(IceAndFire.TAB);
        this.setUnlocalizedName("iceandfire.bestiary");
        GameRegistry.registerItem(this, "bestiary");
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.add(new ItemStack(itemIn));
        ItemStack stack = new ItemStack(ModItems.bestiary);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setIntArray("Pages", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
        subItems.add(stack);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote) {
            IceAndFire.PROXY.openBestiaryGui(itemStackIn);
        }
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setIntArray("Pages", new int[]{EnumBestiaryPages.INTRODUCTION.ordinal()});

        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean f) {
        if (stack.getTagCompound() != null) {
            list.add(StatCollector.translateToLocal("bestiary.contains"));
            List<EnumBestiaryPages> pages = EnumBestiaryPages.containedPages(EnumBestiaryPages.toList(stack.getTagCompound().getIntArray("Pages")));
            for (EnumBestiaryPages page : pages) {
                list.add(TextFormatting.WHITE + "-" + StatCollector.translateToLocal("bestiary." + EnumBestiaryPages.values()[page.ordinal()].toString().toLowerCase()));
            }

        }
    }

}
