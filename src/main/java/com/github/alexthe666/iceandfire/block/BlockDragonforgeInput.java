package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockDragonforgeInput extends BlockContainer implements IDragonProof {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    private boolean isFire;

    public BlockDragonforgeInput(boolean isFire) {
        super(Material.IRON);
        this.setLightOpacity(2);
        this.setHardness(40F);
        this.setResistance(500F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.dragonforge_" + (isFire ? "fire" : "ice") + "_input");
        this.setRegistryName(IceAndFire.MODID, "dragonforge_" + (isFire ? "fire" : "ice") + "_input");
        this.isFire = isFire;
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, Boolean.valueOf(false)));
    }

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.getConnectedTileEntity(worldIn, pos) != null) {
            TileEntityDragonforge forge = this.getConnectedTileEntity(worldIn, pos);
            if (forge.isFire == isFire) {
                worldIn.scheduleUpdate(forge.getPos(), this, this.tickRate(worldIn));
                return forge.getBlockType().onBlockActivated(worldIn, forge.getPos(), worldIn.getBlockState(forge.getPos()), playerIn, hand, facing, hitX, hitY, hitZ);
            }
        }
        return false;
    }

    private TileEntityDragonforge getConnectedTileEntity(World worldIn, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (worldIn.getTileEntity(pos.offset(facing)) != null && worldIn.getTileEntity(pos.offset(facing)) instanceof TileEntityDragonforge) {
                return (TileEntityDragonforge) worldIn.getTileEntity(pos.offset(facing));
            }
        }
        return null;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, Boolean.valueOf(meta > 0));
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE);
    }

    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
            ((TileEntityDragonforgeInput) worldIn.getTileEntity(pos)).resetCore();
        }
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDragonforgeInput();
    }
}
