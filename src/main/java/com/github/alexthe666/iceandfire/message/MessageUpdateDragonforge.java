package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateDragonforge {

    public long blockPos;
    public int cookTime;

    public MessageUpdateDragonforge(long blockPos, int houseType) {
        this.blockPos = blockPos;
        this.cookTime = houseType;

    }

    public MessageUpdateDragonforge() {
    }

    public static MessageUpdateDragonforge read(PacketBuffer buf) {
        return new MessageUpdateDragonforge(buf.readLong(), buf.readInt());
    }

    public static void write(MessageUpdateDragonforge message, PacketBuffer buf) {
        buf.writeLong(message.blockPos);
        buf.writeInt(message.cookTime);
    }


    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageUpdateDragonforge message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if(context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT){
                player = IceAndFire.PROXY.getClientSidePlayer();
            }
            if (player != null) {
                if (player.level != null) {
                    BlockPos pos = BlockPos.of(message.blockPos);
                    if (player.level.getBlockEntity(pos) != null) {
                        if (player.level.getBlockEntity(pos) instanceof TileEntityDragonforge) {
                            TileEntityDragonforge house = (TileEntityDragonforge) player.level.getBlockEntity(pos);
                            house.cookTime = message.cookTime;
                            if (message.cookTime > 0) {
                                house.lastDragonFlameTimer = 40;
                            }
                        }
                    }
                }
            }
        }
    }
}