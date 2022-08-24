package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeBrick;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDragonforgeBricks extends ContainerBlock implements IDragonProof {

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

        this.setRegistryName(IceAndFire.MODID, "dragonforge_" + DragonType.getNameFromInt(isFire) + "_brick");
        this.isFire = isFire;
        this.registerDefaultState(this.getStateDefinition().any().setValue(GRILL, Boolean.valueOf(false)));
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult resultIn) {
        if (this.getConnectedTileEntity(worldIn, resultIn.getBlockPos()) != null) {
            TileEntityDragonforge forge = this.getConnectedTileEntity(worldIn, resultIn.getBlockPos());
            if (forge != null && forge.isFire == isFire) {
                if (worldIn.isClientSide) {
                    IceAndFire.PROXY.setRefrencedTE(worldIn.getBlockEntity(forge.getBlockPos()));
                } else {
                    INamedContainerProvider inamedcontainerprovider = this.getMenuProvider(forge.getBlockState(), worldIn, forge.getBlockPos());
                    if (inamedcontainerprovider != null) {
                        player.openMenu(inamedcontainerprovider);
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!worldIn.isClientSide) {
            this.checkGrill(worldIn, pos);
        }
    }

    public int tickRate(World worldIn) {
        return 3;
    }

    private void checkGrill(World worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        boolean missingFurnace = getConnectedTileEntity(worldIn, pos) == null;
        worldIn.setBlockAndUpdate(pos, state.setValue(GRILL, !missingFurnace));

    }

    private TileEntityDragonforge getConnectedTileEntity(World worldIn, BlockPos pos) {
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

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(GRILL);
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }


    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new TileEntityDragonforgeBrick();
    }
}
