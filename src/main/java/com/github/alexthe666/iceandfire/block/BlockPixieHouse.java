package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.IBlockRenderProperties;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Consumer;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.PIXIE_HOUSE;

public class BlockPixieHouse extends BaseEntityBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public BlockPixieHouse() {
        super(
            Properties
                .of(Material.WOOD)
                .noOcclusion()
                .dynamicShape()
                .strength(2.0F, 5.0F)
                .randomTicks()
		);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    static String name(String type) {
        return "pixie_house_%s".formatted(type);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        dropPixie(worldIn, pos);
        popResource(worldIn, pos, new ItemStack(this, 0));
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public void updateTick(Level worldIn, BlockPos pos, BlockState state, Random rand) {
        this.checkFall(worldIn, pos);
    }

    private boolean checkFall(Level worldIn, BlockPos pos) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            dropPixie(worldIn, pos);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void initializeClient(Consumer<IBlockRenderProperties> consumer) {
        super.initializeClient(consumer);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    private boolean canPlaceBlockAt(Level worldIn, BlockPos pos) {
        return true;
    }

    public void dropPixie(Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityPixieHouse && ((TileEntityPixieHouse) world.getBlockEntity(pos)).hasPixie) {
            ((TileEntityPixieHouse) world.getBlockEntity(pos)).releasePixie();
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return level.isClientSide ? createTickerHelper(entityType, PIXIE_HOUSE.get(), TileEntityPixieHouse::tick) : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityPixieHouse(pos, state);
    }
}
