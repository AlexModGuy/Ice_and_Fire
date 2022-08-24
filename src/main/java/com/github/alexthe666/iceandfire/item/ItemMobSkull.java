package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class ItemMobSkull extends Item implements ICustomRendered {

    private final EnumSkullType skull;

    public ItemMobSkull(EnumSkullType skull) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        this.setRegistryName(IceAndFire.MODID, skull.itemResourceName);
        this.skull = skull;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        EntityMobSkull skull = new EntityMobSkull(IafEntityRegistry.MOB_SKULL.get(), context.getLevel());
        ItemStack stack = player.getItemInHand(context.getHand());
        BlockPos offset = context.getClickedPos().relative(context.getClickedFace(), 1);
        skull.moveTo(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
        float yaw = player.yRot;
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
        return ActionResultType.SUCCESS;
    }
}
