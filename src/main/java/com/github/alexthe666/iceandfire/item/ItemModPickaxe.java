package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.item.CustomToolMaterial;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.props.FrozenProperties;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemModPickaxe extends PickaxeItem {

    private final CustomToolMaterial toolMaterial;

    public ItemModPickaxe(CustomToolMaterial toolmaterial, String gameName) {
        super(toolmaterial, 1, -2.8F, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.toolMaterial = toolmaterial;
        this.setRegistryName(IceAndFire.MODID, gameName);
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND && this.toolMaterial instanceof DragonsteelToolMaterial ? this.bakeDragonsteel() : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    private Multimap<Attribute, AttributeModifier> dragonsteelModifiers;

    private Multimap<Attribute, AttributeModifier> bakeDragonsteel() {
        if (toolMaterial.getAttackDamageBonus() != IafConfig.dragonsteelBaseDamage || dragonsteelModifiers == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> lvt_5_1_ = ImmutableMultimap.builder();
            lvt_5_1_.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", IafConfig.dragonsteelBaseDamage - 1F + 1F, AttributeModifier.Operation.ADDITION));
            lvt_5_1_.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8F, AttributeModifier.Operation.ADDITION));
            this.dragonsteelModifiers = lvt_5_1_.build();
            return this.dragonsteelModifiers;
        } else {
            return dragonsteelModifiers;
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return toolMaterial.getUses();
    }

    public float getAttackDamage() {
        return this.toolMaterial instanceof DragonsteelToolMaterial ? (float) IafConfig.dragonsteelBaseDamage : super.getAttackDamage();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (toolMaterial == IafItemRegistry.SILVER_TOOL_MATERIAL) {
            if (target.getMobType() == CreatureAttribute.UNDEAD) {
                target.hurt(DamageSource.MAGIC, getAttackDamage() + 3.0F);
            }
        }
        if (this.toolMaterial == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            if (target.getMobType() != CreatureAttribute.ARTHROPOD) {
                target.hurt(DamageSource.GENERIC, getAttackDamage() + 5.0F);
            }
            if (target instanceof EntityDeathWorm) {
                target.hurt(DamageSource.GENERIC, getAttackDamage() + 5.0F);
            }
        }
        if (toolMaterial == IafItemRegistry.DRAGONSTEEL_FIRE_TOOL_MATERIAL && IafConfig.dragonWeaponFireAbility) {
            target.setSecondsOnFire(15);
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (toolMaterial == IafItemRegistry.DRAGONSTEEL_ICE_TOOL_MATERIAL && IafConfig.dragonWeaponIceAbility) {
            FrozenProperties.setFrozenFor(target, 300);
            target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 300, 2));
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (toolMaterial == IafItemRegistry.DRAGONSTEEL_LIGHTNING_TOOL_MATERIAL && IafConfig.dragonWeaponLightningAbility) {
            boolean flag = true;
            if (attacker instanceof PlayerEntity) {
                if (attacker.attackAnim > 0.2) {
                    flag = false;
                }
            }
            if (!attacker.level.isClientSide && flag) {
                LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(target.level);
                lightningboltentity.moveTo(target.position());
                if (!target.level.isClientSide) {
                    target.level.addFreshEntity(lightningboltentity);
                }
            }
            target.knockback(1F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (toolMaterial == IafItemRegistry.SILVER_TOOL_MATERIAL) {
            tooltip.add(new TranslationTextComponent("silvertools.hurt").withStyle(TextFormatting.GREEN));
        }
        if (toolMaterial == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            tooltip.add(new TranslationTextComponent("myrmextools.hurt").withStyle(TextFormatting.GREEN));
        }
        if (toolMaterial == IafItemRegistry.DRAGONSTEEL_FIRE_TOOL_MATERIAL && IafConfig.dragonWeaponFireAbility) {
            tooltip.add(new TranslationTextComponent("dragon_sword_fire.hurt2").withStyle(TextFormatting.DARK_RED));
        }
        if (toolMaterial == IafItemRegistry.DRAGONSTEEL_ICE_TOOL_MATERIAL && IafConfig.dragonWeaponIceAbility) {
            tooltip.add(new TranslationTextComponent("dragon_sword_ice.hurt2").withStyle(TextFormatting.AQUA));
        }
        if (toolMaterial == IafItemRegistry.DRAGONSTEEL_LIGHTNING_TOOL_MATERIAL && IafConfig.dragonWeaponLightningAbility) {
            tooltip.add(new TranslationTextComponent("dragon_sword_lightning.hurt2").withStyle(TextFormatting.DARK_PURPLE));
        }
    }
}
