package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDragonforgeCore extends ContainerBlock implements IDragonProof, INoTab {
    private static boolean keepInventory;
    private boolean isFire;
    private boolean activated;

    public BlockDragonforgeCore(boolean isFire, boolean activated) {
        super(Properties.create(Material.IRON).variableOpacity().hardnessAndResistance(40, 500).sound(SoundType.METAL).lightValue(activated ? 15 : 0));
        String disabled = activated ? "" : "_disabled";
        this.setRegistryName(IceAndFire.MODID, "dragonforge_" + (isFire ? "fire" : "ice") + "_core" + disabled);
        this.isFire = isFire;
        this.activated = activated;
    }

    public static void setState(boolean fire, boolean active, World worldIn, BlockPos pos) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        if (active) {
            if (fire) {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE.getDefaultState(), 3);
            } else {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE.getDefaultState(), 3);
            }
        } else {
            if (fire) {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.getDefaultState(), 3);
            } else {
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.getDefaultState(), 3);
            }
        }

        keepInventory = false;

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
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

    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return isFire ? Item.getItemFromBlock(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED) : Item.getItemFromBlock(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(isFire ? Item.getItemFromBlock(IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED) : Item.getItemFromBlock(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED));
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public boolean isFireSource(World world, BlockPos pos, Direction side) {
        return isFire;
    }

    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.activated) {

        }
    }

    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileEntityDragonforge(isFire);
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityDragonforge) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityDragonforge) tileentity);
            worldIn.updateComparatorOutputLevel(pos, this);
        }
    }

    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }


    @Override
    public boolean shouldBeInTab() {
        return !activated;
    }
}
