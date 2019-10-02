package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageUpdatePixieHouse extends AbstractMessage<MessageUpdatePixieHouse> {

    public long blockPos;
    public boolean hasPixie;
    public int pixieType;

    public MessageUpdatePixieHouse(long blockPos, boolean hasPixie, int pixieType) {
        this.blockPos = blockPos;
        this.hasPixie = hasPixie;
        this.pixieType = pixieType;

    }

    public MessageUpdatePixieHouse() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = buf.readLong();
        hasPixie = buf.readBoolean();
        pixieType = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(blockPos);
        buf.writeBoolean(hasPixie);
        buf.writeInt(pixieType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageUpdatePixieHouse message, EntityPlayer player, MessageContext messageContext) {
        BlockPos pos = BlockPos.fromLong(message.blockPos);
        if (client.world.getTileEntity(pos) != null && client.world.getTileEntity(pos) instanceof TileEntityPixieHouse) {
            TileEntityPixieHouse house = (TileEntityPixieHouse) client.world.getTileEntity(pos);
            house.hasPixie = message.hasPixie;
            house.pixieType = message.pixieType;
        } else if (client.world.getTileEntity(pos) != null && client.world.getTileEntity(pos) instanceof TileEntityJar) {
            TileEntityJar jar = (TileEntityJar) client.world.getTileEntity(pos);
            jar.hasPixie = message.hasPixie;
            jar.pixieType = message.pixieType;
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageUpdatePixieHouse message, EntityPlayer player, MessageContext messageContext) {

    }
}