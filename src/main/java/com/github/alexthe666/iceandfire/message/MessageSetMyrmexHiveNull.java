package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSetMyrmexHiveNull extends AbstractMessage<MessageSetMyrmexHiveNull> {

    public MessageSetMyrmexHiveNull() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft client, MessageSetMyrmexHiveNull message, EntityPlayer player, MessageContext messageContext) {
        ClientProxy.setReferedClientHive(null);
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageSetMyrmexHiveNull message, EntityPlayer player, MessageContext messageContext) {

    }
}