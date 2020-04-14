package com.github.alexthe666.iceandfire.compat.tinkers;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.modifiers.IToolMod;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitSweaterSong extends AbstractTrait {

    public TraitSweaterSong() {
        super("sweater_song", TextFormatting.BLUE);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
            if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && target.getItemStackFromSlot(slot) != ItemStack.EMPTY){
                if(random.nextFloat() < IceAndFire.CONFIG.weezerTinkersDisarmChance && isCritical){
                    ItemStack copy = target.getItemStackFromSlot(slot).copy();
                    copy.attemptDamageItem((int)Math.ceil(damage), random, null);
                    target.entityDropItem(copy, 1.0F);
                    target.setItemStackToSlot(slot, ItemStack.EMPTY);
                    if(target instanceof EntityLiving){
                        ((EntityLiving) target).setCanPickUpLoot(false);
                    }
                }
            }
        }
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }
}
