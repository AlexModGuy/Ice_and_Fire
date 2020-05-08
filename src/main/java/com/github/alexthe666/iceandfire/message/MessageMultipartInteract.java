package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
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
    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft client, MessageMultipartInteract message, PlayerEntity player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.creatureID);
            if (entity != null && entity instanceof LivingEntity) {
                double dist = player.getDistance(entity);
                LivingEntity mob = (LivingEntity) entity;
                if(dist < 100) {
                    if (message.dmg > 0F) {
                        mob.attackEntityFrom(DamageSource.causeMobDamage(player), dmg);
                    } else {
                        mob.processInitialInteract(player, Hand.MAIN_HAND);
                    }
                }
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageMultipartInteract message, PlayerEntity player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.creatureID);
            if (entity != null && entity instanceof LivingEntity) {
                double dist = player.getDistance(entity);
                LivingEntity mob = (LivingEntity) entity;
                if(dist < 100) {
                    if (message.dmg > 0F) {
                        mob.attackEntityFrom(DamageSource.causeMobDamage(player), dmg);
                    } else {
                        mob.processInitialInteract(player, Hand.MAIN_HAND);
                    }
                }
            }
        }
    }
}