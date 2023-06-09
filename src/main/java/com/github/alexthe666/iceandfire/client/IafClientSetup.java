package com.github.alexthe666.iceandfire.client;


import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.TabulaModelHandler;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.gui.IafGuiRegistry;
import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.client.model.animator.FireDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.IceDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.LightningTabulaDragonAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.SeaSerpentTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.*;
import com.github.alexthe666.iceandfire.client.render.entity.*;
import com.github.alexthe666.iceandfire.client.render.tile.*;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonBow;
import com.github.alexthe666.iceandfire.item.ItemDragonHorn;
import com.github.alexthe666.iceandfire.item.ItemSummoningCrystal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = IceAndFire.MODID)
public class IafClientSetup {

    public static TabulaModel FIRE_DRAGON_BASE_MODEL;
    public static TabulaModel ICE_DRAGON_BASE_MODEL;
    public static TabulaModel SEA_SERPENT_BASE_MODEL;
    public static TabulaModel LIGHTNING_DRAGON_BASE_MODEL;

    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        IafGuiRegistry.register();
        EnumDragonAnimations.initializeDragonModels();
        EnumSeaSerpentAnimations.initializeSerpentModels();
        DragonAnimationsLibrary.register(EnumDragonPoses.values(), EnumDragonModelTypes.values());
        try {
            SEA_SERPENT_BASE_MODEL = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/seaserpent/seaserpent"), new SeaSerpentTabulaModelAnimator());
            FIRE_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/firedragon_Ground"), new FireDragonTabulaModelAnimator());
            ICE_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/icedragon_Ground"), new IceDragonTabulaModelAnimator());
            LIGHTNING_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/lightningdragon/lightningdragon_Ground"), new LightningTabulaDragonAnimator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //@formatter:off
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.FIRE_DRAGON.get(), manager -> new RenderDragonBase(manager, FIRE_DRAGON_BASE_MODEL, 0));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.ICE_DRAGON.get(), manager -> new RenderDragonBase(manager, ICE_DRAGON_BASE_MODEL, 1));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.LIGHTNING_DRAGON.get(), manager -> new RenderLightningDragon(manager, LIGHTNING_DRAGON_BASE_MODEL, 2));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DRAGON_EGG.get(), manager -> new RenderDragonEgg(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DRAGON_ARROW.get(), manager -> new RenderDragonArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DRAGON_SKULL.get(), manager -> new RenderDragonSkull(manager, FIRE_DRAGON_BASE_MODEL, ICE_DRAGON_BASE_MODEL, LIGHTNING_DRAGON_BASE_MODEL));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.FIRE_DRAGON_CHARGE.get(), manager -> new RenderDragonFireCharge(manager, true));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.ICE_DRAGON_CHARGE.get(), manager -> new RenderDragonFireCharge(manager, false));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.LIGHTNING_DRAGON_CHARGE.get(), manager -> new RenderDragonLightningCharge());
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.HIPPOGRYPH_EGG.get(), manager -> new SpriteRenderer(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.HIPPOGRYPH, manager -> new RenderHippogryph(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.STONE_STATUE.get(), manager -> new RenderStoneStatue(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.GORGON.get(), manager -> new RenderGorgon(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.PIXIE.get(), manager -> new RenderPixie(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.CYCLOPS.get(), manager -> new RenderCyclops(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.SIREN, manager -> new RenderSiren(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.HIPPOCAMPUS, manager -> new RenderHippocampus(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DEATH_WORM.get(), manager -> new RenderDeathWorm(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DEATH_WORM_EGG.get(), manager -> new SpriteRenderer(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.COCKATRICE.get(), manager -> new RenderCockatrice(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.COCKATRICE_EGG.get(), manager -> new SpriteRenderer(manager, Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.STYMPHALIAN_BIRD.get(), manager -> new RenderStymphalianBird(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.STYMPHALIAN_FEATHER.get(), manager -> new RenderStymphalianFeather(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.STYMPHALIAN_ARROW.get(), manager -> new RenderStymphalianArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.TROLL, manager -> new RenderTroll(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.MYRMEX_WORKER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexWorker(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.MYRMEX_SOLDIER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexSoldier(), 0.75F));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.MYRMEX_QUEEN.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexQueen(), 1.25F));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.MYRMEX_EGG.get(), manager -> new RenderMyrmexEgg(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.MYRMEX_SENTINEL.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexSentinel(), 0.85F));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.MYRMEX_ROYAL.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.75F));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.MYRMEX_SWARMER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.25F));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.AMPHITHERE, manager -> new RenderAmphithere(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.AMPHITHERE_ARROW.get(), manager -> new RenderAmphithereArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.SEA_SERPENT, manager -> new RenderSeaSerpent(manager, SEA_SERPENT_BASE_MODEL));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.SEA_SERPENT_BUBBLES.get(), manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.SEA_SERPENT_ARROW.get(), manager -> new RenderSeaSerpentArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.CHAIN_TIE.get(), manager -> new RenderChainTie(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.PIXIE_CHARGE.get(), manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.TIDE_TRIDENT.get(), manager -> new RenderTideTrident(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.MOB_SKULL.get(), manager -> new RenderMobSkull(manager, SEA_SERPENT_BASE_MODEL));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DREAD_SCUTTLER, manager -> new RenderDreadScuttler(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DREAD_GHOUL, manager -> new RenderDreadGhoul(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DREAD_BEAST, manager -> new RenderDreadBeast(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DREAD_SCUTTLER, manager -> new RenderDreadScuttler(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DREAD_THRALL, manager -> new RenderDreadThrall(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DREAD_LICH, manager -> new RenderDreadLich(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DREAD_LICH_SKULL.get(), manager -> new RenderDreadLichSkull());
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DREAD_KNIGHT, manager -> new RenderDreadKnight(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DREAD_HORSE, manager -> new RenderDreadHorse(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.HYDRA, manager -> new RenderHydra(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.HYDRA_BREATH.get(), manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.HYDRA_ARROW.get(), manager -> new RenderHydraArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.SLOW_MULTIPART.get(), manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.DRAGON_MULTIPART.get(), manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.CYCLOPS_MULTIPART.get(), manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.HYDRA_MULTIPART.get(), manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.GHOST, manager -> new RenderGhost(manager));
        RenderingRegistry.registerEntityRenderingHandler(IafEntityRegistry.GHOST_SWORD, manager -> new RenderGhostSword(manager));
        //@formatter:off
        ClientRegistry.bindTileEntityRenderer(IafTileEntityRegistry.PODIUM.get(), manager -> new RenderPodium(manager));
        ClientRegistry.bindTileEntityRenderer(IafTileEntityRegistry.IAF_LECTERN.get(), manager -> new RenderLectern(manager));
        ClientRegistry.bindTileEntityRenderer(IafTileEntityRegistry.EGG_IN_ICE.get(), manager -> new RenderEggInIce(manager));
        ClientRegistry.bindTileEntityRenderer(IafTileEntityRegistry.PIXIE_HOUSE.get(), manager -> new RenderPixieHouse(manager));
        ClientRegistry.bindTileEntityRenderer(IafTileEntityRegistry.PIXIE_JAR.get(), manager -> new RenderJar(manager));
        ClientRegistry.bindTileEntityRenderer(IafTileEntityRegistry.DREAD_PORTAL.get(), manager -> new RenderDreadPortal(manager));
        ClientRegistry.bindTileEntityRenderer(IafTileEntityRegistry.DREAD_SPAWNER.get(), manager -> new RenderDreadSpawner(manager));
        ClientRegistry.bindTileEntityRenderer(IafTileEntityRegistry.GHOST_CHEST.get(), manager -> new RenderGhostChest(manager));
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.GOLD_PILE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.SILVER_PILE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.LECTERN, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PODIUM_OAK, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PODIUM_BIRCH, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PODIUM_SPRUCE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PODIUM_JUNGLE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PODIUM_ACACIA, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PODIUM_DARK_OAK, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.FIRE_LILY, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.FROST_LILY, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.LIGHTNING_LILY, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.DRAGON_ICE_SPIKES, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.MYRMEX_DESERT_RESIN_BLOCK, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.MYRMEX_DESERT_RESIN_GLASS, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.MYRMEX_JUNGLE_RESIN_BLOCK, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.MYRMEX_JUNGLE_RESIN_GLASS, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.MYRMEX_DESERT_BIOLIGHT, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.MYRMEX_JUNGLE_BIOLIGHT, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.DREAD_STONE_FACE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.DREAD_TORCH, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.BURNT_TORCH, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.EGG_IN_ICE, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.JAR_EMPTY, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.JAR_PIXIE_0, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.JAR_PIXIE_1, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.JAR_PIXIE_2, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.JAR_PIXIE_3, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.JAR_PIXIE_4, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_OAK, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_BIRCH, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_SPRUCE, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_DARK_OAK, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.DREAD_SPAWNER, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.DREAD_TORCH_WALL, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(IafBlockRegistry.BURNT_TORCH_WALL, RenderType.getCutout());
        IItemPropertyGetter pulling = ItemModelsProperties.func_239417_a_(Items.BOW, new ResourceLocation("pulling"));
        IItemPropertyGetter pull = (stack, worldIn, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                ItemDragonBow item = ((ItemDragonBow) stack.getItem());
                return entity.getActiveItemStack() != stack ? 0.0F : (stack.getUseDuration() - entity.getItemInUseCount()) / 20.0F;
            }
        };
        ItemModelsProperties.registerProperty(IafItemRegistry.DRAGON_BOW.asItem(), new ResourceLocation("pulling"), pulling);
        ItemModelsProperties.registerProperty(IafItemRegistry.DRAGON_BOW.asItem(), new ResourceLocation("pull"), pull);
        ItemModelsProperties.registerProperty(IafItemRegistry.DRAGON_HORN, new ResourceLocation("iceorfire"), (p_239428_0_, p_239428_1_, p_239428_2_) -> {
            return ItemDragonHorn.getDragonType(p_239428_0_) * 0.25F;
        });
        ItemModelsProperties.registerProperty(IafItemRegistry.SUMMONING_CRYSTAL_FIRE, new ResourceLocation("has_dragon"), (stack, p_239428_1_, p_239428_2_) -> {
            return ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F;
        });
        ItemModelsProperties.registerProperty(IafItemRegistry.SUMMONING_CRYSTAL_ICE, new ResourceLocation("has_dragon"), (stack, p_239428_1_, p_239428_2_) -> {
            return ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F;
        });
        ItemModelsProperties.registerProperty(IafItemRegistry.SUMMONING_CRYSTAL_LIGHTNING, new ResourceLocation("has_dragon"), (stack, p_239428_1_, p_239428_2_) -> {
            return ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F;
        });
        ItemModelsProperties.registerProperty(IafItemRegistry.TIDE_TRIDENT, new ResourceLocation("throwing"), (p_239419_0_, p_239419_1_, p_239419_2_) -> {
            return p_239419_2_ != null && p_239419_2_.isHandActive() && p_239419_2_.getActiveItemStack() == p_239419_0_ ? 1.0F : 0.0F;
        });
    }
}
