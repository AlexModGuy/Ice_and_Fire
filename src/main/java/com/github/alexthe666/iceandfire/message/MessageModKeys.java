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
		if(player.getRidingEntity() != null){
			if(player.getRidingEntity() instanceof EntityDragonBase){
				EntityDragonBase dragon = (EntityDragonBase)player.getRidingEntity();
				if(message.keyId == 1){
					System.out.println("Up");
					//dragon.motionY += 0.1D;
				}
				if(message.keyId == 2){
					System.out.println("Down");
				}
				if(message.keyId == 3){
					if(!dragon.isBreathingFire)
						dragon.isBreathingFire = true;
					dragon.ticksTillStopFire = 20;
					if(dragon.flameTick == 0){
						dragon.flameTick = 1;
						System.out.println(dragon.flameTick);
					}

				}
				if(message.keyId == 4){
					if(dragon.attackTick == 0){
						dragon.attackTick = 1;
					}
					if(dragon.getAnimation() != dragon.animation_bite1){
						dragon.setAnimation(dragon.animation_bite1);
					}
					
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