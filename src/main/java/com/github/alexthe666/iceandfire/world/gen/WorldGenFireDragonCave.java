package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class WorldGenFireDragonCave extends WorldGenDragonCave {

    public static ResourceLocation FIRE_DRAGON_CHEST = new ResourceLocation(IceAndFire.MODID, "chest/fire_dragon_female_cave");
    public static ResourceLocation FIRE_DRAGON_CHEST_MALE = new ResourceLocation(IceAndFire.MODID, "chest/fire_dragon_male_cave");

    public WorldGenFireDragonCave(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
        DRAGON_CHEST = FIRE_DRAGON_CHEST;
        DRAGON_MALE_CHEST = FIRE_DRAGON_CHEST_MALE;
        CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.CHARRED_STONE.get(), 3);
        PALETTE_BLOCK1 = IafBlockRegistry.CHARRED_STONE.get().defaultBlockState();
        PALETTE_BLOCK2 = IafBlockRegistry.CHARRED_COBBLESTONE.get().defaultBlockState();
        TREASURE_PILE = IafBlockRegistry.GOLD_PILE.get().defaultBlockState();
        PALETTE_ORE1 = Blocks.EMERALD_ORE.defaultBlockState();
        PALETTE_ORE2 = Blocks.EMERALD_ORE.defaultBlockState();
        generateGemOre = true;
    }

    @Override
    EntityDragonBase createDragon(WorldGenLevel worldIn, Random rand, BlockPos position, int dragonAge) {
        EntityFireDragon dragon = new EntityFireDragon(IafEntityRegistry.FIRE_DRAGON.get(), worldIn.getLevel());
        dragon.setGender(isMale);
        dragon.growDragon(dragonAge);
        dragon.setAgingDisabled(true);
        dragon.setHealth(dragon.getMaxHealth());
        dragon.setVariant(rand.nextInt(4));
        dragon.absMoveTo(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, rand.nextFloat() * 360, 0);
        dragon.setInSittingPose(true);
        dragon.homePos = new HomePosition(position, worldIn.getLevel());
        dragon.setHunger(50);
        return dragon;
    }

}
