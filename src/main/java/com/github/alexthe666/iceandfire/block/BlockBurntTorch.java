package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockBurntTorch extends BlockTorch implements IDreadBlock {

    public BlockBurntTorch() {
        super();
        this.setTranslationKey("iceandfire.burnt_torch");
        this.setHardness(0F);
        this.setResistance(0F);
        this.setLightLevel(0.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        setRegistryName(IceAndFire.MODID, "burnt_torch");
    }

    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.TORCH);
    }

    @OnlyIn(Dist.CLIENT)
    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

    }
}