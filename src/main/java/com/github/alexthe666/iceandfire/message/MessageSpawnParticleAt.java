package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSpawnParticleAt {
    private double x;
    private double y;
    private double z;
    private int particleType;

    public MessageSpawnParticleAt() {
    }

    public MessageSpawnParticleAt(double x, double y, double z, int particleType) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.particleType = particleType;
    }

    public static MessageSpawnParticleAt read(FriendlyByteBuf buf) {
        return new MessageSpawnParticleAt(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt());
    }

    public static void write(MessageSpawnParticleAt message, FriendlyByteBuf buf) {
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
        buf.writeInt(message.particleType);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageSpawnParticleAt message, final Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                Player player = context.getSender();

                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    ItemStack mainHand = player.getMainHandItem();

                    if (!mainHand.isEmpty() && mainHand.getItem() == IafItemRegistry.DRAGON_DEBUG_STICK.get()) {
                        player.level().addParticle(ParticleTypes.SMOKE, message.x, message.y, message.z, 0, 0, 0);
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }
}
