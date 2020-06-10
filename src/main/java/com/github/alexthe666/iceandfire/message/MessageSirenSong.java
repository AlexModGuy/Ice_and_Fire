package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public class MessageSirenSong extends AbstractMessage<MessageSirenSong> {

    public int sirenId;
    public boolean isSinging;

    public MessageSirenSong(int sirenId, boolean isSinging) {
        this.sirenId = sirenId;
        this.isSinging = isSinging;
    }

    public MessageSirenSong() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        sirenId = buf.readInt();
        isSinging = buf.readBoolean();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(sirenId);
        buf.writeBoolean(isSinging);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft client, MessageSirenSong message, PlayerEntity player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.sirenId);
            if (entity != null && entity instanceof EntitySiren) {
                EntitySiren siren = (EntitySiren) entity;
                siren.setSinging(message.isSinging);
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageSirenSong message, PlayerEntity player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.sirenId);
            if (entity != null && entity instanceof EntitySiren) {
                EntitySiren siren = (EntitySiren) entity;
                siren.setSinging(message.isSinging);
            }
        }
    }
}