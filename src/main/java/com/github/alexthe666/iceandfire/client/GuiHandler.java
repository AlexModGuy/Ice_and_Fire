package com.github.alexthe666.iceandfire.client;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityMyrmexCocoon;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.inventory.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        Entity entity = world.getEntityByID(x);
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        switch (id) {

            case 0:
                if (entity != null) {
                    if (entity instanceof EntityDragonBase) {
                        return new ContainerDragon((EntityDragonBase) entity, player);
                    }
                }
                break;

            case 1:
                if (tile != null) {

                    if (tile instanceof TileEntityPodium) {
                        return new ContainerPodium(player.inventory, (TileEntityPodium) tile, player);
                    }
                }
                break;

            case 2:
                if (tile != null) {

                    if (tile instanceof TileEntityLectern) {
                        return new ContainerLectern(player.inventory, (TileEntityLectern) tile);
                    }
                }
                break;

            case 4:
                if (entity != null) {
                    if (entity instanceof EntityHippogryph) {
                        return new ContainerHippogryph((EntityHippogryph) entity, player);
                    }
                }
                break;

            case 5:
                if (entity != null) {
                    if (entity instanceof EntityHippocampus) {
                        return new ContainerHippocampus((EntityHippocampus) entity, player);
                    }
                }
                break;
            case 6:
                if (tile != null) {

                    if (tile instanceof TileEntityMyrmexCocoon) {
                        return new ContainerMyrmexCocoon(player.inventory, (TileEntityMyrmexCocoon) tile, player);
                    }
                }
                break;
            case 7:
                if (tile != null) {
                    if (tile instanceof TileEntityDragonforge) {
                        return new ContainerDragonForge(player.inventory, (TileEntityDragonforge) tile);
                    }
                }
                break;
        }
        return null;

    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        Entity entity = world.getEntityByID(x);
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        switch (id) {

            case 0:
                if (entity != null) {

                    if (entity instanceof EntityDragonBase) {
                        return new com.github.alexthe666.iceandfire.client.gui.GuiDragon(player.inventory, (EntityDragonBase) entity);
                    }
                }
                break;

            case 1:
                if (tile != null) {

                    if (tile instanceof TileEntityPodium) {
                        return new com.github.alexthe666.iceandfire.client.gui.GuiPodium(player.inventory, (TileEntityPodium) tile);
                    }
                }
                break;

            case 2:
                if (tile != null) {

                    if (tile instanceof TileEntityLectern) {
                        return new com.github.alexthe666.iceandfire.client.gui.GuiLectern(player.inventory, (TileEntityLectern) tile);
                    }
                }
                break;
            case 3:
                return new com.github.alexthe666.iceandfire.client.gui.bestiary.GuiBestiary(player.getActiveItemStack());

            case 4:
                if (entity != null) {

                    if (entity instanceof EntityHippogryph) {
                        return new com.github.alexthe666.iceandfire.client.gui.GuiHippogryph(player.inventory, (EntityHippogryph) entity);
                    }
                }
                break;
            case 5:
                if (entity != null) {

                    if (entity instanceof EntityHippocampus) {
                        return new com.github.alexthe666.iceandfire.client.gui.GuiHippocampus(player.inventory, (EntityHippocampus) entity);
                    }
                }
                break;
            case 6:
                if (tile != null) {
                    if (tile instanceof TileEntityMyrmexCocoon) {
                        return new com.github.alexthe666.iceandfire.client.gui.GuiMyrmexCocoon(player.inventory, (TileEntityMyrmexCocoon) tile);
                    }
                }
                break;
            case 7:
                if (tile != null) {
                    if (tile instanceof TileEntityDragonforge) {
                        return new com.github.alexthe666.iceandfire.client.gui.GuiDragonForge(player.inventory, (TileEntityDragonforge) tile);
                    }
                }
                break;
        }
        return entity;
    }
}