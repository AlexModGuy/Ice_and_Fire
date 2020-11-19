package com.github.alexthe666.iceandfire.item;

import java.util.List;

import javax.annotation.Nullable;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityTideTrident;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemTideTrident extends TridentItem {

    public ItemTideTrident() {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxDamage(400));
        this.setRegistryName(IceAndFire.MODID, "tide_trident");
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack p_77615_1_, World p_77615_2_, LivingEntity p_77615_3_, int p_77615_4_) {
        if (p_77615_3_ instanceof PlayerEntity) {
            PlayerEntity lvt_5_1_ = (PlayerEntity)p_77615_3_;
            int lvt_6_1_ = this.getUseDuration(p_77615_1_) - p_77615_4_;
            if (lvt_6_1_ >= 10) {
                int lvt_7_1_ = EnchantmentHelper.getRiptideModifier(p_77615_1_);
                if (lvt_7_1_ <= 0 || lvt_5_1_.isWet()) {
                    if (!p_77615_2_.isRemote) {
                        p_77615_1_.damageItem(1, lvt_5_1_, (p_220047_1_) -> {
                            p_220047_1_.sendBreakAnimation(p_77615_3_.getActiveHand());
                        });
                        if (lvt_7_1_ == 0) {
                            EntityTideTrident lvt_8_1_ = new EntityTideTrident(p_77615_2_, lvt_5_1_, p_77615_1_);
                            lvt_8_1_.func_234612_a_(lvt_5_1_, lvt_5_1_.rotationPitch, lvt_5_1_.rotationYaw, 0.0F, 2.5F + (float)lvt_7_1_ * 0.5F, 1.0F);
                            if (lvt_5_1_.abilities.isCreativeMode) {
                                lvt_8_1_.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                            }

                            p_77615_2_.addEntity(lvt_8_1_);
                            p_77615_2_.playMovingSound((PlayerEntity)null, lvt_8_1_, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!lvt_5_1_.abilities.isCreativeMode) {
                                lvt_5_1_.inventory.deleteStack(p_77615_1_);
                            }
                        }
                    }

                    lvt_5_1_.addStat(Stats.ITEM_USED.get(this));
                    if (lvt_7_1_ > 0) {
                        float lvt_8_2_ = lvt_5_1_.rotationYaw;
                        float lvt_9_1_ = lvt_5_1_.rotationPitch;
                        float lvt_10_1_ = -MathHelper.sin(lvt_8_2_ * 0.017453292F) * MathHelper.cos(lvt_9_1_ * 0.017453292F);
                        float lvt_11_1_ = -MathHelper.sin(lvt_9_1_ * 0.017453292F);
                        float lvt_12_1_ = MathHelper.cos(lvt_8_2_ * 0.017453292F) * MathHelper.cos(lvt_9_1_ * 0.017453292F);
                        float lvt_13_1_ = MathHelper.sqrt(lvt_10_1_ * lvt_10_1_ + lvt_11_1_ * lvt_11_1_ + lvt_12_1_ * lvt_12_1_);
                        float lvt_14_1_ = 3.0F * ((1.0F + (float)lvt_7_1_) / 4.0F);
                        lvt_10_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_11_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_12_1_ *= lvt_14_1_ / lvt_13_1_;
                        lvt_5_1_.addVelocity((double)lvt_10_1_, (double)lvt_11_1_, (double)lvt_12_1_);
                        lvt_5_1_.startSpinAttack(20);
                        if (lvt_5_1_.func_233570_aj_()) {
                            float lvt_15_1_ = 1.1999999F;
                            lvt_5_1_.move(MoverType.SELF, new Vector3d(0.0D, 1.1999999284744263D, 0.0D));
                        }

                        SoundEvent lvt_15_4_;
                        if (lvt_7_1_ >= 3) {
                            lvt_15_4_ = SoundEvents.ITEM_TRIDENT_RIPTIDE_3;
                        } else if (lvt_7_1_ == 2) {
                            lvt_15_4_ = SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
                        } else {
                            lvt_15_4_ = SoundEvents.ITEM_TRIDENT_RIPTIDE_1;
                        }

                        p_77615_2_.playMovingSound((PlayerEntity)null, lvt_5_1_, lvt_15_4_, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }

                }
            }
        }
    }


    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            builder.put(Attributes.field_233823_f_, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)12.0D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.field_233825_h_, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        }

        return builder.build();
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").func_240699_a_(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.tide_trident.desc_0").func_240699_a_(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.tide_trident.desc_1").func_240699_a_(TextFormatting.GRAY));
    }
}
