package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStartRidingMob {

    public int dragonId;
    public boolean ride;
    public boolean baby;

    public MessageStartRidingMob(int dragonId, boolean ride, boolean baby) {
        this.dragonId = dragonId;
        this.ride = ride;
        this.baby = baby;
    }

    public MessageStartRidingMob() {
    }

    public static MessageStartRidingMob read(FriendlyByteBuf buf) {
        return new MessageStartRidingMob(buf.readInt(), buf.readBoolean(), buf.readBoolean());
    }

    public static void write(MessageStartRidingMob message, FriendlyByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeBoolean(message.ride);
        buf.writeBoolean(message.baby);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageStartRidingMob message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            Player player = context.get().getSender();
            if(context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT){
                player = IceAndFire.PROXY.getClientSidePlayer();
            }
            if (player != null) {
                if (player.level != null) {
                    Entity entity = player.level.getEntity(message.dragonId);
                    if (entity != null && entity instanceof ISyncMount && entity instanceof TamableAnimal) {
                        TamableAnimal dragon = (TamableAnimal) entity;
                        if (dragon.isOwnedBy(player) && dragon.distanceTo(player) < 14) {
                            if (message.ride) {
                                if (message.baby) {
                                    dragon.startRiding(player, true);
                                } else {
                                    player.startRiding(dragon, true);
                                }
                            } else {
                                if (message.baby) {
                                    dragon.stopRiding();
                                } else {
                                    player.stopRiding();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}