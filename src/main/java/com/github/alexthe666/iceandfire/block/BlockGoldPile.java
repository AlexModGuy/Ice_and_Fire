package com.github.alexthe666.iceandfire.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.structures.StructureUtils;

public class BlockGoldPile extends Block
{
	public static final PropertyInteger LAYERS = PropertyInteger.create("layers", 1, 8);

	public BlockGoldPile()
	{
		super(Material.ground);
		GameRegistry.registerBlock(this, "goldpile");
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(IceAndFire.tab);
		this.setUnlocalizedName("iceandfire.goldpile");
		this.setBlockBoundsForItemRender();
		this.setHardness(0.3F);
	}

	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return false;
	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		ItemStack item = playerIn.inventory.getCurrentItem();

		if(item != null){
			if(item.getItem() != null){
				if(item.getItem() == Items.gold_nugget|| item.getItem() == Item.getItemFromBlock(ModBlocks.goldPile)){
					if(item != null){
						if(this.getMetaFromState(state) < 7){
							StructureUtils.setBlock(worldIn, pos.getX(), pos.getY(), pos.getZ(), ModBlocks.goldPile, this.getMetaFromState(state) + 1, 3);
							if (!playerIn.capabilities.isCreativeMode)
							{
								--item.stackSize;

								if (item.stackSize <= 0)
								{
									playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
								}
							}
							return true;
						}
					}
				}
			}
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
	}
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		int i = ((Integer)state.getValue(LAYERS)).intValue();
		float f = 0.125F;
		return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)((float)pos.getY() + (float)i * f), (double)pos.getZ() + this.maxZ);
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean isFullCube()
	{
		return false;
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	public void setBlockBoundsForItemRender()
	{
		this.getBoundsForLayers(0);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		this.getBoundsForLayers(((Integer)iblockstate.getValue(LAYERS)).intValue());
	}

	protected void getBoundsForLayers(int p_150154_1_)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, (float)p_150154_1_ / 8.0F, 1.0F);
	}
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos.down());
		Block block = iblockstate.getBlock();
		return (block == this && !(((Integer)iblockstate.getValue(LAYERS)).intValue() == 7) ? true : block.isOpaqueCube());
	}

	/**
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		this.checkAndDropBlock(worldIn, pos, state);
	}

	private boolean checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!this.canPlaceBlockAt(worldIn, pos))
		{
			worldIn.destroyBlock(pos, true);
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 *  
	 * @param fortune the level of the Fortune enchantment on the player's tool
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.gold_nugget;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(LAYERS, Integer.valueOf((meta & 7) + 1));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		return ((Integer)state.getValue(LAYERS)).intValue() - 1;
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {LAYERS});
	}

	@Override public int quantityDropped(IBlockState state, int fortune, Random random){ return ((Integer)state.getValue(LAYERS)) + 1; }
}