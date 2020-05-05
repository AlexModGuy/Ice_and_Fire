package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.item.ICustomRendered;
import net.minecraft.block.Block;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockState;
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

public class BlockJar extends ContainerBlock implements ICustomRendered {
    private static AxisAlignedBB AABB = new AxisAlignedBB(0.1875F, 0, 0.1875F, 0.8125F, 1F, 0.8125F);
    public Item itemBlock;
    private boolean empty;

    public BlockJar(boolean empty) {
        super(Material.GLASS);
        this.setHardness(1.0F);
        this.setResistance(2.0F);
        this.setSoundType(SoundType.GLASS);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.jar" + (empty ? "_empty" : "_pixie"));
        this.setRegistryName(IceAndFire.MODID, "jar" + (empty ? "_empty" : "_pixie"));
        if (!empty) {
            this.setLightLevel(0.75F);
            GameRegistry.registerTileEntity(TileEntityJar.class, "jar");
        }
        this.empty = empty;
    }

    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.MIDDLE_POLE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
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
        return BlockState.isSideSolid(worldIn, pos, EnumFacing.UP) || BlockState.getBlock().canPlaceTorchOnTop(BlockState, worldIn, pos);
    }

    public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
        dropPixie(worldIn, pos);
        super.breakBlock(worldIn, pos, state);
    }

    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void updateTick(World world, BlockPos pos, BlockState state, Random rand) {
        //this.checkFall(world, pos);
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

    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(IafBlockRegistry.JAR_EMPTY);
    }

    protected ItemStack getSilkTouchDrop(BlockState state) {
        return new ItemStack(IafBlockRegistry.JAR_EMPTY);
    }

    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!empty && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getTileEntity(pos)).hasPixie && ((TileEntityJar) world.getTileEntity(pos)).hasProduced) {
            ((TileEntityJar) world.getTileEntity(pos)).hasProduced = false;
            EntityItem item = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(IafItemRegistry.PIXIE_DUST));
            if (!world.isRemote) {
                world.spawnEntity(item);
            }
            world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundCategory.NEUTRAL, 1, 1, false);

        }
        return false;
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, EntityLivingBase placer, ItemStack stack) {
        if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityJar) {
            if (!empty) {
                ((TileEntityJar) world.getTileEntity(pos)).hasPixie = true;
                ((TileEntityJar) world.getTileEntity(pos)).pixieType = stack.getMetadata();
            } else {
                ((TileEntityJar) world.getTileEntity(pos)).hasPixie = false;
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityJar(empty);
    }

    public class ItemBlockJar extends ItemBlock {
        public ItemBlockJar(Block block) {
            super(block);
            this.maxStackSize = 1;
            this.setHasSubtypes(true);
        }

        public String getUnlocalizedName(ItemStack stack) {
            int i = stack.getMetadata();
            return "tile.iceandfire.jar_" + i;
        }

        @OnlyIn(Dist.CLIENT)
        public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
            if (this.isInCreativeTab(tab)) {
                for (int i = 0; i < 5; i++) {
                    subItems.add(new ItemStack(this, 1, i));
                }
            }
        }
    }
}
