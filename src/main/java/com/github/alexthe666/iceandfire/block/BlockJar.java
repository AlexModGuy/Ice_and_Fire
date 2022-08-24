package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.item.ICustomRendered;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockJar extends ContainerBlock implements ICustomRendered {
    protected static final VoxelShape AABB = Block.box(3, 0, 3, 13, 16, 13);
    public Item itemBlock;
    private final boolean empty;
    private final int pixieType;

    public BlockJar(int pixieType) {
        super(
            pixieType != -1 ?
                Properties
                    .of(Material.GLASS)
                    .noOcclusion()
                    .dynamicShape()
                    .strength(1, 2)
                    .sound(SoundType.GLASS)
                    .lightLevel((state) -> {
                        return pixieType == -1 ? 0 : 10;
                    })
                    .dropsLike(IafBlockRegistry.JAR_EMPTY)
				: Properties
                .of(Material.GLASS)
                .noOcclusion()
                .dynamicShape()
                .strength(1, 2)
					.sound(SoundType.GLASS)
		);

        this.empty = pixieType == -1;
        this.pixieType = pixieType;
        if (empty) {
            this.setRegistryName(IceAndFire.MODID, "pixie_jar_empty");
        } else {
            this.setRegistryName(IceAndFire.MODID, "pixie_jar_" + pixieType);
        }
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }


    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        dropPixie(worldIn, pos);
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public void dropPixie(World world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getBlockEntity(pos)).hasPixie) {
            ((TileEntityJar) world.getBlockEntity(pos)).releasePixie();
        }
    }

    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult resultIn) {
        if (!empty && world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getBlockEntity(pos)).hasPixie && ((TileEntityJar) world.getBlockEntity(pos)).hasProduced) {
            ((TileEntityJar) world.getBlockEntity(pos)).hasProduced = false;
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(IafItemRegistry.PIXIE_DUST));
            if (!world.isClientSide) {
                world.addFreshEntity(item);
            }
            world.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundCategory.NEUTRAL, 1, 1, false);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }


    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar) {
            if (!empty) {
                ((TileEntityJar) world.getBlockEntity(pos)).hasPixie = true;
                ((TileEntityJar) world.getBlockEntity(pos)).pixieType = pixieType;
            } else {
                ((TileEntityJar) world.getBlockEntity(pos)).hasPixie = false;
            }
            world.getBlockEntity(pos).setChanged();
        }
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new TileEntityJar(empty);
    }
}
