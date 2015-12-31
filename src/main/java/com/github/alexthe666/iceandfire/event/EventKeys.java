package com.github.alexthe666.iceandfire.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import org.lwjgl.input.Keyboard;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.message.MessageModKeys;

public class EventKeys {

	@SubscribeEvent
	public void handleClientTick(ClientTickEvent event){
		if(Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode())){
			IceAndFire.channel.sendToServer(new MessageModKeys(1));
		}
		if(Keyboard.isKeyDown(ModKeys.dragon_fireAttack.getKeyCode())){
			System.out.println("i");
			IceAndFire.channel.sendToServer(new MessageModKeys(3));
		}
	}
/*	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event){
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			System.out.println(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode());
		}
		if(ModKeys.dragon_down.isKeyDown()){
			IceAndFire.channel.sendToServer(new MessageModKeys(2));
		}
		if(ModKeys.dragon_fireAttack.isKeyDown()){
			IceAndFire.channel.sendToServer(new MessageModKeys(3));
		}
		if(ModKeys.dragon_strike.isKeyDown()){
			IceAndFire.channel.sendToServer(new MessageModKeys(4));
		}
		
	}*/
}

