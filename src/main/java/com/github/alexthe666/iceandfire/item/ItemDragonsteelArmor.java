package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
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

public class ItemDragonsteelArmor extends ArmorItem implements IProtectAgainstDragonItem {

    private IArmorMaterial material;

    public ItemDragonsteelArmor(IArmorMaterial material, int renderIndex, EquipmentSlotType slot, String gameName, String name) {
        super(material, slot, new Item.Properties().group(IceAndFire.TAB_ITEMS));
        this.material = material;
        this.setRegistryName(IceAndFire.MODID, gameName);
    }


    @OnlyIn(Dist.CLIENT)
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        int legs = 11;
        int armor = 10;
        if (material == IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL) {
            legs = 13;
            armor = 12;
        }
        if (material == IafItemRegistry.DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL) {
            legs = 21;
            armor = 20;
        }
        return (A) IceAndFire.PROXY.getArmorModel(slot == EquipmentSlotType.LEGS ? legs : armor);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.dragonscales_armor.desc").func_240699_a_(TextFormatting.GRAY));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (material == IafItemRegistry.DRAGONSTEEL_FIRE_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/armor_dragonsteel_fire" + (slot == EquipmentSlotType.LEGS ? "_legs.png" : ".png");
        } else if (material == IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/armor_dragonsteel_ice" + (slot == EquipmentSlotType.LEGS ? "_legs.png" : ".png");
        } else {
            return "iceandfire:textures/models/armor/armor_dragonsteel_lightning" + (slot == EquipmentSlotType.LEGS ? "_legs.png" : ".png");
        }
    }
}
