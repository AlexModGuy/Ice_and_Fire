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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageMultipartInteract extends AbstractMessage<MessageMultipartInteract> {

    public int creatureID;
    public float dmg;

    public MessageMultipartInteract(int creatureID, float dmg) {
        this.creatureID = creatureID;
        this.dmg = dmg;
    }

    public MessageMultipartInteract() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        creatureID = buf.readInt();
        dmg = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(creatureID);
        buf.writeFloat(dmg);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageMultipartInteract message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.creatureID);
            if (entity != null && entity instanceof EntityLivingBase) {
                double dist = player.getDistance(entity);
                EntityLivingBase mob = (EntityLivingBase) entity;
                if(dist < 100) {
                    if (message.dmg > 0F) {
                        mob.attackEntityFrom(DamageSource.causeMobDamage(player), dmg);
                    } else {
                        mob.processInitialInteract(player, EnumHand.MAIN_HAND);
                    }
                }
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageMultipartInteract message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.creatureID);
            if (entity != null && entity instanceof EntityLivingBase) {
                double dist = player.getDistance(entity);
                EntityLivingBase mob = (EntityLivingBase) entity;
                if(dist < 100) {
                    if (message.dmg > 0F) {
                        mob.attackEntityFrom(DamageSource.causeMobDamage(player), dmg);
                    } else {
                        mob.processInitialInteract(player, EnumHand.MAIN_HAND);
                    }
                }
            }
        }
    }
}