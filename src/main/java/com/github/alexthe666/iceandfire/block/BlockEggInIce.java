package com.github.alexthe666.iceandfire.block;

import java.util.Random;

import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEggInIce extends BlockContainer {

	public BlockEggInIce() {
		super(Material.ice);
		this.slipperiness = 0.98F;
		this.setHardness(0.5F);
		this.setLightOpacity(3);
		this.setStepSound(soundTypeGlass);
		this.setUnlocalizedName("iceandfire.egginice");
		GameRegistry.registerBlock(this, "egginice");
		GameRegistry.registerTileEntity(TileEntityEggInIce.class, "eggInIce");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityEggInIce();
	}

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return Item.getItemFromBlock(Blocks.ice);
    }
    
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}
	
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
            if (worldIn.getBlockState(pos.offset(side.getOpposite())) != iblockstate)
            {
                return true;
            }

            if (block == this)
            {
                return false;
            }

        return block == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }
	
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
	{
		player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
		player.addExhaustion(0.025F);
		if (worldIn.provider.doesWaterVaporize())
		{
			worldIn.setBlockToAir(pos);
			return;
		}

		int i = EnchantmentHelper.getFortuneModifier(player);
		harvesters.set(player);
		this.dropBlockAsItem(worldIn, pos, state, i);
		harvesters.set(null);
		Material material = worldIn.getBlockState(pos.down()).getBlock().getMaterial();

		if(worldIn.getTileEntity(pos) != null){
			if(worldIn.getTileEntity(pos) instanceof TileEntityEggInIce){
				TileEntityEggInIce tile = (TileEntityEggInIce)worldIn.getTileEntity(pos);
				tile.spawnEgg();
			}
		}
		
		if (material.blocksMovement() || material.isLiquid())
		{
			worldIn.setBlockState(pos, Blocks.flowing_water.getDefaultState());
			
		}
	}
	

	public int quantityDropped(Random random)
	{
		return 0;
	}

	public int getMobilityFlag()
	{
		return 0;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean isFullCube()
	{
		return false;
	}
	
	public int getRenderType(){
		return 3;
	}
}
