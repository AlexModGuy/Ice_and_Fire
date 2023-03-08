package com.github.alexthe666.iceandfire.recipe;

import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class IafRecipeRegistry {

    public static final BannerPattern PATTERN_FIRE = addBanner("iceandfire_fire", "iceandfire_fire");
    public static final BannerPattern PATTERN_ICE = addBanner("iceandfire_ice", "iceandfire_ice");
    public static final BannerPattern PATTERN_LIGHTNING = addBanner("iceandfire_lightning", "iceandfire_lightning");
    public static final BannerPattern PATTERN_FIRE_HEAD = addBanner("iceandfire_fire_head", "iceandfire_fire_head");
    public static final BannerPattern PATTERN_ICE_HEAD = addBanner("iceandfire_ice_head", "iceandfire_ice_head");
    public static final BannerPattern PATTERN_LIGHTNING_HEAD = addBanner("iceandfire_lightning_head", "iceandfire_lightning_head");
    public static final BannerPattern PATTERN_AMPHITHERE = addBanner("iceandfire_amphithere", "iceandfire_amphithere");
    public static final BannerPattern PATTERN_BIRD = addBanner("iceandfire_bird", "iceandfire_bird");
    public static final BannerPattern PATTERN_EYE = addBanner("iceandfire_eye", "iceandfire_eye");
    public static final BannerPattern PATTERN_FAE = addBanner("iceandfire_fae", "iceandfire_fae");
    public static final BannerPattern PATTERN_FEATHER = addBanner("iceandfire_feather", "iceandfire_feather");
    public static final BannerPattern PATTERN_GORGON = addBanner("iceandfire_gorgon", "iceandfire_gorgon");
    public static final BannerPattern PATTERN_HIPPOCAMPUS = addBanner("iceandfire_hippocampus", "iceandfire_hippocampus");
    public static final BannerPattern PATTERN_HIPPOGRYPH_HEAD = addBanner("iceandfire_hippogryph_head", "iceandfire_hippogryph_head");
    public static final BannerPattern PATTERN_MERMAID = addBanner("iceandfire_mermaid", "iceandfire_mermaid");
    public static final BannerPattern PATTERN_SEA_SERPENT = addBanner("iceandfire_sea_serpent", "iceandfire_sea_serpent");
    public static final BannerPattern PATTERN_TROLL = addBanner("iceandfire_troll", "iceandfire_troll");
    public static final BannerPattern PATTERN_WEEZER = addBanner("iceandfire_weezer", "iceandfire_weezer");
    public static final BannerPattern PATTERN_DREAD = addBanner("iceandfire_dread", "iceandfire_dread");

    public static final RecipeType<DragonForgeRecipe> DRAGON_FORGE_TYPE = RecipeType.register("iceandfire:dragonforge");

    public static List<ItemStack> BANNER_ITEMS = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void preInit(RegistryEvent.Register<Block> event) {
        DispenserBlock.registerBehavior(IafItemRegistry.STYMPHALIAN_ARROW.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
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
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
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
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
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
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
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
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
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
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                return new EntityHippogryphEgg(IafEntityRegistry.HIPPOGRYPH_EGG.get(), worldIn, position.x(),
                    position.y(), position.z(), stackIn);
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.ROTTEN_EGG.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                return new EntityCockatriceEgg(IafEntityRegistry.COCKATRICE_EGG.get(), position.x(), position.y(),
                    position.z(), worldIn);
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.DEATHWORM_EGG.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
                return new EntityDeathWormEgg(IafEntityRegistry.DEATH_WORM_EGG.get(), position.x(), position.y(),
                    position.z(), worldIn, false);
            }
        });
        DispenserBlock.registerBehavior(IafItemRegistry.DEATHWORM_EGG_GIGANTIC.get(), new AbstractProjectileDispenseBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            @Override
            protected @NotNull Projectile getProjectile(@NotNull Level worldIn, @NotNull Position position, @NotNull ItemStack stackIn) {
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
        IafItemRegistry.TROLL_MOUNTAIN_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(EnumTroll.MOUNTAIN.leather.get())));
        IafItemRegistry.TROLL_FOREST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(EnumTroll.FOREST.leather.get())));
        IafItemRegistry.TROLL_FROST_ARMOR_MATERIAL.setRepairMaterial(Ingredient.of(new ItemStack(EnumTroll.FROST.leather.get())));
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

    public static BannerPattern addBanner(String enumName, String fileName) {
        return BannerPattern.create(enumName.toUpperCase(), fileName, "iceandfire." + fileName, true);
    }
}
