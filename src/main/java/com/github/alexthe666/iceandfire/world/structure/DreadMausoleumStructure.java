package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.Codec;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class DreadMausoleumStructure extends Structure<NoFeatureConfig> {

    public DreadMausoleumStructure(Codec<NoFeatureConfig> p_i51440_1_) {
        super(p_i51440_1_);
        this.setRegistryName("iceandfire:mausoleum");
    }

    public String getStructureName() {
        return IceAndFire.MODID + ":mausoleum";
    }

    public int getSize() {
        return 8;
    }

    public IStartFactory getStartFactory() {
        return DreadMausoleumStructure.Start::new;
    }

    /*protected int getSeedModifier() {
        return 123456789;
    }

    protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator) {
        return Math.max(IafConfig.generateMausoleumChance, 2);
    }

    protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator) {
        return Math.max(IafConfig.generateMausoleumChance / 2, 1);
    }*/

    public static class Start extends StructureStart {
        public Start(Structure<NoFeatureConfig> p_i225806_1_, int p_i225806_2_, int p_i225806_3_, MutableBoundingBox p_i225806_4_, int p_i225806_5_, long p_i225806_6_) {
            super(p_i225806_1_, p_i225806_2_, p_i225806_3_, p_i225806_4_, p_i225806_5_, p_i225806_6_);
        }

        @Override
        public void func_230364_a_(ChunkGenerator p_230364_1_, TemplateManager p_230364_2_, int p_230364_3_, int p_230364_4_, Biome p_230364_5_, IFeatureConfig p_230364_6_) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            BlockPos blockpos = new BlockPos(p_230364_3_ * 16, 64, p_230364_4_ * 16);
            MausoleumPiece.func_204760_a(p_230364_2_, blockpos, rotation, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }

}
