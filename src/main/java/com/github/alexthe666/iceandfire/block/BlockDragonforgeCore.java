package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDragonforgeCore extends ContainerBlock implements IDragonProof, INoTab {
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

        String disabled = activated ? "" : "_disabled";
        this.setRegistryName(IceAndFire.MODID, "dragonforge_" + DragonType.getNameFromInt(isFire) + "_core" + disabled);
        this.isFire = isFire;
        this.activated = activated;
    }

    public static void setState(int dragonType, boolean active, World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        keepInventory = true;

        if (active) {
            if (dragonType == 0) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.defaultBlockState(), 3);
            } else if(dragonType == 1) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.defaultBlockState(), 3);
            }else if(dragonType == 2) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.defaultBlockState(), 3);
            }
        } else {
            if (dragonType == 0) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.defaultBlockState(), 3);
            } else if(dragonType == 1) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.defaultBlockState(), 3);
            }else if(dragonType == 2) {
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.defaultBlockState(), 3);
                worldIn.setBlock(pos, IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.defaultBlockState(), 3);
            }
        }

        keepInventory = false;

        if (tileentity != null) {
            tileentity.clearRemoved();
            worldIn.setBlockEntity(pos, tileentity);
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!player.isShiftKeyDown()) {
            if (worldIn.isClientSide) {
                IceAndFire.PROXY.setRefrencedTE(worldIn.getBlockEntity(pos));
            } else {
                INamedContainerProvider inamedcontainerprovider = this.getMenuProvider(state, worldIn, pos);
                if (inamedcontainerprovider != null) {
                    player.openMenu(inamedcontainerprovider);
                }
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        if(isFire == 0){
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.asItem());
        }
        if(isFire == 1){
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.asItem());
        }
        if(isFire == 2){
            return new ItemStack(IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.asItem());
        }
        return new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.asItem());
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.activated) {

        }
    }

    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new TileEntityDragonforge(isFire);
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileEntityDragonforge) {
            InventoryHelper.dropContents(worldIn, pos, (TileEntityDragonforge) tileentity);
            worldIn.updateNeighbourForOutputSignal(pos, this);
            worldIn.removeBlockEntity(pos);
        }
    }

    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }


    @Override
    public boolean shouldBeInTab() {
        return !activated;
    }
}
