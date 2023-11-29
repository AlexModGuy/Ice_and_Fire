package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageMyrmexSettings {

    public int queenID;
    public boolean reproduces;
    public boolean deleteRoom;
    public long roomToDelete;

    public MessageMyrmexSettings(int queenID, boolean repoduces, boolean deleteRoom, long roomToDelete) {
        this.queenID = queenID;
        this.reproduces = repoduces;
        this.deleteRoom = deleteRoom;
        this.roomToDelete = roomToDelete;
    }

    public static MessageMyrmexSettings read(FriendlyByteBuf buf) {
        return new MessageMyrmexSettings(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readLong());
    }

    public static void write(MessageMyrmexSettings message, FriendlyByteBuf buf) {
        buf.writeInt(message.queenID);
        buf.writeBoolean(message.reproduces);
        buf.writeBoolean(message.deleteRoom);
        buf.writeLong(message.roomToDelete);

    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageMyrmexSettings message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            Player player = context.get().getSender();
            if(context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT){
                player = IceAndFire.PROXY.getClientSidePlayer();
            }
            if (player != null) {
                if (player.level != null) {
                    Entity entity = player.level.getEntity(message.queenID);
                    if (entity != null && entity instanceof EntityMyrmexBase) {
                        MyrmexHive hive = ((EntityMyrmexBase) entity).getHive();
                        if (hive != null) {
                            hive.reproduces = message.reproduces;
                            if (message.deleteRoom) {
                                hive.removeRoom(BlockPos.of(message.roomToDelete));
                            }
                        }
                    }
                }
            }

        }
    }
}