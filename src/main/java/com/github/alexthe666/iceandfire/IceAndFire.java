package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.client.*;
import com.github.alexthe666.iceandfire.core.*;
import com.github.alexthe666.iceandfire.event.*;
import com.github.alexthe666.iceandfire.message.*;
import com.github.alexthe666.iceandfire.misc.*;
import com.github.alexthe666.iceandfire.world.*;
import com.github.alexthe666.iceandfire.world.village.*;
import net.ilexiconn.llibrary.server.config.*;
import net.ilexiconn.llibrary.server.network.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.structure.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.BiomeDictionary.*;
import net.minecraftforge.common.BiomeManager.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.common.registry.*;

import java.util.*;

@Mod (modid = IceAndFire.MODID, dependencies = "required-after:llibrary@[" + IceAndFire.LLIBRARY_VERSION + ",)", version = IceAndFire.VERSION, name = IceAndFire.NAME)
public class IceAndFire {
	/*
	-Dfml.coreMods.load=com.github.alexthe666.iceandfire.access.IceAndFireForgeLoading
	This is here because Idea keeps deleting the VM arguments...
	 */
	public static final String MODID = "iceandfire";
	public static final String VERSION = "1.0.1";
	public static final String LLIBRARY_VERSION = "1.4.1";
	public static final String NAME = "Ice And Fire";
	@Instance (value = MODID)
	public static IceAndFire INSTANCE;
	@NetworkWrapper ({MessageDaytime.class, MessageDragonArmor.class, MessageDragonControl.class})
	public static SimpleNetworkWrapper NETWORK_WRAPPER;
	@SidedProxy (clientSide = "com.github.alexthe666.iceandfire.ClientProxy", serverSide = "com.github.alexthe666.iceandfire.CommonProxy")
	public static CommonProxy PROXY;
	public static CreativeTabs TAB;
	public static DamageSource dragon;
	public static DamageSource dragonFire;
	public static DamageSource dragonIce;
	public static Biome GLACIER;
	public static Potion FROZEN_POTION;

	@Config
	public static IceAndFireConfig CONFIG;

	@EventHandler
	public void preInit (FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register (new EventLiving ());
		MinecraftForge.EVENT_BUS.register (new EventMapGen ());
	}


	@EventHandler
	public void init (FMLInitializationEvent event) {
		TAB = new CreativeTab (MODID);
		dragon = new DamageSource ("dragon") {
			@Override
			public ITextComponent getDeathMessage (EntityLivingBase entityLivingBaseIn) {
				String s = "death.attack.dragon";
				String s1 = s + ".player_" + new Random ().nextInt (2);
				return new TextComponentString (entityLivingBaseIn.getDisplayName ().getFormattedText () + " ").appendSibling (new TextComponentTranslation (s1, new Object[]{entityLivingBaseIn.getDisplayName ()}));
			}
		};
		dragonFire = new DamageSource ("dragon_fire") {
			@Override
			public ITextComponent getDeathMessage (EntityLivingBase entityLivingBaseIn) {
				String s = "death.attack.dragon_fire";
				String s1 = s + ".player_" + new Random ().nextInt (2);
				return new TextComponentString (entityLivingBaseIn.getDisplayName ().getFormattedText () + " ").appendSibling (new TextComponentTranslation (s1, new Object[]{entityLivingBaseIn.getDisplayName ()}));
			}
		}.setFireDamage ();
		dragonIce = new DamageSource ("dragon_ice") {
			@Override
			public ITextComponent getDeathMessage (EntityLivingBase entityLivingBaseIn) {
				String s = "death.attack.dragon_ice";
				String s1 = s + ".player_" + new Random ().nextInt (2);
				return new TextComponentString (entityLivingBaseIn.getDisplayName ().getFormattedText () + " ").appendSibling (new TextComponentTranslation (s1, new Object[]{entityLivingBaseIn.getDisplayName ()}));
			}
		};
		ModBlocks.init ();
		ModItems.init ();
		ModRecipes.init ();
		ModVillagers.INSTANCE.init ();
		ModEntities.init ();
		MapGenStructureIO.registerStructure (MapGenSnowVillage.Start.class, "SnowVillageStart");
		ModFoods.init ();
		ModSounds.init ();
		ModAchievements.init ();
		try {
			MapGenStructureIO.registerStructureComponent (ComponentAnimalFarm.class, "AnimalFarm");
		} catch (Exception e) {
		}
		VillagerRegistry.instance ().registerVillageCreationHandler (new VillageAnimalFarmCreator ());
		GLACIER = new BiomeGlacier ().setRegistryName (MODID, "Glacier");
		GameRegistry.register (GLACIER);
		BiomeDictionary.registerBiomeType (GLACIER, Type.SNOWY, Type.COLD, Type.SPARSE, Type.DEAD, Type.WASTELAND);
		BiomeManager.addSpawnBiome (GLACIER);
		BiomeManager.addBiome (BiomeType.COOL, new BiomeEntry (GLACIER, 10));
		PROXY.render ();
		GameRegistry.registerWorldGenerator (new StructureGenerator (), 0);
		NetworkRegistry.INSTANCE.registerGuiHandler (this, new GuiHandler ());

	}
}
