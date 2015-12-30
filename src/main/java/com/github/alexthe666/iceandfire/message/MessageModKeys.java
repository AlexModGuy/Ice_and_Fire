package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonFire;

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
					dragon.motionY += 1.1D;
				}
				if(message.keyId == 2){
					System.out.println("Down");
				}
				if(message.keyId == 3){
					System.out.println("FireAttack");
					float headPosX = (float) (dragon.posX + 1.8F * dragon.getDragonSize() * Math.cos((dragon.rotationYaw + 90) * Math.PI/180));
					float headPosZ = (float) (dragon.posZ + 1.8F * dragon.getDragonSize() * Math.sin((dragon.rotationYaw + 90) * Math.PI/180));
					float headPosY = (float) (dragon.posY + 0.7 * dragon.getDragonSize());
					double d1 = 0D;
					Vec3 vec3 = dragon.getLook(1.0F);
					double d2 = player.getLook(1.0F).xCoord;
					double d3 = player.getLook(1.0F).yCoord;
					double d4 = player.getLook(1.0F).zCoord;
					dragon.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1008, new BlockPos(dragon), 0);
					EntityDragonFire entitylargefireball = new EntityDragonFire(dragon.worldObj, dragon, d2, d3, d4);
					entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
					dragon.worldObj.spawnEntityInWorld(entitylargefireball);
					
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