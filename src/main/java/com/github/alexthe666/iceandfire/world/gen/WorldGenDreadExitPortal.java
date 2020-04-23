package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.gen.processor.DreadPortalProcessor;
import net.minecraft.server.MinecraftServer;
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

public class WorldGenDreadExitPortal extends WorldGenerator {
    private static final ResourceLocation STRUCTURE = new ResourceLocation(IceAndFire.MODID, "dread_exit_portal");

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(Rotation.NONE);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        Biome biome = worldIn.getBiome(position);
        template.addBlocksToWorld(worldIn, position, new DreadPortalProcessor(position, settings, biome), settings, 2);
        return true;
    }
}