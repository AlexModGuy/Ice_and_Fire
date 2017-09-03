package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.client.GuiHandler;
import com.github.alexthe666.iceandfire.core.*;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.github.alexthe666.iceandfire.event.StructureGenerator;
import com.github.alexthe666.iceandfire.message.*;
import com.github.alexthe666.iceandfire.misc.CreativeTab;
import com.github.alexthe666.iceandfire.world.BiomeGlacier;
import com.github.alexthe666.iceandfire.world.village.ComponentAnimalFarm;
import com.github.alexthe666.iceandfire.world.village.MapGenSnowVillage;
import com.github.alexthe666.iceandfire.world.village.VillageAnimalFarmCreator;
import net.ilexiconn.llibrary.server.config.Config;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.Random;

@Mod(modid = IceAndFire.MODID, dependencies = "required-after:llibrary@[" + IceAndFire.LLIBRARY_VERSION + ",)", version = IceAndFire.VERSION, name = IceAndFire.NAME)
public class IceAndFire {

    public static final String MODID = "iceandfire";
    public static final String VERSION = "1.1.1";
    public static final String LLIBRARY_VERSION = "1.7.4";
    public static final String NAME = "Ice And Fire";
    @Instance(value = MODID)
    public static IceAndFire INSTANCE;
    @NetworkWrapper({MessageDaytime.class, MessageDragonArmor.class, MessageDragonControl.class, MessageHippogryphArmor.class, MessageStoneStatue.class, MessageUpdatePixieHouse.class, MessageUpdatePixieHouseModel.class, MessageUpdatePixieJar.class})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    @SidedProxy(clientSide = "com.github.alexthe666.iceandfire.ClientProxy", serverSide = "com.github.alexthe666.iceandfire.CommonProxy")
    public static CommonProxy PROXY;
    public static CreativeTabs TAB;
    public static DamageSource dragon;
    public static DamageSource dragonFire;
    public static DamageSource dragonIce;
    public static DamageSource gorgon;
    public static Biome GLACIER;
    public static Potion FROZEN_POTION;

    @Config
    public static IceAndFireConfig CONFIG;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventLiving());
    }


    @EventHandler
    public void init(FMLInitializationEvent event) {
        TAB = new CreativeTab(MODID);
        dragon = new DamageSource("dragon") {
            @Override
            public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                String s = "death.attack.dragon";
                String s1 = s + ".player_" + new Random().nextInt(2);
                return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, new Object[]{entityLivingBaseIn.getDisplayName()}));
            }
        };
        dragonFire = new DamageSource("dragon_fire") {
            @Override
            public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                String s = "death.attack.dragon_fire";
                String s1 = s + ".player_" + new Random().nextInt(2);
                return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, new Object[]{entityLivingBaseIn.getDisplayName()}));
            }
        }.setFireDamage();
        dragonIce = new DamageSource("dragon_ice") {
            @Override
            public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                String s = "death.attack.dragon_ice";
                String s1 = s + ".player_" + new Random().nextInt(2);
                return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, new Object[]{entityLivingBaseIn.getDisplayName()}));
            }
        };
        gorgon = new DamageSource("gorgon") {
            @Override
            public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                String s = "death.attack.gorgon";
                String s1 = s + ".player_" + new Random().nextInt(2);
                return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, new Object[]{entityLivingBaseIn.getDisplayName()}));
            }
        };
        ModBlocks.init();
        ModItems.init();
        ModRecipes.init();
        ModVillagers.INSTANCE.init();
        ModEntities.init();
        ModFoods.init();
        ModSounds.init();
        ModAchievements.init();
        MapGenStructureIO.registerStructure(MapGenSnowVillage.Start.class, "SnowVillageStart");
        MapGenStructureIO.registerStructureComponent(ComponentAnimalFarm.class, "AnimalFarm");
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageAnimalFarmCreator());
        GLACIER = new BiomeGlacier().setRegistryName(MODID, "Glacier");
        GameRegistry.register(GLACIER);
        BiomeDictionary.addTypes(GLACIER, Type.SNOWY, Type.COLD, Type.SPARSE, Type.DEAD, Type.WASTELAND);
        BiomeManager.addSpawnBiome(GLACIER);
        BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(GLACIER, CONFIG.glacierSpawnChance));
        PROXY.render();
        GameRegistry.registerWorldGenerator(new StructureGenerator(), 0);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postRender();
    }
}
