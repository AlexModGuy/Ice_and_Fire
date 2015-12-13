package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;

public class TileEntityEggInIce extends TileEntity implements IUpdatePlayerListBox{
	public EnumDragonEgg type;
	public int ticksExisted;
	
	public TileEntityEggInIce(){
	}
	
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setByte("Color", (byte)type.meta);
	}

	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		type = EnumDragonEgg.byMetadata(tag.getByte("Color"));
	}
	
	@Override
	public Packet getDescriptionPacket() {
	 NBTTagCompound tag = new NBTTagCompound();
	 this.writeToNBT(tag);
	 return new S35PacketUpdateTileEntity(pos, 1, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
	 readFromNBT(packet.getNbtCompound());
	}
	
	public void spawnEgg(){
		if(type != null){
			EntityDragonEgg egg = new EntityDragonEgg(worldObj);
			egg.setType(type);
			egg.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			if(!worldObj.isRemote){
				worldObj.spawnEntityInWorld(egg);
			}
		}
	}
	
	@Override
	public void update() {
		
		ticksExisted++;		
	}
}
