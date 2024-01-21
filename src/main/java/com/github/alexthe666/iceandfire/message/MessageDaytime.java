package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageDaytime {

    public int dragonId;
    public boolean isDay;

    public MessageDaytime(int dragonId, boolean isDay) {
        this.dragonId = dragonId;
        this.isDay = isDay;
    }

    public MessageDaytime() {
    }

    public static MessageDaytime read(FriendlyByteBuf buf) {
        return new MessageDaytime(buf.readInt(), buf.readBoolean());
    }

    public static void write(MessageDaytime message, FriendlyByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeBoolean(message.isDay);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageDaytime message, final Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                Player player = context.getSender();

                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    Entity entity = player.level().getEntity(message.dragonId);

                    if (entity instanceof EntityDragonBase dragon) {
                        dragon.isDaytime = message.isDay;
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }
}