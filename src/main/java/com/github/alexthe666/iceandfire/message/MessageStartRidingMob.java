package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.*;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageStartRidingMob extends AbstractMessage<MessageStartRidingMob> {

    public int dragonId;
    public boolean ride;

    public MessageStartRidingMob(int dragonId, boolean ride) {
        this.dragonId = dragonId;
        this.ride = ride;
    }

    public MessageStartRidingMob() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dragonId = buf.readInt();
        ride = buf.readBoolean();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dragonId);
        buf.writeBoolean(ride);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft client, MessageStartRidingMob message, PlayerEntity player, MessageContext messageContext) {
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageStartRidingMob message, PlayerEntity player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.dragonId);
            if (entity != null && entity instanceof ISyncMount && entity instanceof TameableEntity) {
                TameableEntity dragon = (TameableEntity) entity;
                if(message.ride){
                    player.startRiding(dragon);
                }else{
                    player.dismountRidingEntity();
                }
            }
        }
    }


}