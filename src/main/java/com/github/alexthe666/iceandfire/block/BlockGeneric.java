package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityDreadMob;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGeneric extends Block {
    public Item itemBlock;

    public BlockGeneric(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound) {
        super(materialIn);
        this.setTranslationKey(name);
        this.setHarvestLevel(toolUsed, toolStrength);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        setRegistryName(IceAndFire.MODID, gameName);
    }

    @SuppressWarnings("deprecation")
    public BlockGeneric(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery) {
        super(materialIn);
        this.setTranslationKey(name);
        this.setHarvestLevel(toolUsed, toolStrength);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        if (slippery) {
            this.slipperiness = 0.98F;
        }
        setRegistryName(IceAndFire.MODID, gameName);
    }

    public BlockGeneric(Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound) {
        super(materialIn);
        this.setTranslationKey(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        setRegistryName(IceAndFire.MODID, gameName);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return this == IafBlockRegistry.dragon_ice ? BlockRenderLayer.TRANSLUCENT : super.getRenderLayer();
    }

    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return this != IafBlockRegistry.dragon_ice;
    }

    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return this != IafBlockRegistry.dragon_ice;
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        if (this == IafBlockRegistry.dragon_ice) {
            if (blockState != iblockstate) {
                return true;
            }
            if (block == this) {
                return false;
            }
        } else {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        return block == this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Deprecated
    public boolean canEntitySpawn(IBlockState state, Entity entityIn) {
        return entityIn instanceof EntityDreadMob || !DragonUtils.isDreadBlock(state);
    }


    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return this == IafBlockRegistry.silverBlock || this == IafBlockRegistry.sapphireBlock;
    }

    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return this == IafBlockRegistry.dragonforge_fire_brick;
    }
}
