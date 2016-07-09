package com.github.alexthe666.iceandfire.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
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
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.thePlayer;
		if (mc.inGameHasFocus && player != null) {
			if (player.ticksExisted % 2 == 0 && player.getRidingEntity() instanceof EntityDragonBase) {
				
				Entity dragon = player.getRidingEntity();
				player.worldObj.spawnParticle(EnumParticleTypes.CLOUD, player.posX, player.posY + 2, player.posZ, 0, 0, 0, new int[0]);
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 0));
				}
				if (mc.gameSettings.keyBindSneak.isKeyDown()) {
					player.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, player.posX, player.posY + 2, player.posZ, 0, 0, 0, new int[0]);
					IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 1));
				}
				if (ModKeys.dragon_fireAttack.isKeyDown()) {
					IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 2));
				}
				if (ModKeys.dragon_strike.isKeyDown()) {
					IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 3));
				}
				if (ModKeys.dragon_dismount.isKeyDown()) {
					IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonKeys(dragon.getEntityId(), 4));
				}
			}
		}
	}
}
