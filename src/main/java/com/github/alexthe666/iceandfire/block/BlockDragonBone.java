package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDragonBone extends BlockRotatedPillar {

    public BlockDragonBone() {
        super(Material.ROCK);
        this.setHardness(30F);
        this.setResistance(500F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.dragon_bone_block");
        this.setRegistryName(IceAndFire.MODID, "dragon_bone_block");
    }
}
