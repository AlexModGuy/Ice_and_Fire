package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.config.ConfigHolder;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.inventory.IafContainerRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.loot.IafLootRegistry;
import com.github.alexthe666.iceandfire.message.*;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(IceAndFire.MODID)
@Mod.EventBusSubscriber(modid = IceAndFire.MODID)
public class IceAndFire {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "iceandfire";
    public static final SimpleChannel NETWORK_WRAPPER;
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static boolean DEBUG = true;
    public static String VERSION = "UNKNOWN";
    public static ItemGroup TAB_ITEMS = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE);
        }
    };
    public static ItemGroup TAB_BLOCKS = new ItemGroup("iceandfire.blocks") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(IafBlockRegistry.DRAGON_SCALE_RED);
        }
    };
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    private static int packetsRegistered = 0;

    static {
        NetworkRegistry.ChannelBuilder channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("iceandfire", "main_channel"));
        String version = PROTOCOL_VERSION;
        version.getClass();
        channel = channel.clientAcceptedVersions(version::equals);
        version = PROTOCOL_VERSION;
        version.getClass();
        NETWORK_WRAPPER = channel.serverAcceptedVersions(version::equals).networkProtocolVersion(() -> {
            return PROTOCOL_VERSION;
        }).simpleChannel();
    }

    public IceAndFire() {
        try {
            ModContainer mod = ModList.get().getModContainerById(IceAndFire.MODID).orElseThrow(NullPointerException::new);
            VERSION = mod.getModInfo().getVersion().toString();
        } catch (Exception ignored) {
        }

        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadFromJSON);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        PROXY.init();

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IafContainerRegistry.CONTAINERS.register(modBus);
        IafEntityRegistry.ENTITIES.register(modBus);
        IafTileEntityRegistry.TYPES.register(modBus);
        IafWorldRegistry.STRUCTURES.register(modBus);
        IafWorldRegistry.FEATURES.register(modBus);
        IafRecipeRegistry.SERIALIZERS.register(modBus);
        modBus.addListener(this::setup);
        modBus.addListener(this::setupComplete);
        modBus.addGenericListener(Structure.class, EventPriority.LOW,
            (final RegistryEvent.Register<Structure<?>> event) -> IafWorldRegistry
                .registerStructureConfiguredFeatures());
        modBus.addGenericListener(Feature.class, EventPriority.LOW,
            (final RegistryEvent.Register<Feature<?>> event) -> IafWorldRegistry.registerConfiguredFeatures());
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
        LOGGER.info(IafWorldRegistry.LOADED_FEATURES);
        LOGGER.info(IafEntityRegistry.LOADED_ENTITIES);
    }

    @SubscribeEvent
    public void onBiomeLoadFromJSON(BiomeLoadingEvent event) {
        IafWorldRegistry.onBiomesLoad(event);
        IafEntityRegistry.onBiomesLoad(event);
    }

    public static <MSG> void sendMSGToServer(MSG message) {
        NETWORK_WRAPPER.sendToServer(message);
    }

    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            NETWORK_WRAPPER.sendTo(message, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    public static <MSG> void sendMSGToPlayer(MSG message, ServerPlayerEntity player) {
        NETWORK_WRAPPER.sendTo(message, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    private void setup(final FMLCommonSetupEvent event) {
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageDaytime.class, MessageDaytime::write, MessageDaytime::read, MessageDaytime.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageDeathWormHitbox.class, MessageDeathWormHitbox::write, MessageDeathWormHitbox::read, MessageDeathWormHitbox.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageDragonControl.class, MessageDragonControl::write, MessageDragonControl::read, MessageDragonControl.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageDragonSetBurnBlock.class, MessageDragonSetBurnBlock::write, MessageDragonSetBurnBlock::read, MessageDragonSetBurnBlock.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageDragonSyncFire.class, MessageDragonSyncFire::write, MessageDragonSyncFire::read, MessageDragonSyncFire.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageGetMyrmexHive.class, MessageGetMyrmexHive::write, MessageGetMyrmexHive::read, MessageGetMyrmexHive.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageMyrmexSettings.class, MessageMyrmexSettings::write, MessageMyrmexSettings::read, MessageMyrmexSettings.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageHippogryphArmor.class, MessageHippogryphArmor::write, MessageHippogryphArmor::read, MessageHippogryphArmor.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageMultipartInteract.class, MessageMultipartInteract::write, MessageMultipartInteract::read, MessageMultipartInteract.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessagePlayerHitMultipart.class, MessagePlayerHitMultipart::write, MessagePlayerHitMultipart::read, MessagePlayerHitMultipart.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSetMyrmexHiveNull.class, MessageSetMyrmexHiveNull::write, MessageSetMyrmexHiveNull::read, MessageSetMyrmexHiveNull.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSirenSong.class, MessageSirenSong::write, MessageSirenSong::read, MessageSirenSong.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSpawnParticleAt.class, MessageSpawnParticleAt::write, MessageSpawnParticleAt::read, MessageSpawnParticleAt.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageStartRidingMob.class, MessageStartRidingMob::write, MessageStartRidingMob::read, MessageStartRidingMob.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdatePixieHouse.class, MessageUpdatePixieHouse::write, MessageUpdatePixieHouse::read, MessageUpdatePixieHouse.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdatePixieHouseModel.class, MessageUpdatePixieHouseModel::write, MessageUpdatePixieHouseModel::read, MessageUpdatePixieHouseModel.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdatePixieJar.class, MessageUpdatePixieJar::write, MessageUpdatePixieJar::read, MessageUpdatePixieJar.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdatePodium.class, MessageUpdatePodium::write, MessageUpdatePodium::read, MessageUpdatePodium.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdateDragonforge.class, MessageUpdateDragonforge::write, MessageUpdateDragonforge::read, MessageUpdateDragonforge.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdateLectern.class, MessageUpdateLectern::write, MessageUpdateLectern::read, MessageUpdateLectern.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSyncPath.class, MessageSyncPath::write, MessageSyncPath::read, MessageSyncPath::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSyncPathReached.class, MessageSyncPathReached::write, MessageSyncPathReached::read, MessageSyncPathReached::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSwingArm.class, MessageSwingArm::write, MessageSwingArm::read, MessageSwingArm.Handler::handle);
        event.enqueueWork(() -> {
            PROXY.setup();
            IafProcessors.registerProcessors();
            IafWorldRegistry.setup();
            IafVillagerRegistry.setup();
            IafLootRegistry.init();
        });
    }

    private void setupComplete(final FMLLoadCompleteEvent event) {
        PROXY.postInit();
    }

}