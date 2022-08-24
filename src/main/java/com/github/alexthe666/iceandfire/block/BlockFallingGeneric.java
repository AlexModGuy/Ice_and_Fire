package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;

public class BlockFallingGeneric extends FallingBlock {
    public Item itemBlock;

    public BlockFallingGeneric(Material materialIn, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
        super(
            AbstractBlock.Properties
                .of(materialIn)
                .sound(sound)
                .strength(hardness, resistance)
                .harvestTool(ToolType.get(toolUsed))
                .harvestLevel(toolStrength)
        );

        setRegistryName(IceAndFire.MODID, name);

    }

    @SuppressWarnings("deprecation")
    public BlockFallingGeneric(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery) {
        super(
            AbstractBlock.Properties
                .of(materialIn)
                .sound(sound)
                .strength(hardness, resistance)
                .harvestTool(ToolType.get(toolUsed))
                .harvestLevel(toolStrength)
                .friction(0.98F)
        );

        setRegistryName(IceAndFire.MODID, name);
    }


    public int getDustColor(BlockState blkst) {
        return -8356741;
    }
}
