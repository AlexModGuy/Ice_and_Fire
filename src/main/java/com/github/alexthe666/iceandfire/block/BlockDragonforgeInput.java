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
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.DRAGONFORGE_INPUT;

public class BlockDragonforgeInput extends BaseEntityBlock implements IDragonProof {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    private final int dragonType;

    public BlockDragonforgeInput(int dragonType) {
        super(
            Properties
                .of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .dynamicShape()
                .strength(40, 500)
                .sound(SoundType.METAL)
		);

        this.dragonType = dragonType;
        this.registerDefaultState(this.getStateDefinition().any().setValue(ACTIVE, Boolean.FALSE));
    }

    static String name(int dragonType) {
        return "dragonforge_%s_input".formatted(DragonType.getNameFromInt(dragonType));
    }


    @Override
    public @NotNull PushReaction getPistonPushReaction(@NotNull BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, BlockHitResult resultIn) {
        if (this.getConnectedTileEntity(worldIn, resultIn.getBlockPos()) != null) {
            TileEntityDragonforge forge = this.getConnectedTileEntity(worldIn, resultIn.getBlockPos());
            if (forge != null && forge.fireType == dragonType) {
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
        return this.defaultBlockState().setValue(ACTIVE, meta > 0);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    public int getMetaFromState(BlockState state) {
        return state.getValue(ACTIVE).booleanValue() ? 1 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        if (worldIn.getBlockEntity(pos) instanceof TileEntityDragonforgeInput) {
            ((TileEntityDragonforgeInput) worldIn.getBlockEntity(pos)).resetCore();
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return level.isClientSide ? null : createTickerHelper(entityType, DRAGONFORGE_INPUT.get(), TileEntityDragonforgeInput::tick);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityDragonforgeInput(pos, state);
    }
}
