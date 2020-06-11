package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdatePixieHouseModel {

    public long blockPos;
    public int houseType;

    public MessageUpdatePixieHouseModel(long blockPos, int houseType) {
        this.blockPos = blockPos;
        this.houseType = houseType;

    }

    public MessageUpdatePixieHouseModel() {
    }

    public static MessageUpdatePixieHouseModel read(PacketBuffer buf) {
        return new MessageUpdatePixieHouseModel(buf.readLong(), buf.readInt());
    }

    public static void write(MessageUpdatePixieHouseModel message, PacketBuffer buf) {
        buf.writeLong(message.blockPos);
        buf.writeInt(message.houseType);
    }


    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageUpdatePixieHouseModel message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if (player != null) {
                if (player.world != null) {
                    BlockPos pos = BlockPos.fromLong(message.blockPos);
                    if (player.world.getTileEntity(pos) != null) {
                        if (player.world.getTileEntity(pos) instanceof TileEntityPixieHouse) {
                            TileEntityPixieHouse house = (TileEntityPixieHouse) player.world.getTileEntity(pos);
                            house.houseType = message.houseType;
                        }
                        if (player.world.getTileEntity(pos) instanceof TileEntityJar) {
                            TileEntityJar jar = (TileEntityJar) player.world.getTileEntity(pos);
                            jar.pixieType = message.houseType;
                        }
                    }
                }
            }
        }
    }
}