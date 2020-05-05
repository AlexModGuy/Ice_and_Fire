package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.world.gen.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSilverPile extends Block {
    public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);
    protected static final AxisAlignedBB[] SNOW_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    public Item itemBlock;

    public BlockSilverPile() {
        super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().with(LAYERS, 1));
        this.setTickRandomly(true);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.silverpile");
        this.setHardness(0.3F);
        this.setSoundType(IafBlockRegistry.SOUND_TYPE_GOLD);
        setRegistryName(IceAndFire.MODID, "silverpile");
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return SNOW_AABB[state.get(LAYERS)];
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).get(LAYERS) < 5;
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, Entity entityIn) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isTopSolid(BlockState state) {
        return state.get(LAYERS) == 7;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getSelectedBoundingBox(BlockState blockState, World worldIn, BlockPos pos) {
        int i = blockState.get(LAYERS) - 1;
        float f = 0.125F;
        AxisAlignedBB axisalignedbb = blockState.getBoundingBox(worldIn, pos);
        return new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.maxX, i * f, axisalignedbb.maxZ);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        BlockState BlockState = worldIn.getBlockState(pos.down());
        Block block = BlockState.getBlock();
        return (block != Blocks.ICE && block != Blocks.PACKED_ICE) && (BlockState.getBlock().isLeaves(BlockState, worldIn, pos.down()) || (block == this && BlockState.get(LAYERS) >= 7 || BlockState.isOpaqueCube() && BlockState.getMaterial().blocksMovement()));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack item = playerIn.inventory.getCurrentItem();

        if (!item.isEmpty()) {
            if (item.getItem() != null) {
                if (item.getItem() == Item.getItemFromBlock(IafBlockRegistry.SILVER_PILE)) {
                    if (!item.isEmpty()) {
                        if (this.getMetaFromState(state) < 7) {
                            WorldUtils.setBlock(worldIn, pos.getX(), pos.getY(), pos.getZ(), IafBlockRegistry.SILVER_PILE, this.getMetaFromState(state) + 1, 3);
                            if (!playerIn.capabilities.isCreativeMode) {
                                item.shrink(1);

                                if (item.isEmpty()) {
                                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
                                }
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(BlockState blockstate) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(BlockState blockstate) {
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if (world instanceof World) {
            this.checkAndDropBlock((World) world, pos, world.getBlockState(neighbor));
        }
    }

    private boolean checkAndDropBlock(World worldIn, BlockPos pos, BlockState state) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return IafItemRegistry.SILVER_NUGGET;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP || super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(LAYERS, (meta & 7) + 1);
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).get(LAYERS) == 1;
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.get(LAYERS) - 1;
    }

    @Override
    public int quantityDropped(BlockState state, int fortune, Random random) {
        return (state.get(LAYERS)) + 1;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LAYERS);
    }
}