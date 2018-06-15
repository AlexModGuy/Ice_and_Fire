package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDeathWormInteract extends AbstractMessage<MessageDeathWormInteract> {

    public int deathWormId;
    public float dmg;

    public MessageDeathWormInteract(int deathWormId, float dmg) {
        this.deathWormId = deathWormId;
        this.dmg = dmg;
    }

    public MessageDeathWormInteract() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        deathWormId = buf.readInt();
        dmg = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(deathWormId);
        buf.writeFloat(dmg);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageDeathWormInteract message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.deathWormId);
        if (entity instanceof EntityDeathWorm) {
            EntityDeathWorm worm = (EntityDeathWorm)entity;
            if(message.dmg > 0F){
                worm.attackEntityFrom(DamageSource.causeMobDamage(player), dmg);
            }else{
                worm.processInitialInteract(player, EnumHand.MAIN_HAND);
            }
        }
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageDeathWormInteract message, EntityPlayer player, MessageContext messageContext) {
        Entity entity = player.world.getEntityByID(message.deathWormId);
        if (entity instanceof EntityDeathWorm) {
            EntityDeathWorm worm = (EntityDeathWorm)entity;
            if(message.dmg > 0F){
                worm.attackEntityFrom(DamageSource.causeMobDamage(player), dmg);
            }else{
                worm.processInitialInteract(player, EnumHand.MAIN_HAND);
            }
        }
    }
}