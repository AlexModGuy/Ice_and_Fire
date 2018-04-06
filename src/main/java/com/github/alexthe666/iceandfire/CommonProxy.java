package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.block.BlockJar;
import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.BlockPodium;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.item.block.ItemBlockPodium;
import com.github.alexthe666.iceandfire.recipe.RecipeShinyScales;
import com.github.alexthe666.iceandfire.world.BiomeGlacier;
import net.minecraft.block.Block;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
				ModSounds.SIREN_SONG
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
	}

	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		IceAndFire.GLACIER = new BiomeGlacier().setRegistryName(IceAndFire.MODID, "Glacier");
		event.getRegistry().register(IceAndFire.GLACIER);
		BiomeDictionary.addTypes(IceAndFire.GLACIER, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SPARSE, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.WASTELAND);
		BiomeManager.addSpawnBiome(IceAndFire.GLACIER);
		BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeManager.BiomeEntry(IceAndFire.GLACIER, IceAndFire.CONFIG.glacierSpawnChance));
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

}
