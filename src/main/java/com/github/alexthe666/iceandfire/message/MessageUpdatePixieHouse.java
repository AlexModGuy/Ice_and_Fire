package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdatePixieHouse {

    public long blockPos;
    public boolean hasPixie;
    public int pixieType;

    public MessageUpdatePixieHouse(long blockPos, boolean hasPixie, int pixieType) {
        this.blockPos = blockPos;
        this.hasPixie = hasPixie;
        this.pixieType = pixieType;

    }

    public MessageUpdatePixieHouse() {
    }

    public static MessageUpdatePixieHouse read(FriendlyByteBuf buf) {
        return new MessageUpdatePixieHouse(buf.readLong(), buf.readBoolean(), buf.readInt());
    }

    public static void write(MessageUpdatePixieHouse message, FriendlyByteBuf buf) {
        buf.writeLong(message.blockPos);
        buf.writeBoolean(message.hasPixie);
        buf.writeInt(message.pixieType);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageUpdatePixieHouse message, final Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                Player player = context.getSender();

                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    BlockPos pos = BlockPos.of(message.blockPos);
                    BlockEntity blockEntity = player.level().getBlockEntity(pos);

                    if (blockEntity instanceof TileEntityPixieHouse house) {
                        house.hasPixie = message.hasPixie;
                        house.pixieType = message.pixieType;
                    } else if (blockEntity instanceof TileEntityJar jar) {
                        jar.hasPixie = message.hasPixie;
                        jar.pixieType = message.pixieType;
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }

}