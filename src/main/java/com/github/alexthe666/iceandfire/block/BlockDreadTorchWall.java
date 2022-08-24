package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.material.Material;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDreadTorchWall extends WallTorchBlock implements IDreadBlock {

    public BlockDreadTorchWall() {
        super(
            Properties
                .of(Material.WOOD)
                .lightLevel((state) -> {
                    return 5;
                })
                .sound(SoundType.STONE)
                .noOcclusion()
                .dynamicShape()
                .noCollission()
                .dropsLike(IafBlockRegistry.DREAD_TORCH),
            RedstoneParticleData.REDSTONE
        );

        setRegistryName(IceAndFire.MODID, "dread_torch_wall");
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        Direction direction = stateIn.getValue(FACING);
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;
        double d3 = 0.22D;
        double d4 = 0.27D;
        Direction direction1 = direction.getOpposite();
        IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, d0 + 0.27D * (double) direction1.getStepX(), d1 + 0.22D, d2 + 0.27D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
    }
}