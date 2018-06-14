package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.block.BlockJar;
import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.BlockPodium;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.block.ItemBlockPodium;
import com.github.alexthe666.iceandfire.recipe.RecipeShinyScales;
import com.github.alexthe666.iceandfire.world.BiomeGlacier;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class CommonProxy {

    @SubscribeEvent
    public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                ModSounds.DRAGON_HATCH,
                ModSounds.FIREDRAGON_BREATH,
                ModSounds.ICEDRAGON_BREATH,
                ModSounds.FIREDRAGON_CHILD_IDLE,
                ModSounds.FIREDRAGON_CHILD_HURT,
                ModSounds.FIREDRAGON_CHILD_DEATH,
                ModSounds.FIREDRAGON_CHILD_ROAR,
                ModSounds.FIREDRAGON_TEEN_ROAR,
                ModSounds.FIREDRAGON_TEEN_IDLE,
                ModSounds.FIREDRAGON_TEEN_HURT,
                ModSounds.FIREDRAGON_TEEN_DEATH,
                ModSounds.FIREDRAGON_ADULT_ROAR,
                ModSounds.FIREDRAGON_ADULT_IDLE,
                ModSounds.FIREDRAGON_ADULT_HURT,
                ModSounds.FIREDRAGON_ADULT_DEATH,
                ModSounds.ICEDRAGON_CHILD_IDLE,
                ModSounds.ICEDRAGON_CHILD_HURT,
                ModSounds.ICEDRAGON_CHILD_DEATH,
                ModSounds.ICEDRAGON_CHILD_ROAR,
                ModSounds.ICEDRAGON_TEEN_ROAR,
                ModSounds.ICEDRAGON_TEEN_IDLE,
                ModSounds.ICEDRAGON_TEEN_HURT,
                ModSounds.ICEDRAGON_TEEN_DEATH,
                ModSounds.ICEDRAGON_ADULT_ROAR,
                ModSounds.ICEDRAGON_ADULT_IDLE,
                ModSounds.ICEDRAGON_ADULT_HURT,
                ModSounds.ICEDRAGON_ADULT_DEATH,
                ModSounds.DRAGONFLUTE,
                ModSounds.HIPPOGRYPH_DIE,
                ModSounds.HIPPOGRYPH_IDLE,
                ModSounds.HIPPOGRYPH_HURT,
                ModSounds.GORGON_DIE,
                ModSounds.GORGON_IDLE,
                ModSounds.GORGON_HURT,
                ModSounds.GORGON_ATTACK,
                ModSounds.GORGON_TURN_STONE,
                ModSounds.GORGON_PETRIFY,
                ModSounds.PIXIE_DIE,
                ModSounds.PIXIE_HURT,
                ModSounds.PIXIE_IDLE,
                ModSounds.PIXIE_TAUNT,
                ModSounds.CYCLOPS_BITE,
                ModSounds.CYCLOPS_BLINDED,
                ModSounds.CYCLOPS_HURT,
                ModSounds.CYCLOPS_IDLE,
                ModSounds.GOLD_PILE_STEP,
                ModSounds.GOLD_PILE_BREAK,
                ModSounds.DRAGON_FLIGHT,
                ModSounds.HIPPOCAMPUS_IDLE,
                ModSounds.HIPPOCAMPUS_HURT,
                ModSounds.HIPPOCAMPUS_DIE,
                ModSounds.DEATHWORM_IDLE,
                ModSounds.DEATHWORM_ATTACK,
                ModSounds.DEATHWORM_HURT,
                ModSounds.DEATHWORM_DIE,
                ModSounds.DEATHWORM_GIANT_IDLE,
                ModSounds.DEATHWORM_GIANT_ATTACK,
                ModSounds.DEATHWORM_GIANT_HURT,
                ModSounds.DEATHWORM_GIANT_DIE,
                ModSounds.NAGA_IDLE,
                ModSounds.NAGA_ATTACK,
                ModSounds.NAGA_HURT,
                ModSounds.NAGA_DIE,
                ModSounds.MERMAID_IDLE,
                ModSounds.MERMAID_HURT,
                ModSounds.MERMAID_DIE,
                ModSounds.SIREN_SONG,
                ModSounds.TROLL_IDLE,
                ModSounds.TROLL_HURT,
                ModSounds.TROLL_DIE,
                ModSounds.TROLL_ROAR,
                ModSounds.COCKATRICE_IDLE,
                ModSounds.COCKATRICE_HURT,
                ModSounds.COCKATRICE_DIE,
                ModSounds.COCKATRICE_CRY,
                ModSounds.STYMPHALIAN_BIRD_IDLE,
                ModSounds.STYMPHALIAN_BIRD_HURT,
                ModSounds.STYMPHALIAN_BIRD_DIE,
                ModSounds.STYMPHALIAN_BIRD_ATTACK
        );
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        try {
            for (Field f : ModBlocks.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    event.getRegistry().register((Block) obj);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        event.getRegistry().register(block);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        IRecipe recipe = new RecipeShinyScales();
        recipe.setRegistryName(new ResourceLocation("iceandfire:shiny_scales_recipe"));
        event.getRegistry().register(recipe);
    }


    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        registerUnspawnable(EntityEntryBuilder.<EntityDragonEgg>create(), event,EntityDragonEgg.class, "dragonegg", 1);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonArrow>create(), event,EntityDragonArrow.class, "dragonarrow", 2);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonSkull>create(), event,EntityDragonSkull.class, "dragonskull", 3);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonFire>create(), event,EntityDragonFire.class, "dragonfire", 4);
        registerSpawnable(EntityEntryBuilder.<EntityFireDragon>create(), event, EntityFireDragon.class, "firedragon", 5, 0X340000, 0XA52929);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonIceProjectile>create(), event,EntityDragonIceProjectile.class, "dragonice", 6);
        registerSpawnable(EntityEntryBuilder.<EntityIceDragon>create(), event, EntityIceDragon.class, "icedragon", 7, 0XB5DDFB, 0X7EBAF0);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonFireCharge>create(), event,EntityDragonFireCharge.class, "dragonfirecharge", 8);
        registerUnspawnable(EntityEntryBuilder.<EntityDragonIceCharge>create(), event,EntityDragonIceCharge.class, "dragonicecharge", 9);
        registerSpawnable(EntityEntryBuilder.<EntitySnowVillager>create(), event,EntitySnowVillager.class, "snowvillager", 10, 0X3C2A23, 0X70B1CF);
        registerUnspawnable(EntityEntryBuilder.<EntityHippogryphEgg>create(), event,EntityHippogryphEgg.class, "hippogryphegg", 11);
        registerSpawnable(EntityEntryBuilder.<EntityHippogryph>create(), event,EntityHippogryph.class, "hippogryph", 12, 0XD8D8D8, 0XD1B55D);
        registerUnspawnable(EntityEntryBuilder.<EntityStoneStatue>create(), event,EntityStoneStatue.class, "stonestatue", 13);
        registerSpawnable(EntityEntryBuilder.<EntityGorgon>create(), event,EntityGorgon.class, "gorgon", 14, 0XD0D99F, 0X684530);
        registerSpawnable(EntityEntryBuilder.<EntityPixie>create(), event,EntityPixie.class, "if_pixie", 15, 0XFF7F89, 0XE2CCE2);
        registerSpawnable(EntityEntryBuilder.<EntityCyclops>create(), event,EntityCyclops.class, "cyclops", 17, 0XBBAA92, 0X594729);
        registerSpawnable(EntityEntryBuilder.<EntitySiren>create(), event,EntitySiren.class, "siren", 18, 0X8EE6CA, 0XF2DFC8);
        registerSpawnable(EntityEntryBuilder.<EntityHippocampus>create(), event,EntityHippocampus.class, "hippocampus", 19, 0X4491C7, 0X4FC56B);
        registerSpawnable(EntityEntryBuilder.<EntityDeathWorm>create(), event,EntityDeathWorm.class, "deathworm", 20, 0XD1CDA3, 0X423A3A);
        registerUnspawnable(EntityEntryBuilder.<EntityDeathWormEgg>create(), event,EntityDeathWormEgg.class, "deathwormegg", 21);
        registerSpawnable(EntityEntryBuilder.<EntityCockatrice>create(), event,EntityCockatrice.class, "if_cockatrice", 22, 0X8F5005, 0X4F5A23);
        registerUnspawnable(EntityEntryBuilder.<EntityCockatriceEgg>create(), event,EntityCockatriceEgg.class, "if_cockatriceegg", 23);
        registerSpawnable(EntityEntryBuilder.<EntityStymphalianBird>create(), event,EntityStymphalianBird.class, "stymphalianbird", 24, 0X744F37, 0X9E6C4B);
        registerUnspawnable(EntityEntryBuilder.<EntityStymphalianFeather>create(), event,EntityStymphalianFeather.class, "stymphalianfeather", 25);
        registerUnspawnable(EntityEntryBuilder.<EntityStymphalianArrow>create(), event,EntityStymphalianArrow.class, "stymphalianarrow", 26);
        registerSpawnable(EntityEntryBuilder.<EntityTroll>create(), event,EntityTroll.class, "if_troll", 27, 0X3D413D, 0X58433A);
    }

    public static void registerSpawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id, int mainColor, int subColor) {
        builder.entity(entityClass);
        builder.id(new ResourceLocation(IceAndFire.MODID, name), id);
        builder.name(name);
        builder.egg(mainColor, subColor);
        builder.tracker(64, 3, true);
        event.getRegistry().register(builder.build());
    }

    public static void registerUnspawnable(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id) {
        builder.entity(entityClass);
        builder.id(new ResourceLocation(IceAndFire.MODID, name), id);
        builder.name(name);
        builder.tracker(64, 3, true);
        event.getRegistry().register(builder.build());
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : ModBlocks.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Block) {
                    ItemBlock itemBlock;
                    if (obj instanceof BlockJar) {
                        itemBlock = ((BlockJar) obj).new ItemBlockJar((Block) obj);
                    } else if (obj instanceof BlockPixieHouse) {
                        itemBlock = ((BlockPixieHouse) obj).new ItemBlockPixieHouse((Block) obj);
                    } else if (obj instanceof BlockPodium) {
                        itemBlock = new ItemBlockPodium((Block) obj);
                    } else {
                        itemBlock = new ItemBlock((Block) obj);
                    }
                    itemBlock.setRegistryName(((Block) obj).getRegistryName());
                    event.getRegistry().register(itemBlock);
                } else if (obj instanceof Block[]) {
                    for (Block block : (Block[]) obj) {
                        ItemBlock itemBlock = new ItemBlock(block);
                        itemBlock.setRegistryName(block.getRegistryName());
                        event.getRegistry().register(itemBlock);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        try {
            for (Field f : ModItems.class.getDeclaredFields()) {
                Object obj = f.get(null);
                if (obj instanceof Item) {
                    event.getRegistry().register((Item) obj);
                } else if (obj instanceof Item[]) {
                    for (Item item : (Item[]) obj) {
                        event.getRegistry().register(item);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (EnumDragonArmor armor : EnumDragonArmor.values()) {
            event.getRegistry().register(armor.helmet);
            event.getRegistry().register(armor.chestplate);
            event.getRegistry().register(armor.leggings);
            event.getRegistry().register(armor.boots);
        }
        for (EnumTroll.Weapon weapon : EnumTroll.Weapon.values()) {
            event.getRegistry().register(weapon.item);
        }
        for (EnumTroll troll : EnumTroll.values()) {
            event.getRegistry().register(troll.leather);
            event.getRegistry().register(troll.helmet);
            event.getRegistry().register(troll.chestplate);
            event.getRegistry().register(troll.leggings);
            event.getRegistry().register(troll.boots);
        }
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        IceAndFire.GLACIER = new BiomeGlacier().setRegistryName(IceAndFire.MODID, "Glacier");
        event.getRegistry().register(IceAndFire.GLACIER);
        BiomeDictionary.addTypes(IceAndFire.GLACIER, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.WASTELAND);
        if (IceAndFire.CONFIG.spawnGlaciers) {
            BiomeManager.addSpawnBiome(IceAndFire.GLACIER);
            BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(IceAndFire.GLACIER, IceAndFire.CONFIG.glacierSpawnChance));

        }
    }

    public void preRender() {

    }

    public void render() {
    }

    public void postRender() {
    }

    public void spawnParticle(String name, World world, double x, double y, double z, double motX, double motY, double motZ) {
    }

    public void openBestiaryGui(ItemStack book) {
    }

    public Object getArmorModel(int armorId) {
        return null;
    }

    public Object getFontRenderer() {
        return null;
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(IceAndFire.MODID)) {
            IceAndFire.syncConfig();
        }
    }

}
