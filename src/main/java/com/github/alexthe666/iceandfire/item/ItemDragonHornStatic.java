package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHornStatic extends Item {

    public ItemDragonHornStatic() {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxStackSize(1));
        this.setRegistryName(IceAndFire.MODID, "dragon_horn");
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if(target instanceof EntityDragonBase && ((EntityDragonBase) target).isOwner(playerIn)){
            EntityDragonBase dragon = (EntityDragonBase)target;
            playerIn.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 3, 1.25F);
            ItemStack hornItem = new ItemStack(dragon.getHorn().getItem(), 1);
            hornItem.setTag(new CompoundNBT());
            dragon.writeEntityToNBT(hornItem.getTag());
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

}
