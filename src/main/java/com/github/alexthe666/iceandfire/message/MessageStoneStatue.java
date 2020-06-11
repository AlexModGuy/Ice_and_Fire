package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.entity.props.StoneEntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStoneStatue {

    public int entityId;
    public boolean isStone;

    public MessageStoneStatue(int entityId, boolean isStone) {
        this.entityId = entityId;
        this.isStone = isStone;
    }

    public MessageStoneStatue() {
    }

    public static MessageStoneStatue read(PacketBuffer buf) {
        return new MessageStoneStatue(buf.readInt(), buf.readBoolean());
    }

    public static void write(MessageStoneStatue message, PacketBuffer buf) {
        buf.writeDouble(message.entityId);
        buf.writeBoolean(message.isStone);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageStoneStatue message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if (player != null) {
                if (player.world != null) {
                    Entity entity = player.world.getEntityByID(message.entityId);
                    if (entity != null && entity instanceof LivingEntity) {
                        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
                        properties.isStone = message.isStone;
                    }
                }
            }
        }
    }
}