package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCyclopsEye extends Item {

    public ItemCyclopsEye() {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).durability(500));
        this.setRegistryName(IceAndFire.MODID, "cyclops_eye");
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.sameItem(newStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
        } else {
            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entity;
                if (living.getMainHandItem() == stack || living.getOffhandItem() == stack) {
                    double range = 15;
                    boolean inflictedDamage = false;
                    for (MobEntity LivingEntity : world.getEntitiesOfClass(MobEntity.class, new AxisAlignedBB(living.getX() - range, living.getY() - range, living.getZ() - range, living.getX() + range, living.getY() + range, living.getZ() + range))) {
                        if (!LivingEntity.is(living) && !LivingEntity.isAlliedTo(living) && (LivingEntity.getTarget() == living || LivingEntity.getLastHurtByMob() == living || LivingEntity instanceof IMob)) {
                            LivingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 10, 1));
                            inflictedDamage = true;
                        }
                    }
                    if (inflictedDamage) {
                        stack.getTag().putInt("HurtingTicks", stack.getTag().getInt("HurtingTicks") + 1);
                    }
                }
                if (stack.getTag().getInt("HurtingTicks") > 120) {
                    stack.hurtAndBreak(1, (LivingEntity) entity, (p_220017_1_) -> {
                    });
                    stack.getTag().putInt("HurtingTicks", 0);
                }
            }

        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.cyclops_eye.desc_0").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.cyclops_eye.desc_1").withStyle(TextFormatting.GRAY));
    }
}
