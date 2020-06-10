package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.citadel.server.entity.EntityPropertiesHandler;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.props.ChainEntityProperties;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageDeathWormHitbox {

    public int deathWormId;
    public float scale;

    public MessageDeathWormHitbox(int deathWormId, float scale) {
        this.deathWormId = deathWormId;
        this.scale = scale;
    }

    public MessageDeathWormHitbox() {
    }

    public static MessageDeathWormHitbox read(PacketBuffer buf) {
        return new MessageDeathWormHitbox(buf.readInt(), buf.readFloat());
    }

    public static void write(MessageDeathWormHitbox message, PacketBuffer buf) {
        buf.writeInt(message.deathWormId);
        buf.writeFloat(message.scale);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageDeathWormHitbox message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context)context.get()).setPacketHandled(true);
            PlayerEntity player = context.get().getSender();
            if(player != null) {
                if (player.world != null) {
                    Entity entity = player.world.getEntityByID(message.deathWormId);
                    if (entity != null && entity instanceof EntityDeathWorm) {
                        EntityDeathWorm worm = (EntityDeathWorm) entity;
                        worm.initSegments(message.scale);
                    }
                }
            }
        }
    }
}