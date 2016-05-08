package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class MessageModKeys implements IMessage{

	public int keyId;

	public MessageModKeys(int key)
	{
		keyId = key;
	}
	public MessageModKeys()
	{
	}

	public void fromBytes(ByteBuf buf)
	{
		keyId = buf.readInt();
	}

	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(keyId);
	}
	
	public static class Handler implements IMessageHandler<MessageModKeys, IMessage> {

		@Override
		public IMessage onMessage(MessageModKeys message, MessageContext ctx) {
			if(ctx.side == Side.SERVER){
				if(ctx.getServerHandler().playerEntity.getRidingEntity() != null){
					if(ctx.getServerHandler().playerEntity.getRidingEntity() instanceof EntityDragonBase){
						EntityDragonBase dragon = (EntityDragonBase)ctx.getServerHandler().playerEntity.getRidingEntity();
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
							if(dragon.getAnimation() == dragon.NO_ANIMATION){
								dragon.setAnimation(dragon.animation_flame);
							}

						}
						if(message.keyId == 4){
							if(dragon.attackTick == 0){
								dragon.attackTick = 1;
							}
							if(dragon.getAnimation() == dragon.NO_ANIMATION){
								dragon.setAnimation(dragon.animation_bite);
							}
							
						}
						if(message.keyId == 5){
							ctx.getServerHandler().playerEntity.dismountRidingEntity();
						}
					}	
				}
			}
			return null;
		}
		
	}
}