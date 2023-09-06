package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;

public class DummyPiece extends TemplateStructurePiece {
    // Register to the registry name of the old structure piece removed to prevent logspam for players in existing worlds.

    public DummyPiece(StructureTemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation) {
        super(IafWorldRegistry.DUMMY_PIECE, 0, manager, location, location.toString(), makeSettings(rotation), pos);
    }

    public DummyPiece(StructureTemplateManager structureManager, CompoundTag tag) {
        super(IafWorldRegistry.DUMMY_PIECE, tag, structureManager, (p_163217_) -> makeSettings(Rotation.valueOf(tag.getString("Rot"))));
    }

/*
    public static StructurePieceType setTemplatePieceId(StructurePieceType.StructureTemplateType p_210156_, String p_210157_) {
        return Registry.register(Registries.STRUCTURE_PIECE, p_210157_, p_210156_);
    }
*/

    // Sets up various templateStructurePiece variables these aren't necessarily needed
    // but are included as a backup to avoid crashes
    // Code stems from ShipwreckPieces.class

    private static StructurePlaceSettings makeSettings(Rotation p_163214_) {
        return (new StructurePlaceSettings()).setRotation(p_163214_).setMirror(Mirror.NONE).setRotationPivot(new BlockPos(0, 0, 0)).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
    }


/*    @Override
    public void postProcess(@NotNull WorldGenLevel p_230383_1_, @NotNull StructureFeatureManager p_230383_2_, @NotNull ChunkGenerator p_230383_3_, @NotNull Random p_230383_4_, @NotNull BoundingBox p_230383_5_, @NotNull ChunkPos p_230383_6_, @NotNull BlockPos p_230383_7_) {
        // Override post processing function since we don't have to do any for this dummy piece
    }*/

    @Override
    protected void handleDataMarker(@NotNull String pName, @NotNull BlockPos pPos, @NotNull ServerLevelAccessor pLevel, @NotNull RandomSource pRandom, @NotNull BoundingBox pBox) {

    }
}
