package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.core.*;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.nbt.*;
import net.minecraft.network.*;
import net.minecraft.network.play.server.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;

public class TileEntityEggInIce extends TileEntity implements ITickable {
	public EnumDragonEgg type;
	public int age;
	public int ticksExisted;

	public TileEntityEggInIce () {
	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound tag) {
		if (type != null) {
			tag.setByte ("Color", (byte) type.ordinal ());
		} else {
			tag.setByte ("Color", (byte) 0);
		}
		tag.setByte ("Age", (byte) age);
		return super.writeToNBT (tag);
	}

	@Override
	public void readFromNBT (NBTTagCompound tag) {
		super.readFromNBT (tag);
		type = EnumDragonEgg.values ()[tag.getByte ("Color")];
		age = tag.getByte ("Age");
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket () {
		NBTTagCompound tag = new NBTTagCompound ();
		this.writeToNBT (tag);
		return new SPacketUpdateTileEntity (pos, 1, tag);
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT (packet.getNbtCompound ());
	}

	public void spawnEgg () {
		if (type != null) {
			EntityDragonEgg egg = new EntityDragonEgg (world);
			egg.setType (type);
			egg.setPosition (pos.getX () + 0.5, pos.getY () + 1, pos.getZ () + 0.5);
			if (!world.isRemote) {
				world.spawnEntity (egg);
			}
		}
	}

	@Override
	public void update () {
		age++;
		EntityPlayer player = world.getClosestPlayer (pos.getX () + 0.5D, pos.getY () + 0.5D, pos.getZ () + 0.5D, 5, false);
		if (age == 20 * 60 && type != null && player != null) {
			world.destroyBlock (pos, false);
			world.setBlockState (pos, Blocks.WATER.getDefaultState ());
			EntityIceDragon dragon = new EntityIceDragon (world);
			dragon.setPosition (pos.getX () + 0.5, pos.getY () + 1, pos.getZ () + 0.5);
			dragon.setVariant (type.ordinal () - 4);
			dragon.setTamed (true);
			if (player != null) {
				dragon.setOwnerId (player.getUniqueID ());
				player.addStat (ModAchievements.dragonHatch, 1);
			}
			if (!world.isRemote) {
				world.spawnEntity (dragon);
			}
		}
		ticksExisted++;
	}
}
