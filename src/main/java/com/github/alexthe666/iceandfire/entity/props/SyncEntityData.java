package com.github.alexthe666.iceandfire.entity.props;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncEntityData(int entityId, CompoundTag tag) {
    public void encode(final FriendlyByteBuf buffer) {
        buffer.writeInt(entityId);
        buffer.writeNbt(tag);
    }

    public static SyncEntityData decode(final FriendlyByteBuf buffer) {
        return new SyncEntityData(buffer.readInt(), buffer.readNbt());
    }

    public static void handle(final SyncEntityData message, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            context.enqueueWork(() -> {
                Player localPlayer = CapabilityHandler.getLocalPlayer();

                if (localPlayer != null) {
                    EntityDataProvider.getCapability(localPlayer.level().getEntity(message.entityId)).ifPresent(data -> data.deserialize(message.tag));
                }
            });
        }

        context.setPacketHandled(true);
    }
}
