package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrassPath;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCharedPath extends BlockGrassPath {
    public static final PropertyBool REVERTS = PropertyBool.create("revert");
    public Item itemBlock;
    boolean isFire;

    @SuppressWarnings("deprecation")
    public BlockCharedPath(boolean isFire) {
        super();
        this.isFire = isFire;
        this.setTranslationKey(isFire ? "iceandfire.charedGrassPath" : "iceandfire.frozenGrassPath");
        this.setHarvestLevel("shovel", 0);
        this.setHardness(0.6F);
        this.setSoundType(isFire ? SoundType.GROUND : SoundType.GLASS);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        if (!isFire) {
            this.slipperiness = 0.98F;
        }
        this.setLightOpacity(0);
        setRegistryName(IceAndFire.MODID, isFire ? "chared_grass_path" : "frozen_grass_path");
        this.setDefaultState(this.blockState.getBaseState().withProperty(REVERTS, Boolean.valueOf(false)));
        this.setTickRandomly(true);
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState());
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldSideBeRendered(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        switch (side) {
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                BlockState BlockState = blockAccess.getBlockState(pos.offset(side));
                Block block = BlockState.getBlock();
                return !BlockState.isOpaqueCube() && block != Blocks.FARMLAND && block != Blocks.GRASS_PATH && block != IafBlockRegistry.charedGrassPath && block != IafBlockRegistry.frozenGrassPath;
            default:
                return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    @Nullable
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return isFire ? IafBlockRegistry.charedDirt.getItemDropped(IafBlockRegistry.charedDirt.getDefaultState(), rand, fortune) : IafBlockRegistry.frozenDirt.getItemDropped(IafBlockRegistry.frozenDirt.getDefaultState(), rand, fortune);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
            worldIn.setBlockState(pos, isFire ? IafBlockRegistry.charedDirt.getDefaultState() : IafBlockRegistry.frozenDirt.getDefaultState());
        }
    }

    private void updateBlockState(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid()) {
            worldIn.setBlockState(pos, isFire ? IafBlockRegistry.charedDirt.getDefaultState() : IafBlockRegistry.frozenDirt.getDefaultState());
        }
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(REVERTS, meta == 1);
    }

    public int getMetaFromState(BlockState state) {
        return state.getValue(REVERTS) ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, REVERTS);
    }
}
