package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSpawnParticleAt extends AbstractMessage<MessageSpawnParticleAt> {
    private double x;
    private double y;
    private double z;
    private int particleType;

    public MessageSpawnParticleAt() {}

    public MessageSpawnParticleAt(double x, double y, double z, int particleType) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.particleType = particleType;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageSpawnParticleAt message, EntityPlayer player, MessageContext messageContext) {
        if(!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == IafItemRegistry.dragon_debug_stick){
            client.world.spawnParticle(EnumParticleTypes.getParticleFromId(particleType), message.x, message.y, message.z, 0, 0, 0);
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageSpawnParticleAt message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        particleType = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeInt(particleType);
    }
}
