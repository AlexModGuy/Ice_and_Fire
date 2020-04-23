package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.StoneEntityProperties;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageStoneStatue extends AbstractMessage<MessageStoneStatue> {

    public int entityId;
    public boolean isStone;

    public MessageStoneStatue(int entityId, boolean isStone) {
        this.entityId = entityId;
        this.isStone = isStone;
    }

    public MessageStoneStatue() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        isStone = buf.readBoolean();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeBoolean(isStone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageStoneStatue message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.entityId);
            if (entity != null && entity instanceof EntityLiving) {
                StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
                properties.isStone = message.isStone;
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageStoneStatue message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            if(player.getHeldItemMainhand().getItem() == IafItemRegistry.gorgon_head){
                Entity entity = player.world.getEntityByID(message.entityId);
                if (entity != null && entity instanceof EntityLiving) {
                    StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, StoneEntityProperties.class);
                    properties.isStone = message.isStone;
                }
            }

        }
    }
}