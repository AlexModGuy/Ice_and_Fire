package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class ItemMobSkull extends Item {

    private final EnumSkullType skull;

    public ItemMobSkull(EnumSkullType skull) {
        super(new Item.Properties().stacksTo(1));
        this.skull = skull;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        EntityMobSkull skull = new EntityMobSkull(IafEntityRegistry.MOB_SKULL.get(), context.getLevel());
        ItemStack stack = player.getItemInHand(context.getHand());
        BlockPos offset = context.getClickedPos().relative(context.getClickedFace(), 1);
        skull.moveTo(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
        float yaw = player.getYRot();
        if (context.getClickedFace() != Direction.UP) {
            yaw = player.getDirection().toYRot();
        }
        skull.setYaw(yaw);
        skull.setSkullType(this.skull);
        if (!context.getLevel().isClientSide) {
            context.getLevel().addFreshEntity(skull);
        }
        if (stack.hasCustomHoverName()) {
            skull.setCustomName(stack.getHoverName());
        }
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        return InteractionResult.SUCCESS;
    }
}
