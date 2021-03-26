package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class DummyPiece extends TemplateStructurePiece {
    // Register to the registry name of the old structure piece removed to prevent logspam for players in existing worlds.

    public DummyPiece(TemplateManager p_i48904_1_, ResourceLocation p_i48904_2_, BlockPos p_i48904_3_, Rotation p_i48904_4_, Random random) {
        super(IafWorldRegistry.DUMMY_PIECE, 0);
    }

    public DummyPiece(TemplateManager p_i50445_1_, CompoundNBT p_i50445_2_) {
        super(IafWorldRegistry.DUMMY_PIECE, p_i50445_2_);
    }

    protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
    }
}
