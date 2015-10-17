package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class MessageDragonMotion extends AbstractMessage<MessageDragonMotion>
{

	public int entity; 
	public float motX; 
	public float motY; 
	public float motZ; 

	public MessageDragonMotion(int entityID, float motionX, float motionY, float motionZ)
	{
		entity = entityID;
		motX = motionX;
		motY = motionY;
		motZ = motionZ;
	}
	
	public MessageDragonMotion(){}


	public void handleClientMessage(MessageDragonMotion message, EntityPlayer player)
	{
		World world = FMLClientHandler.instance().getWorldClient();
		EntityDragonBase entity = (EntityDragonBase)world.getEntityByID(message.entity);
		//entity.motionSpeedX = message.motX;
		//entity.motionSpeedY = message.motY;
		//entity.motionSpeedZ = message.motZ;

	}

	public void handleServerMessage(MessageDragonMotion message, EntityPlayer player){}

	public void fromBytes(ByteBuf buf)
	{
		entity = buf.readInt();
		motX = buf.readFloat();
		//motY = buf.readFloat();
		//motZ = buf.readFloat();

	}

	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entity);
		buf.writeFloat(motX);
		//buf.writeFloat(motY);
		//buf.writeFloat(motZ);

	}
}