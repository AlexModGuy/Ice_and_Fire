package com.github.alexthe666.iceandfire.client;

import com.github.alexthe666.iceandfire.client.gui.*;
import com.github.alexthe666.iceandfire.client.gui.bestiary.*;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.tile.*;
import com.github.alexthe666.iceandfire.inventory.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.*;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement (int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity entity = world.getEntityByID (x);
		TileEntity tile = world.getTileEntity (new BlockPos (x, y, z));

		switch (id) {

			case 0:
				if (entity != null) {
					if (entity instanceof EntityDragonBase) {
						return new ContainerDragon ((EntityDragonBase) entity, player);
					}
				}
				break;

			case 1:
				if (tile != null) {

					if (tile instanceof TileEntityPodium) {
						return new ContainerPodium (player.inventory, (TileEntityPodium) tile, player);
					}
				}
				break;

			case 2:
				if (tile != null) {

					if (tile instanceof TileEntityLectern) {
						return new ContainerLectern (player.inventory, (TileEntityLectern) tile);
					}
				}
				break;
		}
		return null;

	}

	@Override
	public Object getClientGuiElement (int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity entity = world.getEntityByID (x);
		TileEntity tile = world.getTileEntity (new BlockPos (x, y, z));
		switch (id) {

			case 0:
				if (entity != null) {

					if (entity instanceof EntityDragonBase) {
						return new GuiDragon (player.inventory, (EntityDragonBase) entity);
					}
				}
				break;

			case 1:
				if (tile != null) {

					if (tile instanceof TileEntityPodium) {
						return new GuiPodium (player.inventory, (TileEntityPodium) tile);
					}
				}
				break;

			case 2:
				if (tile != null) {

					if (tile instanceof TileEntityLectern) {
						return new GuiLectern (player.inventory, (TileEntityLectern) tile);
					}
				}
				break;
			case 3:
				return new GuiBestiary (player.getActiveItemStack ());
		}
		return entity;
	}
}