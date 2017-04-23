package com.github.alexthe666.iceandfire.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockEggInIce extends BlockContainer {
    public Item itemBlock;

    public BlockEggInIce() {
        super(Material.ICE);
        this.slipperiness = 0.98F;
        this.setHardness(0.5F);
        this.setLightOpacity(3);
        this.setSoundType(SoundType.GLASS);
        this.setUnlocalizedName("iceandfire.egginice");
        GameRegistry.registerTileEntity(TileEntityEggInIce.class, "eggInIce");
        setRegistryName(IceAndFire.MODID, "egginice");
        GameRegistry.register(this);
        GameRegistry.register(itemBlock = (new ItemBlock(this).setRegistryName(this.getRegistryName())));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEggInIce();
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.ICE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockstate, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate) {
            return true;
        }

        if (block == this) {
            return false;
        }

        return block == this ? false : super.shouldSideBeRendered(iblockstate, worldIn, pos, side);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        if (worldIn.getTileEntity(pos) != null) {
            if (worldIn.getTileEntity(pos) instanceof TileEntityEggInIce) {
                TileEntityEggInIce tile = (TileEntityEggInIce) worldIn.getTileEntity(pos);
                tile.spawnEgg();
            }
        }
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
            java.util.List<ItemStack> items = new java.util.ArrayList<ItemStack>();
            ItemStack itemstack = new ItemStack(Blocks.ICE, 1);

            if (!itemstack.isEmpty()) {
                items.add(itemstack);
            }

            net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
            for (ItemStack is : items)
                spawnAsEntity(worldIn, pos, is);
        } else {
            if (worldIn.provider.doesWaterVaporize()) {
                worldIn.setBlockToAir(pos);
                return;
            }

            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            harvesters.set(player);
            this.dropBlockAsItem(worldIn, pos, state, i);
            harvesters.set(null);
            Material material = worldIn.getBlockState(pos.down()).getMaterial();

            if (material.blocksMovement() || material.isLiquid()) {
                worldIn.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState());
            }
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    public int getMobilityFlag() {
        return 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockstate) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState blockstate) {
        return false;
    }

    public int getRenderType() {
        return 3;
    }
}
