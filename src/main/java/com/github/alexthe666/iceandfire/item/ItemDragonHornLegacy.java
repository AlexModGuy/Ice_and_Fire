package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHornLegacy extends Item {

    public ItemDragonHornLegacy(String dragon_type) {
        this.maxStackSize = 1;
        this.setTranslationKey("iceandfire.dragon_horn_legacy");
        this.setRegistryName(IceAndFire.MODID, "dragon_horn_" + dragon_type);
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
        itemStack.setTagCompound(new NBTTagCompound());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
    }



    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        worldIn.playSound(player, pos, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.NEUTRAL, 3, 0.75F);
        if (this == IafItemRegistry.dragon_horn_fire_legacy) {
            EntityFireDragon dragon = new EntityFireDragon(worldIn);
            if (stack.getTagCompound() != null) {
                dragon.readFromNBT(stack.getTagCompound());
            }
            dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            dragon.setFlying(false);
            dragon.setHovering(false);
            dragon.getNavigator().clearPath();
            stack.getTagCompound().setBoolean("Released", true);
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(dragon);
            }
            stack.setCount(0);
            ItemStack hornItem = new ItemStack(IafItemRegistry.dragon_horn);
            if (!player.inventory.addItemStackToInventory(hornItem)) {
                player.dropItem(hornItem, false);
            }
        }
        if (this == IafItemRegistry.dragon_horn_ice_legacy) {
            EntityIceDragon dragon = new EntityIceDragon(worldIn);
            dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            if (stack.getTagCompound() != null) {
                dragon.readEntityFromNBT(stack.getTagCompound());
            }
            dragon.setFlying(false);
            dragon.setHovering(false);
            dragon.getNavigator().clearPath();
            if (!worldIn.isRemote) {
                worldIn.spawnEntity(dragon);
            }
            stack.setCount(0);
            ItemStack hornItem = new ItemStack(IafItemRegistry.dragon_horn);
            if (!player.inventory.addItemStackToInventory(hornItem)) {
                player.dropItem(hornItem, false);
            }
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.iceandfire.dragon_horn_legacy.desc"));
    }
}
