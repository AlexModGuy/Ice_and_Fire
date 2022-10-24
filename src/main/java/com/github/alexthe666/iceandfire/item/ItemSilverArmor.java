package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelSilverArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;

public class ItemSilverArmor extends ArmorItem implements IItemRenderProperties {

    public ItemSilverArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            @Nullable
            public HumanoidModel<?> getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return new ModelSilverArmor(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD);
            }
        });
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "iceandfire:textures/models/armor/" + (slot == EquipmentSlot.LEGS ? "armor_silver_metal_layer_2" : "armor_silver_metal_layer_1") + ".png";
    }


}