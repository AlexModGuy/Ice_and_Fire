package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemDragonMeal extends ItemGeneric implements Interactable {
    public ItemDragonMeal() {
        super("dragon_meal", "iceandfire.dragon_meal");
    }

    @Override
    public boolean processInteract(EntityPlayer player, Entity entity, EnumHand hand, ItemStack stack) {
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            if (dragon.isOwner(player) || player.isCreative()) {
                dragon.growDragon(1);
                dragon.setHunger(dragon.getHunger() + 20);
                dragon.heal(Math.min(dragon.getHealth(), (int) (dragon.getMaxHealth() / 2)));
                dragon.playEatSound();
                dragon.spawnItemCrackParticles(stack.getItem());
                dragon.spawnItemCrackParticles(Items.BONE);
                dragon.spawnItemCrackParticles(Items.DYE);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                return true;
            }
        }
        return false;
    }
}
