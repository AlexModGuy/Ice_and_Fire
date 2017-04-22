package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.entity.*;
import io.netty.buffer.*;
import net.ilexiconn.llibrary.server.network.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageDaytime extends AbstractMessage<MessageDaytime> {

	public int dragonId;
	public boolean isDay;

	public MessageDaytime (int dragonId, boolean isDay) {
		this.dragonId = dragonId;
		this.isDay = isDay;
	}

	public MessageDaytime () {
	}

	@Override
	public void fromBytes (ByteBuf buf) {
		dragonId = buf.readInt ();
		isDay = buf.readBoolean ();

	}

	@Override
	public void toBytes (ByteBuf buf) {
		buf.writeInt (dragonId);
		buf.writeBoolean (isDay);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void onClientReceived (Minecraft client, MessageDaytime message, EntityPlayer player, MessageContext messageContext) {
		Entity entity = player.world.getEntityByID (message.dragonId);
		if (entity instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase) entity;
			dragon.isDaytime = message.isDay;
		}
	}

	@Override
	public void onServerReceived (MinecraftServer server, MessageDaytime message, EntityPlayer player, MessageContext messageContext) {
	}
}