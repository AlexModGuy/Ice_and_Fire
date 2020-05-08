package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageSetMyrmexHiveNull;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class ItemMyrmexStaff extends Item {

    public ItemMyrmexStaff(boolean jungle) {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS).maxStackSize(1));
        if (jungle) {
            this.setRegistryName(IceAndFire.MODID, "myrmex_jungle_staff");
        } else {
            this.setRegistryName(IceAndFire.MODID, "myrmex_desert_staff");
        }
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
            stack.getTag().putUniqueId("HiveUUID", new UUID(0, 0));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (playerIn.isShiftKeyDown()) {
            return super.onItemRightClick(worldIn, playerIn, hand);
        }
        UUID id = itemStackIn.getTag().getUniqueId("HiveUUID");
        if (!worldIn.isRemote) {
            MyrmexHive hive = MyrmexWorldData.get(worldIn).getHiveFromUUID(id);
            MyrmexWorldData.get(worldIn).addHive(worldIn, new MyrmexHive());
            if (hive != null) {
                IceAndFire.sendMSGToAll(new MessageGetMyrmexHive(hive));
            } else {
                IceAndFire.sendMSGToAll(new MessageSetMyrmexHiveNull());
            }
        } else if (id != null && !id.equals(new UUID(0, 0))) {
            IceAndFire.PROXY.openMyrmexStaffGui(itemStackIn);
        }
        playerIn.swingArm(hand);
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS.PASS, itemStackIn);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (!context.getPlayer().isShiftKeyDown()) {
            return super.onItemUse(context);
        } else {
            UUID id = context.getPlayer().getHeldItem(context.getHand()).getTag().getUniqueId("HiveUUID");
            if (!context.getWorld().isRemote) {
                MyrmexHive hive = MyrmexWorldData.get(context.getWorld()).getHiveFromUUID(id);
                if (hive != null) {
                    IceAndFire.sendMSGToAll(new MessageGetMyrmexHive(hive));
                } else {
                    IceAndFire.sendMSGToAll(new MessageSetMyrmexHiveNull());
                }
            } else if (id != null && !id.equals(new UUID(0, 0))) {
                IceAndFire.PROXY.openMyrmexAddRoomGui(context.getPlayer().getHeldItem(context.getHand()), context.getPos(), context.getPlayer().getHorizontalFacing());
            }
            context.getPlayer().swingArm(context.getHand());
            return ActionResultType.SUCCESS;
        }
    }
}
