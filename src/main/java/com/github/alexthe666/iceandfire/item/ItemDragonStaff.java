package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class ItemDragonStaff extends ItemGeneric implements Interactable {
    public ItemDragonStaff() {
        super("dragon_stick", "iceandfire.dragon_stick");
        this.maxStackSize = 1;
    }

    @Override
    public boolean processInteract(EntityPlayer player, Entity entity, EnumHand hand, ItemStack stack) {
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            if(dragon.isOwner(player)) {
                if (player.isSneaking()) {
                    BlockPos homePos = new BlockPos(dragon);
                    dragon.setHomePos(homePos);
                    player.sendStatusMessage(new TextComponentTranslation("dragon.command.new_home", homePos.getX(), homePos.getY(), homePos.getZ()), true);
                } else {
                    dragon.playOwnerCommandSound();
                    dragon.setCommand(dragon.getCommand() ^ 1);
                    player.sendStatusMessage(new TextComponentTranslation("dragon.command." + (dragon.getCommand() == 1 ? "sit" : "stand")), true);
                }
                return true;
            }
        }
        return false;
    }
}
