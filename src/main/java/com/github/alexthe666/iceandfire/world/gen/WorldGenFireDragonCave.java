package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class WorldGenFireDragonCave extends WorldGenDragonCave {

    public static ResourceLocation FIRE_DRAGON_CHEST = new ResourceLocation(IceAndFire.MODID, "chest/fire_dragon_female_cave");
    public static ResourceLocation FIRE_DRAGON_CHEST_MALE = new ResourceLocation(IceAndFire.MODID, "chest/fire_dragon_male_cave");

    public WorldGenFireDragonCave(Codec<NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
        DRAGON_CHEST = FIRE_DRAGON_CHEST;
        DRAGON_MALE_CHEST = FIRE_DRAGON_CHEST_MALE;
        CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.CHARRED_STONE, 3);
        PALETTE_BLOCK1 = IafBlockRegistry.CHARRED_STONE.getDefaultState();
        PALETTE_BLOCK2 = IafBlockRegistry.CHARRED_COBBLESTONE.getDefaultState();
        TREASURE_PILE = IafBlockRegistry.GOLD_PILE.getDefaultState();
        PALETTE_ORE1 = Blocks.EMERALD_ORE.getDefaultState();
        PALETTE_ORE2 = Blocks.EMERALD_ORE.getDefaultState();
        generateGemOre = true;
    }

    @Override
    EntityDragonBase createDragon(ISeedReader worldIn, Random rand, BlockPos position, int dragonAge) {
        EntityFireDragon dragon = new EntityFireDragon(IafEntityRegistry.FIRE_DRAGON.get(), worldIn.getWorld());
        dragon.setGender(isMale);
        dragon.growDragon(dragonAge);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(rand.nextInt(4));
        dragon.setPositionAndRotation(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        dragon.setQueuedToSit(true);
        dragon.homePos = new HomePosition(position, worldIn.getWorld());
        dragon.setHunger(50);
        return dragon;
    }

}
