package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockGraveyardSoil extends Block {

    public BlockGraveyardSoil() {
        super(Properties.create(Material.EARTH).sound(SoundType.GROUND).hardnessAndResistance(5, 15).harvestTool(ToolType.SHOVEL).harvestLevel(0).tickRandomly());
        setRegistryName(IceAndFire.MODID, "graveyard_soil");
    }


    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (!worldIn.isDaytime() && !worldIn.getBlockState(pos.up()).isSolid() && rand.nextInt(9) == 0 && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
                EntityGhost ghost = IafEntityRegistry.GHOST.create(worldIn);
                Random random = new Random();
                ghost.setPositionAndRotation(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, random.nextFloat() * 360F, 0);
                if (!worldIn.isRemote) {
                    ghost.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.SPAWNER, null, null);
                    worldIn.addEntity(ghost);
                }
                ghost.setAnimation(EntityGhost.ANIMATION_SCARE);
                ghost.setHomePosAndDistance(pos, 16);
            }
        }
    }
}
