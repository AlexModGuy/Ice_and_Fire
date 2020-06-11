package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockLectern extends ContainerBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    protected static final VoxelShape AABB = Block.makeCuboidShape(4, 0, 4, 12, 19, 12);

    public BlockLectern() {
        super(Properties.create(Material.WOOD).hardnessAndResistance(2, 5).sound(SoundType.WOOD));
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH));
        this.setRegistryName(IceAndFire.MODID, "lectern");
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }


    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityLectern) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityLectern) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        BlockState BlockState = worldIn.getBlockState(pos.down());
        Block block = BlockState.getBlock();
        return BlockState.isSolidSide(worldIn, pos, Direction.UP);
    }

    @Deprecated
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        //worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        this.checkFall(worldIn, pos);
    }

    private boolean checkFall(World worldIn, BlockPos pos) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            return false;
        } else {
            return true;
        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!player.isShiftKeyDown()) {
            if (worldIn.isRemote) {
                IceAndFire.PROXY.setRefrencedTE(worldIn.getTileEntity(pos));
            } else {
                INamedContainerProvider inamedcontainerprovider = this.getContainer(state, worldIn, pos);
                if (inamedcontainerprovider != null) {
                    player.openContainer(inamedcontainerprovider);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityLectern();
    }
}