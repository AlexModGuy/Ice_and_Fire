package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSirenSong {

    public int sirenId;
    public boolean isSinging;

    public MessageSirenSong(int sirenId, boolean isSinging) {
        this.sirenId = sirenId;
        this.isSinging = isSinging;
    }

    public MessageSirenSong() {
    }

    public static MessageSirenSong read(FriendlyByteBuf buf) {
        return new MessageSirenSong(buf.readInt(), buf.readBoolean());
    }

    public static void write(MessageSirenSong message, FriendlyByteBuf buf) {
        buf.writeInt(message.sirenId);
        buf.writeBoolean(message.isSinging);
    }


    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageSirenSong message, final Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                Player player = context.getSender();

                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    Entity entity = player.level().getEntity(message.sirenId);

                    if (entity instanceof EntitySiren siren) {
                        siren.setSinging(message.isSinging);
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }

}