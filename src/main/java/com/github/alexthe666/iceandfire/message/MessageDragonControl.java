package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageDragonControl {

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

    public static MessageDragonControl read(FriendlyByteBuf buf) {
        return new MessageDragonControl(buf.readInt(), buf.readByte(), buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public static void write(MessageDragonControl message, FriendlyByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeByte(message.controlState);
        buf.writeDouble(message.posX);
        buf.writeDouble(message.posY);
        buf.writeDouble(message.posZ);
    }

    private double getPosX() {
        return posX;
    }

    private double getPosY() {
        return posY;
    }

    private double getPosZ() {
        return posZ;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageDragonControl message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            Player player = context.get().getSender();
            if(context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT){
                player = IceAndFire.PROXY.getClientSidePlayer();
            }
            if (player != null) {
                if (player.level != null) {
                    Entity entity = player.level.getEntity(message.dragonId);
                    if (ServerEvents.isRidingOrBeingRiddenBy(entity, player)) {
                        if (entity != null && entity instanceof EntityDragonBase) {
                            EntityDragonBase dragon = (EntityDragonBase) entity;
                            if (dragon.isOwnedBy(player)) {
                                dragon.setControlState(message.controlState);
                            }
                            // The riding setPos is performed in Entity#move
                            // Extra setPos will cause server side movement check to fail, resulting move wrongly msg
//                            dragon.setPos(message.getPosX(), message.getPosY(), message.getPosZ());
                        } else if (entity instanceof EntityHippogryph) {
                            EntityHippogryph hippo = (EntityHippogryph) entity;
                            if (hippo.isOwnedBy(player)) {
                                hippo.setControlState(message.controlState);
                            }
//                            hippo.setPos(message.getPosX(), message.getPosY(), message.getPosZ());
                        } else if (entity instanceof EntityHippocampus) {
                            EntityHippocampus hippo = (EntityHippocampus) entity;
                            if (hippo.isOwnedBy(player)) {
                                hippo.setControlState(message.controlState);
                            }
//                            hippo.setPos(message.getPosX(), message.getPosY(), message.getPosZ());
                        } else if (entity instanceof EntityDeathWorm) {
                            EntityDeathWorm deathworm = (EntityDeathWorm) entity;
                            deathworm.setControlState(message.controlState);
                            deathworm.setPos(message.getPosX(), message.getPosY(), message.getPosZ());
                        } else if (entity instanceof EntityAmphithere) {
                            EntityAmphithere amphi = (EntityAmphithere) entity;
                            if (amphi.isOwnedBy(player)) {
                                amphi.setControlState(message.controlState);
                            }
                            amphi.setPos(message.getPosX(), message.getPosY(), message.getPosZ());
                        }
                    }
                }
            }
        }
    }
}