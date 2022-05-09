package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGhostSword extends SwordItem {

    public ItemGhostSword() {
        super(IafItemRegistry.GHOST_SWORD_TOOL_MATERIAL, 5, -1.0F, new Item.Properties().group(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, "ghost_sword");
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity livingEntity) {
        if (super.onEntitySwing(stack, livingEntity))
            return true;
        if (livingEntity.getHeldItem(Hand.MAIN_HAND) != stack)
            return false;
        if (livingEntity instanceof PlayerEntity) {
            if (((PlayerEntity) livingEntity).getCooldownTracker().hasCooldown(stack.getItem()))
                return false;
        }
        final Multimap<Attribute, AttributeModifier> dmg = stack.getAttributeModifiers(EquipmentSlotType.MAINHAND);
        double totalDmg = 0D;
        for (AttributeModifier modifier : dmg.get(Attributes.ATTACK_DAMAGE)) {
            totalDmg += modifier.getAmount();
        }
        livingEntity.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1, 1);
        EntityGhostSword shot = new EntityGhostSword(IafEntityRegistry.GHOST_SWORD.get(), livingEntity.world, livingEntity,
            totalDmg * 0.5F);
        Vector3d vector3d = livingEntity.getLook(1.0F);
        Vector3f vector3f = new Vector3f(vector3d);
        shot.shoot(vector3f.getX(), vector3f.getY(), vector3f.getZ(), 1.0F, 0.5F);
        livingEntity.world.addEntity(shot);
        stack.damageItem(1, livingEntity, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        if (livingEntity instanceof PlayerEntity) {
            ((PlayerEntity) livingEntity).getCooldownTracker().setCooldown(stack.getItem(), 10);
        }
        return false;

    }


    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        return super.hitEntity(stack, targetEntity, attacker);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.ghost_sword.desc_0").mergeStyle(TextFormatting.GRAY));
    }
}