package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class BlockCharedPath extends DirtPathBlock {
    public static final BooleanProperty REVERTS = BooleanProperty.create("revert");
    public Item itemBlock;
    public int dragonType;

    @SuppressWarnings("deprecation")
    public BlockCharedPath(int dragonType) {
        super(
            BlockBehaviour.Properties
                .of(Material.PLANT)
                .sound(dragonType != 1 ? SoundType.GRAVEL : SoundType.GLASS)
                .strength(0.6F)
                .friction(dragonType != 1 ? 0.6F : 0.98F)
                .randomTicks()
                .requiresCorrectToolForDrops()
		);

        this.dragonType = dragonType;
        //setRegistryName(IceAndFire.MODID, getNameFromType(dragonType));
        this.registerDefaultState(stateDefinition.any().setValue(REVERTS, Boolean.valueOf(false)));
    }

    public static String getNameFromType(int dragonType){
        return switch (dragonType) {
            case 0 -> "chared_dirt_path";
            case 1 -> "frozen_dirt_path";
            case 2 -> "crackled_dirt_path";
            default -> "";
        };
    }

    public BlockState getSmushedState(int dragonType){
        switch (dragonType){
            case 0:
                return IafBlockRegistry.CHARRED_DIRT.get().defaultBlockState();
            case 1:
                return IafBlockRegistry.FROZEN_DIRT.get().defaultBlockState();
            case 2:
                return IafBlockRegistry.CRACKLED_DIRT.get().defaultBlockState();
        }
        return null;
    }

    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
        if (!worldIn.isClientSide) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockAndUpdate(pos, Blocks.DIRT_PATH.defaultBlockState());
            }
        }
        if (worldIn.getBlockState(pos.above()).getMaterial().isSolid()) {
            worldIn.setBlockAndUpdate(pos, getSmushedState(dragonType));
        }
        updateBlockState(worldIn, pos);
    }

    private void updateBlockState(Level worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos.above()).getMaterial().isSolid()) {
            worldIn.setBlockAndUpdate(pos, getSmushedState(dragonType));
        }
    }

    public BlockState getStateFromMeta(int meta) {
        return this.defaultBlockState().setValue(REVERTS, meta == 1);
    }

    public int getMetaFromState(BlockState state) {
        return state.getValue(REVERTS) ? 1 : 0;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
