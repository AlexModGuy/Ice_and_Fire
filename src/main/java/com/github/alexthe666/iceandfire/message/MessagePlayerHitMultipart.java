package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessagePlayerHitMultipart extends AbstractMessage<MessagePlayerHitMultipart> {
    public int creatureID;

    public MessagePlayerHitMultipart(int creatureID) {
        this.creatureID = creatureID;
    }

    public MessagePlayerHitMultipart() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        creatureID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(creatureID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessagePlayerHitMultipart message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.creatureID);
        if (entity != null && entity instanceof EntityLivingBase) {
            EntityLivingBase mob = (EntityLivingBase)entity;
            player.attackTargetEntityWithCurrentItem(mob);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessagePlayerHitMultipart message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.creatureID);
        if (entity != null && entity instanceof EntityLivingBase) {
            EntityLivingBase mob = (EntityLivingBase)entity;
            player.attackTargetEntityWithCurrentItem(mob);
        }
    }
}
