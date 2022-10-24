package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDeathWormArmor;
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

public class ItemDeathwormArmor extends ArmorItem implements IItemRenderProperties {

    public ItemDeathwormArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
    }


    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            @Nullable
            public HumanoidModel<?> getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return new ModelDeathWormArmor(ModelDeathWormArmor.getBakedModel(armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD));
            }
        });
    }


    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (this.getMaterial() == IafItemRegistry.DEATHWORM_2_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/armor_deathworm_red" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png");
        } else if (this.getMaterial() == IafItemRegistry.DEATHWORM_1_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/armor_deathworm_white" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png");
        } else {
            return "iceandfire:textures/models/armor/armor_deathworm_yellow" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png");
        }
    }
}
