package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class DreadRuinProcessor extends StructureProcessor {

    public static final DreadRuinProcessor INSTANCE = new DreadRuinProcessor();
    public static final Codec<DreadRuinProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public DreadRuinProcessor() {
    }

    public static BlockState getRandomCrackedBlock(@Nullable BlockState prev, RandomSource random) {
        float rand = random.nextFloat();
        if (rand < 0.5) {
            return IafBlockRegistry.DREAD_STONE_BRICKS.get().defaultBlockState();
        } else if (rand < 0.9) {
            return IafBlockRegistry.DREAD_STONE_BRICKS_CRACKED.get().defaultBlockState();
        } else {
            return IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.get().defaultBlockState();
        }
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader worldReader, @NotNull BlockPos pos, @NotNull BlockPos pos2, StructureTemplate.@NotNull StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        RandomSource random = settings.getRandom(infoIn2.pos());

        if (infoIn2.state().getBlock() == IafBlockRegistry.DREAD_STONE_BRICKS.get()) {
            BlockState state = getRandomCrackedBlock(null, random);
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), state, null);
        }
        if (infoIn2.state().getBlock() == IafBlockRegistry.DREAD_SPAWNER.get()) {
            CompoundTag tag = new CompoundTag();
            CompoundTag spawnData = new CompoundTag();
            ResourceLocation spawnerMobId = ForgeRegistries.ENTITY_TYPES.getKey(getRandomMobForMobSpawner(random));
            if (spawnerMobId != null) {
                CompoundTag entity = new CompoundTag();
                entity.putString("id", spawnerMobId.toString());
                spawnData.put("entity", entity);
                tag.remove("SpawnPotentials");
                tag.put("SpawnData", spawnData.copy());
            }
            StructureTemplate.StructureBlockInfo newInfo = new StructureTemplate.StructureBlockInfo(infoIn2.pos(), IafBlockRegistry.DREAD_SPAWNER.get().defaultBlockState(), tag);
            return newInfo;

        }
        return infoIn2;

    }

    @Override
    protected @NotNull StructureProcessorType getType() {
        return IafProcessors.DREADRUINPROCESSOR.get();
    }

    private EntityType getRandomMobForMobSpawner(RandomSource random) {
        float rand = random.nextFloat();
        if (rand < 0.3D) {
            return IafEntityRegistry.DREAD_THRALL.get();
        } else if (rand < 0.5D) {
            return IafEntityRegistry.DREAD_GHOUL.get();
        } else if (rand < 0.7D) {
            return IafEntityRegistry.DREAD_BEAST.get();
        } else if (rand < 0.85D) {
            return IafEntityRegistry.DREAD_SCUTTLER.get();
        }
        return IafEntityRegistry.DREAD_KNIGHT.get();
    }
}
