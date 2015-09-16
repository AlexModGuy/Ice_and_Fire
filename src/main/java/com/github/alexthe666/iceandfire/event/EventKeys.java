package com.github.alexthe666.iceandfire.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.message.MessageModKeys;

public class EventKeys {

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event){
		if(ModKeys.dragon_up.isPressed()){
			IceAndFire.channel.sendToServer(new MessageModKeys(1));
		}
		if(ModKeys.dragon_down.isPressed()){
			IceAndFire.channel.sendToServer(new MessageModKeys(2));
		}
		if(ModKeys.dragon_fireAttack.isPressed()){
			IceAndFire.channel.sendToServer(new MessageModKeys(3));
		}
		if(ModKeys.dragon_strike.isPressed()){
			IceAndFire.channel.sendToServer(new MessageModKeys(4));
		}
		
	}
}

