package com.github.alexthe666.iceandfire.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public interface Interactable {
    boolean processInteract(EntityPlayer player, Entity entity, EnumHand hand, ItemStack stack);
}
