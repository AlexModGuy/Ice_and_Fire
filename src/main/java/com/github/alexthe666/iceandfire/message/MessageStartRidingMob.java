package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStartRidingMob {

    public int dragonId;
    public boolean ride;

    public MessageStartRidingMob(int dragonId, boolean ride) {
        this.dragonId = dragonId;
        this.ride = ride;
    }

    public MessageStartRidingMob() {
    }

    public static MessageStartRidingMob read(PacketBuffer buf) {
        return new MessageStartRidingMob(buf.readInt(), buf.readBoolean());
    }

    public static void write(MessageStartRidingMob message, PacketBuffer buf) {
        buf.writeDouble(message.dragonId);
        buf.writeBoolean(message.ride);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageStartRidingMob message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if (player != null) {
                if (player.world != null) {
                    Entity entity = player.world.getEntityByID(message.dragonId);
                    if (entity != null && entity instanceof ISyncMount && entity instanceof TameableEntity) {
                        TameableEntity dragon = (TameableEntity) entity;
                        if (message.ride) {
                            player.startRiding(dragon);
                        } else {
                            player.stopRiding();
                        }
                    }
                }
            }
        }
    }
}