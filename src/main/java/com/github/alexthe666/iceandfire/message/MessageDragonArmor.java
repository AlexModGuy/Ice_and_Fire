package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDragonArmor extends AbstractMessage<MessageDragonArmor> {

    public int dragonId;
    public int armor_index;
    public ItemStack armor_type;

    public MessageDragonArmor(int dragonId, int armor_index, ItemStack armor_type) {
        this.dragonId = dragonId;
        this.armor_index = armor_index;
        this.armor_type = armor_type;
    }

    public MessageDragonArmor() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dragonId = buf.readInt();
        armor_index = buf.readInt();
        armor_type = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dragonId);
        buf.writeInt(armor_index);
        ByteBufUtils.writeItemStack(buf, armor_type);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, MessageDragonArmor message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageDragonArmor message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            Entity entity = player.world.getEntityByID(message.dragonId);
            if (entity != null && entity instanceof EntityDragonBase) {
                EntityDragonBase dragon = (EntityDragonBase) entity;
                dragon.setArmorInSlot(message.armor_index, message.armor_type);
            }
        }
    }
}