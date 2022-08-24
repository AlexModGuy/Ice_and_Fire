package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockEggInIce extends ContainerBlock {
    public Item itemBlock;

    @SuppressWarnings("deprecation")
    public BlockEggInIce() {
        super(
    		Properties
                .of(Material.ICE)
                .noOcclusion()
                .dynamicShape()
                .strength(0.5F)
                .dynamicShape()
    			.sound(SoundType.GLASS)
		);

        setRegistryName(IceAndFire.MODID, "egginice");
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new TileEntityEggInIce();
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void playerDestroy(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
    }

    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (worldIn.getBlockEntity(pos) != null) {
            if (worldIn.getBlockEntity(pos) instanceof TileEntityEggInIce) {
                TileEntityEggInIce tile = (TileEntityEggInIce) worldIn.getBlockEntity(pos);
                tile.spawnEgg();
            }
        }
    }

}
