package com.github.alexthe666.iceandfire;


import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.config.ConfigHolder;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.loot.CustomizeToDragon;
import com.github.alexthe666.iceandfire.loot.CustomizeToSeaSerpent;
import com.github.alexthe666.iceandfire.loot.IafLootRegistry;
import com.github.alexthe666.iceandfire.message.*;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
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
    public static final boolean DEBUG = false;
    public static final String VERSION = "2.0.2-1.16.1";
    private static final String PROTOCOL_VERSION = Integer.toString(1);
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
    public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
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
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupComplete);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, ConfigHolder.SERVER_SPEC);
        PROXY.init();
        IafWorldRegistry.register();
    }

    static{
        IafLootRegistry.init();
    }

    public static <MSG> void sendMSGToServer(MSG message) {
        NETWORK_WRAPPER.sendToServer(message);
    }

    public static <MSG> void sendMSGToAll(MSG message) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendNonLocal(message, player);
        }
    }

    public static <MSG> void sendNonLocal(MSG msg, ServerPlayerEntity player) {
        if (player.server.isDedicatedServer() || !player.getName().equals(player.server.getServerOwner())) {
            NETWORK_WRAPPER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageAddChainedEntity.class, MessageAddChainedEntity::write, MessageAddChainedEntity::read, MessageAddChainedEntity.Handler::handle);
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
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageRemoveChainedEntity.class, MessageRemoveChainedEntity::write, MessageRemoveChainedEntity::read, MessageRemoveChainedEntity.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSetMyrmexHiveNull.class, MessageSetMyrmexHiveNull::write, MessageSetMyrmexHiveNull::read, MessageSetMyrmexHiveNull.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSirenSong.class, MessageSirenSong::write, MessageSirenSong::read, MessageSirenSong.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageSpawnParticleAt.class, MessageSpawnParticleAt::write, MessageSpawnParticleAt::read, MessageSpawnParticleAt.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageStartRidingMob.class, MessageStartRidingMob::write, MessageStartRidingMob::read, MessageStartRidingMob.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageStoneStatue.class, MessageStoneStatue::write, MessageStoneStatue::read, MessageStoneStatue.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdatePixieHouse.class, MessageUpdatePixieHouse::write, MessageUpdatePixieHouse::read, MessageUpdatePixieHouse.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdatePixieHouseModel.class, MessageUpdatePixieHouseModel::write, MessageUpdatePixieHouseModel::read, MessageUpdatePixieHouseModel.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdatePixieJar.class, MessageUpdatePixieJar::write, MessageUpdatePixieJar::read, MessageUpdatePixieJar.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdatePodium.class, MessageUpdatePodium::write, MessageUpdatePodium::read, MessageUpdatePodium.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdateDragonforge.class, MessageUpdateDragonforge::write, MessageUpdateDragonforge::read, MessageUpdateDragonforge.Handler::handle);
        NETWORK_WRAPPER.registerMessage(packetsRegistered++, MessageUpdateLectern.class, MessageUpdateLectern::write, MessageUpdateLectern::read, MessageUpdateLectern.Handler::handle);
        PROXY.setup();
        IafWorldRegistry.setup();
    }

    private void setupComplete(final FMLLoadCompleteEvent event) {
        PROXY.postInit();
    }

    private void setupClient(FMLClientSetupEvent event) {
        PROXY.setupClient();
    }
}