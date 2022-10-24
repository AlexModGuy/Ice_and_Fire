package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.PIXIE_JAR;

public class BlockJar extends BaseEntityBlock {
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

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AABB;
    }


    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        dropPixie(worldIn, pos);
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public void dropPixie(Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getBlockEntity(pos)).hasPixie) {
            ((TileEntityJar) world.getBlockEntity(pos)).releasePixie();
        }
    }

    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult resultIn) {
        if (!empty && world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getBlockEntity(pos)).hasPixie && ((TileEntityJar) world.getBlockEntity(pos)).hasProduced) {
            ((TileEntityJar) world.getBlockEntity(pos)).hasProduced = false;
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(IafItemRegistry.PIXIE_DUST.get()));
            if (!world.isClientSide) {
                world.addFreshEntity(item);
            }
            world.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundSource.NEUTRAL, 1, 1, false);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }


    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (world.getBlockEntity(pos) instanceof TileEntityJar) {
            TileEntityJar jar = ((TileEntityJar) world.getBlockEntity(pos));
            if (!empty) {
                jar.hasPixie = true;
                jar.pixieType = pixieType;
            } else {
                jar.hasPixie = false;
            }
            jar.setChanged();
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return level.isClientSide ? createTickerHelper(entityType, PIXIE_JAR.get(), TileEntityJar::tick) : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityJar(pos, state, empty);
    }
}
