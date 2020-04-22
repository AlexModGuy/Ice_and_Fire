package com.github.alexthe666.iceandfire.world.village;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouseModel;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class PixieVillagePieces {

    public static void registerVillagePieces() {
        MapGenStructureIO.registerStructureComponent(PixieHouse.class, "ViBH");
        MapGenStructureIO.registerStructureComponent(Well.class, "ViW");
    }

    public static List<PixieVillagePieces.PieceWeight> getStructureVillageWeightedPieceList(Random random, int size) {
        List<PixieVillagePieces.PieceWeight> list = Lists.newArrayList();
        list.add(new PieceWeight(PixieHouse.class, 75, 10));
        list.add(new PieceWeight(Road.class, 25, 12));
        return list;
    }

    private static int updatePieceWeight(List<PixieVillagePieces.PieceWeight> p_75079_0_) {
        boolean flag = false;
        int i = 0;
        for (PieceWeight PixieVillagePieces$pieceweight : p_75079_0_) {
            if (PixieVillagePieces$pieceweight.villagePiecesLimit > 0 && PixieVillagePieces$pieceweight.villagePiecesSpawned < PixieVillagePieces$pieceweight.villagePiecesLimit) {
                flag = true;
            }

            i += PixieVillagePieces$pieceweight.villagePieceWeight;
        }

        return flag ? i : -1;
    }

    private static Village findAndCreateComponentFactory(Start start, PieceWeight weight, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType) {
        Class<? extends PixieVillagePieces.Village> oclass = weight.villagePieceClass;
        Object village = PixieHouse.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        return (Village) village;
    }

    private static Village generateComponent(Start start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType) {
        int i = updatePieceWeight(start.structureVillageWeightedPieceList);

        if (i <= 0) {
            return null;
        } else {
            int j = 0;

            while (j < 5) {
                ++j;
                int k = rand.nextInt(i);

                for (PieceWeight PixieVillagePieces$pieceweight : start.structureVillageWeightedPieceList) {
                    k -= PixieVillagePieces$pieceweight.villagePieceWeight;

                    if (k < 0) {
                        if (!PixieVillagePieces$pieceweight.canSpawnMoreVillagePiecesOfType(componentType) || PixieVillagePieces$pieceweight == start.structVillagePieceWeight && start.structureVillageWeightedPieceList.size() > 1) {
                            break;
                        }

                        Village PixieVillagePieces$village = findAndCreateComponentFactory(start, PixieVillagePieces$pieceweight, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);

                        if (PixieVillagePieces$village != null) {
                            ++PixieVillagePieces$pieceweight.villagePiecesSpawned;
                            start.structVillagePieceWeight = PixieVillagePieces$pieceweight;

                            if (!PixieVillagePieces$pieceweight.canSpawnMoreVillagePieces()) {
                                start.structureVillageWeightedPieceList.remove(PixieVillagePieces$pieceweight);
                            }

                            return PixieVillagePieces$village;
                        }
                    }
                }
            }
            return null;
        }
    }

    private static StructureComponent generateAndAddComponent(Start start, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType) {
        if (componentType > 50) {
            return null;
        } else if (Math.abs(structureMinX - start.getBoundingBox().minX) <= 112 && Math.abs(structureMinZ - start.getBoundingBox().minZ) <= 112) {
            StructureComponent structurecomponent = generateComponent(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType + 1);

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

    private static StructureComponent generateAndAddRoadPiece(Start start, List<StructureComponent> p_176069_1_, Random rand, int p_176069_3_, int p_176069_4_, int p_176069_5_, EnumFacing facing, int p_176069_7_) {
        if (p_176069_7_ > 3 + start.terrainType) {
            return null;
        } else if (Math.abs(p_176069_3_ - start.getBoundingBox().minX) <= 112 && Math.abs(p_176069_5_ - start.getBoundingBox().minZ) <= 112) {
            StructureBoundingBox structureboundingbox = Path.findPieceBox(start, p_176069_1_, rand, p_176069_3_, p_176069_4_, p_176069_5_, facing);

            if (structureboundingbox != null && structureboundingbox.minY > 0) {
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

    public static class House1 extends Village {
        public House1() {
        }

        public House1(Start start, int type, Random rand, StructureBoundingBox p_i45571_4_, EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = p_i45571_4_;
        }

        public static House1 createPiece(Start start, List<StructureComponent> p_175850_1_, Random rand, int p_175850_3_, int p_175850_4_, int p_175850_5_, EnumFacing facing, int p_175850_7_) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175850_3_, p_175850_4_, p_175850_5_, 0, 0, 0, 9, 9, 6, facing);
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175850_1_, structureboundingbox) == null ? new House1(start, p_175850_7_, rand, structureboundingbox, facing) : null;
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 9 - 1, 0);
            }

            this.setBlockState(worldIn, IafBlockRegistry.pixieHouse.getDefaultState().withProperty(BlockPixieHouse.FACING, EnumFacing.NORTH), 0, 0, 0, structureBoundingBoxIn);
            BlockPos blockpos = new BlockPos(this.getXWithOffset(0, 0), this.getYWithOffset(0), this.getZWithOffset(0, 0));
            if (worldIn.getTileEntity(blockpos) != null && worldIn.getTileEntity(blockpos) instanceof TileEntityPixieHouse) {
                int houseType = randomIn.nextInt(5);
                ((TileEntityPixieHouse) worldIn.getTileEntity(blockpos)).houseType = houseType;
                if (!worldIn.isRemote) {
                    IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageUpdatePixieHouseModel(blockpos.toLong(), houseType));
                }
            } else {
                TileEntityPixieHouse pixieHouse = new TileEntityPixieHouse();
                pixieHouse.houseType = randomIn.nextInt(5);
                worldIn.setTileEntity(blockpos, pixieHouse);
            }
            this.replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), 0, -1, 0, structureBoundingBoxIn);
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 0, 1, 0, 1);
            return true;
        }

        @SuppressWarnings("deprecation")
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {
            return 1;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
        }
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
            for (int i = 3 * MathHelper.getInt(rand, 3, 5); i >= 3; i -= 3) {
                StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175848_3_, p_175848_4_, p_175848_5_, 0, 0, 0, 1, 1, i, facing);

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

                if (structurecomponent != null) {
                    i += Math.max(structurecomponent.getBoundingBox().getXSize(), structurecomponent.getBoundingBox().getZSize());
                    flag = true;
                }
            }

            for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5)) {
                StructureComponent structurecomponent1 = this.getNextComponentPP((Start) componentIn, listIn, rand, 0, j);

                if (structurecomponent1 != null) {
                    j += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
                    flag = true;
                }
            }

            EnumFacing enumfacing = this.getCoordBaseMode();

            if (flag && rand.nextInt(3) > 0 && enumfacing != null) {
                switch (enumfacing) {
                    case NORTH:
                    default:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.getBoundingBox().minX - 1, this.getBoundingBox().minY, this.getBoundingBox().minZ, EnumFacing.WEST, this.getComponentType());
                        break;
                    case SOUTH:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                        break;
                    case WEST:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    case EAST:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }

            if (flag && rand.nextInt(3) > 0 && enumfacing != null) {
                switch (enumfacing) {
                    case NORTH:
                    default:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                        break;
                    case SOUTH:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                        break;
                    case WEST:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    case EAST:
                        generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }
        }

        /**
         * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
         * Mineshafts at the end, it adds Fences...
         */
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            IBlockState iblockstate = this.getBiomeSpecificBlockState(Blocks.GRASS_PATH.getDefaultState());
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE));
            IBlockState iblockstate2 = this.getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState());
            IBlockState iblockstate3 = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());

            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                    BlockPos blockpos = new BlockPos(i, 64, j);

                    if (structureBoundingBoxIn.isVecInside(blockpos)) {
                        blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos).down();

                        if (blockpos.getY() < worldIn.getSeaLevel()) {
                            blockpos = new BlockPos(blockpos.getX(), worldIn.getSeaLevel() - 1, blockpos.getZ());
                        }

                        while (blockpos.getY() >= worldIn.getSeaLevel() - 1) {
                            IBlockState iblockstate4 = worldIn.getBlockState(blockpos);

                            if (iblockstate4.getBlock() == Blocks.GRASS) {
                                if (!worldIn.isAirBlock(blockpos.up())) {
                                    worldIn.setBlockToAir(blockpos.up());
                                }
                                if (!worldIn.isAirBlock(blockpos.up(2))) {
                                    worldIn.setBlockToAir(blockpos.up(2));
                                }
                                worldIn.setBlockState(blockpos, iblockstate, 2);
                                break;
                            }

                            if (iblockstate4.getMaterial().isLiquid() || iblockstate4.getBlock() == Blocks.ICE) {
                                worldIn.setBlockState(blockpos, iblockstate1, 2);
                                break;
                            }

                            if (iblockstate4.getBlock() == Blocks.SAND || iblockstate4.getBlock() == Blocks.SANDSTONE || iblockstate4.getBlock() == Blocks.RED_SANDSTONE) {
                                worldIn.setBlockState(blockpos, iblockstate2, 2);
                                worldIn.setBlockState(blockpos.down(), iblockstate3, 2);
                                break;
                            }

                            blockpos = blockpos.down();
                        }
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

    public static class PieceWeight {
        public final int villagePieceWeight;
        public Class<? extends PixieVillagePieces.Village> villagePieceClass;
        public int villagePiecesSpawned;
        public int villagePiecesLimit;

        public PieceWeight(Class<? extends PixieVillagePieces.Village> p_i2098_1_, int p_i2098_2_, int p_i2098_3_) {
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

    public static class Start extends Well {
        public BiomeProvider worldChunkMngr;
        /**
         * World terrain type, 0 for normal, 1 for flap map
         */
        public int terrainType;
        public PieceWeight structVillagePieceWeight;
        public List<PixieVillagePieces.PieceWeight> structureVillageWeightedPieceList;
        public List<StructureComponent> pendingHouses = Lists.newArrayList();
        public List<StructureComponent> pendingRoads = Lists.newArrayList();
        public Biome biome;

        public Start() {
        }

        public Start(BiomeProvider chunkManagerIn, int p_i2104_2_, Random rand, int p_i2104_4_, int p_i2104_5_, List<PixieVillagePieces.PieceWeight> p_i2104_6_, int p_i2104_7_) {
            super(null, 0, rand, p_i2104_4_, p_i2104_5_);
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
            tagCompound.setBoolean("Zombie", this.field_189929_i);
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

            this.field_189929_i = tagCompound.getBoolean("Zombie");
        }

        /**
         * Gets the next village component, with the bounding box shifted -1 in the X and Z direction.
         */
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

        /**
         * Gets the next village component, with the bounding box shifted +1 in the X and Z direction.
         */
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

        /**
         * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of
         * all the levels in the BB's horizontal rectangle).
         */
        protected int getAverageGroundLevel(World worldIn, StructureBoundingBox structurebb) {
            int i = 0;
            int j = 0;
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
                return -1;
            } else {
                return i / j;
            }
        }

        /**
         * Spawns a number of villagers in this component. Parameters: world, component bounding box, x offset, y
         * offset, z offset, number of villagers
         */
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

                    EntityPixie entityPixie = new EntityPixie(worldIn);
                    entityPixie.setLocationAndAngles((double) j + 0.5D, (double) k, (double) l + 0.5D, 0.0F, 0.0F);
                    entityPixie.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityPixie)), null);
                    worldIn.spawnEntity(entityPixie);
                }
            }
        }

        @Deprecated // Use Forge version below.
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession) {
            return currentVillagerProfession;
        }

        @SuppressWarnings("deprecation")
        protected net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession chooseForgeProfession(int count, net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof) {
            return net.minecraftforge.fml.common.registry.VillagerRegistry.getById(chooseProfession(count, net.minecraftforge.fml.common.registry.VillagerRegistry.getId(prof)));
        }

        @SuppressWarnings("deprecation")
        protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn) {
            net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID event = new net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID(startPiece == null ? null : startPiece.biome, blockstateIn);
            net.minecraftforge.common.MinecraftForge.TERRAIN_GEN_BUS.post(event);
            if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
                return event.getReplacement();
            if (this.field_189928_h == 1) {
                if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                    return Blocks.SANDSTONE.getDefaultState();
                }

                if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                    return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
                }

                if (blockstateIn.getBlock() == Blocks.PLANKS) {
                    return Blocks.SANDSTONE.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
                }

                if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                    return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
                }

                if (blockstateIn.getBlock() == Blocks.STONE_STAIRS) {
                    return Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
                }

                if (blockstateIn.getBlock() == Blocks.GRAVEL) {
                    return Blocks.SANDSTONE.getDefaultState();
                }
            } else if (this.field_189928_h == 3) {
                if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                    return Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLog.LOG_AXIS, blockstateIn.getValue(BlockLog.LOG_AXIS));
                }

                if (blockstateIn.getBlock() == Blocks.PLANKS) {
                    return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
                }

                if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                    return Blocks.SPRUCE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
                }

                if (blockstateIn.getBlock() == Blocks.OAK_FENCE) {
                    return Blocks.SPRUCE_FENCE.getDefaultState();
                }
            } else if (this.field_189928_h == 2) {
                if (blockstateIn.getBlock() == Blocks.LOG || blockstateIn.getBlock() == Blocks.LOG2) {
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, blockstateIn.getValue(BlockLog.LOG_AXIS));
                }

                if (blockstateIn.getBlock() == Blocks.PLANKS) {
                    return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
                }

                if (blockstateIn.getBlock() == Blocks.OAK_STAIRS) {
                    return Blocks.ACACIA_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, blockstateIn.getValue(BlockStairs.FACING));
                }

                if (blockstateIn.getBlock() == Blocks.COBBLESTONE) {
                    return Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
                }

                if (blockstateIn.getBlock() == Blocks.OAK_FENCE) {
                    return Blocks.ACACIA_FENCE.getDefaultState();
                }
            }

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

    public static class Well extends Village {

        private static final WorldGenCanopyTree TREE_GEN = new WorldGenCanopyTree(true);

        public Well() {
        }

        public Well(Start start, int type, Random rand, int x, int z) {
            super(start, type);
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));

            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 3, 64, z + 3);
            } else {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 3, 64, z + 3);
            }
        }

        public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX, this.boundingBox.maxY - 4, this.boundingBox.minZ + 2, EnumFacing.WEST, this.getComponentType());
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 2, EnumFacing.EAST, this.getComponentType());
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX + 2, this.boundingBox.maxY - 4, this.boundingBox.minZ, EnumFacing.NORTH, this.getComponentType());
            generateAndAddRoadPiece((Start) componentIn, listIn, rand, this.boundingBox.minX + 2, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(1, this.averageGroundLvl - this.boundingBox.maxY - 1, 1);
            }

            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 3, 0, 3, Blocks.GRASS.getDefaultState(), Blocks.GRASS_PATH.getDefaultState(), false);
            BlockPos blockpos = new BlockPos(this.getXWithOffset(1, 2), this.getYWithOffset(1), this.getZWithOffset(1, 2));
            TREE_GEN.generate(worldIn, randomIn, blockpos);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 3, 0, 3, Blocks.GRASS_PATH.getDefaultState(), Blocks.GRASS_PATH.getDefaultState(), false);
            for (int l = 0; l < 3; ++l) {
                for (int k = 0; k < 3; ++k) {
                    this.replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), l, -1, k, structureBoundingBoxIn);
                }
            }
            return true;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound);
        }
    }

    public static class PixieHouse extends Village {
        private boolean isTallHouse;
        private int tablePosition;

        public PixieHouse() {
        }

        public PixieHouse(Start start, int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = structurebb;
            this.isTallHouse = rand.nextBoolean();
            this.tablePosition = rand.nextInt(3);
        }

        public static PixieHouse createPiece(Start start, List<StructureComponent> p_175853_1_, Random rand, int p_175853_3_, int p_175853_4_, int p_175853_5_, EnumFacing facing, int p_175853_7_) {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175853_3_, p_175853_4_, p_175853_5_, 0, 0, 0, 1, 1, 1, facing);
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175853_1_, structureboundingbox) == null ? new PixieHouse(start, p_175853_7_, rand, structureboundingbox, facing) : null;
        }

        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("T", this.tablePosition);
            tagCompound.setBoolean("C", this.isTallHouse);
        }

        protected void readStructureFromNBT(NBTTagCompound tagCompound) {
            super.readStructureFromNBT(tagCompound);
            this.tablePosition = tagCompound.getInteger("T");
            this.isTallHouse = tagCompound.getBoolean("C");
        }

        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (this.averageGroundLvl < 0) {
                this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

                if (this.averageGroundLvl < 0) {
                    return true;
                }

                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY, 0);
            }
            this.replaceAirAndLiquidDownwards(worldIn, Blocks.DIRT.getDefaultState(), 0, -1, 0, structureBoundingBoxIn);

            this.setBlockState(worldIn, IafBlockRegistry.pixieHouse.getDefaultState().withProperty(BlockPixieHouse.FACING, this.getCoordBaseMode() == null ? EnumFacing.NORTH : this.getCoordBaseMode().getOpposite()), 0, 0, 0, structureBoundingBoxIn);
            BlockPos blockpos = new BlockPos(this.getXWithOffset(0, 0), this.getYWithOffset(0), this.getZWithOffset(0, 0));
            if (worldIn.getTileEntity(blockpos) instanceof TileEntityPixieHouse) {
                int houseType = randomIn.nextInt(5);
                ((TileEntityPixieHouse) worldIn.getTileEntity(blockpos)).houseType = houseType;
            }
            worldIn.markBlockRangeForRenderUpdate(blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() + 1, blockpos.getY() + 1, blockpos.getZ() + 1);
            this.spawnVillagers(worldIn, structureBoundingBoxIn, 0, 1, 0, 1);
            return true;
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            super.readStructureFromNBT(tagCompound);
        }
    }
}