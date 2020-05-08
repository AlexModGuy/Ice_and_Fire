package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeBrick;
import net.minecraft.block.Block;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockDragonforgeBricks extends ContainerBlock implements IDragonProof {

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
        this.setDefaultState(this.blockState.getBaseState().with(GRILL, Boolean.valueOf(false)));
    }

    @Override
    public EnumPushReaction getPushReaction(BlockState state) {
        return EnumPushReaction.BLOCK;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (this.getConnectedTileEntity(worldIn, pos) != null) {
            TileEntityDragonforge forge = this.getConnectedTileEntity(worldIn, pos);
            if (forge.isFire == isFire) {
                worldIn.scheduleUpdate(forge.getPos(), this, this.tickRate(worldIn));
                return forge.getBlockType().onBlockActivated(worldIn, forge.getPos(), worldIn.getBlockState(forge.getPos()), playerIn, hand, facing, hitX, hitY, hitZ);
            }
        }
        return false;
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!worldIn.isRemote) {
            this.checkGrill(worldIn, pos);
        }
    }

    public int tickRate(World worldIn) {
        return 3;
    }

    private void checkGrill(World worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos);
        boolean missingFurnace = getConnectedTileEntity(worldIn, pos) == null;
        worldIn.setBlockState(pos, state.with(GRILL, !missingFurnace));

    }

    private TileEntityDragonforge getConnectedTileEntity(World worldIn, BlockPos pos) {
        for (Direction facing : Direction.values()) {
            if (worldIn.getTileEntity(pos.offset(facing)) != null && worldIn.getTileEntity(pos.offset(facing)) instanceof TileEntityDragonforge) {
                TileEntityDragonforge forge = (TileEntityDragonforge) worldIn.getTileEntity(pos.offset(facing));
                if (forge != null && forge.assembled()) {
                    return forge;
                }
            }
        }
        return null;
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(GRILL, Boolean.valueOf(meta > 0));
    }

    public int getMetaFromState(BlockState state) {
        return state.get(GRILL).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, GRILL);
    }

    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityDragonforgeBrick();
    }
}
