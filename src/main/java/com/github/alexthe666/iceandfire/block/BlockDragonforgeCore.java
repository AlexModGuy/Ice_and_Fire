package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Random;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.DRAGONFORGE_CORE;

public class BlockDragonforgeCore extends BaseEntityBlock implements IDragonProof, INoTab {
    private static boolean keepInventory;
    private final int isFire;
    private final boolean activated;

    public BlockDragonforgeCore(int isFire, boolean activated) {
        super(
            Properties
                .of(Material.METAL)
                .dynamicShape()
                .strength(40, 500)
                .sound(SoundType.METAL)
                .lightLevel((state) -> {
                    return activated ? 15 : 0;
                })
        );

        this.isFire = isFire;
        this.activated = activated;
    }

    static String name(int dragonType, boolean activated) {
        return "dragonforge_%s_core%s".formatted(DragonType.getNameFromInt(dragonType), activated ? "": "_disabled");
    }

    public static void setState(int dragonType, boolean active, Level worldIn, BlockPos pos) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        keepInventory = true;

        if (active) {
            if (dragonType == 0) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get().defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get().defaultBlockState(), 3);
            } else if (dragonType == 1) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.get().defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.get().defaultBlockState(), 3);
            } else if (dragonType == 2) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get().defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get().defaultBlockState(), 3);
            }
        } else {
            if (dragonType == 0) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().defaultBlockState(), 3);
            } else if(dragonType == 1) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get().defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get().defaultBlockState(), 3);
            }else if(dragonType == 2) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get().defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get().defaultBlockState(), 3);
            }
        }

        keepInventory = false;

        if (tileentity != null) {
            tileentity.clearRemoved();
            worldIn.setBlockEntity(tileentity);
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.isShiftKeyDown()) {
            if (worldIn.isClientSide) {
                IceAndFire.PROXY.setRefrencedTE(worldIn.getBlockEntity(pos));
            } else {
                MenuProvider inamedcontainerprovider = this.getMenuProvider(state, worldIn, pos);
                if (inamedcontainerprovider != null) {
                    player.openMenu(inamedcontainerprovider);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public ItemStack getItem(Level worldIn, BlockPos pos, BlockState state) {
        if (isFire == 0) {
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().asItem());
        }
        if (isFire == 1) {
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get().asItem());
        }
        if (isFire == 2) {
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get().asItem());
        }
        return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get().asItem());
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public void randomDisplayTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        if (this.activated) {

        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileEntityDragonforge) {
            Containers.dropContents(worldIn, pos, (TileEntityDragonforge) tileentity);
            worldIn.updateNeighbourForOutputSignal(pos, this);
            worldIn.removeBlockEntity(pos);
        }
    }

    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }


    @Override
    public boolean shouldBeInTab() {
        return !activated;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return level.isClientSide ? createTickerHelper(entityType, DRAGONFORGE_CORE.get(), TileEntityDragonforge::tick) : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityDragonforge(pos, state, isFire);
    }
}
