package com.github.alexthe666.iceandfire.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.item.block.ItemBlockPodium;

public class BlockPodium extends BlockContainer
{
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPodium.EnumType.class);

	public BlockPodium()
	{
		super(Material.wood);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPodium.EnumType.OAK));
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(SoundType.WOOD);
		this.setCreativeTab(IceAndFire.tab);
		this.setUnlocalizedName("iceandfire.podium");
		GameRegistry.registerBlock(this, ItemBlockPodium.class, "podium");
		GameRegistry.registerTileEntity(TileEntityPodium.class, "podium");
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return new AxisAlignedBB(0.125F, 0, 0.125F, 0.875F, 1.4375F, 0.875F);
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if(worldIn.getTileEntity(pos) instanceof TileEntityPodium){
			TileEntityPodium podium = (TileEntityPodium)worldIn.getTileEntity(pos);
			if(podium.getStackInSlot(0) != null){
				worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, podium.getStackInSlot(0)));
			}
		}
		super.breakBlock(worldIn, pos, state);
	}
	public int damageDropped(IBlockState state)
	{
		return ((BlockPodium.EnumType)state.getValue(VARIANT)).getMetadata();
	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(player.isSneaking()){
			return false;
		}else{
			player.openGui(IceAndFire.instance, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		BlockPodium.EnumType[] aenumtype = BlockPodium.EnumType.values();
		int i = aenumtype.length;

		for (int j = 0; j < i; ++j)
		{
			BlockPodium.EnumType enumtype = aenumtype[j];
			list.add(new ItemStack(itemIn, 1, enumtype.getMetadata()));
		}
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockPodium.EnumType.byMetadata(meta));
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((BlockPodium.EnumType)state.getValue(VARIANT)).getMetadata();
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean isFullCube()
	{
		return false;
	}

	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos.down());
		return iblockstate.isOpaqueCube();
	}

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


	public static enum EnumType implements IStringSerializable
	{
		OAK(0, "oak"),
		SPRUCE(1, "spruce"),
		BIRCH(2, "birch"),
		JUNGLE(3, "jungle"),
		ACACIA(4, "acacia"),
		DARK_OAK(5, "dark_oak", "big_oak");
		private static final BlockPodium.EnumType[] META_LOOKUP = new BlockPodium.EnumType[values().length];
		private final int meta;
		private final String name;
		private final String unlocalizedName;

		private static final String __OBFID = "CL_00002081";

		private EnumType(int meta, String name)
		{
			this(meta, name, name);
		}

		private EnumType(int meta, String name, String unlocalizedName)
		{
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMetadata()
		{
			return this.meta;
		}

		public String toString()
		{
			return this.name;
		}

		public static BlockPodium.EnumType byMetadata(int meta)
		{
			if (meta < 0 || meta >= META_LOOKUP.length)
			{
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName()
		{
			return this.name;
		}

		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}

		static
		{
			BlockPodium.EnumType[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2)
			{
				BlockPodium.EnumType var3 = var0[var2];
				META_LOOKUP[var3.getMetadata()] = var3;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	public int getRenderType(){
		return 3;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityPodium();
	}
}