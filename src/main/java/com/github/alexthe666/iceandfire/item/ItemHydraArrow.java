package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityHydraArrow;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHydraArrow extends ArrowItem {

    public ItemHydraArrow() {
        super(new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/);
    }

    @Override
    public @NotNull AbstractArrow createArrow(@NotNull Level worldIn, @NotNull ItemStack stack, @NotNull LivingEntity shooter) {
        return new EntityHydraArrow(IafEntityRegistry.HYDRA_ARROW.get(), worldIn, shooter);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.hydra_arrow.desc").withStyle(ChatFormatting.GRAY));
    }
}
