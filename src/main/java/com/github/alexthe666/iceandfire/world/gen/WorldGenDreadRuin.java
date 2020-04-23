package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.processor.DreadRuinProcessor;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class WorldGenDreadRuin extends WorldGenerator {
    private static final ResourceLocation STRUCTURE_0 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_0");
    private static final ResourceLocation STRUCTURE_1 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_1");
    private static final ResourceLocation STRUCTURE_2 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_2");
    private static final ResourceLocation STRUCTURE_3 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_3");
    private static final ResourceLocation STRUCTURE_4 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_4");
    private static final ResourceLocation STRUCTURE_5 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_5");
    private static final ResourceLocation STRUCTURE_6 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_6");
    private static final ResourceLocation STRUCTURE_7 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_7");
    private static final ResourceLocation STRUCTURE_8 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_8");
    private static final ResourceLocation STRUCTURE_9 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_9");
    private static final ResourceLocation STRUCTURE_10 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_10");
    private static final ResourceLocation STRUCTURE_11 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_11");
    private static final ResourceLocation STRUCTURE_12 = new ResourceLocation(IceAndFire.MODID, "dread_ruin_12");

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        ResourceLocation structure = getRandomStructure(rand);
        EnumFacing facing = EnumFacing.HORIZONTALS[rand.nextInt(3)];
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(getRotationFromFacing(facing)).setReplacedBlock(Blocks.AIR);
        Template template = templateManager.getTemplate(server, structure);
        Biome biome = worldIn.getBiome(position);
        BlockPos genPos = position.offset(facing, template.getSize().getZ()/2).offset(facing.rotateYCCW(), template.getSize().getX()/2);
        template.addBlocksToWorld(worldIn, genPos, new DreadRuinProcessor(position, settings, biome), settings, 2);
        return true;
    }

    public static Rotation getRotationFromFacing(EnumFacing facing) {
        switch (facing) {
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }


    private ResourceLocation getRandomStructure(Random rand){
        switch (rand.nextInt(11)){
            case 0:
                return STRUCTURE_0;
            case 1:
                return STRUCTURE_1;
            case 2:
                return STRUCTURE_2;
            case 3:
                return STRUCTURE_3;
            case 4:
                return STRUCTURE_4;
            case 5:
                return STRUCTURE_5;
            case 6:
                return STRUCTURE_6;
            case 7:
                return STRUCTURE_7;
            case 8:
                return STRUCTURE_8;
            case 9:
                return STRUCTURE_9;
            case 10:
                return STRUCTURE_10;
            case 11:
                return STRUCTURE_11;
            case 12:
                return STRUCTURE_12;
        }
        return STRUCTURE_0;
    }
}