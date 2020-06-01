package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDragonSyncFire extends AbstractMessage<MessageDragonSyncFire> {

    public int dragonId;
    public double posX;
    public double posY;
    public double posZ;
    public int syncType;

    public MessageDragonSyncFire(int dragonId, double posX, double posY, double posZ, int syncType) {
        this.dragonId = dragonId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.syncType = syncType;
    }

    public MessageDragonSyncFire() {
    }

    public void fromBytes(ByteBuf buf) {
        dragonId = buf.readInt();
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();
        syncType = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dragonId);
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
        buf.writeInt(syncType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageDragonSyncFire message, EntityPlayer player, MessageContext messageContext) {
        if (player != null && player.world != null) {
            Entity entity = player.world.getEntityByID(message.dragonId);
            if (entity != null && entity instanceof EntityDragonBase) {
                EntityDragonBase dragon = (EntityDragonBase) entity;
                dragon.stimulateFire(message.posX, message.posY, message.posZ, message.syncType);
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageDragonSyncFire message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.dragonId);
            if (entity != null && entity instanceof EntityDragonBase) {
                EntityDragonBase dragon = (EntityDragonBase) entity;
                dragon.stimulateFire(message.posX, message.posY, message.posZ, message.syncType);
            }
        }
    }
}
