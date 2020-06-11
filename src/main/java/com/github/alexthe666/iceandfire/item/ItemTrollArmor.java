package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTrollArmor extends ArmorItem {

    public EnumTroll troll;

    public ItemTrollArmor(EnumTroll troll, CustomArmorMaterial material, EquipmentSlotType slot) {
        super(material, slot, new Item.Properties().group(IceAndFire.TAB_ITEMS));
        this.troll = troll;
        this.setRegistryName(troll.name().toLowerCase() + "_troll_leather_" + getArmorPart(slot));
    }

    public IArmorMaterial getArmorMaterial() {
        return troll.material;
    }


    private String getArmorPart(EquipmentSlotType slot) {
        switch (slot) {
            case HEAD:
                return "helmet";
            case CHEST:
                return "chestplate";
            case LEGS:
                return "leggings";
            case FEET:
                return "boots";
        }
        return "";
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        return (A) IceAndFire.PROXY.getArmorModel(slot == EquipmentSlotType.LEGS ? 7 : 6);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "iceandfire:textures/models/armor/armor_troll_" + troll.name().toLowerCase() + (slot == EquipmentSlotType.LEGS ? "_legs.png" : ".png");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.troll_leather_armor_" + getArmorPart(slot) + ".desc").applyTextStyle(TextFormatting.GREEN));
    }
}
