package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.Random;

public class VillageHouseProcessor extends StructureProcessor {

    public static final ResourceLocation LOOT = new ResourceLocation("iceandfire", "chest/village_scribe");
    public static final VillageHouseProcessor INSTANCE = new VillageHouseProcessor();
    public static final Codec<VillageHouseProcessor> CODEC = Codec.unit(() -> INSTANCE);
    public VillageHouseProcessor() {
    }

    public StructureTemplate.StructureBlockInfo process(LevelReader worldReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        Random random = settings.getRandom(infoIn2.pos);
        if (infoIn2.state.getBlock() == Blocks.CHEST) {
            CompoundTag tag = new CompoundTag();
            tag.putString("LootTable", LOOT.toString());
            tag.putLong("LootTableSeed", random.nextLong());
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos, infoIn2.state, tag);
        }
        return infoIn2;
    }


    @Override
    protected StructureProcessorType getType() {
        return IafProcessors.VILLAGEHOUSEPROCESSOR;
    }

}
