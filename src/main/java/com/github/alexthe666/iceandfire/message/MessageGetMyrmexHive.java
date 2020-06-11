package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.citadel.server.message.PacketBufferUtils;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageGetMyrmexHive {

    public MyrmexHive hive;

    public MessageGetMyrmexHive(MyrmexHive hive) {
        this.hive = hive;
    }

    public MessageGetMyrmexHive() {
    }

    public static MessageGetMyrmexHive read(PacketBuffer buf) {
        MessageGetMyrmexHive mesage = new MessageGetMyrmexHive();
        mesage.hive = new MyrmexHive();
        mesage.hive.readVillageDataFromNBT(PacketBufferUtils.readTag(buf));
        return mesage;
    }

    public static void write(MessageGetMyrmexHive message, PacketBuffer buf) {
        CompoundNBT tag = new CompoundNBT();
        if (message.hive != null) {
            message.hive.writeVillageDataToNBT(tag);
        }
        PacketBufferUtils.writeTag(buf, tag);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageGetMyrmexHive message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if (player != null) {
                IceAndFire.PROXY.setReferencedHive(message.hive);
                if (player.world != null) {
                    MyrmexHive serverHive = MyrmexWorldData.get(player.world).getHiveFromUUID(message.hive.hiveUUID);
                    if (serverHive != null) {
                        CompoundNBT tag = new CompoundNBT();
                        message.hive.writeVillageDataToNBT(tag);
                        serverHive.readVillageDataFromNBT(tag);
                    }
                }
            }
        }
    }
}