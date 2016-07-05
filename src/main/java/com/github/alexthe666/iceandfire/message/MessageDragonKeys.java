package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class MessageDragonKeys extends AbstractMessage<MessageDragonKeys> {

	public int dragonId;
	public int keyId;
	public int armor_type;

	public MessageDragonKeys(int dragonId, int keyId) {
		this.dragonId = dragonId;
		this.keyId = keyId;
	}

	public MessageDragonKeys() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dragonId = buf.readInt();
		keyId = buf.readInt();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dragonId);
		buf.writeInt(keyId);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientReceived(Minecraft client, MessageDragonKeys message, EntityPlayer player, MessageContext messageContext) {

	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageDragonKeys message, EntityPlayer player, MessageContext messageContext) {
		Entity entity = player.worldObj.getEntityByID(message.dragonId);
		if (entity instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase) entity;
			if(dragon.getOwner() != null && dragon.getPassengers().contains(dragon.getOwner())){
				Entity owner = dragon.getOwner();
			switch(message.keyId){
			default://jumpkey
				dragon.spacebarTicks++;
				break;
			case 1://sneakkey
				break;
			case 2://firekey
				break;
			case 3://strikekey
				break;
			case 4://dismountkey
				owner.dismountRidingEntity();
				break;
			}
			}
		}
	}
}