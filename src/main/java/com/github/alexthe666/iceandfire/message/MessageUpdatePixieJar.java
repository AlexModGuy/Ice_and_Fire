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

public class MessageUpdatePixieJar extends AbstractMessage<MessageUpdatePixieJar> {

    public long blockPos;
    public boolean isProducing;

    public MessageUpdatePixieJar(long blockPos, boolean isProducing) {
        this.blockPos = blockPos;
        this.isProducing = isProducing;

    }

    public MessageUpdatePixieJar() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = buf.readLong();
        isProducing = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos);
        buf.writeBoolean(isProducing);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft client, MessageUpdatePixieJar message, EntityPlayer player, MessageContext messageContext) {
        if (client.world != null) {
            BlockPos pos = BlockPos.fromLong(message.blockPos);
            if (client.world.getTileEntity(pos) != null) {
                if (client.world.getTileEntity(pos) instanceof TileEntityJar) {
                    TileEntityJar jar = (TileEntityJar) client.world.getTileEntity(pos);
                    jar.hasProduced = message.isProducing;
                }
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageUpdatePixieJar message, EntityPlayer player, MessageContext messageContext) {

    }
}