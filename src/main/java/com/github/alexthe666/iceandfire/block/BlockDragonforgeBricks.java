package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeBrick;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDragonforgeBricks extends BaseEntityBlock implements IDragonProof {

    public static final BooleanProperty GRILL = BooleanProperty.create("grill");
    private final int isFire;

    public BlockDragonforgeBricks(int isFire) {
        super(
            Properties
                .of(Material.STONE)
                .dynamicShape()
                .strength(40, 500)
    			.sound(SoundType.METAL)
		);

        this.isFire = isFire;
        this.registerDefaultState(this.getStateDefinition().any().setValue(GRILL, Boolean.valueOf(false)));
    }

    static String name(int dragonType) {
        return "dragonforge_%s_brick".formatted(DragonType.getNameFromInt(dragonType));
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult resultIn) {
        if (this.getConnectedTileEntity(worldIn, resultIn.getBlockPos()) != null) {
            TileEntityDragonforge forge = this.getConnectedTileEntity(worldIn, resultIn.getBlockPos());
            if (forge != null && forge.isFire == isFire) {
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
        return InteractionResult.FAIL;
    }

    public void updateTick(Level worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!worldIn.isClientSide) {
            this.checkGrill(worldIn, pos);
        }
    }

    public int tickRate(Level worldIn) {
        return 3;
    }

    private void checkGrill(Level worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        boolean missingFurnace = getConnectedTileEntity(worldIn, pos) == null;
        worldIn.setBlockAndUpdate(pos, state.setValue(GRILL, !missingFurnace));

    }

    private TileEntityDragonforge getConnectedTileEntity(Level worldIn, BlockPos pos) {
        for (Direction facing : Direction.values()) {
            if (worldIn.getBlockEntity(pos.relative(facing)) != null && worldIn.getBlockEntity(pos.relative(facing)) instanceof TileEntityDragonforge) {
                TileEntityDragonforge forge = (TileEntityDragonforge) worldIn.getBlockEntity(pos.relative(facing));
                if (forge != null && forge.assembled()) {
                    return forge;
                }
            }
        }
        return null;
    }

    public BlockState getStateFromMeta(int meta) {
        return this.defaultBlockState().setValue(GRILL, Boolean.valueOf(meta > 0));
    }

    public int getMetaFromState(BlockState state) {
        return state.getValue(GRILL).booleanValue() ? 1 : 0;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GRILL);
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDragonforgeBrick(pos, state);
    }
}
