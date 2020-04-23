package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.storage.loot.LootTableList;

import javax.annotation.Nullable;
import java.util.Random;

public class DreadRuinProcessor implements ITemplateProcessor {

    private float integrity = 1.0F;
    public static final ResourceLocation DREAD_CHEST_LOOT = LootTableList.register(new ResourceLocation("iceandfire", "mausoleum_chest"));

    public DreadRuinProcessor(BlockPos position, PlacementSettings settings, @Nullable Biome biome) {
    }

    @Nullable
    @Override
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        if (worldIn.rand.nextFloat() <= integrity) {
            if (blockInfoIn.blockState.getBlock() == IafBlockRegistry.dread_stone_bricks) {
                IBlockState state = getRandomCrackedBlock(null, worldIn.rand);
                return new Template.BlockInfo(pos, state, null);
            }
            if (blockInfoIn.blockState.getBlock() instanceof BlockChest) {
                ResourceLocation loot = DREAD_CHEST_LOOT;
                Random rand = new Random(worldIn.getSeed() + pos.toLong());
                NBTTagCompound tag = blockInfoIn.tileentityData == null ? new NBTTagCompound() : blockInfoIn.tileentityData;
                tag.setString("LootTable", loot.toString());
                tag.setLong("LootTableSeed", rand.nextLong());
                Template.BlockInfo newInfo = new Template.BlockInfo(pos, Blocks.CHEST.getDefaultState(), tag);
                return newInfo;
            }
            if (blockInfoIn.blockState.getBlock() == IafBlockRegistry.dread_spawner) {
                NBTTagCompound tag = blockInfoIn.tileentityData == null ? new NBTTagCompound() : blockInfoIn.tileentityData;
                NBTTagCompound spawnData = new NBTTagCompound();
                Random rand = new Random(worldIn.getSeed() + pos.toLong());
                ResourceLocation spawnerMobId = EntityList.getKey(getRandomMobForMobSpawner(rand));
                if(spawnerMobId != null){
                    spawnData.setString("id", spawnerMobId.toString());
                    tag.removeTag("SpawnPotentials");
                    tag.setTag("SpawnData", spawnData.copy());
                }
                Template.BlockInfo newInfo = new Template.BlockInfo(pos, IafBlockRegistry.dread_spawner.getDefaultState(), tag);
                return newInfo;

            }
            return blockInfoIn;
        }
        return blockInfoIn;

    }

    private Class<? extends Entity> getRandomMobForMobSpawner(Random random) {
        float rand = random.nextFloat();
        if(rand < 0.3D){
            return EntityDreadThrall.class;
        }else if(rand < 0.5D){
            return EntityDreadGhoul.class;
        }else if(rand < 0.7D){
            return EntityDreadBeast.class;
        }else if(rand < 0.85D){
            return EntityDreadScuttler.class;
        }
        return EntityDreadKnight.class;
    }

    public static IBlockState getRandomCrackedBlock(@Nullable IBlockState prev, Random random) {
        float rand = random.nextFloat();
        if (rand < 0.5) {
            return IafBlockRegistry.dread_stone_bricks.getDefaultState();
        } else if (rand < 0.9) {
            return IafBlockRegistry.dread_stone_bricks_cracked.getDefaultState();
        } else {
            return IafBlockRegistry.dread_stone_bricks_mossy.getDefaultState();
        }
    }
}
