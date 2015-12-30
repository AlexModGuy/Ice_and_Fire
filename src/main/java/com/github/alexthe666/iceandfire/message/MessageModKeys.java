package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class MessageModKeys extends AbstractMessage<MessageModKeys>
{

	public int keyId;

	public MessageModKeys(int key)
	{
		keyId = key;
	}
	public MessageModKeys()
	{
	}


	public void handleClientMessage(MessageModKeys message, EntityPlayer player)
	{

	}

	public void handleServerMessage(MessageModKeys message, EntityPlayer player)
	{
		if(player.ridingEntity != null){
			if(player.ridingEntity instanceof EntityDragonBase){
				EntityDragonBase dragon = (EntityDragonBase)player.ridingEntity;
				if(message.keyId == 1){
					System.out.println("Up");
				}
				if(message.keyId == 2){
					System.out.println("Down");
				}
				if(message.keyId == 3){
					System.out.println("FireAttack");
				}
				if(message.keyId == 4){
					System.out.println("Strike");
				}
			}	
		}
		
	}

	public void fromBytes(ByteBuf buf)
	{
		keyId = buf.readInt();
	}

	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(keyId);

	}
}