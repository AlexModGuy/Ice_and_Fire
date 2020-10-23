package com.github.alexthe666.iceandfire.world.structure;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.serialization.Codec;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class GraveyardStructure extends Structure<NoFeatureConfig> {

    public GraveyardStructure(Codec<NoFeatureConfig> p_i51440_1_) {
        super(p_i51440_1_);
        this.setRegistryName("iceandfire:graveyard");
    }

    public GenerationStage.Decoration func_236396_f_() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public String getStructureName() {
        return IceAndFire.MODID + ":graveyard";
    }

    public int getSize() {
        return 3;
    }

    public IStartFactory getStartFactory() {
        return GraveyardStructure.Start::new;
    }

   /* protected int getSeedModifier() {
        return 123456789;
    }

    protected int getBiomeFeatureDistance(ChunkGenerator<?> chunkGenerator) {
        return 8;// Math.max(IafConfig.spawnGorgonsChance, 2);
    }

    protected int getBiomeFeatureSeparation(ChunkGenerator<?> chunkGenerator) {
        return 4; //Math.max(IafConfig.spawnGorgonsChance / 2, 1);
    }*/

    public static class Start extends StructureStart {
        public Start(Structure<?> p_i225817_1_, int p_i225817_2_, int p_i225817_3_, MutableBoundingBox p_i225817_4_, int p_i225817_5_, long p_i225817_6_) {
            super(p_i225817_1_, p_i225817_2_, p_i225817_3_, p_i225817_4_, p_i225817_5_, p_i225817_6_);
        }

        @Override
        public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager p_230364_3_, int x, int z, Biome p_230364_6_, IFeatureConfig p_230364_7_) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            BlockPos blockpos = new BlockPos(x * 16, 9, z * 16);
            GraveyardPiece.func_204760_a(p_230364_3_, blockpos, rotation, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }

}
