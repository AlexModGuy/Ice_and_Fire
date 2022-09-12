package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;


public class HomePosition {
    int x;
    int y;
    int z;
    BlockPos pos;
    String dimension;

    public HomePosition(CompoundTag compound) {
        read(compound);
    }

    public HomePosition(CompoundTag compound, Level world) {
        read(compound, world);
    }

    public HomePosition(BlockPos pos, Level world) {
        this(pos.getX(), pos.getY(), pos.getZ(), world);
    }

    public HomePosition(int x, int y, int z, Level world) {
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

    public CompoundTag write(CompoundTag compound) {
        compound.putInt("HomeAreaX", this.x);
        compound.putInt("HomeAreaY", this.y);
        compound.putInt("HomeAreaZ", this.z);
        if (dimension != null)
            compound.putString("HomeDimension", this.dimension);
        return compound;
    }

    public HomePosition read(CompoundTag compound, Level world) {
        read(compound);
        if (this.dimension == null)
            this.dimension = DragonUtils.getDimensionName(world);
        return this;
    }

    public HomePosition read(CompoundTag compound) {
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

