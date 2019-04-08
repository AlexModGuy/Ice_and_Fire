package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
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

public class MessageDragonSetBurnBlock extends AbstractMessage<MessageDragonSetBurnBlock> {

    public int dragonId;
    public boolean breathingFire;

    public MessageDragonSetBurnBlock(int dragonId, boolean breathingFire) {
        this.dragonId = dragonId;
        this.breathingFire = breathingFire;
    }

    public MessageDragonSetBurnBlock() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dragonId = buf.readInt();
        breathingFire = buf.readBoolean();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dragonId);
        buf.writeBoolean(breathingFire);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageDragonSetBurnBlock message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.dragonId);
        if (entity != null && entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            dragon.setBreathingFire(message.breathingFire);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageDragonSetBurnBlock message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.dragonId);
        if (entity != null && entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            dragon.setBreathingFire(message.breathingFire);
        }
    }
}