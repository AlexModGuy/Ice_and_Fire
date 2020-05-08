package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityTideTrident;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTideTrident extends Item {

    public ItemTideTrident() {
        super();
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey("iceandfire.tide_trident");
        this.setRegistryName(IceAndFire.MODID, "tide_trident");
        this.maxStackSize = 1;
        this.setMaxDamage(400);
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
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entity, int timeLeft) {
        int i = this.getMaxItemUseDuration(stack) - timeLeft;
        if (i < 0) return;
        float f = getArrowVelocity(i) * 3.0F;
        entity.playSound(SoundEvents.ENTITY_EGG_THROW, 1, 1);
        EntityTideTrident feather = new EntityTideTrident(worldIn, entity, stack);
        feather.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, f, 1.0F);
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(feather);
        }
        if (!(entity instanceof PlayerEntity && ((PlayerEntity) entity).isCreative())) {
            stack.shrink(1);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

        tooltip.add(new TranslationTextComponent("item.iceandfire.legendary_weapon.desc"));
        tooltip.add(new TranslationTextComponent("item.iceandfire.tide_trident.desc_0"));
        tooltip.add(new TranslationTextComponent("item.iceandfire.tide_trident.desc_1"));
    }
}
