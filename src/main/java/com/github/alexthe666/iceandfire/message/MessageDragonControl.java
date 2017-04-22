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

public class MessageDragonControl extends AbstractMessage<MessageDragonControl> {

	public int dragonId;
	public byte controlState;
	public int armor_type;

	public MessageDragonControl (int dragonId, byte controlState) {
		this.dragonId = dragonId;
		this.controlState = controlState;
	}

	public MessageDragonControl () {
	}

	@Override
	public void fromBytes (ByteBuf buf) {
		dragonId = buf.readInt ();
		controlState = buf.readByte ();

	}

	@Override
	public void toBytes (ByteBuf buf) {
		buf.writeInt (dragonId);
		buf.writeByte (controlState);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void onClientReceived (Minecraft client, MessageDragonControl message, EntityPlayer player, MessageContext messageContext) {
	}

	@Override
	public void onServerReceived (MinecraftServer server, MessageDragonControl message, EntityPlayer player, MessageContext messageContext) {
		Entity entity = player.world.getEntityByID (message.dragonId);
		if (entity instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase) entity;
			if (dragon.isOwner (player)) {
				dragon.setControlState (message.controlState);
			}
		}
	}
}