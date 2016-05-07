package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class MessageDragonUpdate extends net.ilexiconn.llibrary.server.network.AbstractMessage<MessageDragonUpdate>
{

	public int dragonId;
	public byte dataType;
	public float data;
	public float prevData;

	public MessageDragonUpdate(int dragonId, byte type, float data, float prevData)
	{
		this.dragonId = dragonId;
		this.dataType = type;
		this.data = data;
		this.prevData = prevData;
	}
	public MessageDragonUpdate()
	{
	}

	public void fromBytes(ByteBuf buf)
	{
		dragonId = buf.readInt();
		dataType = buf.readByte();
		data = buf.readFloat();
		prevData = buf.readFloat();
	}

	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(dragonId);
		buf.writeByte(dataType);
		buf.writeFloat(data);
		buf.writeFloat(prevData);
	}
	@Override
	public void onClientReceived(Minecraft client, MessageDragonUpdate message, EntityPlayer player, MessageContext messageContext) {
		EntityDragonBase entity = (EntityDragonBase)player.worldObj.getEntityByID(message.dragonId);
		if(entity != null && !entity.isDead){
			switch(message.dataType){
			default:
			//	entity.hoverProgress = message.data;
				if(message.data > message.prevData){
					entity.setHovering(true);
				}
				else if(message.data == 0){
					entity.setHovering(false);
				}
				break;
			case 1:
			//	entity.flightProgress = message.data;
				if(message.data > message.prevData){
					entity.setFlying(true);
				}
				else if(message.data == 0){
					entity.setFlying(false);
				}
				break;
			case 2:
			//	entity.fireBreathProgress = message.data;
				if(message.data > message.prevData){
					entity.isBreathingFire = true;
				}
				else if(message.data == 0){
					entity.isBreathingFire = false;
				}
				
			}
		}
	}
	
	@Override
	public void onServerReceived(MinecraftServer server, MessageDragonUpdate message, EntityPlayer player, MessageContext messageContext) {
		
	}
}