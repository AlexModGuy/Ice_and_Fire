package com.github.alexthe666.iceandfire;


import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;

import java.util.logging.Logger;

@Mod(IceAndFire.MODID)
@Mod.EventBusSubscriber(modid = IceAndFire.MODID)
public class IceAndFire {
    public static final Logger LOGGER = (Logger)LogManager.getLogger();
    public static final String MODID = "iceandfire";
    public static final SimpleChannel NETWORK_WRAPPER;
    public static final boolean DEBUG = false;
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
            return new ItemStack(IafItemRegistry.DRAGONSCALES_RED);
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
        //modLoadingContext.registerConfig(ModConfig.Type.CLIENT, ConfigHolder.CLIENT_SPEC);
        //modLoadingContext.registerConfig(ModConfig.Type.SERVER, ConfigHolder.SERVER_SPEC);
        PROXY.render();
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
    }

    private void setupClient(FMLClientSetupEvent event) {
    }
}