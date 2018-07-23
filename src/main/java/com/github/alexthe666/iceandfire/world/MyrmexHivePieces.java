package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModVillagers;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.EntitySnowVillager;
import com.github.alexthe666.iceandfire.world.village.SnowVillagePieces;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MyrmexHivePieces {

    public static void registerVillagePieces() {
        MapGenStructureIO.registerStructureComponent(Chamber1.class, "ViBH");
        MapGenStructureIO.registerStructureComponent(Center.class, "ViW");
    }

    private static int updatePieceWeight(List<MyrmexHivePieces.PieceWeight> weights) {
        boolean flag = false;
        int i = 0;
        for (PieceWeight weight : weights) {
            if (weight.villagePiecesLimit > 0 && weight.villagePiecesSpawned < weight.villagePiecesLimit) {
                flag = true;
            }
            i += weight.villagePieceWeight;
        }
        return flag ? i : -1;
    }

    private static Village findAndCreateComponentFactory(Start start, PieceWeight weight, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType) {
        return null;
    }

    private static StructureComponent generateAndAddChamber(Start start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType) {
        if (componentType > 50) {
            return null;
        } else if (Math.abs(structureMinX - start.getBoundingBox().minX) <= 112 && Math.abs(structureMinZ - start.getBoundingBox().minZ) <= 112) {
            StructureComponent structurecomponent = Chamber1.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType + 1);
            if (structurecomponent != null) {
                structureComponents.add(structurecomponent);
                start.pendingHouses.add(structurecomponent);
                return structurecomponent;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    private static StructureComponent generateAndAddComponent(Start start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType) {
        return null;
    }

    private static StructureComponent generateAndAddRoadPiece(Start start, List<StructureComponent> p_176069_1_, Random rand, int p_176069_3_, int p_176069_4_, int p_176069_5_, EnumFacing facing, int p_176069_7_) {
        if (p_176069_7_ > 3 + start.terrainType) {
            return null;
        } else if (Math.abs(p_176069_3_ - start.getBoundingBox().minX) <= 112 && Math.abs(p_176069_5_ - start.getBoundingBox().minZ) <= 112) {
            StructureBoundingBox structureboundingbox = Path.findPieceBox(start, p_176069_1_, rand, p_176069_3_, p_176069_4_, p_176069_5_, facing);

            if (structureboundingbox != null && structureboundingbox.minY > 10) {
                StructureComponent structurecomponent = new Path(start, p_176069_7_, rand, structureboundingbox, facing);
                p_176069_1_.add(structurecomponent);
                start.pendingRoads.add(structurecomponent);
                return structurecomponent;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static List<PieceWeight> getStructureVillageWeightedPieceList(Random random, int size) {
        List<PieceWeight> list = Lists.<PieceWeight>newArrayList();
        list.add(new PieceWeight(Chamber1.class, 75, MathHelper.getInt(random, 5 + size, 6 + size)));
        Iterator<PieceWeight> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (((PieceWeight) iterator.next()).villagePiecesLimit == 0) {
                iterator.remove();
            }
        }
        return list;
    }

    public static class Path extends Road {
        private int length;

        public Path() {
        }

        public Path(Start start, int p_i45562_2_, Random rand, StructureBoundingBox p_i45562_4_, EnumFacing facing) {
            super(start, p_i45562_2_);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45562_4_;
            this.length = Math.max(p_i45562_4_.getXSize(), p_i45562_4_.getZSize());
        }

        public static StructureBoundingBox findPieceBox(Start start, List<StructureComponent> p_175848_1_, Random rand, int p_175848_3_, int p_175848_4_, int p_175848_5_, EnumFacing facing) {
            for (int i = 7 * MathHelper.getInt(rand, 3, 5); i >= 7; i -= 7) {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175848_3_, p_175848_4_, p_175848_5_, 0, 0, 0, 3, 3, i, facing);

                if (StructureComponent.findIntersecting(p_175848_1_, structureboundingbox) == null) {
                    return structureboundingbox;
                }
            }

            return null;
        }

        /**
         * (abstract) Helper method to write subclass data to NBT
         */
        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("Length", this.length);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        protected void readStructureFromNBT(NBTTagCompound tagCompound) {
            super.readStructureFromNBT(tagCompound);
            this.length = tagCompound.getInteger("Length");
        }

        /**
         * Initiates construction of the Structure Component picked, at the current Location of StructGen
         */
        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
            boolean flag = false;

            for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5)) {
                StructureComponent structurecomponent = this.getNextComponentNN((Start) componentIn, listIn, rand, 0, i);

                if (structurecomponent != null && structurecomponent.getBoundingBox() != null) {
                    i += Math.max(structurecomponent.getBoundingBox().getXSize(), structurecomponent.getBoundingBox().getZSize());
                    flag = true;
                }
            }

            for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5)) {
                StructureComponent structurecomponent1 = this.getNextComponentPP((Start) componentIn, listIn, rand, 0, j);

                if (structurecomponent1 != null && structurecomponent1.getBoundingBox() != null) {
                    j += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
                    flag = true;
                }
            }

            EnumFacing enumfacing = this.getCoordBaseMode();

            if (flag && enumfacing != null) {
                switch (enumfacing) {
                    case NORTH:
                    default:
                        generateNextPiece((Start) componentIn, listIn, rand, this.getBoundingBox().minX - 1, this.getBoundingBox().minY, this.getBoundingBox().minZ, EnumFacing.WEST, this.getComponentType());
                        break;
                    case SOUTH:
                        generateNextPiece((Start) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                        break;
                    case WEST:
                        generateNextPiece((Start) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    case EAST:
                        generateNextPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }
            if (!flag && enumfacing != null) {
                switch (enumfacing) {
                    case NORTH:
                    default:
                        generateNextPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                        break;
                    case SOUTH:
                        generateNextPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                        break;
                    case WEST:
                        generateNextPiece((Start) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    case EAST:
                        generateNextPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }

        }

        void generateNextPiece(Start componentIn, List<StructureComponent> listIn, Random rand, int x, int y, int z, EnumFacing facing, int type) {
            switch (rand.nextInt(5)) {
                case 0:
                    generateAndAddChamber((Start) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    break;
                case 1:
                    generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    break;
                default:
                    break;
            }
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(Blocks.AIR.getDefaultState());

            for (int i = this.boundingBox.minX - 2; i <= this.boundingBox.maxX + 2; ++i) {
                for (int j = this.boundingBox.minZ - 3; j <= this.boundingBox.maxZ + 3; ++j) {
                    BlockPos blockpos = new BlockPos(i, 64, j);
                    if (structureBoundingBoxIn.isVecInside(blockpos)) {
                        blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();
                        if (blockpos.getY() < worldIn.getSeaLevel() - getAverageGroundLevel(worldIn, structureBoundingBoxIn)) {
                            blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 2 - getAverageGroundLevel(worldIn, structureBoundingBoxIn) - randomIn.nextInt(1), blockpos.getZ());
                        }
                        while (blockpos.getY() >= worldIn.getSeaLevel() - 2 - getAverageGroundLevel(worldIn, structureBoundingBoxIn)) {
                            if (blockpos.getY() < worldIn.getSeaLevel() + 3 + randomIn.nextInt(2) - getAverageGroundLevel(worldIn, structureBoundingBoxIn) && !worldIn.isAirBlock(blockpos)) {
                                IBlockState state = randomIn.nextBoolean() ? ModBlocks.myrmex_resin_sticky.getDefaultState() : ModBlocks.myrmex_resin.getDefaultState();
                                worldIn.setBlockState(blockpos, state, 2);
                            }
                            blockpos = blockpos.down();
                        }
                    }
                }
            }

            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                for (int j = this.boundingBox.minZ - randomIn.nextInt(2); j <= this.boundingBox.maxZ + randomIn.nextInt(2); ++j) {
                    BlockPos blockpos = new BlockPos(i, 64, j);
                    if (structureBoundingBoxIn.isVecInside(blockpos)) {
                        blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();
                        if (blockpos.getY() < worldIn.getSeaLevel() - getAverageGroundLevel(worldIn, structureBoundingBoxIn)) {
                            blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 1 - getAverageGroundLevel(worldIn, structureBoundingBoxIn) - randomIn.nextInt(1), blockpos.getZ());
                        }
                        while (blockpos.getY() >= worldIn.getSeaLevel() - 1 - getAverageGroundLevel(worldIn, structureBoundingBoxIn)) {
                            if (blockpos.getY() < worldIn.getSeaLevel() + 2 + randomIn.nextInt(2) - getAverageGroundLevel(worldIn, structureBoundingBoxIn)) {
                                worldIn.setBlockState(blockpos, iblockstate1, 2);
                                worldIn.setBlockState(blockpos.add(randomIn.nextInt(4) - 2, 0, randomIn.nextInt(4) - 2), iblockstate1, 2);

                            }
                            blockpos = blockpos.down();
                        }
                    }
                }
            }
            return true;
        }

        protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb) {
            int i = 0;
            int j = 0;
            int worldHeight;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
                for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                    blockpos$mutableblockpos.setPos(l, 64, k);

                    if (structurebb.isVecInside(blockpos$mutableblockpos)) {
                        i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel() - 1);
                        ++j;
                    }
                }
            }

            if (j == 0) {
                worldHeight = -1;
            } else {
                worldHeight = i / j;
            }
            return (worldHeight + 1) / 4;
        }


        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound);
        }
    }

    public static class PieceWeight {
        public final int villagePieceWeight;
        public Class<? extends MyrmexHivePieces.Village> villagePieceClass;
        public int villagePiecesSpawned;
        public int villagePiecesLimit;

        public PieceWeight(Class<? extends MyrmexHivePieces.Village> p_i2098_1_, int p_i2098_2_, int p_i2098_3_) {
            this.villagePieceClass = p_i2098_1_;
            this.villagePieceWeight = p_i2098_2_;
            this.villagePiecesLimit = p_i2098_3_;
        }

        public boolean canSpawnMoreVillagePiecesOfType(int componentType) {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }

        public boolean canSpawnMoreVillagePieces() {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
    }

    public abstract static class Road extends Village {
        public Road() {
        }

        protected Road(Start start, int type) {
            super(start, type);
        }
    }

    public static class Start extends Center {
        public BiomeProvider worldChunkMngr;
        public int terrainType;
        public PieceWeight structVillagePieceWeight;
        public List<MyrmexHivePieces.PieceWeight> structureVillageWeightedPieceList;
        public List<StructureComponent> pendingHouses = Lists.<StructureComponent>newArrayList();
        public List<StructureComponent> pendingRoads = Lists.<StructureComponent>newArrayList();
        public Biome biome;

        public Start() {
        }

        public Start(BiomeProvider chunkManagerIn, int p_i2104_2_, Random rand, int p_i2104_4_, int p_i2104_5_, List<MyrmexHivePieces.PieceWeight> p_i2104_6_, int p_i2104_7_) {
            super((Start) null, 0, rand, p_i2104_4_, p_i2104_5_);
            this.worldChunkMngr = chunkManagerIn;
            this.structureVillageWeightedPieceList = p_i2104_6_;
            this.terrainType = p_i2104_7_;
            Biome biome = chunkManagerIn.getBiome(new BlockPos(p_i2104_4_, 0, p_i2104_5_), Biomes.DEFAULT);
            this.biome = biome;
            this.startPiece = this;
            this.func_189924_a(this.field_189928_h);
            this.field_189929_i = rand.nextInt(50) == 0;
        }
    }

    public abstract static class Village extends StructureComponent {
        protected int averageGroundLvl = -1;
        protected int field_189928_h;
        protected boolean field_189929_i;
        protected Start startPiece;
        /**
         * The number of villagers that have been spawned in this component.
         */
        private int villagersSpawned;

        public Village() {
        }

        protected Village(Start start, int type) {
            super(type);

            if (start != null) {
                this.field_189928_h = start.field_189928_h;
                this.field_189929_i = start.field_189929_i;
                startPiece = start;
            }
        }

        protected static boolean canVillageGoDeeper(StructureBoundingBox structurebb) {
            return structurebb != null && structurebb.minY > 10;
        }

        /**
         * (abstract) Helper method to write subclass data to NBT
         */
        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            tagCompound.setInteger("HPos", this.averageGroundLvl);
            tagCompound.setInteger("VCount", this.villagersSpawned);
            tagCompound.setByte("Type", (byte) this.field_189928_h);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        protected void readStructureFromNBT(NBTTagCompound tagCompound) {
            this.averageGroundLvl = tagCompound.getInteger("HPos");
            this.villagersSpawned = tagCompound.getInteger("VCount");
            this.field_189928_h = tagCompound.getByte("Type");
            if (tagCompound.getBoolean("Desert")) {
                this.field_189928_h = 1;
            }
        }

        protected StructureComponent getNextComponentNN(Start start, List<StructureComponent> structureComponents, Random rand, int p_74891_4_, int p_74891_5_) {
            EnumFacing enumfacing = this.getCoordBaseMode();

            if (enumfacing != null) {
                switch (enumfacing) {
                    case NORTH:
                    default:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());
                    case SOUTH:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX - 1, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ + p_74891_5_, EnumFacing.WEST, this.getComponentType());
                    case WEST:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    case EAST:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74891_5_, this.boundingBox.minY + p_74891_4_, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            } else {
                return null;
            }
        }

        protected StructureComponent getNextComponentPP(Start start, List<StructureComponent> structureComponents, Random rand, int p_74894_4_, int p_74894_5_) {
            EnumFacing enumfacing = this.getCoordBaseMode();

            if (enumfacing != null) {
                switch (enumfacing) {
                    case NORTH:
                    default:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());
                    case SOUTH:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + p_74894_4_, this.boundingBox.minZ + p_74894_5_, EnumFacing.EAST, this.getComponentType());
                    case WEST:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    case EAST:
                        return generateAndAddComponent(start, structureComponents, rand, this.boundingBox.minX + p_74894_5_, this.boundingBox.minY + p_74894_4_, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            } else {
                return null;
            }
        }

        protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb) {
            int i = 0;
            int j = 0;
            int worldHeight;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
                for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                    blockpos$mutableblockpos.setPos(l, 64, k);

                    if (structurebb.isVecInside(blockpos$mutableblockpos)) {
                        i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel() - 1);
                        ++j;
                    }
                }
            }

            if (j == 0) {
                worldHeight = -1;
            } else {
                worldHeight = i / j;
            }
            return (worldHeight + 1) / 4;
        }

        protected void spawnVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count) {
            if (this.villagersSpawned < count) {
                for (int i = this.villagersSpawned; i < count; ++i) {
                    int j = this.getXWithOffset(x + i, z);
                    int k = this.getYWithOffset(y);
                    int l = this.getZWithOffset(x + i, z);

                    if (!structurebb.isVecInside(new BlockPos(j, k, l))) {
                        break;
                    }

                    ++this.villagersSpawned;

                    EntityMyrmexWorker worker = new EntityMyrmexWorker(worldIn);
                    worker.setLocationAndAngles((double) j + 0.5D, (double) k, (double) l + 0.5D, 0.0F, 0.0F);
                    worker.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(worker)), (IEntityLivingData) null);
                    worker.setGrowthStage(new Random().nextInt(2));
                    worldIn.spawnEntity(worker);
                }
            }
        }

        protected void setBlockState(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
            BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
            worldIn.setBlockState(blockpos, blockstateIn, 2);
        }

        protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn) {

            return blockstateIn;
        }

        protected void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, int x, int y, int z, StructureBoundingBox boundingboxIn) {
            IBlockState iblockstate = this.getBiomeSpecificBlockState(blockstateIn);
            super.replaceAirAndLiquidDownwards(worldIn, iblockstate, x, y, z, boundingboxIn);
        }

        protected void func_189924_a(int p_189924_1_) {
            this.field_189928_h = p_189924_1_;
        }
    }

    public static class Center extends Village {
        public Center() {
        }

        public Center(Start start, int type, Random rand, int x, int z) {
            super(start, type);
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));

            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
            } else {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
            }
        }

        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
            this.boundingBox.offset(0, -this.averageGroundLvl, 0);
            int yRand = randomIn.nextInt(6);
            int xRand = randomIn.nextInt(6);
            int zRand = randomIn.nextInt(6);
            {
                int i1 = 14;
                int i2 = i1 - 2;
                int j = i2 + xRand;
                int k = 5 + yRand;
                int l = i2 + zRand;
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(-j, -k, -l), new BlockPos(j, k, l))) {
                    if (blockpos.distanceSq(BlockPos.ORIGIN) <= (double) (f * f * MathHelper.clamp(randomIn.nextFloat(), 0.8F, 1.2F))) {
                        IBlockState state = randomIn.nextBoolean() ? ModBlocks.myrmex_resin_sticky.getDefaultState() : ModBlocks.myrmex_resin.getDefaultState();
                        this.setBlockState(worldIn, state, blockpos.getX(), blockpos.getY(), blockpos.getZ(), structureBoundingBoxIn);

                    }
                }
            }
            {
                int ySize = randomIn.nextInt(6);
                int i1 = 12;
                int i2 = i1 - 2;
                int j = i2 + xRand;
                int k = 3 + yRand;
                int l = i2 + zRand;
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(-j, -k, -l), new BlockPos(j, k, l))) {
                    if (blockpos.distanceSq(BlockPos.ORIGIN) <= (double) (f * f * MathHelper.clamp(randomIn.nextFloat(), 0.8F, 1.2F))) {
                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), structureBoundingBoxIn);

                    }
                }
            }
            return true;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound);
        }
    }

    public static class Chamber1 extends Village {
        public Chamber1() {
        }

        public Chamber1(Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
            super(start, type);
            this.boundingBox = structurebb;
            this.setCoordBaseMode(facing);
        }

        public static Chamber1 createPiece(Start start, List<StructureComponent> p_175853_1_, Random rand, int p_175853_3_, int p_175853_4_, int p_175853_5_, EnumFacing facing, int p_175853_7_) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175853_3_, p_175853_4_, p_175853_5_, 0, -4, 0, 0, 4, 0, facing);
            return new Chamber1(start, p_175853_7_, rand, structureboundingbox, facing);
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
        }

        protected void readStructureFromNBT(NBTTagCompound tagCompound) {
            super.readStructureFromNBT(tagCompound);
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);
            this.boundingBox.offset(0, -this.averageGroundLvl - 20, 0);
            int yRand = randomIn.nextInt(6);
            int xRand = randomIn.nextInt(6);
            int zRand = randomIn.nextInt(6);
            {
                int i1 = 7;
                int i2 = i1 - 2;
                int j = i2 + xRand;
                int k = 5 + yRand;
                int l = i2 + zRand;
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(-j, -k, -l), new BlockPos(j, k, l))) {
                    if (blockpos.distanceSq(BlockPos.ORIGIN) <= (double) (f * f * MathHelper.clamp(randomIn.nextFloat(), 0.8F, 1.2F))) {
                        IBlockState state = randomIn.nextBoolean() ? ModBlocks.myrmex_resin_sticky.getDefaultState() : ModBlocks.myrmex_resin.getDefaultState();
                        if (getBlockStateFromPos(worldIn, blockpos.getX(), blockpos.getY(), blockpos.getZ(), structureBoundingBoxIn).getBlock() != Blocks.AIR) {
                            this.setBlockState(worldIn, state, blockpos.getX(), blockpos.getY(), blockpos.getZ(), structureBoundingBoxIn);
                        }
                    }
                }
            }
            {
                int ySize = randomIn.nextInt(6);
                int i1 = 6;
                int i2 = i1 - 2;
                int j = i2 + xRand;
                int k = 3 + yRand;
                int l = i2 + zRand;
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                for (BlockPos blockpos : BlockPos.getAllInBox(new BlockPos(-j, -k, -l), new BlockPos(j, k, l))) {
                    if (blockpos.distanceSq(BlockPos.ORIGIN) <= (double) (f * f * MathHelper.clamp(randomIn.nextFloat(), 0.8F, 1.2F))) {
                        this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), blockpos.getX(), blockpos.getY(), blockpos.getZ(), structureBoundingBoxIn);

                    }
                }
            }
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1);
            return true;
        }

        protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb) {
            int i = 0;
            int j = 0;
            int worldHeight;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
                for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                    blockpos$mutableblockpos.setPos(l, 64, k);

                    if (structurebb.isVecInside(blockpos$mutableblockpos)) {
                        i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel() - 1);
                        ++j;
                    }
                }
            }

            if (j == 0) {
                worldHeight = -1;
            } else {
                worldHeight = i / j;
            }
            return (worldHeight + 1) / 4;
        }


        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound);
        }
    }
}