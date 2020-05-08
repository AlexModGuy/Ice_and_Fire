package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSeaSerpentArmor extends ItemArmor {

    public EnumSeaSerpent armor_type;

    public ItemSeaSerpentArmor(EnumSeaSerpent armorType, ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot) {
        super(material, renderIndex, slot);
        this.armor_type = armorType;
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
    }

    @OnlyIn(Dist.CLIENT)
    public ModelBiped getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        return (ModelBiped) IceAndFire.PROXY.getArmorModel(renderIndex == 2 ? 9 : 8);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "iceandfire:textures/models/armor/armor_tide_" + armor_type.resourceName + (renderIndex == 2 ? "_legs.png" : ".png");
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        tooltip.add(armor_type.color + new TranslationTextComponent("sea_serpent." + armor_type.resourceName));
        tooltip.add(new TranslationTextComponent("item.iceandfire.sea_serpent_armor.desc_0"));
        tooltip.add(new TranslationTextComponent("item.iceandfire.sea_serpent_armor.desc_1"));
    }
}
