package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class BlockGraveyardSoil extends Block {

    public BlockGraveyardSoil() {
        super(
            Properties
                .of()
                .mapColor(MapColor.DIRT)
                .sound(SoundType.GRAVEL)
                .strength(5, 1F)
                .randomTicks()
		);
    }


    @Override
    public void tick(@NotNull BlockState state, ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (!worldIn.isClientSide) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (!worldIn.isDay() && !worldIn.getBlockState(pos.above()).canOcclude() && rand.nextInt(9) == 0 && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
                int checkRange = 32;
                int k = worldIn.getEntitiesOfClass(EntityGhost.class, (new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)).inflate(checkRange)).size();
                if (k < 10) {
                    EntityGhost ghost = IafEntityRegistry.GHOST.get().create(worldIn);
                    ghost.absMoveTo(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
                        ThreadLocalRandom.current().nextFloat() * 360F, 0);
                    if (!worldIn.isClientSide) {
                        ghost.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(pos), MobSpawnType.SPAWNER, null, null);
                        worldIn.addFreshEntity(ghost);
                    }
                    ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                    ghost.restrictTo(pos, 16);
                }
            }
        }
    }
}
