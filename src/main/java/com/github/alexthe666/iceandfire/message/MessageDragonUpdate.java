package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonFire;

public class MessageDragonUpdate extends AbstractMessage<MessageDragonUpdate>
{

	public int dragonId;
	public byte dataType;
	public float data;

	public MessageDragonUpdate(int dragonId, byte type, float data)
	{
		this.dragonId = dragonId;
		this.dataType = type;
		this.data = data;
	}
	public MessageDragonUpdate()
	{
	}


	public void handleClientMessage(MessageDragonUpdate message, EntityPlayer player)
	{

	}

	public void handleServerMessage(MessageDragonUpdate message, EntityPlayer player)
	{
		EntityDragonBase entity = (EntityDragonBase) player.worldObj.getEntityByID(message.dragonId);
		switch(message.dataType){
		default://set Hover
			entity.hoverProgress = message.data;
			break;
		case 1://start Hover
			entity.hoverProgress = message.data;
			if(!entity.isHovering())
				entity.setHovering(true);
			break;
		case 2://end Hover
			entity.hoverProgress = message.data;
			if(entity.isHovering())
				entity.setHovering(false);
			break;
		case 3://setFlight
			entity.flightProgress = message.data;
			break;
		case 4://start Flight
			entity.flightProgress = message.data;
			if(!entity.isFlying())
				entity.setFlying(true);
			break;
		case 5://end Flight
			entity.flightProgress = message.data;
			if(entity.isFlying())
				entity.setFlying(false);
			break;
		}
	}

	public void fromBytes(ByteBuf buf)
	{
		dragonId = buf.readInt();
		dataType = buf.readByte();
		data = buf.readFloat();
	}

	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(dragonId);
		buf.writeByte(dataType);
		buf.writeFloat(data);
	}
}