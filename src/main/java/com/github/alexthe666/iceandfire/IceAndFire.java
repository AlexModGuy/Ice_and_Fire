package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.compat.CraftTweakerCompatBridge;
import com.github.alexthe666.iceandfire.compat.OneProbeCompatBridge;
import com.github.alexthe666.iceandfire.compat.ThaumcraftCompatBridge;
import com.github.alexthe666.iceandfire.compat.TinkersCompatBridge;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.event.WorldGenEvents;
import com.github.alexthe666.iceandfire.loot.CustomizeToDragon;
import com.github.alexthe666.iceandfire.loot.CustomizeToSeaSerpent;
import com.github.alexthe666.iceandfire.message.*;
import com.github.alexthe666.iceandfire.misc.CreativeTab;
import com.github.alexthe666.iceandfire.world.village.ComponentAnimalFarm;
import com.github.alexthe666.iceandfire.world.village.MapGenSnowVillage;
import com.github.alexthe666.iceandfire.world.village.VillageAnimalFarmCreator;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Random;

@Mod(modid = IceAndFire.MODID,
        dependencies = "required-after:llibrary@[" + IceAndFire.LLIBRARY_VERSION + ",);after:thaumicadds",
        version = IceAndFire.VERSION, name = IceAndFire.NAME, guiFactory = "com.github.alexthe666.iceandfire.client.gui.IceAndFireGuiFactory")
public class IceAndFire {

    public static final String MODID = "iceandfire";
    public static final String VERSION = "1.9.1";
    public static final String LLIBRARY_VERSION = "1.7.9";
    public static final String NAME = "Ice And Fire";
    public static final Logger logger = LogManager.getLogger(NAME);
    @Instance(value = MODID)
    public static IceAndFire INSTANCE;
    @NetworkWrapper({MessageDaytime.class, MessageDragonControl.class, MessageHippogryphArmor.class, MessageStoneStatue.class,
            MessageUpdatePixieHouse.class, MessageUpdatePodium.class, MessageUpdatePixieHouseModel.class, MessageUpdatePixieJar.class, MessageSirenSong.class,
            MessageDeathWormHitbox.class, MessageMultipartInteract.class, MessageGetMyrmexHive.class, MessageSetMyrmexHiveNull.class, MessagePlayerHitMultipart.class,
            MessageAddChainedEntity.class, MessageRemoveChainedEntity.class, MessageDragonSetBurnBlock.class, MessageDragonSyncFire.class, MessageSpawnParticleAt.class,
            MessageStartRidingMob.class})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;
    @SidedProxy(clientSide = "com.github.alexthe666.iceandfire.ClientProxy", serverSide = "com.github.alexthe666.iceandfire.CommonProxy")
    public static CommonProxy PROXY;
    public static CreativeTabs TAB_ITEMS;
    public static CreativeTabs TAB_BLOCKS;
    public static DamageSource dragon;
    public static DamageSource dragonFire;
    public static DamageSource dragonIce;
    public static DamageSource gorgon;
    public static IceAndFireConfig CONFIG = new IceAndFireConfig();
    public static Configuration config;
    public static final boolean DEBUG = false;

    public static void loadConfig() {
        File configFile = new File(Loader.instance().getConfigDir(), "ice_and_fire.cfg");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                logger.warn("Could not create a new Ice and Fire config file.");
                logger.warn(e.getLocalizedMessage());
            }
        }
        config = new Configuration(configFile);
        config.load();
    }

    public static void syncConfig() {
        CONFIG.init(config);
        config.save();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        loadConfig();
        syncConfig();
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
        TAB_ITEMS = new CreativeTab(MODID + "_items");
        TAB_BLOCKS = new CreativeTab(MODID + "_blocks");
        IafEntityRegistry.init();
        IafWorldRegistry.init();
        MinecraftForge.EVENT_BUS.register(PROXY);
        logger.info("A raven flies from the north to the sea");
        logger.info("A dragon whispers her name in the east");
        ThaumcraftCompatBridge.loadThaumcraftCompat();
        LootFunctionManager.registerFunction(new CustomizeToDragon.Serializer());
        LootFunctionManager.registerFunction(new CustomizeToSeaSerpent.Serializer());
        OneProbeCompatBridge.loadPreInit();
        TinkersCompatBridge.loadTinkersCompat();
        CraftTweakerCompatBridge.loadTweakerCompat();
        PROXY.preRender();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        IafVillagerRegistry.INSTANCE.init();
        logger.info("The watcher waits on the northern wall");
        logger.info("A daughter picks up a warrior's sword");
        MapGenStructureIO.registerStructure(MapGenSnowVillage.Start.class, "SnowVillageStart");
        MapGenStructureIO.registerStructureComponent(ComponentAnimalFarm.class, "AnimalFarm");
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageAnimalFarmCreator());
        PROXY.render();
        GameRegistry.registerWorldGenerator(new WorldGenEvents(), 0);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new com.github.alexthe666.iceandfire.client.GuiHandler());
        dragon = new DamageSource("dragon") {
            @Override
            public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                String s = "death.attack.dragon";
                String s1 = s + ".player_" + new Random().nextInt(2);
                return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName()));
            }
        };
        dragonFire = new DamageSource("dragon_fire") {
            @Override
            public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                String s = "death.attack.dragon_fire";
                String s1 = s + ".player_" + new Random().nextInt(2);
                return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName()));
            }
        }.setFireDamage();
        dragonIce = new DamageSource("dragon_ice") {
            @Override
            public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                String s = "death.attack.dragon_ice";
                String s1 = s + ".player_" + new Random().nextInt(2);
                return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName()));
            }
        };
        gorgon = new DamageSource("gorgon") {
            @Override
            public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
                String s = "death.attack.gorgon";
                String s1 = s + ".player_" + new Random().nextInt(2);
                return new TextComponentString(entityLivingBaseIn.getDisplayName().getFormattedText() + " ").appendSibling(new TextComponentTranslation(s1, entityLivingBaseIn.getDisplayName()));
            }
        }.setDamageBypassesArmor();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postRender();
        TinkersCompatBridge.loadTinkersPostInitCompat();
        IafRecipeRegistry.postInit();
        logger.info("A brother bound to a love he must hide");
        logger.info("The younger's armor is worn in the mind");
        logger.info("A cold iron throne holds a boy barely grown");
        logger.info("And now it is known");
        logger.info("A claim to the prize, a crown laced in lies");
        logger.info("You win or you die");
        logger.info("Damn season 8 really sucked didn't it");
    }
}
