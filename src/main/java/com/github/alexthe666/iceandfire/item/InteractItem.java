package com.github.alexthe666.iceandfire.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;

public class InteractItem extends Item{
    public boolean processInteract(EntityPlayer player, Entity entity, EnumHand hand) {

        return false;
    }
}
