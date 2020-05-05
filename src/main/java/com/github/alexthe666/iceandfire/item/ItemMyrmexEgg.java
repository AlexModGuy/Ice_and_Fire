package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexEgg;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMyrmexEgg extends Item implements ICustomRendered {

    boolean isJungle;

    public ItemMyrmexEgg(boolean isJungle) {
        this.setHasSubtypes(true);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.isJungle = isJungle;
        this.setTranslationKey(isJungle ? "iceandfire.myrmex_jungle_egg" : "iceandfire.myrmex_desert_egg");
        this.maxStackSize = 1;
        this.setRegistryName(IceAndFire.MODID, isJungle ? "myrmex_jungle_egg" : "myrmex_desert_egg");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
            items.add(new ItemStack(this, 1, 2));
            items.add(new ItemStack(this, 1, 3));
            items.add(new ItemStack(this, 1, 4));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String caste;
        switch (stack.getMetadata()) {
            default:
                caste = "worker";
                break;
            case 1:
                caste = "soldier";
                break;
            case 2:
                caste = "royal";
                break;
            case 3:
                caste = "sentinel";
                break;
            case 4:
                caste = "queen";
        }
        if (stack.getMetadata() == 4) {
            tooltip.add(TextFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("myrmex.caste_" + caste + ".name"));
        } else {
            tooltip.add(StatCollector.translateToLocal("myrmex.caste_" + caste + ".name"));
        }
    }

    public EnumActionResult onItemUse(PlayerEntity player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
            return EnumActionResult.PASS;
        } else {
            EntityMyrmexEgg egg = new EntityMyrmexEgg(worldIn);
            egg.setJungle(isJungle);
            egg.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            egg.onPlayerPlace(player);
            ItemStack itemstack = player.getHeldItem(hand);
            egg.setMyrmexCaste(itemstack.getMetadata());
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(egg);
            }

            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
                if (itemstack.getCount() <= 0) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
                }
            }
            return EnumActionResult.SUCCESS;

        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return super.hasEffect(stack) || stack.getMetadata() == 4;
    }
}
