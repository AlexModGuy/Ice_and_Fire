package com.github.alexthe666.iceandfire.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.github.alexthe666.iceandfire.client.gui.GuiDragon;
import com.github.alexthe666.iceandfire.client.gui.GuiLectern;
import com.github.alexthe666.iceandfire.client.gui.GuiPodium;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import com.github.alexthe666.iceandfire.inventory.ContainerPodium;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity entity = world.getEntityByID(x);
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

		switch(id) {

		case 0:
			if(entity != null) {

				if (entity instanceof EntityDragonBase) {
					return new ContainerDragon((EntityDragonBase)entity, player);
				}
			}
			break;

		case 1:
			if(tile != null) {

				if (tile instanceof TileEntityPodium) {
					return new ContainerPodium(player.inventory, (TileEntityPodium)tile, player);
				}
			}
			break;
			
		case 2:
			if(tile != null) {

				if (tile instanceof TileEntityLectern) {
					return new ContainerLectern(player.inventory, (TileEntityLectern)tile);
				}
			}
			break;
		}
		return entity;

	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity entity = world.getEntityByID(x);
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		switch(id) {

		case 0:
			if(entity != null) {

				if (entity instanceof EntityDragonBase) {
					return new GuiDragon(player.inventory, (EntityDragonBase)entity);
				}
			}
			break;
			
		case 1:
			if(tile != null) {

				if (tile instanceof TileEntityPodium) {
					return new GuiPodium(player.inventory, (TileEntityPodium)tile);
				}
			}
			break;
			
		case 2:
			if(tile != null) {

				if (tile instanceof TileEntityLectern) {
					return new GuiLectern(player.inventory, (TileEntityLectern)tile);
				}
			}
			break;
		}
		return entity;
	}
}