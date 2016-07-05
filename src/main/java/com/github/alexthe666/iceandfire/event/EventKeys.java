package com.github.alexthe666.iceandfire.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import org.lwjgl.input.Keyboard;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.message.MessageDragonKeys;

public class EventKeys {

	@SubscribeEvent
	public void handleClientTick(ClientTickEvent event) {
		if (checkIfPlayer()) {
			Entity dragon = Minecraft.getMinecraft().thePlayer.getRidingEntity();
			if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode())) {
				IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 0));
			}
			if (Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode())) {
				IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 1));
			}
			if (Keyboard.isKeyDown(ModKeys.dragon_fireAttack.getKeyCode())) {
				IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 2));
			}
			if (Keyboard.isKeyDown(ModKeys.dragon_strike.getKeyCode())) {
				IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 3));
			}
			if (Keyboard.isKeyDown(ModKeys.dragon_dismount.getKeyCode())) {
				IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 4));
			}
		}
	}

	public boolean checkIfPlayer() {
		if (Minecraft.getMinecraft().inGameHasFocus && Minecraft.getMinecraft().thePlayer != null) {
			return Minecraft.getMinecraft().thePlayer.ticksExisted % 2 == 0 && Minecraft.getMinecraft().thePlayer.worldObj.isRemote && Minecraft.getMinecraft().thePlayer.getRidingEntity() != null && Minecraft.getMinecraft().thePlayer.getRidingEntity() instanceof EntityDragonBase;
		} else {
			return false;
		}
	}
}
