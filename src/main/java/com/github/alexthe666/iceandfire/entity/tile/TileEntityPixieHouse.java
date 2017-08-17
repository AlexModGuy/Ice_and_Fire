package com.github.alexthe666.iceandfire.entity.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPixieHouse extends TileEntity {
    public int houseType;


    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("HouseType", houseType);
        return super.writeToNBT(compound);
    }

    public void readFromNBT(NBTTagCompound compound) {
        houseType = compound.getInteger("HouseType");
        super.readFromNBT(compound);
    }

    public void onLoad() {

    }
}
