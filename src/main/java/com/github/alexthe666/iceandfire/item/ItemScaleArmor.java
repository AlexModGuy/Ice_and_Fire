package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelFireDragonScaleArmor;
import com.github.alexthe666.iceandfire.client.model.armor.ModelIceDragonScaleArmor;
import com.github.alexthe666.iceandfire.client.model.armor.ModelLightningDragonScaleArmor;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.List;

public class ItemScaleArmor extends ArmorItem implements IProtectAgainstDragonItem, IItemRenderProperties {

    public EnumDragonArmor armor_type;
    public EnumDragonEgg eggType;

    public ItemScaleArmor(EnumDragonEgg eggType, EnumDragonArmor armorType, CustomArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.armor_type = armorType;
        this.eggType = eggType;
    }

    public String getDescriptionId() {
        switch (this.slot) {
            case HEAD:
                return "item.iceandfire.dragon_helmet";
            case CHEST:
                return "item.iceandfire.dragon_chestplate";
            case LEGS:
                return "item.iceandfire.dragon_leggings";
            case FEET:
                return "item.iceandfire.dragon_boots";
        }
        return "item.iceandfire.dragon_helmet";
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            @Nullable
            public HumanoidModel<?> getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                boolean inner = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD;
                if (itemStack.getItem() instanceof ItemScaleArmor) {
                    DragonType dragonType = ((ItemScaleArmor) itemStack.getItem()).armor_type.eggType.dragonType;

                    if (DragonType.FIRE == dragonType)
                        return new ModelFireDragonScaleArmor(inner);
                    if (DragonType.ICE == dragonType)
                        return new ModelIceDragonScaleArmor(inner);
                    if (DragonType.LIGHTNING == dragonType)
                        return new ModelLightningDragonScaleArmor(inner);
                }
                return _default;

            }
        });
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "iceandfire:textures/models/armor/" + armor_type.name() + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png");
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("dragon." + eggType.toString().toLowerCase()).withStyle(eggType.color));
        tooltip.add(new TranslatableComponent("item.dragonscales_armor.desc").withStyle(ChatFormatting.GRAY));
    }
}
