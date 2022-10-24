package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDragonsteelFireArmor;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDragonsteelIceArmor;
import com.github.alexthe666.iceandfire.client.model.armor.ModelDragonsteelLightningArmor;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.github.alexthe666.iceandfire.item.IafItemRegistry.*;

public class ItemDragonsteelArmor extends ArmorItem implements IProtectAgainstDragonItem, IItemRenderProperties {

    private static final UUID[] ARMOR_MODIFIERS = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    private final ArmorMaterial material;
    private Multimap<Attribute, AttributeModifier> attributeModifierMultimap;

    public ItemDragonsteelArmor(ArmorMaterial material, int renderIndex, EquipmentSlot slot) {
        super(material, slot, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.material = material;
        this.attributeModifierMultimap = createAttributeMap();
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            @Nullable
            public HumanoidModel<?> getArmorModel(LivingEntity LivingEntity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                boolean inner = armorSlot == EquipmentSlot.LEGS || armorSlot == EquipmentSlot.HEAD;
                if (itemStack.getItem() instanceof ArmorItem) {
                    ArmorMaterial armorMaterial = ((ArmorItem) itemStack.getItem()).getMaterial();
                    if (DRAGONSTEEL_FIRE_ARMOR_MATERIAL.equals(armorMaterial))
                        return new ModelDragonsteelFireArmor(inner);
                    if (DRAGONSTEEL_ICE_ARMOR_MATERIAL.equals(armorMaterial))
                        return new ModelDragonsteelIceArmor(inner);
                    if (DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL.equals(armorMaterial))
                        return new ModelDragonsteelLightningArmor(inner);
                }
                return _default;

            }
        });
    }


    //Workaround for armor attributes being registered before the config gets loaded
    private Multimap<Attribute, AttributeModifier> createAttributeMap() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID uuid = ARMOR_MODIFIERS[slot.getIndex()];
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", material.getDefenseForSlot(slot), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", material.getToughness(), AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }

    private Multimap<Attribute, AttributeModifier> getOrUpdateAttributeMap() {
        //If the armor values have changed recreate the map
        //There might be a prettier way of accomplishing this but it works
        if (this.attributeModifierMultimap.containsKey(Attributes.ARMOR)
            && !this.attributeModifierMultimap.get(Attributes.ARMOR).isEmpty()
            && this.attributeModifierMultimap.get(Attributes.ARMOR).toArray()[0] instanceof AttributeModifier
            && ((AttributeModifier) this.attributeModifierMultimap.get(Attributes.ARMOR).toArray()[0]).getAmount() != getDefense()
        ) {
            this.attributeModifierMultimap = createAttributeMap();
        }
        return attributeModifierMultimap;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (this.slot != null) {
            return (this.getMaterial()).getDurabilityForSlot(this.slot);
        }
        return super.getMaxDamage(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.dragonscales_armor.desc").withStyle(ChatFormatting.GRAY));
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == this.slot ? getOrUpdateAttributeMap() : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public int getDefense() {
        if (this.material != null)
            return this.material.getDefenseForSlot(this.getSlot());
        return super.getDefense();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (material == DRAGONSTEEL_FIRE_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/armor_dragonsteel_fire" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png");
        } else if (material == IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL) {
            return "iceandfire:textures/models/armor/armor_dragonsteel_ice" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png");
        } else {
            return "iceandfire:textures/models/armor/armor_dragonsteel_lightning" + (slot == EquipmentSlot.LEGS ? "_legs.png" : ".png");
        }
    }
}
