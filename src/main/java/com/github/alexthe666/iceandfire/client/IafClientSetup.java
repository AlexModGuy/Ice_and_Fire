package com.github.alexthe666.iceandfire.client;


import com.github.alexthe666.citadel.client.model.TabulaModel;
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
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = IceAndFire.MODID)
public class IafClientSetup {

    public static TabulaModel FIRE_DRAGON_BASE_MODEL;
    public static TabulaModel ICE_DRAGON_BASE_MODEL;
    public static TabulaModel SEA_SERPENT_BASE_MODEL;
    public static TabulaModel LIGHTNING_DRAGON_BASE_MODEL;
    private static ShaderInstance rendertypeDreadPortalShader;
    private static ShaderInstance rendertypeScalableTextureShader;
    public static final ResourceLocation GHOST_CHEST_LOCATION = new ResourceLocation(IceAndFire.MODID, "models/ghost/ghost_chest");
    public static final ResourceLocation GHOST_CHEST_LEFT_LOCATION = new ResourceLocation(IceAndFire.MODID, "models/ghost/ghost_chest_left");
    public static final ResourceLocation GHOST_CHEST_RIGHT_LOCATION = new ResourceLocation(IceAndFire.MODID, "models/ghost/ghost_chest_right");


    @SubscribeEvent
    public static void setupLayer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(IafEntityRegistry.FIRE_DRAGON.get(), manager -> new RenderDragonBase(manager, FIRE_DRAGON_BASE_MODEL, 0));
        event.registerEntityRenderer(IafEntityRegistry.ICE_DRAGON.get(), manager -> new RenderDragonBase(manager, ICE_DRAGON_BASE_MODEL, 1));
        event.registerEntityRenderer(IafEntityRegistry.LIGHTNING_DRAGON.get(), manager -> new RenderLightningDragon(manager, LIGHTNING_DRAGON_BASE_MODEL, 2));
        event.registerEntityRenderer(IafEntityRegistry.DRAGON_EGG.get(), RenderDragonEgg::new);
        event.registerEntityRenderer(IafEntityRegistry.DRAGON_ARROW.get(), RenderDragonArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.DRAGON_SKULL.get(), manager -> new RenderDragonSkull(manager, FIRE_DRAGON_BASE_MODEL, ICE_DRAGON_BASE_MODEL, LIGHTNING_DRAGON_BASE_MODEL));
        event.registerEntityRenderer(IafEntityRegistry.FIRE_DRAGON_CHARGE.get(), manager -> new RenderDragonFireCharge(manager, true));
        event.registerEntityRenderer(IafEntityRegistry.ICE_DRAGON_CHARGE.get(), manager -> new RenderDragonFireCharge(manager, false));
        event.registerEntityRenderer(IafEntityRegistry.LIGHTNING_DRAGON_CHARGE.get(), RenderDragonLightningCharge::new);
        event.registerEntityRenderer(IafEntityRegistry.HIPPOGRYPH_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(IafEntityRegistry.HIPPOGRYPH.get(), RenderHippogryph::new);
        event.registerEntityRenderer(IafEntityRegistry.STONE_STATUE.get(), RenderStoneStatue::new);
        event.registerEntityRenderer(IafEntityRegistry.GORGON.get(), RenderGorgon::new);
        event.registerEntityRenderer(IafEntityRegistry.PIXIE.get(), RenderPixie::new);
        event.registerEntityRenderer(IafEntityRegistry.CYCLOPS.get(), RenderCyclops::new);
        event.registerEntityRenderer(IafEntityRegistry.SIREN.get(), RenderSiren::new);
        event.registerEntityRenderer(IafEntityRegistry.HIPPOCAMPUS.get(), RenderHippocampus::new);
        event.registerEntityRenderer(IafEntityRegistry.DEATH_WORM.get(), RenderDeathWorm::new);
        event.registerEntityRenderer(IafEntityRegistry.DEATH_WORM_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(IafEntityRegistry.COCKATRICE.get(), RenderCockatrice::new);
        event.registerEntityRenderer(IafEntityRegistry.COCKATRICE_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(IafEntityRegistry.STYMPHALIAN_BIRD.get(), RenderStymphalianBird::new);
        event.registerEntityRenderer(IafEntityRegistry.STYMPHALIAN_FEATHER.get(), RenderStymphalianFeather::new);
        event.registerEntityRenderer(IafEntityRegistry.STYMPHALIAN_ARROW.get(), RenderStymphalianArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.TROLL.get(), RenderTroll::new);
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_WORKER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexWorker(), 0.5F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_SOLDIER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexSoldier(), 0.75F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_QUEEN.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexQueen(), 1.25F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_EGG.get(), RenderMyrmexEgg::new);
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_SENTINEL.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexSentinel(), 0.85F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_ROYAL.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.75F));
        event.registerEntityRenderer(IafEntityRegistry.MYRMEX_SWARMER.get(), manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.25F));
        event.registerEntityRenderer(IafEntityRegistry.AMPHITHERE.get(), RenderAmphithere::new);
        event.registerEntityRenderer(IafEntityRegistry.AMPHITHERE_ARROW.get(), RenderAmphithereArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.SEA_SERPENT.get(), manager -> new RenderSeaSerpent(manager, SEA_SERPENT_BASE_MODEL));
        event.registerEntityRenderer(IafEntityRegistry.SEA_SERPENT_BUBBLES.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.SEA_SERPENT_ARROW.get(), RenderSeaSerpentArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.CHAIN_TIE.get(), RenderChainTie::new);
        event.registerEntityRenderer(IafEntityRegistry.PIXIE_CHARGE.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.TIDE_TRIDENT.get(), RenderTideTrident::new);
        event.registerEntityRenderer(IafEntityRegistry.MOB_SKULL.get(), manager -> new RenderMobSkull(manager, SEA_SERPENT_BASE_MODEL));
        event.registerEntityRenderer(IafEntityRegistry.DREAD_SCUTTLER.get(), RenderDreadScuttler::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_GHOUL.get(), RenderDreadGhoul::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_BEAST.get(), RenderDreadBeast::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_SCUTTLER.get(), RenderDreadScuttler::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_THRALL.get(), RenderDreadThrall::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_LICH.get(), RenderDreadLich::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_LICH_SKULL.get(), RenderDreadLichSkull::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_KNIGHT.get(), RenderDreadKnight::new);
        event.registerEntityRenderer(IafEntityRegistry.DREAD_HORSE.get(), RenderDreadHorse::new);
        event.registerEntityRenderer(IafEntityRegistry.HYDRA.get(), RenderHydra::new);
        event.registerEntityRenderer(IafEntityRegistry.HYDRA_BREATH.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.HYDRA_ARROW.get(), RenderHydraArrow::new);
        event.registerEntityRenderer(IafEntityRegistry.SLOW_MULTIPART.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.DRAGON_MULTIPART.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.CYCLOPS_MULTIPART.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.HYDRA_MULTIPART.get(), RenderNothing::new);
        event.registerEntityRenderer(IafEntityRegistry.GHOST.get(), RenderGhost::new);
        event.registerEntityRenderer(IafEntityRegistry.GHOST_SWORD.get(), RenderGhostSword::new);

        event.registerBlockEntityRenderer(IafTileEntityRegistry.PODIUM.get(), RenderPodium::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.IAF_LECTERN.get(), RenderLectern::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.EGG_IN_ICE.get(), RenderEggInIce::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.PIXIE_HOUSE.get(), RenderPixieHouse::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.PIXIE_JAR.get(), RenderJar::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.DREAD_PORTAL.get(), RenderDreadPortal::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.DREAD_SPAWNER.get(), RenderDreadSpawner::new);
        event.registerBlockEntityRenderer(IafTileEntityRegistry.GHOST_CHEST.get(), RenderGhostChest::new);
    }

    @SubscribeEvent
    public static void setupShaders(RegisterShadersEvent event) throws IOException {
        ResourceManager manager = event.getResourceManager();
        event.registerShader(new ShaderInstance(manager, new ResourceLocation(IceAndFire.MODID, "rendertype_dread_portal"), DefaultVertexFormat.POSITION_COLOR), (p_172782_) -> {
            rendertypeDreadPortalShader = p_172782_;
        });
        event.registerShader(new ShaderInstance(manager, new ResourceLocation(IceAndFire.MODID, "rendertype_scalable_texture"), DefaultVertexFormat.NEW_ENTITY), (p_172782_) -> {
            rendertypeScalableTextureShader = p_172782_;
        });
    }

    public static ShaderInstance getRendertypeDreadPortalShader() {
        return rendertypeDreadPortalShader;
    }

    public static ShaderInstance getRendertypeScalableTextureShader() {
        return rendertypeScalableTextureShader;
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (!event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            return;
        }
        event.addSprite(GHOST_CHEST_LOCATION);
        event.addSprite(GHOST_CHEST_RIGHT_LOCATION);
        event.addSprite(GHOST_CHEST_LEFT_LOCATION);
    }

    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent event) {
        IafGuiRegistry.register();
        event.enqueueWork(() -> {
            EnumDragonAnimations.initializeDragonModels();
            EnumSeaSerpentAnimations.initializeSerpentModels();
            DragonAnimationsLibrary.register(EnumDragonPoses.values(), EnumDragonModelTypes.values());

            try {
                SEA_SERPENT_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/seaserpent/seaserpent"), new SeaSerpentTabulaModelAnimator());
                FIRE_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/firedragon_Ground"), new FireDragonTabulaModelAnimator());
                ICE_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/icedragon_Ground"), new IceDragonTabulaModelAnimator());
                LIGHTNING_DRAGON_BASE_MODEL = new TabulaModel(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/lightningdragon/lightningdragon_Ground"), new LightningTabulaDragonAnimator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.GOLD_PILE, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.SILVER_PILE, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.LECTERN, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PODIUM_OAK, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PODIUM_BIRCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PODIUM_SPRUCE, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PODIUM_JUNGLE, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PODIUM_ACACIA, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PODIUM_DARK_OAK, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.FIRE_LILY, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.FROST_LILY, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.LIGHTNING_LILY, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.DRAGON_ICE_SPIKES, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.MYRMEX_DESERT_RESIN_BLOCK, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.MYRMEX_DESERT_RESIN_GLASS, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.MYRMEX_JUNGLE_RESIN_BLOCK, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.MYRMEX_JUNGLE_RESIN_GLASS, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.MYRMEX_DESERT_BIOLIGHT, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.MYRMEX_JUNGLE_BIOLIGHT, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.DREAD_STONE_FACE, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.DREAD_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.BURNT_TORCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.EGG_IN_ICE, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.JAR_EMPTY, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.JAR_PIXIE_0, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.JAR_PIXIE_1, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.JAR_PIXIE_2, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.JAR_PIXIE_3, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.JAR_PIXIE_4, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_OAK, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_BIRCH, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_SPRUCE, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.PIXIE_HOUSE_DARK_OAK, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.DREAD_SPAWNER, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.DREAD_TORCH_WALL, RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(IafBlockRegistry.BURNT_TORCH_WALL, RenderType.cutout());
        ItemPropertyFunction pulling = ItemProperties.getProperty(Items.BOW, new ResourceLocation("pulling"));
        ItemPropertyFunction pull = (stack, worldIn, entity, p) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                ItemDragonBow item = ((ItemDragonBow) stack.getItem());
                return entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        };
        ItemProperties.register(IafItemRegistry.DRAGON_BOW.asItem(), new ResourceLocation("pulling"), pulling);
        ItemProperties.register(IafItemRegistry.DRAGON_BOW.asItem(), new ResourceLocation("pull"), pull);
        ItemProperties.register(IafItemRegistry.DRAGON_HORN, new ResourceLocation("iceorfire"), (stack, level, entity, p) -> {
            return ItemDragonHorn.getDragonType(stack) * 0.25F;
        });
        ItemProperties.register(IafItemRegistry.SUMMONING_CRYSTAL_FIRE, new ResourceLocation("has_dragon"), (stack, level, entity, p) -> {
            return ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F;
        });
        ItemProperties.register(IafItemRegistry.SUMMONING_CRYSTAL_ICE, new ResourceLocation("has_dragon"), (stack, level, entity, p) -> {
            return ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F;
        });
        ItemProperties.register(IafItemRegistry.SUMMONING_CRYSTAL_LIGHTNING, new ResourceLocation("has_dragon"), (stack, level, entity, p) -> {
            return ItemSummoningCrystal.hasDragon(stack) ? 1.0F : 0.0F;
        });
        ItemProperties.register(IafItemRegistry.TIDE_TRIDENT, new ResourceLocation("throwing"), (stack, level, entity, p) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        });
    }

}
