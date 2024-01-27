package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemDragonArrow extends ArrowItem {
    public ItemDragonArrow() {
        super(new Properties());
    }

    @Override
    public @NotNull AbstractArrow createArrow(@NotNull final Level level, @NotNull final ItemStack arrow, @NotNull final LivingEntity shooter) {
        return new EntityDragonArrow(IafEntityRegistry.DRAGON_ARROW.get(), shooter, level);
    }

    @Override
    public boolean isInfinite(@NotNull final ItemStack arrow, @NotNull final ItemStack bow, @NotNull final Player player) {
        // Technically this would always return false - it's more a compat layer for Apotheosis' Endless Quiver enchantment
        boolean isInfinite = super.isInfinite(arrow, bow, player);

        if (!isInfinite) {
            isInfinite = bow.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0 && getClass() == ItemDragonArrow.class;
        }

        return isInfinite;
    }
}
