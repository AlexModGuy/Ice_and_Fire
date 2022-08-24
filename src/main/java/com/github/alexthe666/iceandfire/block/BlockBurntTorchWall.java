package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBurntTorchWall extends WallTorchBlock implements IDreadBlock {

    public BlockBurntTorchWall() {
        super(
            Properties.of(Material.WOOD)
                .lightLevel((state) -> {
                    return 0;
                })
                .sound(SoundType.WOOD).noOcclusion().dynamicShape()
                .dropsLike(IafBlockRegistry.BURNT_TORCH)
                .noCollission(),
            RedstoneParticleData.REDSTONE
        );

        setRegistryName(IceAndFire.MODID, "burnt_torch_wall");
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

    }
}