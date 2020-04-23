package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntityHydraHead;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessagePlayerHitMultipart extends AbstractMessage<MessagePlayerHitMultipart> {
    public int creatureID;
    public int extraData;

    public MessagePlayerHitMultipart(int creatureID) {
        this.creatureID = creatureID;
        this.extraData = 0;
    }

    public MessagePlayerHitMultipart(int creatureID, int extraData) {
        this.creatureID = creatureID;
        this.extraData = extraData;
    }

    public MessagePlayerHitMultipart() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        creatureID = buf.readInt();
        extraData = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(creatureID);
        buf.writeInt(extraData);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessagePlayerHitMultipart message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.creatureID);
            if (entity != null && entity instanceof EntityLivingBase) {
                double dist = player.getDistance(entity);
                EntityLivingBase mob = (EntityLivingBase) entity;
                if(dist < 100) {
                    player.attackTargetEntityWithCurrentItem(mob);
                    if (mob instanceof EntityHydra) {
                        ((EntityHydra) mob).triggerHeadFlags(message.extraData);
                    }
                }
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessagePlayerHitMultipart message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.creatureID);
            if (entity != null && entity instanceof EntityLivingBase) {
                double dist = player.getDistance(entity);
                EntityLivingBase mob = (EntityLivingBase) entity;
                if(dist < 100) {
                    player.attackTargetEntityWithCurrentItem(mob);
                    if (mob instanceof EntityHydra) {
                        ((EntityHydra) mob).triggerHeadFlags(message.extraData);
                    }
                }
            }
        }
    }
}
