package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdatePixieJar {

    public long blockPos;
    public boolean isProducing;

    public MessageUpdatePixieJar(long blockPos, boolean isProducing) {
        this.blockPos = blockPos;
        this.isProducing = isProducing;

    }

    public MessageUpdatePixieJar() {
    }

    public static MessageUpdatePixieJar read(FriendlyByteBuf buf) {
        return new MessageUpdatePixieJar(buf.readLong(), buf.readBoolean());
    }

    public static void write(MessageUpdatePixieJar message, FriendlyByteBuf buf) {
        buf.writeLong(message.blockPos);
        buf.writeBoolean(message.isProducing);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageUpdatePixieJar message, final Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                Player player = context.getSender();

                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    BlockPos pos = BlockPos.of(message.blockPos);

                    if (player.level().getBlockEntity(pos) instanceof TileEntityJar jar) {
                        jar.hasProduced = message.isProducing;
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }
}