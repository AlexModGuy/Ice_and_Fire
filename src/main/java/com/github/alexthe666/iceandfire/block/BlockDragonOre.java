package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDragonOre extends Block {
    public Item itemBlock;

    public BlockDragonOre(int toollevel, float hardness, float resistance, String name, String gameName) {
        super(Material.ROCK);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setHarvestLevel("pickaxe", toollevel);
        this.setResistance(resistance);
        this.setHardness(hardness);
        this.setTranslationKey(name);
        setRegistryName(IceAndFire.MODID, gameName);

    }

    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(this.getBlockState().getValidStates().iterator().next(), random, fortune)) {
            int i = random.nextInt(fortune + 2) - 1;

            if (i < 0) {
                i = 0;
            }

            return this.quantityDropped(random) * (i + 1);
        } else {
            return this.quantityDropped(random);
        }
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
            if (this == IafBlockRegistry.sapphireOre) {
                return MathHelper.getInt(rand, 3, 7);
            }
        }
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this == IafBlockRegistry.sapphireOre ? IafItemRegistry.sapphireGem : Item.getItemFromBlock(IafBlockRegistry.silverOre);
    }
}
