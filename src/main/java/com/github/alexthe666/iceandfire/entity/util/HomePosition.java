package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class HomePosition {
    int x;
    int y;
    int z;
    BlockPos pos;
    String dimension;

    public HomePosition(CompoundNBT compound) {
        read(compound);
    }

    public HomePosition(BlockPos pos, World world) {
        this(pos.getX(), pos.getY(), pos.getZ(), world);
    }

    public HomePosition(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        pos = new BlockPos(x, y, z);
        this.dimension = DragonUtils.getDimensionName(world);
    }

    public BlockPos getPosition() {
        return pos;
    }

    public String getDimension() {
        return dimension == null ? "" : dimension;
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("HomeAreaX", this.x);
        compound.putInt("HomeAreaY", this.y);
        compound.putInt("HomeAreaZ", this.z);
        compound.putString("HomeDimension", this.dimension);
        return compound;
    }

    public HomePosition read(CompoundNBT compound) {
        if (compound.contains("HomeAreaX"))
            this.x = compound.getInt("HomeAreaX");
        if (compound.contains("HomeAreaY"))
            this.y = compound.getInt("HomeAreaY");
        if (compound.contains("HomeAreaZ"))
            this.z = compound.getInt("HomeAreaZ");
        pos = new BlockPos(x, y, z);
        if (compound.contains("HomeDimension"))
            this.dimension = compound.getString("HomeDimension");

        return this;
    }
}

