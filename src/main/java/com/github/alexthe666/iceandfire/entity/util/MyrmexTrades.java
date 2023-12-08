package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class MyrmexTrades {
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> DESERT_WORKER;
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> JUNGLE_WORKER;
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> DESERT_SOLDIER;
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> JUNGLE_SOLDIER;
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> DESERT_SENTINEL;
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> JUNGLE_SENTINEL;
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> DESERT_ROYAL;
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> JUNGLE_ROYAL;
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> DESERT_QUEEN;
    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> JUNGLE_QUEEN;

    static {
        DESERT_WORKER = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new DesertResinForItemsTrade(Items.DIRT, 64, 1, 5),
                new DesertResinForItemsTrade(Items.SAND, 64, 1, 5),
                new ItemsForDesertResinTrade(Items.DEAD_BUSH, 2, 8, 5, 2),
                new DesertResinForItemsTrade(Items.BONE, 10, 1, 1),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForDesertResinTrade(Items.IRON_ORE, 1, 6, 3, 2),
                new DesertResinForItemsTrade(Items.SUGAR, 15, 2, 1),
                new ItemsForDesertResinTrade(Items.STICK, 1, 64, 5, 2),
                new ItemsForDesertResinTrade(IafItemRegistry.COPPER_NUGGET.get(), 1, 4, 10),
            }));
        JUNGLE_WORKER = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new JungleResinForItemsTrade(Items.DIRT, 64, 1, 5),
                new ItemsForJungleResinTrade(Items.MELON_SLICE, 1, 20, 3, 1),
                new ItemsForJungleResinTrade(Items.JUNGLE_LEAVES, 1, 64, 5, 1),
                new JungleResinForItemsTrade(Items.BONE, 10, 1, 5),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForJungleResinTrade(Items.GOLD_ORE, 2, 15, 3, 2),
                new JungleResinForItemsTrade(Items.SUGAR, 15, 2, 3),
                new ItemsForJungleResinTrade(Items.STICK, 1, 64, 5, 2),
                new ItemsForJungleResinTrade(IafItemRegistry.COPPER_NUGGET.get(), 1, 4, 10),
            }));
        DESERT_SOLDIER = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new DesertResinForItemsTrade(Items.BONE, 7, 1, 3),
                new DesertResinForItemsTrade(Items.FEATHER, 16, 3, 3),
                new DesertResinForItemsTrade(Items.GUNPOWDER, 5, 1, 4),
                new ItemsForDesertResinTrade(Items.RABBIT, 1, 3, 6, 2),
                new DesertResinForItemsTrade(Items.IRON_NUGGET, 4, 1, 4),
                new ItemsForDesertResinTrade(Items.CHICKEN, 2, 2, 7),
                new ItemsForDesertResinTrade(IafItemRegistry.SILVER_NUGGET.get(), 4, 1, 15),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForDesertResinTrade(Items.CACTUS, 1, 15, 6, 2),
                new ItemsForDesertResinTrade(Items.GOLD_NUGGET, 1, 4, 6, 2),
                new ItemsForDesertResinTrade(IafItemRegistry.TROLL_TUSK.get(), 6, 1, 4, 2),
                new DesertResinForItemsTrade(IafItemRegistry.DRAGON_BONE.get(), 6, 2, 3),
            }));
        JUNGLE_SOLDIER = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new JungleResinForItemsTrade(Items.BONE, 7, 1, 3),
                new JungleResinForItemsTrade(Items.FEATHER, 16, 3, 3),
                new JungleResinForItemsTrade(Items.GUNPOWDER, 5, 1, 4),
                new ItemsForJungleResinTrade(Items.EGG, 1, 4, 6, 2),
                new JungleResinForItemsTrade(Items.IRON_NUGGET, 4, 1, 4),
                new ItemsForJungleResinTrade(Items.CHICKEN, 2, 2, 7),
                new ItemsForJungleResinTrade(IafItemRegistry.SILVER_NUGGET.get(), 1, 4, 15),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForJungleResinTrade(Items.ROTTEN_FLESH, 1, 15, 6, 2),
                new ItemsForJungleResinTrade(Items.GOLD_NUGGET, 1, 4, 6, 2),
                new ItemsForJungleResinTrade(IafItemRegistry.TROLL_TUSK.get(), 6, 1, 4, 2),
                new JungleResinForItemsTrade(IafItemRegistry.DRAGON_BONE.get(), 6, 2, 3),
            }));
        DESERT_SENTINEL = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new DesertResinForItemsTrade(Items.SPIDER_EYE, 10, 2, 3),
                new DesertResinForItemsTrade(Items.POISONOUS_POTATO, 2, 1, 2),
                new DesertResinForItemsTrade(Items.PUFFERFISH, 4, 2, 4),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForDesertResinTrade(Items.REDSTONE, 2, 5, 5, 1),
                new ItemsForDesertResinTrade(Items.PORKCHOP, 2, 3, 4),
                new ItemsForDesertResinTrade(Items.BEEF, 2, 3, 4),
                new ItemsForDesertResinTrade(Items.MUTTON, 2, 3, 4),
                new ItemsForDesertResinTrade(Items.SKELETON_SKULL, 15, 1, 2, 1),
            }));
        JUNGLE_SENTINEL = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new JungleResinForItemsTrade(Items.SPIDER_EYE, 10, 2, 3),
                new JungleResinForItemsTrade(Items.POISONOUS_POTATO, 2, 1, 2),
                new JungleResinForItemsTrade(Items.PUFFERFISH, 4, 2, 4),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForJungleResinTrade(Items.REDSTONE, 2, 5, 5, 1),
                new ItemsForJungleResinTrade(Items.PORKCHOP, 2, 3, 4),
                new ItemsForJungleResinTrade(Items.BEEF, 2, 3, 4),
                new ItemsForJungleResinTrade(Items.MUTTON, 2, 3, 4),
                new ItemsForJungleResinTrade(Items.SKELETON_SKULL, 15, 1, 2, 1),
            }));
        DESERT_ROYAL = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new ItemsForDesertResinTrade(IafItemRegistry.MANUSCRIPT.get(), 1, 3, 5, 1),
                new ItemsForDesertResinTrade(IafItemRegistry.WITHER_SHARD.get(), 3, 1, 3, 1),
                new ItemsForDesertResinTrade(Items.EMERALD, 10, 1, 3, 1),
                new ItemsForDesertResinTrade(Items.QUARTZ, 2, 4, 3, 1),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForDesertResinTrade(Items.GOLDEN_CARROT, 3, 1, 2, 1),
                new ItemsForDesertResinTrade(Items.MAGMA_CREAM, 5, 1, 3, 1),
                new ItemsForDesertResinTrade(Items.GOLD_INGOT, 3, 1, 5, 1),
                new ItemsForDesertResinTrade(IafItemRegistry.SILVER_INGOT.get(), 3, 1, 5, 1),
                new ItemsForDesertResinTrade(Items.COPPER_INGOT, 2, 2, 3, 1),
                new ItemsForDesertResinTrade(Items.ENDER_PEARL, 8, 1, 5, 1),
                new ItemsForDesertResinTrade(Items.RABBIT_FOOT, 3, 1, 5, 1),
            }));
        JUNGLE_ROYAL = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new ItemsForJungleResinTrade(IafItemRegistry.MANUSCRIPT.get(), 1, 3, 5, 1),
                new ItemsForJungleResinTrade(IafItemRegistry.WITHER_SHARD.get(), 3, 1, 3, 1),
                new ItemsForJungleResinTrade(Items.EMERALD, 10, 1, 3, 1),
                new ItemsForJungleResinTrade(Items.QUARTZ, 2, 4, 3, 1),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForJungleResinTrade(Items.GOLDEN_CARROT, 3, 1, 2, 1),
                new ItemsForJungleResinTrade(Items.MAGMA_CREAM, 5, 1, 3, 1),
                new ItemsForJungleResinTrade(Items.GOLD_INGOT, 3, 1, 5, 1),
                new ItemsForJungleResinTrade(IafItemRegistry.SILVER_INGOT.get(), 3, 1, 5, 1),
                new ItemsForJungleResinTrade(Items.COPPER_INGOT, 2, 2, 3, 1),
                new ItemsForJungleResinTrade(Items.ENDER_PEARL, 8, 1, 5, 1),
                new ItemsForJungleResinTrade(Items.RABBIT_FOOT, 3, 1, 5, 1),
            }));

        DESERT_QUEEN = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new ItemsForDesertResinTrade(createEgg(false, 0), 10, 1, 10, 1),
                new ItemsForDesertResinTrade(createEgg(false, 1), 20, 1, 8, 1),
                new ItemsForDesertResinTrade(createEgg(false, 2), 30, 1, 5, 1),
                new ItemsForDesertResinTrade(createEgg(false, 3), 40, 1, 3, 1),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForDesertResinTrade(createEgg(false, 4), 60, 1, 2, 1),
                new ItemsForDesertResinTrade(Items.EMERALD, 15, 1, 9, 1),
                new ItemsForDesertResinTrade(Items.DIAMOND, 25, 1, 9, 1),
            }));
        JUNGLE_QUEEN = createTrades(ImmutableMap.of(1,
            new VillagerTrades.ItemListing[]{
                new ItemsForJungleResinTrade(createEgg(true, 0), 10, 1, 10, 1),
                new ItemsForJungleResinTrade(createEgg(true, 1), 20, 1, 8, 1),
                new ItemsForJungleResinTrade(createEgg(true, 2), 30, 1, 5, 1),
                new ItemsForJungleResinTrade(createEgg(true, 3), 40, 1, 3, 1),
            },
            //Only 3 of these appears per myrmex
            2, new VillagerTrades.ItemListing[]{
                new ItemsForJungleResinTrade(createEgg(true, 4), 60, 1, 2, 1),
                new ItemsForDesertResinTrade(Items.EMERALD, 15, 1, 9, 1),
                new ItemsForDesertResinTrade(Items.DIAMOND, 25, 1, 9, 1),
            }));
    }

    private static ItemStack createEgg(boolean jungle, int caste){
        ItemStack egg = new ItemStack(jungle ? IafItemRegistry.MYRMEX_JUNGLE_EGG.get() : IafItemRegistry.MYRMEX_DESERT_EGG.get());
        CompoundTag tag = new CompoundTag();
        tag.putInt("EggOrdinal", caste);
        egg.setTag(tag);
        return egg;
    }

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> createTrades(ImmutableMap<Integer, VillagerTrades.ItemListing[]> p_221238_0_) {
        return new Int2ObjectOpenHashMap(p_221238_0_);
    }

    static class ItemsForDesertResinAndItemsTrade implements VillagerTrades.ItemListing {
        private final ItemStack buyingItem;
        private final int buyingItemCount;
        private final int emeraldCount;
        private final ItemStack sellingItem;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForDesertResinAndItemsTrade(ItemLike buyingItem, int buyingItemCount, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this(buyingItem, buyingItemCount, 1, sellingItem, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForDesertResinAndItemsTrade(ItemLike buyingItem, int buyingItemCount, int emeraldCount, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this.buyingItem = new ItemStack(buyingItem);
            this.buyingItemCount = buyingItemCount;
            this.emeraldCount = emeraldCount;
            this.sellingItem = new ItemStack(sellingItem);
            this.sellingItemCount = sellingItemCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        @Nullable
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN.get(), this.emeraldCount), new ItemStack(this.buyingItem.getItem(), this.buyingItemCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }


    static class ItemWithPotionForDesertResinAndItemsTrade implements VillagerTrades.ItemListing {
        private final ItemStack potionStack;
        private final int potionCount;
        private final int emeraldCount;
        private final int maxUses;
        private final int xpValue;
        private final Item buyingItem;
        private final int buyingItemCount;
        private final float priceMultiplier;

        public ItemWithPotionForDesertResinAndItemsTrade(Item buyingItem, int buyingItemCount, Item p_i50526_3_, int p_i50526_4_, int emeralds, int maxUses, int xpValue) {
            this.potionStack = new ItemStack(p_i50526_3_);
            this.emeraldCount = emeralds;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.buyingItem = buyingItem;
            this.buyingItemCount = buyingItemCount;
            this.potionCount = p_i50526_4_;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, RandomSource rand) {
            ItemStack lvt_3_1_ = new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN.get(), this.emeraldCount);
            List<Potion> lvt_4_1_ = BuiltInRegistries.POTION.stream().filter((potion) -> {
                return !potion.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(potion);
            }).collect(Collectors.toList());
            Potion lvt_5_1_ = lvt_4_1_.get(rand.nextInt(lvt_4_1_.size()));
            ItemStack lvt_6_1_ = PotionUtils.setPotion(new ItemStack(this.potionStack.getItem(), this.potionCount), lvt_5_1_);
            return new MerchantOffer(lvt_3_1_, new ItemStack(this.buyingItem, this.buyingItemCount), lvt_6_1_, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class EnchantedItemForDesertResinTrade implements VillagerTrades.ItemListing {
        private final ItemStack sellingStack;
        private final int emeraldCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public EnchantedItemForDesertResinTrade(Item p_i50535_1_, int emeraldCount, int maxUses, int xpValue) {
            this(p_i50535_1_, emeraldCount, maxUses, xpValue, 0.05F);
        }

        public EnchantedItemForDesertResinTrade(Item sellItem, int emeraldCount, int maxUses, int xpValue, float priceMultiplier) {
            this.sellingStack = new ItemStack(sellItem);
            this.emeraldCount = emeraldCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, RandomSource rand) {
            int lvt_3_1_ = 5 + rand.nextInt(15);
            ItemStack lvt_4_1_ = EnchantmentHelper.enchantItem(rand, new ItemStack(this.sellingStack.getItem()), lvt_3_1_, false);
            int lvt_5_1_ = Math.min(this.emeraldCount + lvt_3_1_, 64);
            ItemStack lvt_6_1_ = new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN.get(), lvt_5_1_);
            return new MerchantOffer(lvt_6_1_, lvt_4_1_, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class SuspiciousStewForEmeraldTrade implements VillagerTrades.ItemListing {
        final MobEffect effect;
        final int duration;
        final int xpValue;
        private final float priceMultiplier;

        public SuspiciousStewForEmeraldTrade(MobEffect effectIn, int durationIn, int xpValue) {
            this.effect = effectIn;
            this.duration = durationIn;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        @Nullable
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            ItemStack lvt_3_1_ = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.saveMobEffect(lvt_3_1_, this.effect, this.duration);
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN.get(), 1), lvt_3_1_, 12, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemsForDesertResinTrade implements VillagerTrades.ItemListing {
        private final ItemStack stack;
        private final int emeraldCount;
        private final int itemCount;
        private final int maxUses;
        private final int exp;
        private final float multiplier;

        public ItemsForDesertResinTrade(Block sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForDesertResinTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, 12, xpValue);
        }

        public ItemsForDesertResinTrade(Item item, int DesertResin, int items, int maxUses, int exp) {
            this(new ItemStack(item), DesertResin, items, maxUses, exp);
        }

        public ItemsForDesertResinTrade(ItemStack stack, int DesertResin, int items, int maxUses, int exp) {
            this(stack, DesertResin, items, maxUses, exp, 0.05F);
        }

        public ItemsForDesertResinTrade(ItemStack stack, int DesertResin, int items, int maxUses, int exp, float multi) {
            this.stack = stack;
            this.emeraldCount = DesertResin;
            this.itemCount = items;
            this.maxUses = maxUses;
            this.exp = exp;
            this.multiplier = multi;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            ItemStack cloneStack = new ItemStack(this.stack.getItem(), this.itemCount);
            cloneStack.setTag(this.stack.getTag());
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN.get(), this.emeraldCount), cloneStack, this.maxUses, this.exp, this.multiplier);
        }
    }

    static class DesertResinForItemsTrade implements VillagerTrades.ItemListing {
        private final Item tradeItem;
        private final int count;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public DesertResinForItemsTrade(ItemLike tradeItemIn, int countIn, int maxUsesIn, int xpValueIn) {
            this.tradeItem = tradeItemIn.asItem();
            this.count = countIn;
            this.maxUses = maxUsesIn;
            this.xpValue = xpValueIn;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            ItemStack lvt_3_1_ = new ItemStack(this.tradeItem, this.count);
            return new MerchantOffer(lvt_3_1_, new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN.get()), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }


    static class ItemsForJungleResinAndItemsTrade implements VillagerTrades.ItemListing {
        private final ItemStack buyingItem;
        private final int buyingItemCount;
        private final int emeraldCount;
        private final ItemStack sellingItem;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForJungleResinAndItemsTrade(ItemLike buyingItem, int buyingItemCount, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this(buyingItem, buyingItemCount, 1, sellingItem, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForJungleResinAndItemsTrade(ItemLike buyingItem, int buyingItemCount, int emeraldCount, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this.buyingItem = new ItemStack(buyingItem);
            this.buyingItemCount = buyingItemCount;
            this.emeraldCount = emeraldCount;
            this.sellingItem = new ItemStack(sellingItem);
            this.sellingItemCount = sellingItemCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        @Nullable
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN.get(), this.emeraldCount), new ItemStack(this.buyingItem.getItem(), this.buyingItemCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemWithPotionForJungleResinAndItemsTrade implements VillagerTrades.ItemListing {
        private final ItemStack potionStack;
        private final int potionCount;
        private final int emeraldCount;
        private final int maxUses;
        private final int xpValue;
        private final Item buyingItem;
        private final int buyingItemCount;
        private final float priceMultiplier;

        public ItemWithPotionForJungleResinAndItemsTrade(Item buyingItem, int buyingItemCount, Item p_i50526_3_, int p_i50526_4_, int emeralds, int maxUses, int xpValue) {
            this.potionStack = new ItemStack(p_i50526_3_);
            this.emeraldCount = emeralds;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.buyingItem = buyingItem;
            this.buyingItemCount = buyingItemCount;
            this.potionCount = p_i50526_4_;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, RandomSource rand) {
            ItemStack lvt_3_1_ = new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN.get(), this.emeraldCount);
            List<Potion> lvt_4_1_ = BuiltInRegistries.POTION.stream().filter((potion) -> {
                return !potion.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(potion);
            }).collect(Collectors.toList());
            Potion lvt_5_1_ = lvt_4_1_.get(rand.nextInt(lvt_4_1_.size()));
            ItemStack lvt_6_1_ = PotionUtils.setPotion(new ItemStack(this.potionStack.getItem(), this.potionCount), lvt_5_1_);
            return new MerchantOffer(lvt_3_1_, new ItemStack(this.buyingItem, this.buyingItemCount), lvt_6_1_, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class EnchantedItemForJungleResinTrade implements VillagerTrades.ItemListing {
        private final ItemStack sellingStack;
        private final int emeraldCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public EnchantedItemForJungleResinTrade(Item p_i50535_1_, int emeraldCount, int maxUses, int xpValue) {
            this(p_i50535_1_, emeraldCount, maxUses, xpValue, 0.05F);
        }

        public EnchantedItemForJungleResinTrade(Item sellItem, int emeraldCount, int maxUses, int xpValue, float priceMultiplier) {
            this.sellingStack = new ItemStack(sellItem);
            this.emeraldCount = emeraldCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, RandomSource rand) {
            int lvt_3_1_ = 5 + rand.nextInt(15);
            ItemStack lvt_4_1_ = EnchantmentHelper.enchantItem(rand, new ItemStack(this.sellingStack.getItem()), lvt_3_1_, false);
            int lvt_5_1_ = Math.min(this.emeraldCount + lvt_3_1_, 64);
            ItemStack lvt_6_1_ = new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN.get(), lvt_5_1_);
            return new MerchantOffer(lvt_6_1_, lvt_4_1_, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemsForJungleResinTrade implements VillagerTrades.ItemListing {
        private final ItemStack stack;
        private final int emeraldCount;
        private final int itemCount;
        private final int maxUses;
        private final int exp;
        private final float multiplier;

        public ItemsForJungleResinTrade(Block sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForJungleResinTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, 12, xpValue);
        }

        public ItemsForJungleResinTrade(Item item, int JungleResin, int items, int maxUses, int exp) {
            this(new ItemStack(item), JungleResin, items, maxUses, exp);
        }

        public ItemsForJungleResinTrade(ItemStack stack, int JungleResin, int items, int maxUses, int exp) {
            this(stack, JungleResin, items, maxUses, exp, 0.05F);
        }

        public ItemsForJungleResinTrade(ItemStack stack, int JungleResin, int items, int maxUses, int exp, float multi) {
            this.stack = stack;
            this.emeraldCount = JungleResin;
            this.itemCount = items;
            this.maxUses = maxUses;
            this.exp = exp;
            this.multiplier = multi;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            ItemStack cloneStack = new ItemStack(this.stack.getItem(), this.itemCount);
            cloneStack.setTag(this.stack.getTag());
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN.get(), this.emeraldCount), cloneStack, this.maxUses, this.exp, this.multiplier);
        }
    }

    static class JungleResinForItemsTrade implements VillagerTrades.ItemListing {
        private final Item tradeItem;
        private final int count;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public JungleResinForItemsTrade(ItemLike tradeItemIn, int countIn, int maxUsesIn, int xpValueIn) {
            this.tradeItem = tradeItemIn.asItem();
            this.count = countIn;
            this.maxUses = maxUsesIn;
            this.xpValue = xpValueIn;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@NotNull Entity trader, @NotNull RandomSource rand) {
            ItemStack lvt_3_1_ = new ItemStack(this.tradeItem, this.count);
            return new MerchantOffer(lvt_3_1_, new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN.get()), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }
}

