package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import thaumcraft.api.crafting.IInfusionStabiliser;

import java.util.Random;

@Optional.Interface(iface = "thaumcraft.api.crafting.IInfusionStabiliser", modid = "thaumcraft")
public class BlockElementalFlower extends BlockBush implements IInfusionStabiliser {
    public Item itemBlock;

    public BlockElementalFlower(boolean isFire) {
        this.setTickRandomly(true);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey(isFire ? "iceandfire.fire_lily" : "iceandfire.frost_lily");
        setRegistryName(IceAndFire.MODID, isFire ? "fire_lily" : "frost_lily");
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && canStay(worldIn, pos);
    }

    public boolean canStay(World worldIn, BlockPos pos) {
        IBlockState soil = worldIn.getBlockState(pos.down());
        if (this == IafBlockRegistry.fire_lily) {
            return soil.getMaterial() == Material.SAND || soil.getBlock() == Blocks.NETHERRACK;
        } else {
            return soil.getMaterial() == Material.PACKED_ICE || soil.getMaterial() == Material.ICE;
        }
    }

    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, 1);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.checkFall(worldIn, pos);
    }

    private boolean checkFall(World worldIn, BlockPos pos) {
        if (!this.canStay(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            return false;
        } else {
            return true;
        }
    }

    protected boolean canSustainBush(IBlockState state) {
        return true;
    }

    @Override
    @Optional.Method(modid = "thaumcraft")
    public boolean canStabaliseInfusion(World world, BlockPos pos) {
        return true;
    }
}
