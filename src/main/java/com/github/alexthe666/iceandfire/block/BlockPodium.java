package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.item.ICustomRendered;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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

public class BlockPodium extends BlockContainer implements ICustomRendered {
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPodium.EnumType.class);
    public Item itemBlock;

    public BlockPodium() {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPodium.EnumType.OAK));
        this.setHardness(2.0F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.podium");
        this.setRegistryName(IceAndFire.MODID, "podium");
        GameRegistry.registerTileEntity(TileEntityPodium.class, "podium");
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.125F, 0, 0.125F, 0.875F, 1.4375F, 0.875F);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.getTileEntity(pos) instanceof TileEntityPodium) {
            TileEntityPodium podium = (TileEntityPodium) worldIn.getTileEntity(pos);
            if (!podium.getStackInSlot(0).isEmpty()) {
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, podium.getStackInSlot(0)));
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ((BlockPodium.EnumType) state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            return false;
        } else {
            playerIn.openGui(IceAndFire.INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.UP) {
            return BlockFaceShape.SOLID;
        } else if (face == EnumFacing.DOWN) {
            return BlockFaceShape.SOLID;
        } else {
            return BlockFaceShape.UNDEFINED;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        BlockPodium.EnumType[] aenumtype = BlockPodium.EnumType.values();
        int i = aenumtype.length;

        for (EnumType enumtype : aenumtype) {
            items.add(new ItemStack(this, 1, enumtype.getMetadata()));
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, BlockPodium.EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockPodium.EnumType) state.getValue(VARIANT)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
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

    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.checkFall(worldIn, pos);
    }

    private boolean checkFall(World worldIn, BlockPos pos) {
        if (!this.canPlaceBlockAt(worldIn, pos)) {
            worldIn.destroyBlock(pos, true);
            return false;
        } else {
            return true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityPodium();
    }

    public enum EnumType implements IStringSerializable {
        OAK(0, "oak"), SPRUCE(1, "spruce"), BIRCH(2, "birch"), JUNGLE(3, "jungle"), ACACIA(4, "acacia"), DARK_OAK(5, "dark_oak", "big_oak");
        private static final BlockPodium.EnumType[] META_LOOKUP = new BlockPodium.EnumType[values().length];
        private static final String __OBFID = "CL_00002081";

        static {
            BlockPodium.EnumType[] var0 = values();
            int var1 = var0.length;

            for (EnumType var3 : var0) {
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }

        private final int meta;
        private final String name;
        private final String unlocalizedName;

        EnumType(int meta, String name) {
            this(meta, name, name);
        }

        EnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public static BlockPodium.EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata() {
            return this.meta;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }
    }
}