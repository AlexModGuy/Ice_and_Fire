package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageUpdatePodium extends AbstractMessage<MessageUpdatePodium> {

    public long blockPos;
    public ItemStack heldStack;

    public MessageUpdatePodium(long blockPos, ItemStack heldStack) {
        this.blockPos = blockPos;
        this.heldStack = heldStack;

    }

    public MessageUpdatePodium() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = buf.readLong();
        heldStack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos);
        ByteBufUtils.writeItemStack(buf, heldStack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft client, MessageUpdatePodium message, PlayerEntity player, MessageContext messageContext) {
        if (client.world != null) {
            BlockPos pos = BlockPos.fromLong(message.blockPos);
            if (client.world.getTileEntity(pos) != null) {
                if (client.world.getTileEntity(pos) instanceof TileEntityPodium) {
                    TileEntityPodium podium = (TileEntityPodium) client.world.getTileEntity(pos);
                    podium.setInventorySlotContents(0, message.heldStack);
                }
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageUpdatePodium message, PlayerEntity player, MessageContext messageContext) {

    }
}