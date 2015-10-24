package com.github.alexthe666.iceandfire.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.github.alexthe666.iceandfire.client.gui.GuiDragon;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity entity = world.getEntityByID(x);
		switch(id) {

		case 0:
			if(entity != null) {

				if (entity instanceof EntityDragonBase) {
					return new ContainerDragon(player.inventory, (EntityDragonBase)entity);
				}
			}
			break;
		}
		return entity;

	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		Entity entity = world.getEntityByID(x);
		switch(id) {

		case 0:
			if(entity != null) {

				if (entity instanceof EntityDragonBase) {
					return new GuiDragon(player.inventory, (EntityDragonBase)entity);
				}
			}
			break;
		}
		return entity;
	}
}