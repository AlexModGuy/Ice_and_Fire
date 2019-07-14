package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemSicklyDragonMeal extends ItemGenericDesc implements Interactable{
    public ItemSicklyDragonMeal() {
        super("sickly_dragon_meal", "iceandfire.sickly_dragon_meal");
    }

    @Override
    public boolean processInteract(EntityPlayer player, Entity entity, EnumHand hand, ItemStack stack) {
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            if (dragon.isOwner(player) || player.isCreative()) {
                if (!dragon.isAgingDisabled()) {
                    dragon.setHunger(dragon.getHunger() + 20);
                    dragon.heal(dragon.getMaxHealth());
                    dragon.playCureSound();
                    dragon.spawnItemCrackParticles(stack.getItem());
                    dragon.spawnItemCrackParticles(Items.BONE);
                    dragon.spawnItemCrackParticles(Items.DYE);
                    dragon.spawnItemCrackParticles(Items.POISONOUS_POTATO);
                    dragon.spawnItemCrackParticles(Items.POISONOUS_POTATO);
                    dragon.setAgingDisabled(true);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    return true;
                }
            }
        }

        return false;
    }
}
