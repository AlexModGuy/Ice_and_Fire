package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.ChainEntityProperties;
import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageRemoveChainedEntity extends AbstractMessage<MessageRemoveChainedEntity> {

    public int chainedId;
    public int RemoveedEntityId;

    public MessageRemoveChainedEntity(int entityId, int RemoveedEntityId) {
        this.chainedId = entityId;
        this.RemoveedEntityId = RemoveedEntityId;
    }

    public MessageRemoveChainedEntity() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        chainedId = buf.readInt();
        RemoveedEntityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(chainedId);
        buf.writeInt(RemoveedEntityId);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageRemoveChainedEntity message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.chainedId);
        Entity toChain = player.world.getEntityByID(message.RemoveedEntityId);
        if (entity != null && entity instanceof EntityLivingBase && toChain != null) {
            ChainEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, ChainEntityProperties.class);
            properties.connectedEntities.remove(toChain);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageRemoveChainedEntity message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.chainedId);
        Entity toChain = player.world.getEntityByID(message.RemoveedEntityId);
        if (entity != null && entity instanceof EntityLivingBase && toChain != null) {
            ChainEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, ChainEntityProperties.class);
            properties.connectedEntities.remove(toChain);
        }
    }
}