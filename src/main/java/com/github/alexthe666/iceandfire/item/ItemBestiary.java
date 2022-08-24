package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.google.common.primitives.Ints;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class ItemBestiary extends Item {

    public ItemBestiary() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        this.setRegistryName(IceAndFire.MODID, "bestiary");
    }

    @Override
    public void onCraftedBy(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        stack.setTag(new CompoundNBT());
        stack.getTag().putIntArray("Pages", new int[]{0});

    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            items.add(new ItemStack(this));
            ItemStack stack = new ItemStack(IafItemRegistry.BESTIARY);
            stack.setTag(new CompoundNBT());
            int[] pages = new int[EnumBestiaryPages.values().length];
            for (int i = 0; i < EnumBestiaryPages.values().length; i++) {
                pages[i] = i;
            }
            stack.getTag().putIntArray("Pages", pages);
            items.add(stack);
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        if (worldIn.isClientSide) {
            IceAndFire.PROXY.openBestiaryGui(itemStackIn);
        }
        return new ActionResult<>(ActionResultType.PASS, itemStackIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
            stack.getTag().putIntArray("Pages", new int[]{EnumBestiaryPages.INTRODUCTION.ordinal()});

        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() != null) {
            if (IceAndFire.PROXY.shouldSeeBestiaryContents()) {
                tooltip.add(new TranslationTextComponent("bestiary.contains").withStyle(TextFormatting.GRAY));
                final Set<EnumBestiaryPages> pages = EnumBestiaryPages
                    .containedPages(Ints.asList(stack.getTag().getIntArray("Pages")));
                for (EnumBestiaryPages page : pages) {
                    tooltip.add(new StringTextComponent(TextFormatting.WHITE + "-").append(new TranslationTextComponent("bestiary." + EnumBestiaryPages.values()[page.ordinal()].toString().toLowerCase())).withStyle(TextFormatting.GRAY));
                }
            } else {
                tooltip.add(new TranslationTextComponent("bestiary.hold_shift").withStyle(TextFormatting.GRAY));
            }

        }
    }

}
