package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.DRAGONFORGE_INPUT;

public class BlockDragonforgeInput extends BaseEntityBlock implements IDragonProof {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    private final int dragonType;

    public BlockDragonforgeInput(int dragonType) {
        super(
            Properties
                .of(Material.STONE)
                .dynamicShape()
                .strength(40, 500)
                .sound(SoundType.METAL)
		);

        this.dragonType = dragonType;
        this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, Boolean.valueOf(false)));
    }

    static String name (int dragonType) {
        return "dragonforge_%s_input".formatted(DragonType.getNameFromInt(dragonType));
    }


    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult resultIn) {
        if (this.getConnectedTileEntity(worldIn, resultIn.getBlockPos()) != null) {
            TileEntityDragonforge forge = this.getConnectedTileEntity(worldIn, resultIn.getBlockPos());
            if (forge != null && forge.isFire == dragonType) {
                if (worldIn.isClientSide) {
                    IceAndFire.PROXY.setRefrencedTE(worldIn.getBlockEntity(forge.getBlockPos()));
                } else {
                    MenuProvider inamedcontainerprovider = this.getMenuProvider(forge.getBlockState(), worldIn, forge.getBlockPos());
                    if (inamedcontainerprovider != null) {
                        player.openMenu(inamedcontainerprovider);
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    private TileEntityDragonforge getConnectedTileEntity(Level worldIn, BlockPos pos) {
        for (Direction facing : Direction.values()) {
            if (worldIn.getBlockEntity(pos.relative(facing)) != null && worldIn.getBlockEntity(pos.relative(facing)) instanceof TileEntityDragonforge) {
                return (TileEntityDragonforge) worldIn.getBlockEntity(pos.relative(facing));
            }
        }
        return null;
    }

    public BlockState getStateFromMeta(int meta) {
        return this.defaultBlockState().setValue(ACTIVE, Boolean.valueOf(meta > 0));
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public int getMetaFromState(BlockState state) {
        return state.getValue(ACTIVE).booleanValue() ? 1 : 0;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.getBlockEntity(pos) instanceof TileEntityDragonforgeInput) {
            ((TileEntityDragonforgeInput) worldIn.getBlockEntity(pos)).resetCore();
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return level.isClientSide ? createTickerHelper(entityType, DRAGONFORGE_INPUT.get(), TileEntityDragonforgeInput::tick) : null;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDragonforgeInput(pos, state);
    }
}
