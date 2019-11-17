package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockDreadWoodLog extends BlockRotatedPillar implements IDragonProof, IDreadBlock {

    public BlockDreadWoodLog() {
        super(Material.WOOD);
        this.setHardness(2F);
        this.setResistance(10000F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.dreadwood_log");
        this.setRegistryName(IceAndFire.MODID, "dreadwood_log");
    }
}
