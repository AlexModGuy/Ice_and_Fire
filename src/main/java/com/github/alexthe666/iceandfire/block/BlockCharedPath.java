package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockCharedPath extends GrassPathBlock {
    public static final BooleanProperty REVERTS = BooleanProperty.create("revert");
    public Item itemBlock;
    public int dragonType;

    @SuppressWarnings("deprecation")
    public BlockCharedPath(int dragonType) {
        super(
            AbstractBlock.Properties
                .of(Material.PLANT)
                .sound(dragonType != 1 ? SoundType.GRAVEL : SoundType.GLASS)
                .strength(0.6F).harvestTool(ToolType.SHOVEL)
                .harvestLevel(0)
                .friction(dragonType != 1 ? 0.6F : 0.98F)
                .randomTicks()
                .requiresCorrectToolForDrops()
		);

        this.dragonType = dragonType;
        setRegistryName(IceAndFire.MODID, getNameFromType(dragonType));
        this.registerDefaultState(stateDefinition.any().setValue(REVERTS, Boolean.valueOf(false)));
    }

    public String getNameFromType(int dragonType){
        switch (dragonType){
            case 0:
                return "chared_grass_path";
            case 1:
                return "frozen_grass_path";
            case 2:
                return "crackled_grass_path";
        }
        return "";
    }

    public BlockState getSmushedState(int dragonType){
        switch (dragonType){
            case 0:
                return IafBlockRegistry.CHARRED_DIRT.defaultBlockState();
            case 1:
                return IafBlockRegistry.FROZEN_DIRT.defaultBlockState();
            case 2:
                return IafBlockRegistry.CRACKLED_DIRT.defaultBlockState();
        }
        return null;
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
        if (!worldIn.isClientSide) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockAndUpdate(pos, Blocks.GRASS_PATH.defaultBlockState());
            }
        }
        if (worldIn.getBlockState(pos.above()).getMaterial().isSolid()) {
            worldIn.setBlockAndUpdate(pos, getSmushedState(dragonType));
        }
        updateBlockState(worldIn, pos);
    }

    private void updateBlockState(World worldIn, BlockPos pos) {
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

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
