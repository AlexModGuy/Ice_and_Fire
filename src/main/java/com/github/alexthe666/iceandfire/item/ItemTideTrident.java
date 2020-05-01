package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityTideTrident;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft) {
        int i = this.getMaxItemUseDuration(stack) - timeLeft;
        if (i < 0) return;
        float f = getArrowVelocity(i) * 3.0F;
        entity.playSound(SoundEvents.ENTITY_EGG_THROW, 1, 1);
        EntityTideTrident feather = new EntityTideTrident(worldIn, entity, stack);
        feather.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, f, 1.0F);
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(feather);
        }
        if (!(entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative())) {
            stack.shrink(1);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
        ItemStack itemStackIn = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("item.iceandfire.legendary_weapon.desc"));
        tooltip.add(I18n.format("item.iceandfire.tide_trident.desc_0"));
        tooltip.add(I18n.format("item.iceandfire.tide_trident.desc_1"));
    }
}
