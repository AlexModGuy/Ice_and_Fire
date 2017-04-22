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

public class MessageDragonArmor extends AbstractMessage<MessageDragonArmor> {

	public int dragonId;
	public int armor_index;
	public int armor_type;

	public MessageDragonArmor (int dragonId, int armor_index, int armor_type) {
		this.dragonId = dragonId;
		this.armor_index = armor_index;
		this.armor_type = armor_type;
	}

	public MessageDragonArmor () {
	}

	@Override
	public void fromBytes (ByteBuf buf) {
		dragonId = buf.readInt ();
		armor_index = buf.readInt ();
		armor_type = buf.readInt ();

	}

	@Override
	public void toBytes (ByteBuf buf) {
		buf.writeInt (dragonId);
		buf.writeInt (armor_index);
		buf.writeInt (armor_type);
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void onClientReceived (Minecraft client, MessageDragonArmor message, EntityPlayer player, MessageContext messageContext) {

	}

	@Override
	public void onServerReceived (MinecraftServer server, MessageDragonArmor message, EntityPlayer player, MessageContext messageContext) {
		Entity entity = player.world.getEntityByID (message.dragonId);
		if (entity instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase) entity;
			dragon.setArmorInSlot (message.armor_index, message.armor_type);
		}
	}
}