package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageSetMyrmexHiveNull;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ItemMyrmexStaff extends Item {

    public ItemMyrmexStaff(boolean jungle) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Level world, Player player) {
        itemStack.setTag(new CompoundTag());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
            stack.getTag().putUUID("HiveUUID", new UUID(0, 0));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);
        if (playerIn.isShiftKeyDown()) {
            return super.use(worldIn, playerIn, hand);
        }
        if (itemStackIn.getTag() != null && itemStackIn.getTag().hasUUID("HiveUUID")) {
            UUID id = itemStackIn.getTag().getUUID("HiveUUID");
            if (!worldIn.isClientSide) {
                MyrmexHive hive = MyrmexWorldData.get(worldIn).getHiveFromUUID(id);
                MyrmexWorldData.get(worldIn).addHive(worldIn, new MyrmexHive());
                if (hive != null) {
                    IceAndFire.sendMSGToAll(new MessageGetMyrmexHive(hive.toNBT()));
                } else {
                    IceAndFire.sendMSGToAll(new MessageSetMyrmexHiveNull());
                }
            } else if (id != null && !id.equals(new UUID(0, 0))) {
                IceAndFire.PROXY.openMyrmexStaffGui(itemStackIn);
            }
        }
        playerIn.swing(hand);
        return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, itemStackIn);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getPlayer().isShiftKeyDown()) {
            return super.useOn(context);
        } else {
            UUID id = context.getPlayer().getItemInHand(context.getHand()).getTag().getUUID("HiveUUID");
            if (!context.getLevel().isClientSide) {
                MyrmexHive hive = MyrmexWorldData.get(context.getLevel()).getHiveFromUUID(id);
                if (hive != null) {
                    IceAndFire.sendMSGToAll(new MessageGetMyrmexHive(hive.toNBT()));
                } else {
                    IceAndFire.sendMSGToAll(new MessageSetMyrmexHiveNull());
                }
            } else if (id != null && !id.equals(new UUID(0, 0))) {
                IceAndFire.PROXY.openMyrmexAddRoomGui(context.getPlayer().getItemInHand(context.getHand()), context.getClickedPos(), context.getPlayer().getDirection());
            }
            context.getPlayer().swing(context.getHand());
            return InteractionResult.SUCCESS;
        }
    }
}
