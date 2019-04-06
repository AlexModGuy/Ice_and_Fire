package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDragonforgeBricks extends Block {

    private final boolean isFire;
    public static final PropertyBool GRILL = PropertyBool.create("grill");

    public BlockDragonforgeBricks(boolean isFire) {
        super(Material.ROCK);
        this.setLightOpacity(2);
        this.setHardness(40F);
        this.setResistance(500F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.dragonforge_" + (isFire ? "fire" : "ice") + "_brick");
        this.setRegistryName(IceAndFire.MODID, "dragonforge_" + (isFire ? "fire" : "ice") + "_brick");
        this.isFire = isFire;
        this.setDefaultState(this.blockState.getBaseState().withProperty(GRILL, Boolean.valueOf(false)));
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (((Boolean) state.getValue(GRILL)).booleanValue()) {
            if (this.getConnectedTileEntity(worldIn, pos) != null) {
                TileEntityDragonforge forge = this.getConnectedTileEntity(worldIn, pos);
                if (forge.isFire == isFire) {
                    if (playerIn.isSneaking()) {
                        return false;
                    } else {
                        playerIn.openGui(IceAndFire.INSTANCE, 7, worldIn, forge.getPos().getX(), forge.getPos().getY(), forge.getPos().getZ());
                        return true;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            this.checkGrill(worldIn, pos);
        }
    }

    public int tickRate(World worldIn)
    {
        return 2;
    }

    private void checkGrill(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        if (((Boolean) state.getValue(GRILL)).booleanValue()) {
            if(getConnectedTileEntity(worldIn, pos) == null){
                worldIn.setBlockState(pos, state.withProperty(GRILL, false));
            }
        }
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
        return this.getDefaultState().withProperty(GRILL, Boolean.valueOf(meta > 0));
    }

    public int getMetaFromState(IBlockState state) {
        return ((Boolean) state.getValue(GRILL)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{GRILL});
    }

}
