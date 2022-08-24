package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class WorldGenIceDragonCave extends WorldGenDragonCave {

    public static ResourceLocation ICE_DRAGON_CHEST = new ResourceLocation(IceAndFire.MODID, "chest/ice_dragon_female_cave");
    public static ResourceLocation ICE_DRAGON_CHEST_MALE = new ResourceLocation(IceAndFire.MODID, "chest/ice_dragon_male_cave");


    public WorldGenIceDragonCave(Codec<NoFeatureConfig> codec) {
        super(codec);
        DRAGON_CHEST = ICE_DRAGON_CHEST;
        DRAGON_MALE_CHEST = ICE_DRAGON_CHEST_MALE;
        CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.FROZEN_STONE, 3);
        PALETTE_BLOCK1 = IafBlockRegistry.FROZEN_STONE.defaultBlockState();
        PALETTE_BLOCK2 = IafBlockRegistry.FROZEN_COBBLESTONE.defaultBlockState();
        TREASURE_PILE = IafBlockRegistry.SILVER_PILE.defaultBlockState();
        PALETTE_ORE1 = IafBlockRegistry.SAPPHIRE_ORE.defaultBlockState();
        PALETTE_ORE2 = Blocks.EMERALD_ORE.defaultBlockState();
        generateGemOre = IafConfig.generateSapphireOre;
    }

    @Override
    EntityDragonBase createDragon(ISeedReader worldIn, Random rand, BlockPos position, int dragonAge) {
        EntityIceDragon dragon = new EntityIceDragon(IafEntityRegistry.ICE_DRAGON.get(), worldIn.getLevel());
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
