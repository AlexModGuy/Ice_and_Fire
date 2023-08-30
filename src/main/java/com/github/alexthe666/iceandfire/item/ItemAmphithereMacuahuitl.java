package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemAmphithereMacuahuitl extends SwordItem {

    public ItemAmphithereMacuahuitl() {
        super(IafItemRegistry.AMPHITHERE_SWORD_TOOL_MATERIAL, 3, -2.4F, new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        targetEntity.playSound(IafSoundRegistry.AMPHITHERE_GUST, 1, 1);
        targetEntity.playSound(SoundEvents.SHIELD_BLOCK, 1, 1);
        targetEntity.hasImpulse = true;
        double xRatio = -Mth.sin(attacker.getYRot() * 0.017453292F);
        double zRatio = Mth.cos(attacker.getYRot() * 0.017453292F);
        float strength = -0.6F;
        float f = Mth.sqrt((float) (xRatio * xRatio + zRatio * zRatio));
        targetEntity.setDeltaMovement((targetEntity.getDeltaMovement().x / 2) - xRatio / (double) f * (double) strength, 0.8D, (targetEntity.getDeltaMovement().z / 2) - zRatio / (double) f * (double) strength);
        Random rand = new Random();
        for (int i = 0; i < 20; ++i) {
            double d0 = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            double d3 = 10.0D;
            targetEntity.level().addParticle(ParticleTypes.CLOUD, targetEntity.getX() + (double) (rand.nextFloat() * targetEntity.getBbWidth() * 5.0F) - (double) targetEntity.getBbWidth() - d0 * 10.0D, targetEntity.getY() + (double) (rand.nextFloat() * targetEntity.getBbHeight()) - d1 * 10.0D, targetEntity.getZ() + (double) (rand.nextFloat() * targetEntity.getBbWidth() * 5.0F) - (double) targetEntity.getBbWidth() - d2 * 10.0D, d0, d1, d2);
        }
        return super.hurtEnemy(stack, targetEntity, attacker);
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.amphithere_macuahuitl.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.amphithere_macuahuitl.desc_1").withStyle(ChatFormatting.GRAY));
    }
}
