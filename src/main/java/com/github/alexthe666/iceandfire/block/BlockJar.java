package com.github.alexthe666.iceandfire.block;

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
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

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
                    .of()
                    .mapColor(MapColor.NONE)
                    .instrument(NoteBlockInstrument.HAT)
                    .noOcclusion()
                    .dynamicShape()
                    .strength(1, 2)
                    .sound(SoundType.GLASS)
                    .lightLevel((state) -> pixieType == -1 ? 0 : 10)
                    .dropsLike(IafBlockRegistry.JAR_EMPTY.get())
				: Properties
                .of()
                .mapColor(MapColor.NONE)
                .instrument(NoteBlockInstrument.HAT)
                .noOcclusion()
                .dynamicShape()
                .strength(1, 2)
					.sound(SoundType.GLASS)
		);

        this.empty = pixieType == -1;
        this.pixieType = pixieType;
    }

    static String name(int pixieType) {
        if (pixieType == -1)
            return "pixie_jar_empty";
        return "pixie_jar_%d".formatted(pixieType);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABB;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABB;
    }


    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        dropPixie(worldIn, pos);
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    public void dropPixie(Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof TileEntityJar && ((TileEntityJar) world.getBlockEntity(pos)).hasPixie) {
            ((TileEntityJar) world.getBlockEntity(pos)).releasePixie();
        }
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult resultIn) {
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


    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level world, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
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
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return createTickerHelper(entityType, PIXIE_JAR.get(), TileEntityJar::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityJar(pos, state, empty);
    }
}
