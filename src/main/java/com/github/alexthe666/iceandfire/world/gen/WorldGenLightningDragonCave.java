package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class WorldGenLightningDragonCave extends WorldGenDragonCave {

    public static ResourceLocation LIGHTNING_DRAGON_CHEST = new ResourceLocation(IceAndFire.MODID, "chest/lightning_dragon_female_cave");
    public static ResourceLocation LIGHTNING_DRAGON_CHEST_MALE = new ResourceLocation(IceAndFire.MODID, "chest/lightning_dragon_male_cave");

    public WorldGenLightningDragonCave(Codec<NoneFeatureConfiguration> configFactoryIn) {
        super(configFactoryIn);
        DRAGON_CHEST = LIGHTNING_DRAGON_CHEST;
        DRAGON_MALE_CHEST = LIGHTNING_DRAGON_CHEST_MALE;
        CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.CRACKLED_STONE.get(), 9);
        PALETTE_BLOCK1 = IafBlockRegistry.CRACKLED_STONE.get().defaultBlockState();
        PALETTE_BLOCK2 = IafBlockRegistry.CRACKLED_COBBLESTONE.get().defaultBlockState();
        TREASURE_PILE = IafBlockRegistry.COPPER_PILE.get().defaultBlockState();
        PALETTE_ORE1 = IafBlockRegistry.AMYTHEST_ORE.get().defaultBlockState();
        PALETTE_ORE2 = Blocks.EMERALD_ORE.defaultBlockState();
        generateGemOre = IafConfig.generateAmythestOre;
    }

    @Override
    EntityDragonBase createDragon(WorldGenLevel worldIn, Random rand, BlockPos position, int dragonAge) {
        EntityLightningDragon dragon = new EntityLightningDragon(IafEntityRegistry.LIGHTNING_DRAGON.get(),
            worldIn.getLevel());
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
