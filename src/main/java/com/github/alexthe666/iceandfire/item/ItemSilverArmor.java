package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemSilverArmor extends ItemArmor {

    public ItemSilverArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String gameName, String name) {
        super(material, renderIndex, slot);
        this.setCreativeTab(IceAndFire.TAB);
        this.setUnlocalizedName(name);
        this.setRegistryName(IceAndFire.MODID, gameName);
        GameRegistry.register(this);
    }
}
