package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.github.alexthe666.iceandfire.world.gen.processor.DreadRuinProcessor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTables;

import java.util.List;
import java.util.Random;

public class GorgonTemplePiece  {
    private static final BlockPos STRUCTURE_OFFSET = new BlockPos(0, 0, 0);
    private static final ResourceLocation PART_1 = new ResourceLocation("iceandfire:gorgon_temple");

    public static void func_204760_a(TemplateManager p_204760_0_, BlockPos p_204760_1_, Rotation p_204760_2_, List<StructurePiece> p_204760_3_, Random p_204760_4_) {
        p_204760_3_.add(new GorgonTemplePiece.Piece(p_204760_0_, PART_1, p_204760_1_, p_204760_2_, p_204760_4_));
    }

    public static class Piece extends TemplateStructurePiece {
        private final Rotation rotation;
        private final ResourceLocation field_204756_e;
        private final Random random;
        private final TemplateManager manager;
        private BlockPos firstPos = null;
        private boolean spawnedGorgon;

        public boolean func_225577_a_(IWorld world, ChunkGenerator<?> p_225577_2_, Random p_225577_3_, MutableBoundingBox p_225577_4_, ChunkPos p_225577_5_) {
            p_225577_4_.expandTo(this.template.getMutableBoundingBox(this.placeSettings, this.templatePosition));
            int i = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, firstPos.getX(), firstPos.getZ());
            this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
            return super.func_225577_a_(world, p_225577_2_, p_225577_3_, p_225577_4_, p_225577_5_);
        }

        public Piece(TemplateManager p_i48904_1_, ResourceLocation p_i48904_2_, BlockPos p_i48904_3_, Rotation p_i48904_4_, Random random) {
            super(IafWorldRegistry.GORGON_PIECE, 0);
            this.templatePosition = p_i48904_3_;
            this.rotation = p_i48904_4_;
            this.field_204756_e = p_i48904_2_;
            this.func_204754_a(p_i48904_1_);
            this.template = p_i48904_1_.getTemplate(PART_1);
            this.random = new Random();
            this.manager = p_i48904_1_;
            this.firstPos = new BlockPos(templatePosition);
        }

        public Piece(TemplateManager p_i50445_1_, CompoundNBT p_i50445_2_) {
            super(IafWorldRegistry.GORGON_PIECE, p_i50445_2_);
            this.field_204756_e = new ResourceLocation(p_i50445_2_.getString("Template"));
            this.rotation = Rotation.valueOf(p_i50445_2_.getString("Rot"));
            this.func_204754_a(p_i50445_1_);
            this.template = p_i50445_1_.getTemplate(PART_1);
            this.random = new Random();
            this.manager = p_i50445_1_;
            this.firstPos = new BlockPos(templatePosition);
        }

        protected void readAdditional(CompoundNBT p_143011_1_) {
            super.readAdditional(p_143011_1_);
            p_143011_1_.putString("Template", this.field_204756_e.toString());
            this.firstPos = new BlockPos(templatePosition);
            p_143011_1_.putString("Rot", this.rotation.name());
        }

        private void func_204754_a(TemplateManager p_204754_1_) {
            Template lvt_2_1_ = p_204754_1_.getTemplateDefaulted(PART_1);
            PlacementSettings lvt_3_1_ = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(lvt_2_1_, this.templatePosition, lvt_3_1_);
        }

        protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {
            if ("iceandfire:spawn_gorgon".equals(function)) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                if(!spawnedGorgon){
                    spawnedGorgon = true;
                    EntityGorgon gorgon = IafEntityRegistry.GORGON.create(worldIn.getWorld());
                    gorgon.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.SPAWNER, null, null);
                    gorgon.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    gorgon.enablePersistence();
                    worldIn.addEntity(gorgon);
                }

            }
        }
    }


}
