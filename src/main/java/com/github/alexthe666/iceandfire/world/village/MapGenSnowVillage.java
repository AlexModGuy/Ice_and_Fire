package com.github.alexthe666.iceandfire.world.village;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.init.Biomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class MapGenSnowVillage extends WorldGenerator {
    public static List<Biome> VILLAGE_SPAWN_BIOMES = Arrays.asList(Biomes.ICE_MOUNTAINS, Biomes.ICE_PLAINS, Biomes.MUTATED_ICE_FLATS, IafWorldRegistry.GLACIER_BIOME, Biomes.COLD_TAIGA_HILLS, Biomes.FROZEN_OCEAN, Biomes.COLD_TAIGA);
    private final int minTownSeparation;
    private int size;
    private int distance;

    public MapGenSnowVillage() {
        this.distance = 9;
        this.minTownSeparation = 8;
    }

    public MapGenSnowVillage(Map<String, String> map) {
        this();
        for (Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals("size")) {
                this.size = MathHelper.getInt(entry.getValue(), this.size, 0);
            } else if (entry.getKey().equals("distance")) {
                this.distance = MathHelper.getInt(entry.getValue(), this.distance, 9);
            }
        }
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position) {
        this.distance = 9;
        boolean canSpawn = canSpawnStructureAtCoords(world, position.getX() >> 4, position.getZ() >> 4);
        if (new Random().nextInt(IceAndFire.CONFIG.generateSnowVillageChance + 1) == 0) {
            int new_size = 32;
            getStructureStart(world, position.getX() >> 4, position.getZ() >> 4, rand).generateStructure(world, rand, new StructureBoundingBox(position.getX() - new_size, position.getZ() - new_size, position.getX() + new_size, position.getZ() + new_size));
        }
        return canSpawn;
    }

    public String getStructureName() {
        return "SnowVillage";
    }

    protected boolean canSpawnStructureAtCoords(World world, int chunkX, int chunkZ) {
        int i = chunkX;
        int j = chunkZ;
        if (chunkX < 0) {
            chunkX -= this.distance - 1;
        }
        if (chunkZ < 0) {
            chunkZ -= this.distance - 1;
        }

        int k = chunkX / this.distance;
        int l = chunkZ / this.distance;
        Random random = world.setRandomSeed(k, l, 10387312);
        k = k * this.distance;
        l = l * this.distance;
        k = k + random.nextInt(this.distance - 8);
        l = l + random.nextInt(this.distance - 8);
        return true;
    }

    protected StructureStart getStructureStart(World world, int chunkX, int chunkZ, Random random) {
        return new MapGenSnowVillage.Start(world, random, chunkX, chunkZ, this.size);
    }

    public static class Start extends StructureStart {
        /**
         * well ... thats what it does
         */
        private boolean hasMoreThanTwoComponents;

        public Start() {
        }

        public Start(World worldIn, Random rand, int x, int z, int size) {
            super(x, z);
            List<SnowVillagePieces.PieceWeight> list = SnowVillagePieces.getStructureVillageWeightedPieceList(rand, size);
            SnowVillagePieces.Start structurevillagepieces$start = new SnowVillagePieces.Start(worldIn.getBiomeProvider(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, size);
            this.components.add(structurevillagepieces$start);
            structurevillagepieces$start.buildComponent(structurevillagepieces$start, this.components, rand);
            List<StructureComponent> list1 = structurevillagepieces$start.pendingRoads;
            List<StructureComponent> list2 = structurevillagepieces$start.pendingHouses;

            while (!list1.isEmpty() || !list2.isEmpty()) {
                if (list1.isEmpty()) {
                    int i = rand.nextInt(list2.size());
                    StructureComponent structurecomponent = list2.remove(i);
                    structurecomponent.buildComponent(structurevillagepieces$start, this.components, rand);
                } else {
                    int j = rand.nextInt(list1.size());
                    StructureComponent structurecomponent2 = list1.remove(j);
                    structurecomponent2.buildComponent(structurevillagepieces$start, this.components, rand);
                }
            }

            this.updateBoundingBox();
            int k = 0;

            for (StructureComponent structurecomponent1 : this.components) {
                if (!(structurecomponent1 instanceof StructureVillagePieces.Road)) {
                    ++k;
                }
            }

            this.hasMoreThanTwoComponents = k > 2;
        }

        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }

        public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }

        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
        }
    }
}