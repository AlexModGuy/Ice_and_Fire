package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDreadWoodLock extends Block implements IDragonProof {

    public BlockDreadWoodLock() {
        super(Material.ROCK);
        this.setHardness(30F);
        this.setResistance(500F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.dreadwood_planks_lock");
        this.setRegistryName(IceAndFire.MODID, "dreadwood_planks_lock");
    }
}
