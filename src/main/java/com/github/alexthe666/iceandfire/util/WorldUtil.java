package com.github.alexthe666.iceandfire.util;

import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

/**
 * Class which has world related util functions like chunk load checks
 */
public class WorldUtil {
    /**
     * Checks if the block is loaded for block access
     *
     * @param world world to use
     * @param pos   position to check
     * @return true if block is accessible/loaded
     */
    public static boolean isBlockLoaded(final LevelAccessor world, final BlockPos pos) {
        return isChunkLoaded(world, pos.getX() >> 4, pos.getZ() >> 4);
    }

    /**
     * Returns whether a chunk is fully loaded
     *
     * @param world world to check on
     * @param x     chunk position
     * @param z     chunk position
     * @return true if loaded
     */
    public static boolean isChunkLoaded(final LevelAccessor world, final int x, final int z) {
        if (world.getChunkSource() instanceof ServerChunkCache) {
            final ChunkHolder holder = ((ServerChunkCache) world.getChunkSource()).chunkMap.getVisibleChunkIfPresent(ChunkPos.asLong(x, z));
            if (holder != null) {
                return holder.getFullChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK).left().isPresent();
            }

            return false;
        }
        return world.getChunk(x, z, ChunkStatus.FULL, false) != null;
    }

    /**
     * Mark a chunk at a position dirty if loaded.
     *
     * @param world the world to mark it dirty in.
     * @param pos   the position within the chunk.
     */
    public static void markChunkDirty(final Level world, final BlockPos pos) {
        if (WorldUtil.isBlockLoaded(world, pos)) {
            world.getChunk(pos.getX() >> 4, pos.getZ() >> 4).setUnsaved(true);
            final BlockState state = world.getBlockState(pos);
            world.sendBlockUpdated(pos, state, state, 3);
        }
    }

    /**
     * Returns whether a chunk is fully loaded
     *
     * @param world world to check on
     * @param pos   chunk position
     * @return true if loaded
     */
    public static boolean isChunkLoaded(final LevelAccessor world, final ChunkPos pos) {
        return isChunkLoaded(world, pos.x, pos.z);
    }

    /**
     * Checks if the block is loaded for ticking entities(not all chunks tick entities)
     *
     * @param world world to use
     * @param pos   position to check
     * @return true if block is accessible/loaded
     */
    public static boolean isEntityBlockLoaded(final LevelAccessor world, final BlockPos pos) {
        return isEntityChunkLoaded(world, pos.getX() >> 4, pos.getZ() >> 4);
    }

    /**
     * Returns whether an entity ticking chunk is loaded at the position
     *
     * @param world world to check on
     * @param x     chunk position
     * @param z     chunk position
     * @return true if loaded
     */
    public static boolean isEntityChunkLoaded(final LevelAccessor world, final int x, final int z) {
        return isEntityChunkLoaded(world, new ChunkPos(x, z));
    }

    /**
     * Returns whether an entity ticking chunk is loaded at the position
     *
     * @param world world to check on
     * @param pos   chunk position
     * @return true if loaded
     */
    public static boolean isEntityChunkLoaded(final LevelAccessor world, final ChunkPos pos) {
        if (world instanceof ServerLevel) {
            return isChunkLoaded(world, pos) && ((ServerLevel) world).isPositionEntityTicking(pos.getWorldPosition());
        }
        return isChunkLoaded(world, pos);
    }

    /**
     * Returns whether an axis aligned bb is entirely loaded.
     *
     * @param world world to check on.
     * @param box   the box.
     * @return true if loaded.
     */
    public static boolean isAABBLoaded(final Level world, final AABB box) {
        return isChunkLoaded(world, ((int) box.minX) >> 4, ((int) box.minZ) >> 4) && isChunkLoaded(world, ((int) box.maxX) >> 4, ((int) box.maxZ) >> 4);
    }


    /**
     * Check if it's currently day inn the world.
     *
     * @param world the world to check.
     * @return true if so.
     */
    public static boolean isPastTime(final Level world, final int pastTime) {
        return world.getDayTime() % 24000 <= pastTime;
    }


    /**
     * Check if a world is of the overworld type.
     *
     * @param world the world to check.
     * @return true if so.
     */
    public static boolean isOverworldType(@NotNull final Level world) {
        return isOfWorldType(world, BuiltinDimensionTypes.OVERWORLD);
    }

    /**
     * Check if a world is of the nether type.
     *
     * @param world the world to check.
     * @return true if so.
     */
    public static boolean isNetherType(@NotNull final Level world) {
        return isOfWorldType(world, BuiltinDimensionTypes.NETHER);
    }

    /**
     * Check if a world has a specific dimension type.
     *
     * @param world the world to check.
     * @param type  the type to compare.
     * @return true if it matches.
     */
    public static boolean isOfWorldType(@NotNull final Level world, @NotNull final ResourceKey<DimensionType> type) {
        RegistryAccess dynRegistries = world.registryAccess();
        ResourceLocation loc = dynRegistries.registry(Registries.DIMENSION_TYPE).get().getKey(world.dimensionType());
        if (loc == null) {
            if (world.isClientSide) {
                return world.dimensionType().effectsLocation().equals(type.location());
            }
            return false;
        }
        ResourceKey<DimensionType> regKey = ResourceKey.create(Registries.DIMENSION_TYPE, loc);
        return regKey == type;
    }

    /**
     * Check to see if the world is peaceful.
     * <p>
     * There are several checks performed here, currently both gamerule and difficulty.
     *
     * @param world world to check
     * @return true if peaceful
     */
    public static boolean isPeaceful(@NotNull final Level world) {
        return !world.getLevelData().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) || world.getDifficulty().equals(Difficulty.PEACEFUL);
    }

    /**
     * Returns a dimensions max height
     *
     * @param dimensionType
     * @return
     */
    public static int getDimensionMaxHeight(final DimensionType dimensionType) {
        return dimensionType.logicalHeight() + dimensionType.minY();
    }

    /**
     * Returns a dimension min height
     *
     * @param dimensionType
     * @return
     */
    public static int getDimensionMinHeight(final DimensionType dimensionType) {
        return dimensionType.minY();
    }

    /**
     * Check if a given block y is within world bounds
     *
     * @param yBlock
     * @param world
     * @return
     */
    public static boolean isInWorldHeight(final int yBlock, final Level world) {
        final DimensionType dimensionType = world.dimensionType();
        return yBlock > getDimensionMinHeight(dimensionType) && yBlock < getDimensionMaxHeight(dimensionType);
    }

    public static boolean canGenerate(int configChance, final WorldGenLevel level, final RandomSource random, final BlockPos origin, final String id, boolean checkFluid) {
        return canGenerate(configChance, level, random, origin, id, IafWorldData.FeatureType.SURFACE, checkFluid);
    }

    public static boolean canGenerate(int configChance, final WorldGenLevel level, final RandomSource random, final BlockPos origin, final String id, final IafWorldData.FeatureType type, boolean checkFluid) {
        boolean canGenerate = random.nextInt(configChance) == 0 && IafWorldRegistry.isFarEnoughFromSpawn(level, origin) && IafWorldRegistry.isFarEnoughFromDangerousGen(level, origin, id, type);

        if (canGenerate && checkFluid) {
            if (!level.getFluidState(origin.below()).isEmpty()) {
                return false;
            }
        }

        return canGenerate;
    }
}
