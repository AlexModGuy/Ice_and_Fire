package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.google.common.collect.Maps;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.Map;
import java.util.Random;

public class ModVillagers {

	public static final ModVillagers INSTANCE = new ModVillagers();

	public VillagerRegistry.VillagerProfession fisherman;
	public VillagerRegistry.VillagerProfession craftsman;
	public VillagerRegistry.VillagerProfession shaman;
	public VillagerRegistry.VillagerProfession desertMyrmexWorker;
	public VillagerRegistry.VillagerProfession jungleMyrmexWorker;
	public VillagerRegistry.VillagerProfession desertMyrmexSoldier;
	public VillagerRegistry.VillagerProfession jungleMyrmexSoldier;
	public VillagerRegistry.VillagerProfession desertMyrmexSentinel;
	public VillagerRegistry.VillagerProfession jungleMyrmexSentinel;
	public VillagerRegistry.VillagerProfession desertMyrmexRoyal;
	public VillagerRegistry.VillagerProfession jungleMyrmexRoyal;
	public VillagerRegistry.VillagerProfession desertMyrmexQueen;
	public VillagerRegistry.VillagerProfession jungleMyrmexQueen;
	public Map<Integer, VillagerRegistry.VillagerProfession> professions = Maps.newHashMap();

	public void init() {
		fisherman = new VillagerRegistry.VillagerProfession("iceandfire:fisherman", "iceandfire:textures/models/snowvillager/fisherman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(fisherman, "fisherman");
			career.addTrade(1, new SapphireForItems(Items.FISH, new EntityVillager.PriceInfo(1, 10)));
			career.addTrade(2, new ListItemForSapphires(Items.FISHING_ROD, new EntityVillager.PriceInfo(1, 3)));
			career.addTrade(1, new ListItemForSapphires(ModItems.fishing_spear, new EntityVillager.PriceInfo(1, 5)));
			career.addTrade(2, new ListItemForSapphires(Items.COOKED_FISH, new EntityVillager.PriceInfo(1, 5)));
			career.addTrade(2, new ListItemForSapphires(Items.COOKED_FISH, new EntityVillager.PriceInfo(1, 5)));
			career.addTrade(3, new ListItemForSapphires(new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()), new EntityVillager.PriceInfo(2, 1)));
			career.addTrade(3, new ListItemForSapphires(new ItemStack(Blocks.TRIPWIRE_HOOK), new EntityVillager.PriceInfo(1, 3)));
			career.addTrade(3, new ListItemForSapphires(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), new EntityVillager.PriceInfo(1, 4)));
			register(fisherman, 0);
		}
		craftsman = new VillagerRegistry.VillagerProfession("iceandfire:craftsman", "iceandfire:textures/models/snowvillager/craftsman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(craftsman, "craftsman");
			//over 30 words for ice
			career.addTrade(1, new SapphireForItems(Item.getItemFromBlock(Blocks.SNOW), new EntityVillager.PriceInfo(1, 32)));
			career.addTrade(2, new ListItemForSapphires(Item.getItemFromBlock(Blocks.PACKED_ICE), new EntityVillager.PriceInfo(1, 3)));
			career.addTrade(3, new ListItemForSapphires(Item.getItemFromBlock(ModBlocks.dragon_ice), new EntityVillager.PriceInfo(4, 1)));
			career.addTrade(1, new ListItemForSapphires(Items.IRON_SHOVEL, new EntityVillager.PriceInfo(1, 4)));
			career.addTrade(2, new ListItemForSapphires(ModItems.silver_shovel, new EntityVillager.PriceInfo(1, 5)));
			career.addTrade(3, new ListItemForSapphires(Items.DIAMOND_SHOVEL, new EntityVillager.PriceInfo(1, 9)));
			career.addTrade(2, new ListItemForSapphires(Items.LEATHER, new EntityVillager.PriceInfo(10, 1)));
			career.addTrade(3, new ListItemForSapphires(Items.LEATHER_BOOTS, new EntityVillager.PriceInfo(1, 3)));
			career.addTrade(3, new ListItemForSapphires(Items.LEATHER_HELMET, new EntityVillager.PriceInfo(1, 4)));
			career.addTrade(3, new ListItemForSapphires(Items.LEATHER_CHESTPLATE, new EntityVillager.PriceInfo(1, 6)));
			career.addTrade(3, new ListItemForSapphires(Items.LEATHER_LEGGINGS, new EntityVillager.PriceInfo(1, 6)));
			career.addTrade(3, new ListItemForSapphires(Items.DIAMOND_SHOVEL, new EntityVillager.PriceInfo(1, 7)));
			career.addTrade(3, new ListItemForSapphires(EnumTroll.FROST.leather, new EntityVillager.PriceInfo(1, 5)));
			register(craftsman, 1);
		}
		shaman = new VillagerRegistry.VillagerProfession("iceandfire:shaman", "iceandfire:textures/models/snowvillager/shaman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(shaman, "shaman");
			career.addTrade(1, new SapphireForItems(Items.BLAZE_POWDER, new EntityVillager.PriceInfo(2, 3)));
			career.addTrade(1, new SapphireForItems(Items.GHAST_TEAR, new EntityVillager.PriceInfo(1, 4)));
			career.addTrade(2, new SapphireForItems(Items.BREWING_STAND, new EntityVillager.PriceInfo(9, 1)));
			career.addTrade(1, new SapphireForItems(ModItems.dragonbone, new EntityVillager.PriceInfo(1, 12)));
			ItemStack stack = new ItemStack(ModItems.bestiary);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setIntArray("Pages", new int[]{EnumBestiaryPages.INTRODUCTION.ordinal(), EnumBestiaryPages.ICEDRAGON.ordinal(), EnumBestiaryPages.ICEDRAGONEGG.ordinal(), EnumBestiaryPages.MATERIALS.ordinal(), EnumBestiaryPages.VILLAGERS.ordinal()});
			career.addTrade(2, new ListItemForSapphires(stack, new EntityVillager.PriceInfo(1, 3)));
			career.addTrade(1, new ListItemForSapphires(ModItems.manuscript, new EntityVillager.PriceInfo(1, 2)));
			career.addTrade(3, new ListItemForSapphires(ModItems.ice_dragon_flesh, new EntityVillager.PriceInfo(1, 5)));
			career.addTrade(3, new ListItemForSapphires(ModItems.ice_dragon_blood, new EntityVillager.PriceInfo(1, 12)));
			career.addTrade(3, new ListItemForSapphires(ModItems.dragon_flute, new EntityVillager.PriceInfo(1, 5)));
			career.addTrade(2, new ListItemForSapphires(Items.ENDER_EYE, new EntityVillager.PriceInfo(2, 5)));
			career.addTrade(2, new ListItemForSapphires(ModItems.witherbone, new EntityVillager.PriceInfo(2, 5)));
			career.addTrade(2, new ListItemForSapphires(ModItems.wither_shard, new EntityVillager.PriceInfo(3, 2)));
			register(shaman, 2);
		}

		desertMyrmexWorker = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexWorker", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexWorker, "desert_myrmex_worker");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(32, 64), new EntityVillager.PriceInfo(1, 2)));
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.SAND), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(32, 64), new EntityVillager.PriceInfo(1, 2)));
			career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DEADBUSH), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(5, 10), new EntityVillager.PriceInfo(1, 2)));
			career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.IRON_ORE), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(10, 15), new EntityVillager.PriceInfo(1, 4)));
			career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(ModItems.myrmex_desert_resin), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(5, 15)));
			career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SUGAR), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 2)));
			career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.DIAMOND), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(3, 7)));
			career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(ModItems.myrmex_desert_resin), new ItemStack(ModItems.dragonbone), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(1, 3)));
		}

		jungleMyrmexWorker = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexWorker", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexWorker, "jungle_myrmex_worker");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(32, 64), new EntityVillager.PriceInfo(1, 2)));
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.MELON), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(10, 20), new EntityVillager.PriceInfo(1, 2)));
			career.addTrade(2, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.LEAVES, 1, 3), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(48, 64), new EntityVillager.PriceInfo(1, 1)));
			career.addTrade(3, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.GOLD_ORE), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(7, 10), new EntityVillager.PriceInfo(1, 4)));
			career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(ModItems.myrmex_jungle_resin), new ItemStack(Items.BONE), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(5, 15)));
			career.addTrade(4, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.SUGAR), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(5, 8), new EntityVillager.PriceInfo(1, 2)));
			career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(Items.DIAMOND), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 1), new EntityVillager.PriceInfo(3, 7)));
			career.addTrade(5, new EntityMyrmexBase.BasicTrade(new ItemStack(ModItems.myrmex_jungle_resin), new ItemStack(Items.EMERALD), new EntityVillager.PriceInfo(1, 5), new EntityVillager.PriceInfo(1, 1)));
		}

		desertMyrmexSoldier = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexSoldier", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexSoldier, "desert_myrmex_soldier");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 2)));
		}

		jungleMyrmexSoldier = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexSoldier", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexSoldier, "jungle_myrmex_soldier");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 2)));
		}

		desertMyrmexSentinel = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexSentinel", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexSentinel, "desert_myrmex_sentinel");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 2)));
		}

		jungleMyrmexSentinel = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexSentinel", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexSentinel, "jungle_myrmex_sentinel");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 2)));
		}

		desertMyrmexRoyal = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexRoyal", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexRoyal, "desert_myrmex_royal");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 2)));
		}

		jungleMyrmexRoyal = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexRoyal", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexRoyal, "jungle_myrmex_royal");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 2)));
		}

		desertMyrmexQueen = new VillagerRegistry.VillagerProfession("iceandfire:desertMyrmexQueen", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(desertMyrmexQueen, "desert_myrmex_queen");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_desert_resin), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 2)));
		}

		jungleMyrmexQueen = new VillagerRegistry.VillagerProfession("iceandfire:jungleMyrmexQueen", "minecraft:textures/entity/zombie_villager/zombie_farmer.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
		{
			VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(jungleMyrmexQueen, "jungle_myrmex_queen");
			career.addTrade(1, new EntityMyrmexBase.BasicTrade(new ItemStack(Blocks.DIRT), new ItemStack(ModItems.myrmex_jungle_resin), new EntityVillager.PriceInfo(1, 10), new EntityVillager.PriceInfo(1, 2)));
		}
	}

	public void setRandomProfession(EntityVillager entity, Random rand) {
		entity.setProfession(professions.get(rand.nextInt(professions.size())));
	}

	private void register(VillagerRegistry.VillagerProfession prof, int id) {
		professions.put(id, prof);
	}

	/**
	 * Sell items for sapphires
	 */
	public static class SapphireForItems implements EntityVillager.ITradeList {
		/**
		 * The item that is being sold for emeralds
		 */
		public Item buyingItem;
		public EntityVillager.PriceInfo price;

		public SapphireForItems(Item itemIn, EntityVillager.PriceInfo priceIn) {
			this.buyingItem = itemIn;
			this.price = priceIn;
		}

		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
			int i = 1;
			if (this.price != null) {
				i = this.price.getPrice(random);
			}
			recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItem, i, 0), ModItems.sapphireGem));
		}
	}

	/**
	 * Buy items for sapphires
	 */
	public static class ListItemForSapphires implements EntityVillager.ITradeList {
		/**
		 * The item that is being bought for emeralds
		 */
		public ItemStack itemToBuy;
		public EntityVillager.PriceInfo priceInfo;

		public ListItemForSapphires(Item par1Item, EntityVillager.PriceInfo priceInfo) {
			this.itemToBuy = new ItemStack(par1Item);
			this.priceInfo = priceInfo;
		}

		public ListItemForSapphires(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
			this.itemToBuy = stack;
			this.priceInfo = priceInfo;
		}

		@Override
		public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (this.priceInfo != null) {
				i = this.priceInfo.getPrice(random);
			}

			ItemStack itemstack;
			ItemStack itemstack1;
			if (i < 0) {
				itemstack = new ItemStack(ModItems.sapphireGem);
				itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
			} else {
				itemstack = new ItemStack(ModItems.sapphireGem, i, 0);
				itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
			}
			recipeList.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}


}
