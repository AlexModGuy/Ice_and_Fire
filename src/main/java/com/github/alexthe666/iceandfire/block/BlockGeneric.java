package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityDreadMob;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.ToolType;

public class BlockGeneric extends Block {
    public BlockGeneric(Material materialIn, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
        super(Block.Properties.create(materialIn).sound(sound).hardnessAndResistance(hardness, resistance).harvestTool(ToolType.get(toolUsed)).harvestLevel(toolStrength));
        this.setRegistryName(IceAndFire.MODID, name);
    }

    public BlockGeneric(Material materialIn, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery) {
        super(Block.Properties.create(materialIn).sound(sound).hardnessAndResistance(hardness, resistance).harvestTool(ToolType.get(toolUsed)).harvestLevel(toolStrength).slipperiness(0.98F));
        this.setRegistryName(IceAndFire.MODID, name);
    }

    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(BlockState state) {
        return this != IafBlockRegistry.DRAGON_ICE;
    }

    @SuppressWarnings("deprecation")
    public boolean isFullCube(BlockState state) {
        return this != IafBlockRegistry.DRAGON_ICE;
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, Entity entityIn) {
        return entityIn instanceof EntityDreadMob || !DragonUtils.isDreadBlock(state);
    }
}
