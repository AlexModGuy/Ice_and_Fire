package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockFallingGeneric extends BlockFalling {
    public Item itemBlock;

    public BlockFallingGeneric(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
        super(materialIn);
        this.setUnlocalizedName(name);
        this.setHarvestLevel(toolUsed, toolStrength);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB);
        setRegistryName(IceAndFire.MODID, gameName);
        GameRegistry.register(this);
        GameRegistry.register(itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName())));

    }

    public BlockFallingGeneric(Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound) {
        super(materialIn);
        this.setUnlocalizedName(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB);
        setRegistryName(IceAndFire.MODID, gameName);
        GameRegistry.register(this);
        GameRegistry.register(itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName())));

    }

}
