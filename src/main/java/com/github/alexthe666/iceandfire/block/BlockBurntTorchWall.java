package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class BlockBurntTorchWall extends WallTorchBlock implements IDreadBlock {

    public BlockBurntTorchWall() {
        super(
            Properties.of(Material.WOOD)
                .lightLevel((state) -> {
                    return 0;
                })
                .sound(SoundType.WOOD).noOcclusion().dynamicShape()
                .dropsLike(IafBlockRegistry.BURNT_TORCH.get())
                .noCollission(),
            DustParticleOptions.REDSTONE
        );
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {

    }
}