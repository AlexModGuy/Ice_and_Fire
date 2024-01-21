package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class MessageDeathWormHitbox {

    public int deathWormId;
    public float scale;

    public MessageDeathWormHitbox(int deathWormId, float scale) {
        this.deathWormId = deathWormId;
        this.scale = scale;
    }

    public MessageDeathWormHitbox() {
    }

    public static MessageDeathWormHitbox read(FriendlyByteBuf buf) {
        return new MessageDeathWormHitbox(buf.readInt(), buf.readFloat());
    }

    public static void write(MessageDeathWormHitbox message, FriendlyByteBuf buf) {
        buf.writeInt(message.deathWormId);
        buf.writeFloat(message.scale);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageDeathWormHitbox message, final Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                Player player = context.getSender();

                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    Entity entity = player.level().getEntity(message.deathWormId);

                    if (entity instanceof EntityDeathWorm deathWorm) {
                        deathWorm.initSegments(message.scale);
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }
}