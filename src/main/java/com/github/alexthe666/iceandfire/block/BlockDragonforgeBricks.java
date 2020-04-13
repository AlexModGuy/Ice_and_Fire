package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeBrick;
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
import java.util.Random;

public class BlockDragonforgeBricks extends BlockContainer implements IDragonProof {

    public static final PropertyBool GRILL = PropertyBool.create("grill");
    private final boolean isFire;

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

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            this.checkGrill(worldIn, pos);
        }
    }

    public int tickRate(World worldIn) {
        return 3;
    }

    private void checkGrill(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos);
        boolean missingFurnace = getConnectedTileEntity(worldIn, pos) == null;
        worldIn.setBlockState(pos, state.withProperty(GRILL, !missingFurnace));

    }

    private TileEntityDragonforge getConnectedTileEntity(World worldIn, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.values()) {
            if (worldIn.getTileEntity(pos.offset(facing)) != null && worldIn.getTileEntity(pos.offset(facing)) instanceof TileEntityDragonforge) {
                TileEntityDragonforge forge = (TileEntityDragonforge) worldIn.getTileEntity(pos.offset(facing));
                if (forge != null && forge.assembled()) {
                    return forge;
                }
            }
        }
        return null;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(GRILL, Boolean.valueOf(meta > 0));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(GRILL).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, GRILL);
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDragonforgeBrick();
    }
}
