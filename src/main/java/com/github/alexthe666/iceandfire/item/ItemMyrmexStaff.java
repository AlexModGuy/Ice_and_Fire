package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
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
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.UUID;

public class ItemMyrmexStaff extends Item {

    public ItemMyrmexStaff(boolean jungle) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS).stacksTo(1));
        if (jungle) {
            this.setRegistryName(IceAndFire.MODID, "myrmex_jungle_staff");
        } else {
            this.setRegistryName(IceAndFire.MODID, "myrmex_desert_staff");
        }
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundNBT());
            stack.getTag().putUUID("HiveUUID", new UUID(0, 0));
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand hand) {
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
        return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
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
            return ActionResultType.SUCCESS;
        }
    }
}
