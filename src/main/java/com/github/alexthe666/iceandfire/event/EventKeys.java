package com.github.alexthe666.iceandfire.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import org.lwjgl.input.Keyboard;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.message.MessageModKeys;

public class EventKeys {

	@SubscribeEvent
	public void handleClientTick(ClientTickEvent event){
		if(FMLCommonHandler.instance().getSide().isClient() && checkIfPlayer()){
			if(Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode())){
				IceAndFire.channel.sendToServer(new MessageModKeys(1));
			}
			if(Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())){
				IceAndFire.channel.sendToServer(new MessageModKeys(2));
			}
			if(Keyboard.isKeyDown(ModKeys.dragon_fireAttack.getKeyCode())){
				IceAndFire.channel.sendToServer(new MessageModKeys(3));
			}
			if(Keyboard.isKeyDown(ModKeys.dragon_strike.getKeyCode())){
				IceAndFire.channel.sendToServer(new MessageModKeys(4));
			}
			if(Keyboard.isKeyDown(ModKeys.dragon_dismount.getKeyCode())){
				IceAndFire.channel.sendToServer(new MessageModKeys(5));
			}
		}
	}

	public boolean checkIfPlayer(){
		if(Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().thePlayer != null){
			return Minecraft.getMinecraft().thePlayer.ticksExisted % 2 == 0 && Minecraft.getMinecraft().thePlayer.worldObj.isRemote;
		}
		else{
			return false;
		}
	}
}

