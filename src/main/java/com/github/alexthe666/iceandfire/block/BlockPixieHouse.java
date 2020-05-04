package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.github.alexthe666.iceandfire.item.ICustomRendered;
import net.minecraft.block.Block;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockPixieHouse extends ContainerBlock implements ICustomRendered {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public Item itemBlock;

    public BlockPixieHouse(String type) {
        super(Material.WOOD);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setDefaultState(this.blockState.getBaseState().with(FACING, EnumFacing.NORTH));
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.pixie_house");
        this.setRegistryName(IceAndFire.MODID, "pixie_house");
        GameRegistry.registerTileEntity(TileEntityPixieHouse.class, "pixie_house");
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(BlockState blockstate) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(BlockState blockstate) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        BlockState BlockState = worldIn.getBlockState(pos.down());
        return BlockState.isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
        dropPixie(worldIn, pos);
        int meta = 0;
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityPixieHouse) {
            meta = ((TileEntityPixieHouse) worldIn.getTileEntity(pos)).houseType;
        }
        spawnAsEntity(worldIn, pos, new ItemStack(IafBlockRegistry.PIXIE_HOUSE, 1, meta));
        super.breakBlock(worldIn, pos, state);
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        this.checkFall(worldIn, pos);
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
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityPixieHouse && ((TileEntityPixieHouse) world.getTileEntity(pos)).hasPixie) {
            ((TileEntityPixieHouse) world.getTileEntity(pos)).releasePixie();
        }
    }

    @SuppressWarnings("deprecation")
    public BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).with(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.get(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, EntityLivingBase placer, ItemStack stack) {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityPixieHouse) {
            ((TileEntityPixieHouse) world.getTileEntity(pos)).houseType = stack.getMetadata();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 6; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityPixieHouse();
    }

    public class ItemBlockPixieHouse extends ItemBlock {
        public ItemBlockPixieHouse(Block block) {
            super(block);
            this.maxStackSize = 1;
            this.setHasSubtypes(true);
        }

        public String getTranslationKey(ItemStack stack) {
            int i = stack.getMetadata();
            return "tile.iceandfire.pixie_house_" + i;
        }
    }
}
