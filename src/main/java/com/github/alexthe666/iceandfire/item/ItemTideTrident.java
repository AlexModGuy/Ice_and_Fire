package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityTideTrident;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.PiercingEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTideTrident extends TridentItem {

    public ItemTideTrident() {
        super(IceAndFire.PROXY.setupISTER(new Item.Properties().tab(IceAndFire.TAB_ITEMS).durability(400)));
        this.setRegistryName(IceAndFire.MODID, "tide_trident");
    }

    @Override
    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity lvt_5_1_ = (PlayerEntity) entityLiving;
            int lvt_6_1_ = this.getUseDuration(stack) - timeLeft;
            if (lvt_6_1_ >= 10) {
                int lvt_7_1_ = EnchantmentHelper.getRiptide(stack);
                if (lvt_7_1_ <= 0 || lvt_5_1_.isInWaterOrRain()) {
                    if (!worldIn.isClientSide) {
                        stack.hurtAndBreak(1, lvt_5_1_, (player) -> {
                            player.broadcastBreakEvent(entityLiving.getUsedItemHand());
                        });
                        if (lvt_7_1_ == 0) {
                            EntityTideTrident lvt_8_1_ = new EntityTideTrident(worldIn, lvt_5_1_, stack);
                            lvt_8_1_.shootFromRotation(lvt_5_1_, lvt_5_1_.xRot, lvt_5_1_.yRot, 0.0F, 2.5F + (float) lvt_7_1_ * 0.5F, 1.0F);
                            if (lvt_5_1_.abilities.instabuild) {
                                lvt_8_1_.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                            }

                            worldIn.addFreshEntity(lvt_8_1_);
                            worldIn.playSound(null, lvt_8_1_, SoundEvents.TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!lvt_5_1_.abilities.instabuild) {
                                lvt_5_1_.inventory.removeItem(stack);
                            }
                        }
                    }

                    lvt_5_1_.awardStat(Stats.ITEM_USED.get(this));
                    if (lvt_7_1_ > 0) {
                        float lvt_8_2_ = lvt_5_1_.yRot;
                        float lvt_9_1_ = lvt_5_1_.xRot;
                        float lvt_10_1_ = -MathHelper.sin(lvt_8_2_ * 0.017453292F) * MathHelper.cos(lvt_9_1_ * 0.017453292F);
                        float lvt_11_1_ = -MathHelper.sin(lvt_9_1_ * 0.017453292F);
                        float lvt_12_1_ = MathHelper.cos(lvt_8_2_ * 0.017453292F) * MathHelper.cos(lvt_9_1_ * 0.017453292F);
                        float lvt_13_1_ = MathHelper.sqrt(lvt_10_1_ * lvt_10_1_ + lvt_11_1_ * lvt_11_1_ + lvt_12_1_ * lvt_12_1_);
                        float lvt_14_1_ = 3.0F * ((1.0F + (float) lvt_7_1_) / 4.0F);
                        lvt_10_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_11_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_12_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_5_1_.push(lvt_10_1_, lvt_11_1_, lvt_12_1_);
                        lvt_5_1_.startAutoSpinAttack(20);
                        if (lvt_5_1_.isOnGround()) {
                            float lvt_15_1_ = 1.1999999F;
                            lvt_5_1_.move(MoverType.SELF, new Vector3d(0.0D, 1.1999999284744263D, 0.0D));
                        }

                        SoundEvent lvt_15_4_;
                        if (lvt_7_1_ >= 3) {
                            lvt_15_4_ = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if (lvt_7_1_ == 2) {
                            lvt_15_4_ = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            lvt_15_4_ = SoundEvents.TRIDENT_RIPTIDE_1;
                        }

                        worldIn.playSound(null, lvt_5_1_, lvt_15_4_, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }

                }
            }
        }
    }


    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 12.0D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.9F, AttributeModifier.Operation.ADDITION));
        }

        return builder.build();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
        if (enchantment instanceof PiercingEnchantment)
            return true;
        return enchantment.category.canEnchant(stack.getItem());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.tide_trident.desc_0").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.tide_trident.desc_1").withStyle(TextFormatting.GRAY));
    }
}
