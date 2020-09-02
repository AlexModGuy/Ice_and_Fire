package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorldGenMobSpawn extends Feature<NoFeatureConfig> {

    public WorldGenMobSpawn(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos position, NoFeatureConfig config) {
        position = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, position.add(8, 0, 8));
        BlockPos oceanPos = worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, position.add(8, 0, 8));
        Biome biome = worldIn.getBiome(position);
        if (IafConfig.spawnDeathWorm && IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)) {
            if (rand.nextInt(IafConfig.deathWormSpawnRate + 1) == 0) {
                EntityDeathWorm deathWorm = IafEntityRegistry.DEATH_WORM.create(worldIn.getWorld());
                deathWorm.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                deathWorm.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(position), SpawnReason.CHUNK_GENERATION, null, null);
                worldIn.addEntity(deathWorm);
            }

        }

        if (IafConfig.generateWanderingCyclops && IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS)) {
            if (rand.nextInt(IafConfig.spawnWanderingCyclopsChance + 1) == 0) {
                EntityCyclops cyclops = IafEntityRegistry.CYCLOPS.create(worldIn.getWorld());
                cyclops.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                cyclops.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(position), SpawnReason.SPAWNER, null, null);
                cyclops.setVariant(rand.nextInt(3));
                for (int i = 0; i < 3 + rand.nextInt(3); i++) {
                    SheepEntity sheep = EntityType.SHEEP.create(worldIn.getWorld());
                    sheep.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                    sheep.setFleeceColor(SheepEntity.getRandomSheepColor(rand));
                    worldIn.addEntity(sheep);
                }
            }
        }

        if (IafConfig.generateDragonSkeletons) {
            if ((BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)) && rand.nextInt(IafConfig.generateDragonSkeletonChance + 1) == 0) {
                EntityLightningDragon firedragon = IafEntityRegistry.LIGHTNING_DRAGON.create(worldIn.getWorld());
                firedragon.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                int dragonage = 10 + rand.nextInt(100);
                firedragon.growDragon(dragonage);
                firedragon.modelDeadProgress = 20;
                firedragon.setModelDead(true);
                firedragon.setDeathStage((dragonage / 5) / 2);
                firedragon.rotationYaw = rand.nextInt(360);
                worldIn.addEntity(firedragon);
            }else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && rand.nextInt(IafConfig.generateDragonSkeletonChance + 1) == 0) {
                EntityFireDragon firedragon = IafEntityRegistry.FIRE_DRAGON.create(worldIn.getWorld());
                firedragon.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                int dragonage = 10 + rand.nextInt(100);
                firedragon.growDragon(dragonage);
                firedragon.modelDeadProgress = 20;
                firedragon.setModelDead(true);
                firedragon.setDeathStage((dragonage / 5) / 2);
                firedragon.rotationYaw = rand.nextInt(360);
                worldIn.addEntity(firedragon);
            }
            if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY) && rand.nextInt(IafConfig.generateDragonSkeletonChance + 1) == 0) {
                EntityIceDragon icedragon = IafEntityRegistry.ICE_DRAGON.create(worldIn.getWorld());
                icedragon.setPosition(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                int dragonage = 10 + rand.nextInt(100);
                icedragon.growDragon(dragonage);
                icedragon.modelDeadProgress = 20;
                icedragon.setModelDead(true);
                icedragon.setDeathStage((dragonage / 5) / 2);
                icedragon.rotationYaw = rand.nextInt(360);
                worldIn.addEntity(icedragon);
            }
        }
        if (IafConfig.spawnHippocampus && BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) && rand.nextInt(IafConfig.hippocampusSpawnChance + 1) == 0) {
            for (int i = 0; i < rand.nextInt(5); i++) {
                BlockPos pos =oceanPos.add(rand.nextInt(10) - 5, rand.nextInt(30), rand.nextInt(10) - 5);
                if (worldIn.getBlockState(pos).getMaterial() == Material.WATER) {
                    EntityHippocampus campus = IafEntityRegistry.HIPPOCAMPUS.create(worldIn.getWorld());
                    campus.setVariant(rand.nextInt(5));
                    campus.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                    if (campus.isNotColliding(worldIn)) {
                        worldIn.addEntity(campus);
                    }
                }
            }
        }
        if (IafConfig.spawnSeaSerpents && IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) && rand.nextInt(IafConfig.seaSerpentSpawnChance + 1) == 0) {
            BlockPos pos =oceanPos.add(rand.nextInt(10) - 5, rand.nextInt(30), rand.nextInt(10) - 5);
            if (worldIn.getBlockState(pos).getMaterial() == Material.WATER) {
                EntitySeaSerpent serpent = IafEntityRegistry.SEA_SERPENT.create(worldIn.getWorld());
                serpent.onWorldSpawn(rand);
                serpent.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                worldIn.addEntity(serpent);
            }
        }
        if (IafConfig.spawnStymphalianBirds && IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) && rand.nextInt(IafConfig.stymphalianBirdSpawnChance + 1) == 0) {
            for (int i = 0; i < 4 + rand.nextInt(4); i++) {
                BlockPos pos = position.add(rand.nextInt(10) - 5, 0, rand.nextInt(10) - 5);
                if (worldIn.getBlockState(pos.down()).isSolid()) {
                    EntityStymphalianBird bird = IafEntityRegistry.STYMPHALIAN_BIRD.create(worldIn.getWorld());
                    bird.setLocationAndAngles(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0, 0);
                    if (bird.isNotColliding(worldIn)) {
                        worldIn.addEntity(bird);
                    }
                }
            }
        }
        return false;
    }
}
