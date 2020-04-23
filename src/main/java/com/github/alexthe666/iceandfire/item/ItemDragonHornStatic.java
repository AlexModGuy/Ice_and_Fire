package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHornStatic extends Item {

    public ItemDragonHornStatic() {
        this.maxStackSize = 1;
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.dragon_horn");
        this.setRegistryName(IceAndFire.MODID, "dragon_horn");
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
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if(target instanceof EntityDragonBase && ((EntityDragonBase) target).isOwner(playerIn)){
            EntityDragonBase dragon = (EntityDragonBase)target;
            playerIn.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 3, 1.25F);
            ItemStack hornItem = new ItemStack(dragon.getHorn().getItem(), 1);
            hornItem.setTagCompound(new NBTTagCompound());
            dragon.writeToNBT(hornItem.getTagCompound());
            if (!playerIn.inventory.addItemStackToInventory(hornItem)) {
                playerIn.dropItem(hornItem, false);
            }
            playerIn.swingArm(hand);
            dragon.setDead();
            stack.shrink(1);
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    }
}
