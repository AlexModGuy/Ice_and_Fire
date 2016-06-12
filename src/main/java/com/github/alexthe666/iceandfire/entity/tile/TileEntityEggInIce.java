package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;

public class TileEntityEggInIce extends TileEntity implements ITickable {
	public EnumDragonEgg type;
	public int age;
	public int ticksExisted;

	public TileEntityEggInIce() {
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setByte("Color", (byte) type.meta);
		tag.setByte("Age", (byte) age);

	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		type = EnumDragonEgg.byMetadata(tag.getByte("Color"));
		age = tag.getByte("Age");
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new SPacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}

	public void spawnEgg() {
		if (type != null) {
			EntityDragonEgg egg = new EntityDragonEgg(worldObj);
			egg.setType(type);
			egg.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			if (!worldObj.isRemote) {
				worldObj.spawnEntityInWorld(egg);
			}
		}
	}

	@Override
	public void update() {
		age++;
		if (age == 60 && type != null && type.meta > 4) {
			worldObj.destroyBlock(pos, false);
			EntityFireDragon dragon = new EntityFireDragon(worldObj);
			dragon.setColor(type.meta - 4);
			dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			if (!worldObj.isRemote) {
				worldObj.spawnEntityInWorld(dragon);
			}
		}
		ticksExisted++;
	}
}
