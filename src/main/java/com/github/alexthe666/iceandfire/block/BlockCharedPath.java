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
    boolean isFire;

    @SuppressWarnings("deprecation")
    public BlockCharedPath(boolean isFire) {
        super(Block.Properties.create(Material.PLANTS).sound(isFire ? SoundType.GROUND : SoundType.GLASS).hardnessAndResistance(0.6F).harvestTool(ToolType.SHOVEL).harvestLevel(0).slipperiness(isFire ? 1 : 0.98F).tickRandomly());
        this.isFire = isFire;
        setRegistryName(IceAndFire.MODID, isFire ? "chared_grass_path" : "frozen_grass_path");
        this.setDefaultState(stateContainer.getBaseState().with(REVERTS, Boolean.valueOf(false)));
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.get(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState());
            }
        }
        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
            worldIn.setBlockState(pos, isFire ? IafBlockRegistry.CHARRED_DIRT.getDefaultState() : IafBlockRegistry.FROZEN_DIRT.getDefaultState());
        }
        updateBlockState(worldIn, pos);
        super.tick(state, worldIn, pos, rand);
    }

    private void updateBlockState(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
            worldIn.setBlockState(pos, isFire ? IafBlockRegistry.CHARRED_DIRT.getDefaultState() : IafBlockRegistry.FROZEN_DIRT.getDefaultState());
        }
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(REVERTS, meta == 1);
    }

    public int getMetaFromState(BlockState state) {
        return state.get(REVERTS) ? 1 : 0;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
