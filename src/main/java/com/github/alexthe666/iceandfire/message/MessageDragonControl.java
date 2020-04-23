package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.*;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDragonControl extends AbstractMessage<MessageDragonControl> {

    public int dragonId;
    public byte controlState;
    public int armor_type;
    private double posX;
    private double posY;
    private double posZ;

    public MessageDragonControl(int dragonId, byte controlState, double posX, double posY, double posZ) {
        this.dragonId = dragonId;
        this.controlState = controlState;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public MessageDragonControl() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dragonId = buf.readInt();
        controlState = buf.readByte();
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dragonId);
        buf.writeByte(controlState);
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageDragonControl message, EntityPlayer player, MessageContext messageContext) {
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageDragonControl message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.dragonId);
            if (entity.isRidingOrBeingRiddenBy(player)) {
                if (entity != null && entity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) entity;
                    if (dragon.isOwner(player)) {
                        dragon.setControlState(message.controlState);
                    }
                    dragon.setPosition(message.posX, message.posY, message.posZ);
                } else if (entity instanceof EntityHippogryph) {
                    EntityHippogryph hippo = (EntityHippogryph) entity;
                    if (hippo.isOwner(player)) {
                        hippo.setControlState(message.controlState);
                    }
                    hippo.setPosition(message.posX, message.posY, message.posZ);
                } else if (entity instanceof EntityHippocampus) {
                    EntityHippocampus hippo = (EntityHippocampus) entity;
                    if (hippo.isOwner(player)) {
                        hippo.setControlState(message.controlState);
                    }
                    hippo.setPosition(message.posX, message.posY, message.posZ);
                } else if (entity instanceof EntityDeathWorm) {
                    EntityDeathWorm deathworm = (EntityDeathWorm) entity;
                    deathworm.setControlState(message.controlState);
                    deathworm.setPosition(message.posX, message.posY, message.posZ);
                } else if (entity instanceof EntityAmphithere) {
                    EntityAmphithere amphi = (EntityAmphithere) entity;
                    if (amphi.isOwner(player)) {
                        amphi.setControlState(message.controlState);
                    }
                    amphi.setPosition(message.posX, message.posY, message.posZ);
                }
            }
        }
    }


}