package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class WorldGenLightningDragonCave extends WorldGenDragonCave {

    public static ResourceLocation LIGHTNING_DRAGON_CHEST = new ResourceLocation(IceAndFire.MODID, "chest/lightning_dragon_female_cave");
    public static ResourceLocation LIGHTNING_DRAGON_CHEST_MALE = new ResourceLocation(IceAndFire.MODID, "chest/lightning_dragon_male_cave");

    public WorldGenLightningDragonCave(Codec<NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
        DRAGON_CHEST = LIGHTNING_DRAGON_CHEST;
        DRAGON_MALE_CHEST = LIGHTNING_DRAGON_CHEST_MALE;
        CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.CRACKLED_STONE, 9);
        PALETTE_BLOCK1 = IafBlockRegistry.CRACKLED_STONE.getDefaultState();
        PALETTE_BLOCK2 = IafBlockRegistry.CRACKLED_COBBLESTONE.getDefaultState();
        TREASURE_PILE = IafBlockRegistry.COPPER_PILE.getDefaultState();
        PALETTE_ORE1 = IafBlockRegistry.AMYTHEST_ORE.getDefaultState();
        PALETTE_ORE2 = Blocks.EMERALD_ORE.getDefaultState();
        generateGemOre = IafConfig.generateAmythestOre;
    }

    @Override
    EntityDragonBase createDragon(ISeedReader worldIn, Random rand, BlockPos position, int dragonAge) {
        EntityLightningDragon dragon = new EntityLightningDragon(IafEntityRegistry.LIGHTNING_DRAGON.get(),
            worldIn.getWorld());
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
