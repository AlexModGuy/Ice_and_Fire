package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IafRecipeRegistry extends SimpleJsonResourceReloadListener {

    DeferredRegister<?> deferredRegister = DeferredRegister.create(ForgeRegistries.ENTITIES, IceAndFire.MODID);
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(DragonForgeRecipe.class, new DragonForgeRecipe.Deserializer()).create();
    public static final BannerPattern PATTERN_FIRE = addBanner("fire");
    public static final BannerPattern PATTERN_ICE = addBanner("ice");
    public static final BannerPattern PATTERN_LIGHTNING = addBanner("lightning");
    public static final BannerPattern PATTERN_FIRE_HEAD = addBanner("fire_head");
    public static final BannerPattern PATTERN_ICE_HEAD = addBanner("ice_head");
    public static final BannerPattern PATTERN_LIGHTNING_HEAD = addBanner("lightning_head");
    public static final BannerPattern PATTERN_AMPHITHERE = addBanner("amphithere");
    public static final BannerPattern PATTERN_BIRD = addBanner("bird");
    public static final BannerPattern PATTERN_EYE = addBanner("eye");
    public static final BannerPattern PATTERN_FAE = addBanner("fae");
    public static final BannerPattern PATTERN_FEATHER = addBanner("feather");
    public static final BannerPattern PATTERN_GORGON = addBanner("gorgon");
    public static final BannerPattern PATTERN_HIPPOCAMPUS = addBanner("hippocampus");
    public static final BannerPattern PATTERN_HIPPOGRYPH_HEAD = addBanner("hippogryph_head");
    public static final BannerPattern PATTERN_MERMAID = addBanner("mermaid");
    public static final BannerPattern PATTERN_SEA_SERPENT = addBanner("sea_serpent");
    public static final BannerPattern PATTERN_TROLL = addBanner("troll");
    public static final BannerPattern PATTERN_WEEZER = addBanner("weezer");
    public static final BannerPattern PATTERN_DREAD = addBanner("dread");
    public static List<DragonForgeRecipe> ALL_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> FIRE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> ICE_FORGE_RECIPES = new ArrayList<>();
    public static List<DragonForgeRecipe> LIGHTNING_FORGE_RECIPES = new ArrayList<>();
    public static List<ItemStack> BANNER_ITEMS = new ArrayList<>();

    public IafRecipeRegistry() {
        super(GSON, "dragonforge_recipes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> splashList, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        ImmutableMap.Builder<ResourceLocation, DragonForgeRecipe> builder = ImmutableMap.builder();
        ALL_FORGE_RECIPES.clear();
        IceAndFire.LOGGER.info("Loading in dragonforge_recipes jsons...");
        splashList.forEach((p_223385_1_, p_223385_2_) -> {
            try {
                DragonForgeRecipe fold = GSON.fromJson(p_223385_2_, DragonForgeRecipe.class);
                builder.put(p_223385_1_, fold);
            } catch (Exception exception) {
                IceAndFire.LOGGER.error("Couldn't parse dragonforge recipe {}", p_223385_1_, exception);
            }
        });
        ImmutableMap<ResourceLocation, DragonForgeRecipe> immutablemap = builder.build();
        immutablemap.forEach((p_215305_2_, p_215305_3_) -> {
            ALL_FORGE_RECIPES.add(p_215305_3_);
        });
        FIRE_FORGE_RECIPES.clear();
        ICE_FORGE_RECIPES.clear();
        LIGHTNING_FORGE_RECIPES.clear();
        for(DragonForgeRecipe recipe : ALL_FORGE_RECIPES){
            if(recipe.getDragonType().equals("fire")){
                FIRE_FORGE_RECIPES.add(recipe);
            }
            if(recipe.getDragonType().equals("ice")){
                ICE_FORGE_RECIPES.add(recipe);
            }
            if(recipe.getDragonType().equals("lightning")){
                LIGHTNING_FORGE_RECIPES.add(recipe);
            }
        }
    }

    public static void preInit() {
        DispenserBlock.registerBehavior(IafItemRegistry.STYMPHALIAN_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityStymphalianArrow entityarrow = new EntityStymphalianArrow(
                    IafEntityRegistry.STYMPHALIAN_ARROW.get(), worldIn, position.x(), position.y(),
                    position.z());
                entityarrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.AMPHITHERE_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityAmphithereArrow entityarrow = new EntityAmphithereArrow(IafEntityRegistry.AMPHITHERE_ARROW.get(),
                    worldIn, position.x(), position.y(), position.z());
                entityarrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.SEA_SERPENT_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntitySeaSerpentArrow entityarrow = new EntitySeaSerpentArrow(IafEntityRegistry.SEA_SERPENT_ARROW.get(),
                    worldIn, position.x(), position.y(), position.z());
                entityarrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.DRAGONBONE_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityDragonArrow entityarrow = new EntityDragonArrow(IafEntityRegistry.DRAGON_ARROW.get(),
                    position.x(), position.y(), position.z(), worldIn);
                entityarrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.HYDRA_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                EntityHydraArrow entityarrow = new EntityHydraArrow(IafEntityRegistry.HYDRA_ARROW.get(), worldIn,
                    position.x(), position.y(), position.z());
                entityarrow.pickup = AbstractArrow.Pickup.ALLOWED;
                return entityarrow;
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.HIPPOGRYPH_EGG.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                return new EntityHippogryphEgg(IafEntityRegistry.HIPPOGRYPH_EGG.get(), worldIn, position.x(),
                    position.y(), position.z(), stackIn);
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.ROTTEN_EGG.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                return new EntityCockatriceEgg(IafEntityRegistry.COCKATRICE_EGG.get(), position.x(), position.y(),
                    position.z(), worldIn);
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.DEATHWORM_EGG.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                return new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG.get(), position.x(), position.y(),
                    position.z(), worldIn, false);
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.DEATHWORM_EGG_GIGANTIC.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
                return new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG.get(), position.x(), position.y(),
                    position.z(), worldIn, true);
            }
        });
        IafItemRegistry.BLINDFOLD_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(Items.STRING)));
        IafItemRegistry.SILVER_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.SILVER_INGOT.get())));
        IafItemRegistry.SILVER_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.SILVER_INGOT.get())));
        IafItemRegistry.DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGON_BONE.get())));
        IafItemRegistry.FIRE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGON_BONE.get())));
        IafItemRegistry.ICE_DRAGONBONE_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGON_BONE.get())));
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            armor.armorMaterial.setRepairMaterial(Ingredient.of(new ItemStack(EnumDragonArmor.getScaleItem(armor))));
        }
        IafItemRegistry.DRAGONSTEEL_FIRE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get())));
        IafItemRegistry.DRAGONSTEEL_ICE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get())));
        IafItemRegistry.DRAGONSTEEL_LIGHTNING_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get())));
        IafItemRegistry.SHEEP_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(Blocks.WHITE_WOOL)));
        IafItemRegistry.EARPLUGS_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(Blocks.OAK_BUTTON)));
        IafItemRegistry.DEATHWORM_0_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN_YELLOW.get())));
        IafItemRegistry.DEATHWORM_1_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN_RED.get())));
        IafItemRegistry.DEATHWORM_2_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DEATH_WORM_CHITIN_WHITE.get())));
        IafItemRegistry.TROLL_WEAPON_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(Blocks.STONE)));
        IafItemRegistry.TROLL_MOUNTAIN_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(EnumTroll.MOUNTAIN.leather)));
        IafItemRegistry.TROLL_FOREST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(EnumTroll.FOREST.leather)));
        IafItemRegistry.TROLL_FROST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(EnumTroll.FROST.leather)));
        IafItemRegistry.HIPPOGRYPH_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.HIPPOGRYPH_TALON.get())));
        IafItemRegistry.HIPPOCAMPUS_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.SHINY_SCALES.get())));
        IafItemRegistry.AMPHITHERE_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.AMPHITHERE_FEATHER.get())));
        IafItemRegistry.STYMHALIAN_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.STYMPHALIAN_BIRD_FEATHER.get())));
        IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.MYRMEX_DESERT_CHITIN.get())));
        IafItemRegistry.MYRMEX_DESERT_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.MYRMEX_DESERT_CHITIN.get())));
        IafItemRegistry.MYRMEX_JUNGLE_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.MYRMEX_JUNGLE_CHITIN.get())));
        IafItemRegistry.DREAD_SWORD_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DREAD_SHARD.get())));
        IafItemRegistry.DREAD_KNIGHT_TOOL_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(IafItemRegistry.DREAD_SHARD.get())));
        for (EnumSeaSerpent serpent : EnumSeaSerpent.values()) {
            serpent.armorMaterial.setRepairMaterial(Ingredient.of(new ItemStack(serpent.scale.get())));
        }
        BrewingRecipeRegistry.addRecipe(Ingredient.of(createPotion(Potions.WATER).getItem()), Ingredient.of(IafItemRegistry.SHINY_SCALES.get()), createPotion(Potions.WATER_BREATHING));
    }

    public static ItemStack createPotion(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    }

    public static BannerPattern addBanner(String name) {
        return BannerPattern.create(name.toUpperCase(), name, "iceandfire." + name, true);
    }

    public static DragonForgeRecipe getFireForgeRecipe(ItemStack stack) {
        for (DragonForgeRecipe recipe : FIRE_FORGE_RECIPES) {
            if (recipe.getInput().test(stack)) {
                return recipe;
            }
        }
        return null;
    }

    public static DragonForgeRecipe getIceForgeRecipe(ItemStack stack) {
        for (DragonForgeRecipe recipe : ICE_FORGE_RECIPES) {
            if (recipe.getInput().test(stack)) {
                return recipe;
            }
        }
        return null;
    }

    public static DragonForgeRecipe getLightningForgeRecipe(ItemStack stack) {
        for (DragonForgeRecipe recipe : LIGHTNING_FORGE_RECIPES) {
            if (recipe.getInput().test(stack)) {
                return recipe;
            }
        }
        return null;
    }


    public static DragonForgeRecipe getFireForgeRecipeForBlood(ItemStack stack) {
        for (DragonForgeRecipe recipe : FIRE_FORGE_RECIPES) {
            if (recipe.getBlood().test(stack)) {
                return recipe;
            }
        }
        return null;
    }


    public static DragonForgeRecipe getIceForgeRecipeForBlood(ItemStack stack) {
        for (DragonForgeRecipe recipe : ICE_FORGE_RECIPES) {
            if (recipe.getBlood().test(stack)) {
                return recipe;
            }
        }
        return null;
    }

    public static DragonForgeRecipe getLightningForgeRecipeForBlood(ItemStack stack) {
        for (DragonForgeRecipe recipe : LIGHTNING_FORGE_RECIPES) {
            if (recipe.getBlood().test(stack)) {
                return recipe;
            }
        }
        return null;
    }
}
