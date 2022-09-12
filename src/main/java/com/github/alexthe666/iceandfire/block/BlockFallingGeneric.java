package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class BlockFallingGeneric extends FallingBlock {
    public Item itemBlock;

    public BlockFallingGeneric(Material materialIn, String name, float hardness, float resistance, SoundType sound) {
        super(
            BlockBehaviour.Properties
                .of(materialIn)
                .sound(sound)
                .strength(hardness, resistance)
        );

        setRegistryName(IceAndFire.MODID, name);

    }

    @SuppressWarnings("deprecation")
    public BlockFallingGeneric(Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound, boolean slippery) {
        super(
            BlockBehaviour.Properties
                .of(materialIn)
                .sound(sound)
                .strength(hardness, resistance)
                .friction(0.98F)
        );

        setRegistryName(IceAndFire.MODID, name);
    }


    public int getDustColor(BlockState blkst) {
        return -8356741;
    }
}
