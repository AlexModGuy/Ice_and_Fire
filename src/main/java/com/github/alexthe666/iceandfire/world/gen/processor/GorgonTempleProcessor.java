package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.BlockGhostChest;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

public class GorgonTempleProcessor extends StructureProcessor {

    private float integrity = 1.0F;

    public GorgonTempleProcessor() {
    }


    public Template.BlockInfo func_230386_a_(IWorldReader worldReader, BlockPos pos, BlockPos pos2, Template.BlockInfo infoIn1, Template.BlockInfo infoIn2, PlacementSettings settings) {
        if(infoIn2.state.getBlock() instanceof StairsBlock) {
            return new Template.BlockInfo(infoIn2.pos, infoIn2.state.with(StairsBlock.WATERLOGGED, false), null);
        }else if(infoIn2.state.getBlock() instanceof WallBlock) {
            return new Template.BlockInfo(infoIn2.pos, infoIn2.state.with(WallBlock.field_235616_f_, false), null);
        }else if(infoIn2.state.getBlock() instanceof IWaterLoggable){
            return new Template.BlockInfo(infoIn2.pos, infoIn2.state.with(BlockStateProperties.WATERLOGGED, false), null);
        }else{
            BlockPos blockpos = infoIn2.pos;
            boolean flag = worldReader.getBlockState(blockpos).isIn(Blocks.WATER);
            return flag && !Block.isOpaque(infoIn2.state.getShape(worldReader, blockpos)) ? new Template.BlockInfo(blockpos, Blocks.AIR.getDefaultState(), infoIn2.nbt) : infoIn2;
        }
    }


   /* @Nullable
    @Override
    public Template.BlockInfo process(IWorldReader worldIn, BlockPos pos, Template.BlockInfo blockInfoIn, Template.BlockInfo blockInfoIn2, PlacementSettings settings) {
        Random random = settings.getRandom(pos);
        if (random.nextFloat() <= integrity) {
            if (blockInfoIn.state.getBlock() == IafBlockRegistry.DREAD_STONE_BRICKS) {
                BlockState state = getRandomCrackedBlock(null, random);
                return new Template.BlockInfo(pos, state, null);
            }
            if (blockInfoIn.state.getBlock() instanceof AbstractChestBlock) {
                ResourceLocation loot = DREAD_CHEST_LOOT;
                CompoundNBT tag = new CompoundNBT();
                tag.putString("LootTable", loot.toString());
                tag.putLong("LootTableSeed", random.nextLong());
                Template.BlockInfo newInfo = new Template.BlockInfo(pos, Blocks.CHEST.getDefaultState(), tag);
                return newInfo;
            }
            if (blockInfoIn.state.getBlock() == IafBlockRegistry.DREAD_SPAWNER) {
                CompoundNBT tag = new CompoundNBT();
                CompoundNBT spawnData = new CompoundNBT();
                ResourceLocation spawnerMobId = ForgeRegistries.ENTITIES.getKey(getRandomMobForMobSpawner(random));
                if (spawnerMobId != null) {
                    spawnData.putString("id", spawnerMobId.toString());
                    tag.remove("SpawnPotentials");
                    tag.put("SpawnData", spawnData.copy());
                }
                Template.BlockInfo newInfo = new Template.BlockInfo(pos, IafBlockRegistry.DREAD_SPAWNER.getDefaultState(), tag);
                return newInfo;

            }
            return blockInfoIn;
        }
        return blockInfoIn;

    }*/

    @Override
    protected IStructureProcessorType getType() {
        return IStructureProcessorType.BLOCK_ROT;
    }

    private EntityType getRandomMobForMobSpawner(Random random) {
        float rand = random.nextFloat();
        if (rand < 0.3D) {
            return IafEntityRegistry.DREAD_THRALL;
        } else if (rand < 0.5D) {
            return IafEntityRegistry.DREAD_GHOUL;
        } else if (rand < 0.7D) {
            return IafEntityRegistry.DREAD_BEAST;
        } else if (rand < 0.85D) {
            return IafEntityRegistry.DREAD_SCUTTLER;
        }
        return IafEntityRegistry.DREAD_KNIGHT;
    }
}
