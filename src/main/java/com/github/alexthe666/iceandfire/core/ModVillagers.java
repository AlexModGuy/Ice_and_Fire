package com.github.alexthe666.iceandfire.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModVillagers {

    public static final ModVillagers INSTANCE = new ModVillagers();

    public static final ResourceLocation PROFESSIONS = new ResourceLocation("iceandfire:snowvillagerprofessions");
    public VillagerRegistry.VillagerProfession fisherman;
    public VillagerRegistry.VillagerProfession craftsman;
    public VillagerRegistry.VillagerProfession shaman;
    public Map<Integer, VillagerRegistry.VillagerProfession> professions = Maps.newHashMap();

    public void init() {
        fisherman = new VillagerRegistry.VillagerProfession("iceandfire:fisherman", "iceandfire:textures/models/snowvillager/fisherman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(fisherman, "fisherman");
            career.addTrade(1, new SapphireForItems(Items.FISH, new EntityVillager.PriceInfo(1, 10)));
            career.addTrade(2, new ListItemForSapphires(Items.FISHING_ROD, new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(1, new SapphireForItems(ModItems.fishing_spear, new EntityVillager.PriceInfo(1, 5)));
            career.addTrade(2, new ListItemForSapphires(Items.COOKED_FISH, new EntityVillager.PriceInfo(5, 4)));
            register(fisherman, 0);
        }
        craftsman = new VillagerRegistry.VillagerProfession("iceandfire:craftsman", "iceandfire:textures/models/snowvillager/craftsman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(craftsman, "craftsman");
            //over 30 words for ice
            career.addTrade(1, new SapphireForItems(Item.getItemFromBlock(Blocks.SNOW), new EntityVillager.PriceInfo(1, 32)));
            career.addTrade(2, new ListItemForSapphires(Item.getItemFromBlock(Blocks.PACKED_ICE), new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(1, new ListItemForSapphires(Items.IRON_SHOVEL, new EntityVillager.PriceInfo(3, 1)));
            career.addTrade(2, new ListItemForSapphires(ModItems.silver_shovel, new EntityVillager.PriceInfo(5, 1)));
            career.addTrade(3, new ListItemForSapphires(Items.DIAMOND_SHOVEL, new EntityVillager.PriceInfo(9, 1)));
            career.addTrade(2, new ListItemForSapphires(Items.LEATHER, new EntityVillager.PriceInfo(1, 10)));
            career.addTrade(3, new ListItemForSapphires(Items.LEATHER_BOOTS, new EntityVillager.PriceInfo(3, 1)));
            career.addTrade(3, new ListItemForSapphires(Items.LEATHER_HELMET, new EntityVillager.PriceInfo(4, 1)));
            career.addTrade(3, new ListItemForSapphires(Items.LEATHER_CHESTPLATE, new EntityVillager.PriceInfo(6, 1)));
            career.addTrade(3, new ListItemForSapphires(Items.LEATHER_LEGGINGS, new EntityVillager.PriceInfo(5, 1)));
            career.addTrade(3, new SapphireForItems(Items.DIAMOND_SHOVEL, new EntityVillager.PriceInfo(7, 1)));
            register(craftsman, 1);
        }
        shaman = new VillagerRegistry.VillagerProfession("iceandfire:shaman", "iceandfire:textures/models/snowvillager/shaman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");
        {
            VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(shaman, "shaman");
            career.addTrade(1, new SapphireForItems(Items.BLAZE_POWDER, new EntityVillager.PriceInfo(2, 3)));
            career.addTrade(1, new SapphireForItems(Items.GHAST_TEAR, new EntityVillager.PriceInfo(1, 4)));
            career.addTrade(2, new SapphireForItems(Items.BREWING_STAND, new EntityVillager.PriceInfo(1, 9)));
            career.addTrade(1, new SapphireForItems(ModItems.dragonbone, new EntityVillager.PriceInfo(1, 12)));
            career.addTrade(2, new ListItemForSapphires(ModItems.bestiary, new EntityVillager.PriceInfo(1, 3)));
            career.addTrade(1, new ListItemForSapphires(ModItems.manuscript, new EntityVillager.PriceInfo(1, 2)));
            career.addTrade(3, new ListItemForSapphires(ModItems.ice_dragon_flesh, new EntityVillager.PriceInfo(1, 5)));
            career.addTrade(3, new ListItemForSapphires(ModItems.ice_dragon_blood, new EntityVillager.PriceInfo(1, 12)));
            career.addTrade(2, new ListItemForSapphires(Items.ENDER_EYE, new EntityVillager.PriceInfo(2, 5)));
            career.addTrade(2, new ListItemForSapphires(ModItems.witherbone, new EntityVillager.PriceInfo(2, 5)));
            career.addTrade(2, new ListItemForSapphires(ModItems.wither_shard, new EntityVillager.PriceInfo(3, 2)));
            register(shaman, 2);
        }
    }

    public void setRandomProfession(EntityVillager entity, Random rand) {
        entity.setProfession(professions.get(rand.nextInt(professions.size())));
    }

    private void register(VillagerRegistry.VillagerProfession prof, int id) {
        professions.put(id, prof);
    }

    /** Sell items for sapphires */
    public static class SapphireForItems implements EntityVillager.ITradeList {
        /** The item that is being sold for emeralds */
        public Item buyingItem;
        public EntityVillager.PriceInfo price;

        public SapphireForItems(Item itemIn, EntityVillager.PriceInfo priceIn) {
            this.buyingItem = itemIn;
            this.price = priceIn;
        }

        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
            int i = 1;
            if (this.price != null) {
                i = this.price.getPrice(random);
            }
            recipeList.add(new MerchantRecipe(new ItemStack(this.buyingItem, i, 0), ModItems.sapphireGem));
        }
    }

    /** Buy items for sapphires */
    public static class ListItemForSapphires implements EntityVillager.ITradeList {
        /** The item that is being bought for emeralds */
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

        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
            int i = 1;

            if (this.priceInfo != null) {
                i = this.priceInfo.getPrice(random);
            }

            ItemStack itemstack;
            ItemStack itemstack1;
            if (i < 0) {
                itemstack = new ItemStack(ModItems.sapphireGem);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
            }
            else {
                itemstack = new ItemStack(ModItems.sapphireGem, i, 0);
                itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
            }
            recipeList.add(new MerchantRecipe(itemstack, itemstack1));
        }
    }


}
