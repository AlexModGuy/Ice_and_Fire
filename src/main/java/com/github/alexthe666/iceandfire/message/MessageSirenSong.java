package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageSirenSong message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.sirenId);
        if (entity instanceof EntitySiren) {
            EntitySiren siren = (EntitySiren) entity;
            siren.setSinging(message.isSinging);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageSirenSong message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.sirenId);
        if (entity instanceof EntitySiren) {
            EntitySiren siren = (EntitySiren) entity;
            siren.setSinging(message.isSinging);
        }
    }
}