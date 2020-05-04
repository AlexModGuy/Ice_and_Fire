package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFallingGeneric extends FallingBlock {
    public Item itemBlock;

    public BlockFallingGeneric(Material materialIn, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
        super(materialIn);
        this.setTranslationKey(name);
        this.setHarvestLevel(toolUsed, toolStrength);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        setRegistryName(IceAndFire.MODID, gameName);

    }

    @SuppressWarnings("deprecation")
    public BlockFallingGeneric(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery) {
        super(materialIn);
        this.setTranslationKey(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setHarvestLevel(toolUsed, toolStrength);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        setRegistryName(IceAndFire.MODID, gameName);
        if (slippery) {
            this.slipperiness = 0.98F;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState blkst) {
        return -8356741;
    }
}
