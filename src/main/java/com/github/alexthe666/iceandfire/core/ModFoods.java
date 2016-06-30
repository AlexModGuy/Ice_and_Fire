package com.github.alexthe666.iceandfire.core;

import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import fossilsarcheology.api.FoodMappings;

public class ModFoods {

	public static void init() {
		FoodMappings.instance().addPlant(Items.REEDS, 15);
		FoodMappings.instance().addPlant(Items.WHEAT, 13);
		FoodMappings.instance().addPlant(Items.MELON, 10);
		FoodMappings.instance().addPlant(Items.APPLE, 20);
		FoodMappings.instance().addPlant(Items.BAKED_POTATO, 35);
		FoodMappings.instance().addPlant(Items.CARROT, 15);
		FoodMappings.instance().addPlant(Items.COOKIE, 10);
		FoodMappings.instance().addPlant(Items.PUMPKIN_PIE, 25);
		FoodMappings.instance().addPlant(Items.SUGAR, 7);
		FoodMappings.instance().addPlant(Items.BREAD, 25);
		FoodMappings.instance().addPlant(Items.WHEAT_SEEDS, 5);
		FoodMappings.instance().addPlant(Items.MELON_SEEDS, 5);
		FoodMappings.instance().addPlant(Items.PUMPKIN_SEEDS, 5);
		FoodMappings.instance().addPlant(Items.BEETROOT_SEEDS, 5);
		FoodMappings.instance().addPlant(Blocks.CARROTS, 20);
		FoodMappings.instance().addPlant(Blocks.LEAVES, 20);
		FoodMappings.instance().addPlant(Blocks.LEAVES2, 20);
		FoodMappings.instance().addPlant(Blocks.MELON_BLOCK, 65);
		FoodMappings.instance().addPlant(Blocks.HAY_BLOCK, 150);
		FoodMappings.instance().addPlant(Blocks.BROWN_MUSHROOM, 15);
		FoodMappings.instance().addPlant(Blocks.RED_MUSHROOM, 15);
		FoodMappings.instance().addPlant(Blocks.RED_FLOWER, 5);
		FoodMappings.instance().addPlant(Blocks.YELLOW_FLOWER, 5);
		FoodMappings.instance().addPlant(Blocks.POTATOES, 25);
		FoodMappings.instance().addPlant(Blocks.PUMPKIN, 30);
		FoodMappings.instance().addPlant(Blocks.SAPLING, 15);
		FoodMappings.instance().addPlant(Blocks.TALLGRASS, 5);
		FoodMappings.instance().addFish(Items.FISH, 30);
		FoodMappings.instance().addFish(Items.COOKED_FISH, 45);
		FoodMappings.instance().addMeat(Items.COOKED_BEEF, 60);
		FoodMappings.instance().addMeat(Items.BEEF, 40);
		FoodMappings.instance().addMeat(Items.COOKED_CHICKEN, 15);
		FoodMappings.instance().addMeat(Items.CHICKEN, 10);
		FoodMappings.instance().addMeat(Items.PORKCHOP, 35);
		FoodMappings.instance().addMeat(Items.COOKED_PORKCHOP, 55);
		FoodMappings.instance().addMeat(Items.MUTTON, 35);
		FoodMappings.instance().addMeat(Items.COOKED_MUTTON, 55);
		FoodMappings.instance().addMeat(Items.RABBIT, 10);
		FoodMappings.instance().addMeat(Items.COOKED_RABBIT, 15);
		FoodMappings.instance().addMeat(EntityPlayer.class, 27);
		FoodMappings.instance().addMeat(EntityPlayerMP.class, 27);
		FoodMappings.instance().addMeat(EntityVillager.class, 27);
		FoodMappings.instance().addMeat(EntityZombie.class, 23);
		FoodMappings.instance().addMeat(EntityChicken.class, 5);
		FoodMappings.instance().addMeat(EntityCow.class, 40);
		FoodMappings.instance().addMeat(EntityHorse.class, 55);
		FoodMappings.instance().addMeat(EntityPig.class, 20);
		FoodMappings.instance().addMeat(EntitySheep.class, 35);
		FoodMappings.instance().addMeat(EntityRabbit.class, 20);
		FoodMappings.instance().addFish(EntitySquid.class, 30);
		FoodMappings.instance().addFish(EntityWolf.class, 30);
		FoodMappings.instance().addFish(EntityPolarBear.class, 70);
	}
}
