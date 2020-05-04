package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDragonforgeCore extends ContainerBlock implements IDragonProof {
    private static boolean keepInventory;
    private boolean isFire;
    private boolean activated;

    public BlockDragonforgeCore(boolean isFire, boolean activated) {
        super(Material.IRON);
        this.setHardness(40F);
        this.setResistance(500F);
        this.setSoundType(SoundType.METAL);
        if (!activated) {
            this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        }
        String disabled = activated ? "" : "_disabled";
        this.setTranslationKey("iceandfire.dragonforge_" + (isFire ? "fire" : "ice") + "_core");
        this.setRegistryName(IceAndFire.MODID, "dragonforge_" + (isFire ? "fire" : "ice") + "_core" + disabled);
        if (activated) {
            this.setLightLevel(1.0F);
        }
        this.isFire = isFire;
        this.activated = activated;
    }

    @Override
    public EnumPushReaction getPushReaction(BlockState state) {
        return EnumPushReaction.BLOCK;
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
                worldIn.setBlockState(pos, IafBlockRegistry.dragonforge_fire_core_disabled.getDefaultState(), 3);
                worldIn.setBlockState(pos, IafBlockRegistry.dragonforge_fire_core_disabled.getDefaultState(), 3);
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            return false;
        } else {
            playerIn.openGui(IceAndFire.INSTANCE, 7, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return isFire ? Item.getItemFromBlock(IafBlockRegistry.dragonforge_fire_core_disabled) : Item.getItemFromBlock(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(isFire ? Item.getItemFromBlock(IafBlockRegistry.dragonforge_fire_core_disabled) : Item.getItemFromBlock(IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED));
    }

    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return isFire;
    }

    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.activated) {

        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDragonforge(isFire);
    }

    public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
        if (!keepInventory) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityDragonforge) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityDragonforge) tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }


}
