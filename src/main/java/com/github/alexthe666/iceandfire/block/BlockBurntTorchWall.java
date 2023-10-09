package com.github.alexthe666.iceandfire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockBurntTorchWall extends WallTorchBlock implements IDreadBlock {

    public BlockBurntTorchWall() {
        super(
            Properties.of()
                    .mapColor(MapColor.WOOD)
                    .ignitedByLava()
                    .lightLevel((state) -> 0)
                    .sound(SoundType.WOOD).noOcclusion().dynamicShape()
                    .lootFrom(IafBlockRegistry.BURNT_TORCH)
                    .noCollission(),
            DustParticleOptions.REDSTONE
        );
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {

    }
}