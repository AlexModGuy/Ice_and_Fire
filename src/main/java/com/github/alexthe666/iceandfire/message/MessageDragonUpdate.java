package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class MessageDragonUpdate extends AbstractMessage<MessageDragonUpdate>
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


	public void handleClientMessage(MessageDragonUpdate message, EntityPlayer player)
	{
		EntityDragonBase entity = (EntityDragonBase)player.worldObj.getEntityByID(message.dragonId);
		if(entity != null && !entity.isDead){
			switch(message.dataType){
			default:
				entity.hoverProgress = message.data;
				if(message.data > message.prevData){
					entity.setHovering(true);
				}
				else if(message.data == 0){
					entity.setHovering(false);
				}
				break;
			case 1:
				entity.flightProgress = message.data;
				if(message.data > message.prevData){
					entity.setFlying(true);
				}
				else if(message.data == 0){
					entity.setFlying(false);
				}
				break;
			case 2:
				entity.fireBreathProgress = message.data;
				if(message.data > message.prevData){
					entity.isBreathingFire = true;
				}
				else if(message.data == 0){
					entity.isBreathingFire = false;
				}
				
			}
		}
	}

	public void handleServerMessage(MessageDragonUpdate message, EntityPlayer player)
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
}