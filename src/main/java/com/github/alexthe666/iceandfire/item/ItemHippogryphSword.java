package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHippogryphSword extends SwordItem {

    public ItemHippogryphSword() {
        super(IafItemRegistry.HIPPOGRYPH_SWORD_TOOL_MATERIAL, 3, -2.4F, new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, "hippogryph_sword");
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity targetEntity, LivingEntity attacker) {
        float f = (float) attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(attacker) * f;
        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;
            for (LivingEntity LivingEntity : attacker.level.getEntitiesOfClass(LivingEntity.class, targetEntity.getBoundingBox().inflate(1.0D, 0.25D, 1.0D))) {
                if (LivingEntity != player && LivingEntity != targetEntity && !attacker.isAlliedTo(LivingEntity) && attacker.distanceToSqr(LivingEntity) < 9.0D) {
                    LivingEntity.knockback(0.4F, MathHelper.sin(attacker.yRot * 0.017453292F), -MathHelper.cos(attacker.yRot * 0.017453292F));
                    LivingEntity.hurt(DamageSource.playerAttack(player), f3);
                }
            }
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
            player.sweepAttack();
        }
        return super.hurtEnemy(stack, targetEntity, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.hippogryph_sword.desc_0").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.hippogryph_sword.desc_1").withStyle(TextFormatting.GRAY));
    }
}
