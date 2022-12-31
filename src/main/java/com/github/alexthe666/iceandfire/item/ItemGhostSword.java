package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityGhostSword;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.google.common.collect.Multimap;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGhostSword extends SwordItem {

    public ItemGhostSword() {
        super(IafItemRegistry.GHOST_SWORD_TOOL_MATERIAL, 5, -1.0F, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
    }

    public static void spawnGhostSwordEntity(ItemStack stack, Player playerEntity) {
        if (playerEntity.getCooldowns().isOnCooldown(stack.getItem()))
            return;
        if (playerEntity.getItemInHand(InteractionHand.MAIN_HAND) != stack)
            return;
        final Multimap<Attribute, AttributeModifier> dmg = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);
        double totalDmg = 0D;
        for (AttributeModifier modifier : dmg.get(Attributes.ATTACK_DAMAGE)) {
            totalDmg += modifier.getAmount();
        }
        playerEntity.playSound(SoundEvents.ZOMBIE_INFECT, 1, 1);
        EntityGhostSword shot = new EntityGhostSword(IafEntityRegistry.GHOST_SWORD.get(), playerEntity.level, playerEntity,
            totalDmg * 0.5F);
        Vec3 vector3d = playerEntity.getViewVector(1.0F);
        Vector3f vector3f = new Vector3f(vector3d);
        shot.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 1.0F, 0.5F);
        playerEntity.level.addFreshEntity(shot);
        stack.hurtAndBreak(1, playerEntity, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        playerEntity.getCooldowns().addCooldown(stack.getItem(), 10);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        return super.hurtEnemy(stack, targetEntity, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("item.iceandfire.ghost_sword.desc_0").withStyle(ChatFormatting.GRAY));
    }
}