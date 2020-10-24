package com.github.alexthe666.iceandfire.world.structure;

import java.util.List;
import java.util.Random;

import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.github.alexthe666.iceandfire.world.gen.processor.GraveyardProcessor;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class GraveyardPiece  {
    private static final BlockPos STRUCTURE_OFFSET = new BlockPos(0, 0, 0);
    private static final ResourceLocation PART_1 = new ResourceLocation("iceandfire:graveyard");

    public static void func_204760_a(TemplateManager p_204760_0_, BlockPos p_204760_1_, Rotation p_204760_2_, List<StructurePiece> p_204760_3_, Random p_204760_4_) {
        p_204760_3_.add(new GraveyardPiece.Piece(p_204760_0_, PART_1, p_204760_1_, p_204760_2_, p_204760_4_));
    }

    public static class Piece extends TemplateStructurePiece {
        private final Rotation rotation;
        private final ResourceLocation field_204756_e;
        private boolean offsetOnce = false;

        public boolean func_230383_a_(ISeedReader world, StructureManager p_230383_2_, ChunkGenerator p_230383_3_, Random p_230383_4_, MutableBoundingBox p_230383_5_, ChunkPos p_230383_6_, BlockPos p_230383_7_) {
            if (!offsetOnce) {
                p_230383_5_.expandTo(this.template.getMutableBoundingBox(this.placeSettings, this.templatePosition));
            	this.offset(0, -4, 0);
            	offsetOnce = true;
            }
            return super.func_230383_a_(world, p_230383_2_, p_230383_3_, p_230383_4_, p_230383_5_, p_230383_6_, p_230383_7_);
        }

        public Piece(TemplateManager p_i48904_1_, ResourceLocation p_i48904_2_, BlockPos p_i48904_3_, Rotation p_i48904_4_, Random random) {
            super(IafWorldRegistry.GRAVEYARD_PIECE, 0);
            this.templatePosition = p_i48904_3_;
            this.rotation = p_i48904_4_;
            this.field_204756_e = p_i48904_2_;
            this.func_204754_a(p_i48904_1_);
            this.template = p_i48904_1_.getTemplate(PART_1);
        }

        public Piece(TemplateManager p_i50445_1_, CompoundNBT p_i50445_2_) {
            super(IafWorldRegistry.GRAVEYARD_PIECE, p_i50445_2_);
            this.field_204756_e = new ResourceLocation(p_i50445_2_.getString("Template"));
            this.rotation = Rotation.valueOf(p_i50445_2_.getString("Rot"));
            this.func_204754_a(p_i50445_1_);
            this.template = p_i50445_1_.getTemplate(PART_1);
        }

        protected void readAdditional(CompoundNBT p_143011_1_) {
            super.readAdditional(p_143011_1_);
            p_143011_1_.putString("Template", this.field_204756_e.toString());
            p_143011_1_.putString("Rot", this.rotation.name());
        }

        private void func_204754_a(TemplateManager p_204754_1_) {
            Template lvt_2_1_ = p_204754_1_.getTemplateDefaulted(PART_1);
            PlacementSettings lvt_3_1_ = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(new GraveyardProcessor());
            this.setup(lvt_2_1_, this.templatePosition, lvt_3_1_);
        }


        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
        }
    }


}
