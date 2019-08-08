package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSyncMountPosition extends AbstractMessage<MessageSyncMountPosition> {

    public int dragonId;
    private double posX;
    private double posY;
    private double posZ;

    public MessageSyncMountPosition(int dragonId, double posX, double posY, double posZ) {
        this.dragonId = dragonId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public MessageSyncMountPosition() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dragonId = buf.readInt();
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dragonId);
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageSyncMountPosition message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.dragonId);
        if(entity != null){
            entity.setPosition(message.posX, message.posY, message.posZ);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageSyncMountPosition message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.dragonId);
        if(entity != null){
            entity.setPosition(message.posX, message.posY, message.posZ);
        }
    }
}