package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemBlindfold extends ItemArmor {

    public ItemBlindfold() {
        super(IafItemRegistry.blindfoldArmor, 0, EntityEquipmentSlot.HEAD);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.blindfold");
        this.setRegistryName(IceAndFire.MODID, "blindfold");
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20, 2, true, false));
    }

}
