package com.github.alexthe666.iceandfire.message;

import io.netty.buffer.ByteBuf;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class MessageDragonKeys extends AbstractMessage<MessageDragonKeys> {

	public int dragonId;
	public int keyId;
	public int armor_type;

	public MessageDragonKeys(int dragonId, int keyId) {
		this.dragonId = dragonId;
		this.keyId = keyId;
	}

	public MessageDragonKeys() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		dragonId = buf.readInt();
		keyId = buf.readInt();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dragonId);
		buf.writeInt(keyId);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientReceived(Minecraft client, MessageDragonKeys message, EntityPlayer player, MessageContext messageContext) {

	}

	@Override
	public void onServerReceived(MinecraftServer server, MessageDragonKeys message, EntityPlayer player, MessageContext messageContext) {
		Entity entity = player.worldObj.getEntityByID(message.dragonId);
		if (entity instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase) entity;
			if (dragon.getOwner() != null && dragon.getPassengers().contains(dragon.getOwner())) {
				Entity owner = dragon.getOwner();
				switch (message.keyId) {
				default:// jumpkey
					if (!dragon.isFlying() && !dragon.isHovering()) {
						dragon.spacebarTicks += 2;
					}
					if (dragon.isFlying() || dragon.isHovering()) {
						dragon.motionY += 0.1D;
					}
					break;
				case 1:// sneakkey
					if (dragon.isFlying() || dragon.isHovering()) {
						dragon.motionY -= 0.2D;
					}
					break;
				case 2:// firekey
					dragon.setBreathingFire(true);
					dragon.riderShootFire(owner);
					dragon.fireStopTicks = 10;
					break;
				case 3:// strikekey
					List<Entity> list = dragon.worldObj.getEntitiesWithinAABBExcludingEntity(dragon, dragon.getEntityBoundingBox().expand(dragon.getRenderSize() / 2, dragon.getRenderSize() / 2, dragon.getRenderSize() / 2));
					if (!list.isEmpty()) {
						Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(dragon));
						Iterator<Entity> itr = list.iterator();
						while(itr.hasNext()){
							Entity mob = itr.next();
							if(mob instanceof EntityLivingBase && !dragon.getPassengers().contains(mob) && !dragon.isOwner((EntityLivingBase)mob) && mob != dragon){
								dragon.attackEntityAsMob(dragon);
							}
						}
					}
					break;
				case 4:// dismountkey
					owner.dismountRidingEntity();
					break;
				}
			}
		}
	}
}