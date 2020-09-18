package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.villager.IVillagerDataHolder;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class MyrmexTrades {
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> DESERT_WORKER;
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> JUNGLE_WORKER;
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> DESERT_SOLDIER;
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> JUNGLE_SOLDIER;
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> DESERT_SENTINEL;
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> JUNGLE_SENTINEL;
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> DESERT_ROYAL;
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> JUNGLE_ROYAL;
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> DESERT_QUEEN;
    public static final Int2ObjectMap<VillagerTrades.ITrade[]> JUNGLE_QUEEN;

    static {
        DESERT_WORKER = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new DesertResinForItemsTrade(Items.DIRT, 64, 1, 5),
                        new DesertResinForItemsTrade(Items.SAND, 64, 1, 5),
                        new ItemsForDesertResinTrade(Items.DEAD_BUSH, 2, 8, 5, 2),
                        new DesertResinForItemsTrade(Items.BONE, 10, 1, 1),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForDesertResinTrade(Items.IRON_ORE, 1, 6, 3, 2),
                        new DesertResinForItemsTrade(Items.SUGAR, 15, 2, 1),
                        new ItemsForDesertResinTrade(Items.STICK, 1, 64, 5, 2),
                        new ItemsForDesertResinTrade(IafItemRegistry.COPPER_NUGGET, 1, 4, 10),
                }));
        JUNGLE_WORKER = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new JungleResinForItemsTrade(Items.DIRT, 64, 1, 5),
                        new ItemsForJungleResinTrade(Items.MELON_SLICE, 1, 20, 3, 1),
                        new ItemsForJungleResinTrade(Items.JUNGLE_LEAVES, 1, 64, 5, 1),
                        new JungleResinForItemsTrade(Items.BONE, 10, 1, 5),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForJungleResinTrade(Items.GOLD_ORE, 2, 15, 3, 2),
                        new JungleResinForItemsTrade(Items.SUGAR, 15, 2, 3),
                        new ItemsForJungleResinTrade(Items.STICK, 1, 64, 5, 2),
                        new ItemsForJungleResinTrade(IafItemRegistry.COPPER_NUGGET, 1, 4, 10),
                }));
        DESERT_SOLDIER = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new DesertResinForItemsTrade(Items.BONE, 7, 1, 3),
                        new DesertResinForItemsTrade(Items.FEATHER, 16, 3, 3),
                        new DesertResinForItemsTrade(Items.GUNPOWDER, 5, 1, 4),
                        new ItemsForDesertResinTrade(Items.RABBIT, 1, 3, 6, 2),
                        new DesertResinForItemsTrade(Items.IRON_NUGGET, 4, 1, 4),
                        new ItemsForDesertResinTrade(Items.CHICKEN, 2, 2, 7),
                        new ItemsForDesertResinTrade(IafItemRegistry.SILVER_NUGGET, 4, 1, 15),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForDesertResinTrade(Items.CACTUS, 1, 15, 6, 2),
                        new ItemsForDesertResinTrade(Items.GOLD_NUGGET, 1, 4, 6, 2),
                        new ItemsForDesertResinTrade(IafItemRegistry.TROLL_TUSK, 6, 1, 4, 2),
                        new DesertResinForItemsTrade(IafItemRegistry.DRAGON_BONE, 6, 2, 3),
                }));
        JUNGLE_SOLDIER = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new JungleResinForItemsTrade(Items.BONE, 7, 1, 3),
                        new JungleResinForItemsTrade(Items.FEATHER, 16, 3, 3),
                        new JungleResinForItemsTrade(Items.GUNPOWDER, 5, 1, 4),
                        new ItemsForJungleResinTrade(Items.EGG, 1, 4, 6, 2),
                        new JungleResinForItemsTrade(Items.IRON_NUGGET, 4, 1, 4),
                        new ItemsForJungleResinTrade(Items.CHICKEN, 2, 2, 7),
                        new ItemsForJungleResinTrade(IafItemRegistry.SILVER_NUGGET, 1, 4, 15),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForJungleResinTrade(Items.ROTTEN_FLESH, 1, 15, 6, 2),
                        new ItemsForJungleResinTrade(Items.GOLD_NUGGET, 1, 4, 6, 2),
                        new ItemsForJungleResinTrade(IafItemRegistry.TROLL_TUSK, 6, 1, 4, 2),
                        new JungleResinForItemsTrade(IafItemRegistry.DRAGON_BONE, 6, 2, 3),
                }));
        DESERT_SENTINEL = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new DesertResinForItemsTrade(Items.SPIDER_EYE, 10, 2, 3),
                        new DesertResinForItemsTrade(Items.POISONOUS_POTATO, 2, 1, 2),
                        new DesertResinForItemsTrade(Items.PUFFERFISH, 4, 2, 4),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForDesertResinTrade(Items.REDSTONE, 2, 5, 5, 1),
                        new ItemsForDesertResinTrade(Items.PORKCHOP, 2, 3, 4),
                        new ItemsForDesertResinTrade(Items.BEEF, 2, 3, 4),
                        new ItemsForDesertResinTrade(Items.MUTTON, 2, 3, 4),
                        new ItemsForDesertResinTrade(Items.SKELETON_SKULL, 15, 1, 2, 1),
                }));
        JUNGLE_SENTINEL = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new JungleResinForItemsTrade(Items.SPIDER_EYE, 10, 2, 3),
                        new JungleResinForItemsTrade(Items.POISONOUS_POTATO, 2, 1, 2),
                        new JungleResinForItemsTrade(Items.PUFFERFISH, 4, 2, 4),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForJungleResinTrade(Items.REDSTONE, 2, 5, 5, 1),
                        new ItemsForJungleResinTrade(Items.PORKCHOP, 2, 3, 4),
                        new ItemsForJungleResinTrade(Items.BEEF, 2, 3, 4),
                        new ItemsForJungleResinTrade(Items.MUTTON, 2, 3, 4),
                        new ItemsForJungleResinTrade(Items.SKELETON_SKULL, 15, 1, 2, 1),
                }));
        DESERT_ROYAL = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new ItemsForDesertResinTrade(IafItemRegistry.MANUSCRIPT, 1, 3, 5, 1),
                        new ItemsForDesertResinTrade(IafItemRegistry.WITHER_SHARD, 3, 1, 3, 1),
                        new ItemsForDesertResinTrade(Items.EMERALD, 10, 1, 3, 1),
                        new ItemsForDesertResinTrade(Items.QUARTZ, 2, 4, 3, 1),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForDesertResinTrade(Items.GOLDEN_CARROT, 3, 1, 2, 1),
                        new ItemsForDesertResinTrade(Items.MAGMA_CREAM, 5, 1, 3, 1),
                        new ItemsForDesertResinTrade(Items.GOLD_INGOT, 3, 1, 5, 1),
                        new ItemsForDesertResinTrade(IafItemRegistry.SILVER_INGOT, 3, 1, 5, 1),
                        new ItemsForDesertResinTrade(IafItemRegistry.COPPER_INGOT, 2, 2, 3, 1),
                        new ItemsForDesertResinTrade(Items.ENDER_PEARL, 8, 1, 5, 1),
                        new ItemsForDesertResinTrade(Items.RABBIT_FOOT, 3, 1, 5, 1),
                }));
        JUNGLE_ROYAL = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new ItemsForJungleResinTrade(IafItemRegistry.MANUSCRIPT, 1, 3, 5, 1),
                        new ItemsForJungleResinTrade(IafItemRegistry.WITHER_SHARD, 3, 1, 3, 1),
                        new ItemsForJungleResinTrade(Items.EMERALD, 10, 1, 3, 1),
                        new ItemsForJungleResinTrade(Items.QUARTZ, 2, 4, 3, 1),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForJungleResinTrade(Items.GOLDEN_CARROT, 3, 1, 2, 1),
                        new ItemsForJungleResinTrade(Items.MAGMA_CREAM, 5, 1, 3, 1),
                        new ItemsForJungleResinTrade(Items.GOLD_INGOT, 3, 1, 5, 1),
                        new ItemsForJungleResinTrade(IafItemRegistry.SILVER_INGOT, 3, 1, 5, 1),
                        new ItemsForJungleResinTrade(IafItemRegistry.COPPER_INGOT, 2, 2, 3, 1),
                        new ItemsForJungleResinTrade(Items.ENDER_PEARL, 8, 1, 5, 1),
                        new ItemsForJungleResinTrade(Items.RABBIT_FOOT, 3, 1, 5, 1),
                }));

        DESERT_QUEEN = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new ItemsForDesertResinTrade(createEgg(false, 0), 10, 1, 10, 1),
                        new ItemsForDesertResinTrade(createEgg(false, 1), 20, 1, 8, 1),
                        new ItemsForDesertResinTrade(createEgg(false, 2), 30, 1, 5, 1),
                        new ItemsForDesertResinTrade(createEgg(false, 3), 40, 1, 3, 1),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForDesertResinTrade(createEgg(false, 4), 60, 1, 2, 1),
                        new ItemsForDesertResinTrade(Items.EMERALD, 15, 1, 9, 1),
                        new ItemsForDesertResinTrade(Items.DIAMOND, 25, 1, 9, 1),
                }));
        JUNGLE_QUEEN = createTrades(ImmutableMap.of(1,
                new VillagerTrades.ITrade[]{
                        new ItemsForJungleResinTrade(createEgg(true, 0), 10, 1, 10, 1),
                        new ItemsForJungleResinTrade(createEgg(true, 1), 20, 1, 8, 1),
                        new ItemsForJungleResinTrade(createEgg(true, 2), 30, 1, 5, 1),
                        new ItemsForJungleResinTrade(createEgg(true, 3), 40, 1, 3, 1),
                },
                //Only 3 of these appears per myrmex
                2, new VillagerTrades.ITrade[]{
                        new ItemsForJungleResinTrade(createEgg(true, 4), 60, 1, 2, 1),
                        new ItemsForDesertResinTrade(Items.EMERALD, 15, 1, 9, 1),
                        new ItemsForDesertResinTrade(Items.DIAMOND, 25, 1, 9, 1),
                }));
    }

    private static ItemStack createEgg(boolean jungle, int caste){
        ItemStack egg = new ItemStack(jungle ? IafItemRegistry.MYRMEX_JUNGLE_EGG : IafItemRegistry.MYRMEX_DESERT_EGG);
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("EggOrdinal", caste);
        egg.setTag(tag);
        return egg;
    }

    private static Int2ObjectMap<VillagerTrades.ITrade[]> createTrades(ImmutableMap<Integer, VillagerTrades.ITrade[]> p_221238_0_) {
        return new Int2ObjectOpenHashMap(p_221238_0_);
    }

    static class ItemsForDesertResinAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack field_221200_a;
        private final int field_221201_b;
        private final int field_221202_c;
        private final ItemStack field_221203_d;
        private final int field_221204_e;
        private final int field_221205_f;
        private final int field_221206_g;
        private final float field_221207_h;

        public ItemsForDesertResinAndItemsTrade(IItemProvider p_i50533_1_, int p_i50533_2_, Item p_i50533_3_, int p_i50533_4_, int p_i50533_5_, int p_i50533_6_) {
            this(p_i50533_1_, p_i50533_2_, 1, p_i50533_3_, p_i50533_4_, p_i50533_5_, p_i50533_6_);
        }

        public ItemsForDesertResinAndItemsTrade(IItemProvider p_i50534_1_, int p_i50534_2_, int p_i50534_3_, Item p_i50534_4_, int p_i50534_5_, int p_i50534_6_, int p_i50534_7_) {
            this.field_221200_a = new ItemStack(p_i50534_1_);
            this.field_221201_b = p_i50534_2_;
            this.field_221202_c = p_i50534_3_;
            this.field_221203_d = new ItemStack(p_i50534_4_);
            this.field_221204_e = p_i50534_5_;
            this.field_221205_f = p_i50534_6_;
            this.field_221206_g = p_i50534_7_;
            this.field_221207_h = 0.05F;
        }

        @Nullable
        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN, this.field_221202_c), new ItemStack(this.field_221200_a.getItem(), this.field_221201_b), new ItemStack(this.field_221203_d.getItem(), this.field_221204_e), this.field_221205_f, this.field_221206_g, this.field_221207_h);
        }
    }


    static class ItemWithPotionForDesertResinAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack field_221219_a;
        private final int field_221220_b;
        private final int field_221221_c;
        private final int field_221222_d;
        private final int field_221223_e;
        private final Item field_221224_f;
        private final int field_221225_g;
        private final float field_221226_h;

        public ItemWithPotionForDesertResinAndItemsTrade(Item p_i50526_1_, int p_i50526_2_, Item p_i50526_3_, int p_i50526_4_, int p_i50526_5_, int p_i50526_6_, int p_i50526_7_) {
            this.field_221219_a = new ItemStack(p_i50526_3_);
            this.field_221221_c = p_i50526_5_;
            this.field_221222_d = p_i50526_6_;
            this.field_221223_e = p_i50526_7_;
            this.field_221224_f = p_i50526_1_;
            this.field_221225_g = p_i50526_2_;
            this.field_221220_b = p_i50526_4_;
            this.field_221226_h = 0.05F;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack lvt_3_1_ = new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN, this.field_221221_c);
            List<Potion> lvt_4_1_ = Registry.POTION.stream().filter((p_221218_0_) -> {
                return !p_221218_0_.getEffects().isEmpty() && PotionBrewing.func_222124_a(p_221218_0_);
            }).collect(Collectors.toList());
            Potion lvt_5_1_ = lvt_4_1_.get(p_221182_2_.nextInt(lvt_4_1_.size()));
            ItemStack lvt_6_1_ = PotionUtils.addPotionToItemStack(new ItemStack(this.field_221219_a.getItem(), this.field_221220_b), lvt_5_1_);
            return new MerchantOffer(lvt_3_1_, new ItemStack(this.field_221224_f, this.field_221225_g), lvt_6_1_, this.field_221222_d, this.field_221223_e, this.field_221226_h);
        }
    }

    static class EnchantedItemForDesertResinTrade implements VillagerTrades.ITrade {
        private final ItemStack field_221195_a;
        private final int field_221196_b;
        private final int field_221197_c;
        private final int field_221198_d;
        private final float field_221199_e;

        public EnchantedItemForDesertResinTrade(Item p_i50535_1_, int p_i50535_2_, int p_i50535_3_, int p_i50535_4_) {
            this(p_i50535_1_, p_i50535_2_, p_i50535_3_, p_i50535_4_, 0.05F);
        }

        public EnchantedItemForDesertResinTrade(Item p_i50536_1_, int p_i50536_2_, int p_i50536_3_, int p_i50536_4_, float p_i50536_5_) {
            this.field_221195_a = new ItemStack(p_i50536_1_);
            this.field_221196_b = p_i50536_2_;
            this.field_221197_c = p_i50536_3_;
            this.field_221198_d = p_i50536_4_;
            this.field_221199_e = p_i50536_5_;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            int lvt_3_1_ = 5 + p_221182_2_.nextInt(15);
            ItemStack lvt_4_1_ = EnchantmentHelper.addRandomEnchantment(p_221182_2_, new ItemStack(this.field_221195_a.getItem()), lvt_3_1_, false);
            int lvt_5_1_ = Math.min(this.field_221196_b + lvt_3_1_, 64);
            ItemStack lvt_6_1_ = new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN, lvt_5_1_);
            return new MerchantOffer(lvt_6_1_, lvt_4_1_, this.field_221197_c, this.field_221198_d, this.field_221199_e);
        }
    }

    static class SuspiciousStewForEmeraldTrade implements VillagerTrades.ITrade {
        final Effect field_221214_a;
        final int field_221215_b;
        final int field_221216_c;
        private final float field_221217_d;

        public SuspiciousStewForEmeraldTrade(Effect p_i50527_1_, int p_i50527_2_, int p_i50527_3_) {
            this.field_221214_a = p_i50527_1_;
            this.field_221215_b = p_i50527_2_;
            this.field_221216_c = p_i50527_3_;
            this.field_221217_d = 0.05F;
        }

        @Nullable
        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack lvt_3_1_ = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.addEffect(lvt_3_1_, this.field_221214_a, this.field_221215_b);
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN, 1), lvt_3_1_, 12, this.field_221216_c, this.field_221217_d);
        }
    }

    static class ItemsForDesertResinTrade implements VillagerTrades.ITrade {
        private final ItemStack stack;
        private final int emeraldCount;
        private final int itemCount;
        private final int maxUses;
        private final int exp;
        private final float multiplier;

        public ItemsForDesertResinTrade(Block p_i50528_1_, int p_i50528_2_, int p_i50528_3_, int p_i50528_4_, int p_i50528_5_) {
            this(new ItemStack(p_i50528_1_), p_i50528_2_, p_i50528_3_, p_i50528_4_, p_i50528_5_);
        }

        public ItemsForDesertResinTrade(Item p_i50529_1_, int p_i50529_2_, int p_i50529_3_, int p_i50529_4_) {
            this(new ItemStack(p_i50529_1_), p_i50529_2_, p_i50529_3_, 12, p_i50529_4_);
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

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack cloneStack =  new ItemStack(this.stack.getItem(), this.itemCount);
            cloneStack.setTag(this.stack.getTag());
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN, this.emeraldCount), cloneStack, this.maxUses, this.exp, this.multiplier);
        }
    }

    static class DesertResinForVillageTypeItemTrade implements VillagerTrades.ITrade {
        private final Map<IVillagerType, Item> field_221190_a;
        private final int field_221191_b;
        private final int field_221192_c;
        private final int field_221193_d;

        public DesertResinForVillageTypeItemTrade(int p_i50538_1_, int p_i50538_2_, int p_i50538_3_, Map<IVillagerType, Item> p_i50538_4_) {
            Registry.VILLAGER_TYPE.stream().filter((p_221188_1_) -> {
                return !p_i50538_4_.containsKey(p_221188_1_);
            }).findAny().ifPresent((p_221189_0_) -> {
                throw new IllegalStateException("Missing trade for villager type: " + Registry.VILLAGER_TYPE.getKey(p_221189_0_));
            });
            this.field_221190_a = p_i50538_4_;
            this.field_221191_b = p_i50538_1_;
            this.field_221192_c = p_i50538_2_;
            this.field_221193_d = p_i50538_3_;
        }

        @Nullable
        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            if (p_221182_1_ instanceof IVillagerDataHolder) {
                ItemStack lvt_3_1_ = new ItemStack(this.field_221190_a.get(((IVillagerDataHolder) p_221182_1_).getVillagerData().getType()), this.field_221191_b);
                return new MerchantOffer(lvt_3_1_, new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), this.field_221192_c, this.field_221193_d, 0.05F);
            } else {
                return null;
            }
        }
    }

    static class DesertResinForItemsTrade implements VillagerTrades.ITrade {
        private final Item field_221183_a;
        private final int field_221184_b;
        private final int field_221185_c;
        private final int field_221186_d;
        private final float field_221187_e;

        public DesertResinForItemsTrade(IItemProvider p_i50539_1_, int p_i50539_2_, int p_i50539_3_, int p_i50539_4_) {
            this.field_221183_a = p_i50539_1_.asItem();
            this.field_221184_b = p_i50539_2_;
            this.field_221185_c = p_i50539_3_;
            this.field_221186_d = p_i50539_4_;
            this.field_221187_e = 0.05F;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack lvt_3_1_ = new ItemStack(this.field_221183_a, this.field_221184_b);
            return new MerchantOffer(lvt_3_1_, new ItemStack(IafItemRegistry.MYRMEX_DESERT_RESIN), this.field_221185_c, this.field_221186_d, this.field_221187_e);
        }
    }


    static class ItemsForJungleResinAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack field_221200_a;
        private final int field_221201_b;
        private final int field_221202_c;
        private final ItemStack field_221203_d;
        private final int field_221204_e;
        private final int field_221205_f;
        private final int field_221206_g;
        private final float field_221207_h;

        public ItemsForJungleResinAndItemsTrade(IItemProvider p_i50533_1_, int p_i50533_2_, Item p_i50533_3_, int p_i50533_4_, int p_i50533_5_, int p_i50533_6_) {
            this(p_i50533_1_, p_i50533_2_, 1, p_i50533_3_, p_i50533_4_, p_i50533_5_, p_i50533_6_);
        }

        public ItemsForJungleResinAndItemsTrade(IItemProvider p_i50534_1_, int p_i50534_2_, int p_i50534_3_, Item p_i50534_4_, int p_i50534_5_, int p_i50534_6_, int p_i50534_7_) {
            this.field_221200_a = new ItemStack(p_i50534_1_);
            this.field_221201_b = p_i50534_2_;
            this.field_221202_c = p_i50534_3_;
            this.field_221203_d = new ItemStack(p_i50534_4_);
            this.field_221204_e = p_i50534_5_;
            this.field_221205_f = p_i50534_6_;
            this.field_221206_g = p_i50534_7_;
            this.field_221207_h = 0.05F;
        }

        @Nullable
        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN, this.field_221202_c), new ItemStack(this.field_221200_a.getItem(), this.field_221201_b), new ItemStack(this.field_221203_d.getItem(), this.field_221204_e), this.field_221205_f, this.field_221206_g, this.field_221207_h);
        }
    }

    static class ItemWithPotionForJungleResinAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack field_221219_a;
        private final int field_221220_b;
        private final int field_221221_c;
        private final int field_221222_d;
        private final int field_221223_e;
        private final Item field_221224_f;
        private final int field_221225_g;
        private final float field_221226_h;

        public ItemWithPotionForJungleResinAndItemsTrade(Item p_i50526_1_, int p_i50526_2_, Item p_i50526_3_, int p_i50526_4_, int p_i50526_5_, int p_i50526_6_, int p_i50526_7_) {
            this.field_221219_a = new ItemStack(p_i50526_3_);
            this.field_221221_c = p_i50526_5_;
            this.field_221222_d = p_i50526_6_;
            this.field_221223_e = p_i50526_7_;
            this.field_221224_f = p_i50526_1_;
            this.field_221225_g = p_i50526_2_;
            this.field_221220_b = p_i50526_4_;
            this.field_221226_h = 0.05F;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack lvt_3_1_ = new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN, this.field_221221_c);
            List<Potion> lvt_4_1_ = Registry.POTION.stream().filter((p_221218_0_) -> {
                return !p_221218_0_.getEffects().isEmpty() && PotionBrewing.func_222124_a(p_221218_0_);
            }).collect(Collectors.toList());
            Potion lvt_5_1_ = lvt_4_1_.get(p_221182_2_.nextInt(lvt_4_1_.size()));
            ItemStack lvt_6_1_ = PotionUtils.addPotionToItemStack(new ItemStack(this.field_221219_a.getItem(), this.field_221220_b), lvt_5_1_);
            return new MerchantOffer(lvt_3_1_, new ItemStack(this.field_221224_f, this.field_221225_g), lvt_6_1_, this.field_221222_d, this.field_221223_e, this.field_221226_h);
        }
    }

    static class EnchantedItemForJungleResinTrade implements VillagerTrades.ITrade {
        private final ItemStack field_221195_a;
        private final int field_221196_b;
        private final int field_221197_c;
        private final int field_221198_d;
        private final float field_221199_e;

        public EnchantedItemForJungleResinTrade(Item p_i50535_1_, int p_i50535_2_, int p_i50535_3_, int p_i50535_4_) {
            this(p_i50535_1_, p_i50535_2_, p_i50535_3_, p_i50535_4_, 0.05F);
        }

        public EnchantedItemForJungleResinTrade(Item p_i50536_1_, int p_i50536_2_, int p_i50536_3_, int p_i50536_4_, float p_i50536_5_) {
            this.field_221195_a = new ItemStack(p_i50536_1_);
            this.field_221196_b = p_i50536_2_;
            this.field_221197_c = p_i50536_3_;
            this.field_221198_d = p_i50536_4_;
            this.field_221199_e = p_i50536_5_;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            int lvt_3_1_ = 5 + p_221182_2_.nextInt(15);
            ItemStack lvt_4_1_ = EnchantmentHelper.addRandomEnchantment(p_221182_2_, new ItemStack(this.field_221195_a.getItem()), lvt_3_1_, false);
            int lvt_5_1_ = Math.min(this.field_221196_b + lvt_3_1_, 64);
            ItemStack lvt_6_1_ = new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN, lvt_5_1_);
            return new MerchantOffer(lvt_6_1_, lvt_4_1_, this.field_221197_c, this.field_221198_d, this.field_221199_e);
        }
    }

    static class ItemsForJungleResinTrade implements VillagerTrades.ITrade {
        private final ItemStack stack;
        private final int emeraldCount;
        private final int itemCount;
        private final int maxUses;
        private final int exp;
        private final float multiplier;

        public ItemsForJungleResinTrade(Block p_i50528_1_, int p_i50528_2_, int p_i50528_3_, int p_i50528_4_, int p_i50528_5_) {
            this(new ItemStack(p_i50528_1_), p_i50528_2_, p_i50528_3_, p_i50528_4_, p_i50528_5_);
        }

        public ItemsForJungleResinTrade(Item p_i50529_1_, int p_i50529_2_, int p_i50529_3_, int p_i50529_4_) {
            this(new ItemStack(p_i50529_1_), p_i50529_2_, p_i50529_3_, 12, p_i50529_4_);
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

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack cloneStack = new ItemStack(this.stack.getItem(), this.itemCount);
            cloneStack.setTag(this.stack.getTag());
            return new MerchantOffer(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN, this.emeraldCount), cloneStack, this.maxUses, this.exp, this.multiplier);
        }
    }

    static class JungleResinForVillageTypeItemTrade implements VillagerTrades.ITrade {
        private final Map<IVillagerType, Item> field_221190_a;
        private final int field_221191_b;
        private final int field_221192_c;
        private final int field_221193_d;

        public JungleResinForVillageTypeItemTrade(int p_i50538_1_, int p_i50538_2_, int p_i50538_3_, Map<IVillagerType, Item> p_i50538_4_) {
            Registry.VILLAGER_TYPE.stream().filter((p_221188_1_) -> {
                return !p_i50538_4_.containsKey(p_221188_1_);
            }).findAny().ifPresent((p_221189_0_) -> {
                throw new IllegalStateException("Missing trade for villager type: " + Registry.VILLAGER_TYPE.getKey(p_221189_0_));
            });
            this.field_221190_a = p_i50538_4_;
            this.field_221191_b = p_i50538_1_;
            this.field_221192_c = p_i50538_2_;
            this.field_221193_d = p_i50538_3_;
        }

        @Nullable
        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            if (p_221182_1_ instanceof IVillagerDataHolder) {
                ItemStack lvt_3_1_ = new ItemStack(this.field_221190_a.get(((IVillagerDataHolder) p_221182_1_).getVillagerData().getType()), this.field_221191_b);
                return new MerchantOffer(lvt_3_1_, new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), this.field_221192_c, this.field_221193_d, 0.05F);
            } else {
                return null;
            }
        }
    }

    static class JungleResinForItemsTrade implements VillagerTrades.ITrade {
        private final Item field_221183_a;
        private final int field_221184_b;
        private final int field_221185_c;
        private final int field_221186_d;
        private final float field_221187_e;

        public JungleResinForItemsTrade(IItemProvider p_i50539_1_, int p_i50539_2_, int p_i50539_3_, int p_i50539_4_) {
            this.field_221183_a = p_i50539_1_.asItem();
            this.field_221184_b = p_i50539_2_;
            this.field_221185_c = p_i50539_3_;
            this.field_221186_d = p_i50539_4_;
            this.field_221187_e = 0.05F;
        }

        public MerchantOffer getOffer(Entity p_221182_1_, Random p_221182_2_) {
            ItemStack lvt_3_1_ = new ItemStack(this.field_221183_a, this.field_221184_b);
            return new MerchantOffer(lvt_3_1_, new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_RESIN), this.field_221185_c, this.field_221186_d, this.field_221187_e);
        }
    }
}

