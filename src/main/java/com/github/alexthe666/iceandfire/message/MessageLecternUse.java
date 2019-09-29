package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLecternUse extends AbstractMessage<MessageLecternUse> {

    public long blockPos;
    public int buttonId;

    public MessageLecternUse(long blockPos, int buttonId) {
        this.blockPos = blockPos;
        this.buttonId = buttonId;

    }

    public MessageLecternUse() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = buf.readLong();
        buttonId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos);
        buf.writeInt(buttonId);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageLecternUse message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageLecternUse message, EntityPlayer player, MessageContext messageContext) {
        BlockPos pos = BlockPos.fromLong(message.blockPos);
        /*if (player.openContainer.windowId == message.getWindowId() && player.openContainer.getCanCraft(player) && !player.isSpectator())
        {
            player.openContainer.enchantItem(player, message.buttonId);
            player.openContainer.detectAndSendChanges();
        }*/
    }
}