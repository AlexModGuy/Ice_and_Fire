package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonSkull extends Item {
    private final int dragonType;

    public ItemDragonSkull(int dragonType) {
        super(new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/.stacksTo(1));
        this.dragonType = dragonType;
    }

    static String getName(int type) {
        return "dragon_skull_%s".formatted(getType(type));
    }

    private static String getType(int type) {
        if (type == 2) {
            return "lightning";
        } else if (type == 1) {
            return "ice";
        } else {
            return "fire";
        }
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
            stack.getTag().putInt("Stage", 4);
            stack.getTag().putInt("DragonAge", 75);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        String iceorfire = "dragon." + getType(dragonType);
        tooltip.add(Component.translatable(iceorfire).withStyle(ChatFormatting.GRAY));
        if (stack.getTag() != null) {
            tooltip.add(Component.translatable("dragon.stage").withStyle(ChatFormatting.GRAY).append(Component.literal(" " + stack.getTag().getInt("Stage"))));
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        /*
         * EntityDragonEgg egg = new EntityDragonEgg(worldIn);
         * egg.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() +
         * 0.5); if(!worldIn.isRemote){ worldIn.spawnEntityInWorld(egg); }
         */
        if (stack.getTag() != null) {
            EntityDragonSkull skull = new EntityDragonSkull(IafEntityRegistry.DRAGON_SKULL.get(), context.getLevel());
            skull.setDragonType(dragonType);
            skull.setStage(stack.getTag().getInt("Stage"));
            skull.setDragonAge(stack.getTag().getInt("DragonAge"));
            BlockPos offset = context.getClickedPos().relative(context.getClickedFace(), 1);
            skull.moveTo(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
            float yaw = context.getPlayer().getYRot();
            if (context.getClickedFace() != Direction.UP) {
                yaw = context.getPlayer().getDirection().toYRot();
            }
            skull.setYaw(yaw);
            if (stack.hasCustomHoverName()) {
                skull.setCustomName(stack.getHoverName());
            }
            if (!context.getLevel().isClientSide) {
                context.getLevel().addFreshEntity(skull);
            }
            if (!context.getPlayer().isCreative()) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;

    }
}
