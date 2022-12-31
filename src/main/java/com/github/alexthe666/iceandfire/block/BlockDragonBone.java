package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;

public class BlockDragonBone extends RotatedPillarBlock implements IDragonProof {

    public BlockDragonBone() {
        super(
            BlockBehaviour.Properties
                .of(Material.STONE)
                .sound(SoundType.WOOD)
                .strength(30F, 500F)
                .requiresCorrectToolForDrops()
		);

    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }
}
