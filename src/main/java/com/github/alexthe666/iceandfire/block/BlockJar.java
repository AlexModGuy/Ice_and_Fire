package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockJar extends BlockContainer {
	private static AxisAlignedBB AABB = new AxisAlignedBB(0.1875F, 0, 0.1875F, 0.8125F, 1F, 0.8125F);
	public Item itemBlock;

	public BlockJar() {
		super(Material.GLASS);
		this.setHardness(1.0F);
		this.setResistance(2.0F);
		this.setSoundType(SoundType.GLASS);
		this.setCreativeTab(IceAndFire.TAB);
		this.setUnlocalizedName("iceandfire.jar");
		this.setRegistryName(IceAndFire.MODID, "jar");
		GameRegistry.registerTileEntity(TileEntityJar.class, "jar");
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState blockstate) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState blockstate) {
		return false;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos.down());
		return iblockstate.isSideSolid(worldIn, pos, EnumFacing.UP);
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		dropPixie(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}

	@SuppressWarnings("deprecation")
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	}

	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		this.checkFall(world, pos);
	}

	private boolean checkFall(World worldIn, BlockPos pos) {
		if (!this.canPlaceBlockAt(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
			dropPixie(worldIn, pos);
			return false;
		} else {
			return true;
		}
	}

	public void dropPixie(World world, BlockPos pos) {
		if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getTileEntity(pos)).hasPixie) {
			((TileEntityJar) world.getTileEntity(pos)).releasePixie();
		}
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getTileEntity(pos)).hasPixie && ((TileEntityJar) world.getTileEntity(pos)).hasProduced) {
			((TileEntityJar) world.getTileEntity(pos)).hasProduced = false;
			EntityItem item = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(ModItems.pixie_dust));
			if (!world.isRemote) {
				world.spawnEntity(item);
			}
			world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, ModSounds.PIXIE_HURT, SoundCategory.NEUTRAL, 1, 1, false);

		}
		return false;
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

	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityJar) {
			if (stack.getMetadata() > 0) {
				((TileEntityJar) world.getTileEntity(pos)).hasPixie = true;
				((TileEntityJar) world.getTileEntity(pos)).pixieType = stack.getMetadata() - 1;
			} else {
				((TileEntityJar) world.getTileEntity(pos)).hasPixie = false;
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityJar();
	}

	public class ItemBlockJar extends ItemBlock {
		public ItemBlockJar(Block block) {
			super(block);
			this.maxStackSize = 1;
			this.setHasSubtypes(true);
		}

		public String getUnlocalizedName(ItemStack stack) {
			int i = stack.getMetadata();
			return stack.getMetadata() == 0 ? "tile.iceandfire.jar" : "tile.iceandfire.jar_" + (i - 1);
		}

		@SideOnly(Side.CLIENT)
		public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
			for (int i = 0; i < 6; i++) {
				subItems.add(new ItemStack(itemIn, 1, i));
			}
		}
	}
}
