package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityTideTrident;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTideTrident extends Item {

    public ItemTideTrident() {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxDamage(400));
        this.setRegistryName(IceAndFire.MODID, "tide_trident");
    }

    public static float getArrowVelocity(int i) {
        float f = i / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entity, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        if (i < 0) return;
        float f = getArrowVelocity(i) * 3.0F;
        entity.playSound(SoundEvents.ENTITY_EGG_THROW, 1, 1);
        EntityTideTrident feather = new EntityTideTrident(worldIn, entity, stack);
        feather.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, f, 1.0F);
        if (!worldIn.isRemote) {
            worldIn.addEntity(feather);
        }
        if (!(entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative())) {
            stack.shrink(1);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc").applyTextStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.tide_trident.desc_0").applyTextStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.tide_trident.desc_1").applyTextStyle(TextFormatting.GRAY));
    }
}
