package com.github.alexthe666.iceandfire;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.TabulaModelHandler;
import com.github.alexthe666.iceandfire.client.gui.GuiMyrmexAddRoom;
import com.github.alexthe666.iceandfire.client.gui.GuiMyrmexStaff;
import com.github.alexthe666.iceandfire.client.gui.bestiary.GuiBestiary;
import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.client.model.animator.FireDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.IceDragonTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.animator.SeaSerpentTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonAnimations;
import com.github.alexthe666.iceandfire.client.model.util.EnumSeaSerpentAnimations;
import com.github.alexthe666.iceandfire.client.particle.*;
import com.github.alexthe666.iceandfire.client.render.RenderDreadlandsAurora;
import com.github.alexthe666.iceandfire.client.render.RenderDreadlandsSky;
import com.github.alexthe666.iceandfire.client.render.RenderDreadlandsWeather;
import com.github.alexthe666.iceandfire.client.render.entity.*;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerDragonArmor;
import com.github.alexthe666.iceandfire.client.render.tile.*;
import com.github.alexthe666.iceandfire.client.render.entity.RenderHydra;
import com.github.alexthe666.iceandfire.event.ClientEvents;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.tile.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler.*;

@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    private static final ModelFireDragonArmor FIRE_DRAGON_SCALE_ARMOR_MODEL = new ModelFireDragonArmor(0.5F, false);
    private static final ModelFireDragonArmor FIRE_DRAGON_SCALE_ARMOR_MODEL_LEGS = new ModelFireDragonArmor(0.2F, true);
    private static final ModelIceDragonArmor ICE_DRAGON_SCALE_ARMOR_MODEL = new ModelIceDragonArmor(0.5F, false);
    private static final ModelIceDragonArmor ICE_DRAGON_SCALE_ARMOR_MODEL_LEGS = new ModelIceDragonArmor(0.2F, true);
    private static final ModelDeathWormArmor DEATHWORM_ARMOR_MODEL = new ModelDeathWormArmor(0.5F);
    private static final ModelDeathWormArmor DEATHWORM_ARMOR_MODEL_LEGS = new ModelDeathWormArmor(0.2F);
    private static final ModelTrollArmor TROLL_ARMOR_MODEL = new ModelTrollArmor(0.75F);
    private static final ModelTrollArmor TROLL_ARMOR_MODEL_LEGS = new ModelTrollArmor(0.35F);
    private static final ModelSeaSerpentArmor TIDE_ARMOR_MODEL = new ModelSeaSerpentArmor(0.4F);
    private static final ModelSeaSerpentArmor TIDE_ARMOR_MODEL_LEGS = new ModelSeaSerpentArmor(0.2F);
    private static final ModelDragonsteelFireArmor DRAGONSTEEL_FIRE_ARMOR_MODEL = new ModelDragonsteelFireArmor(0.4F);
    private static final ModelDragonsteelFireArmor DRAGONSTEEL_FIRE_ARMOR_MODEL_LEGS = new ModelDragonsteelFireArmor(0.2F);
    private static final ModelDragonsteelIceArmor DRAGONSTEEL_ICE_ARMOR_MODEL = new ModelDragonsteelIceArmor(0.4F);
    private static final ModelDragonsteelIceArmor DRAGONSTEEL_ICE_ARMOR_MODEL_LEGS = new ModelDragonsteelIceArmor(0.2F);
    private static final ModelSilverArmor SILVER_ARMOR_MODEL = new ModelSilverArmor(0.5F);
    private static final ModelSilverArmor SILVER_ARMOR_MODEL_LEGS = new ModelSilverArmor(0.2F);
    @OnlyIn(Dist.CLIENT)
    private static final IceAndFireTEISR TEISR = new IceAndFireTEISR();
    public static List<UUID> currentDragonRiders = new ArrayList<UUID>();
    private static MyrmexHive referedClientHive = null;
    private IceAndFireParticleSpawner particleSpawner;
    private FontRenderer bestiaryFontRenderer;
    private int previousViewType = 0;
    private int thirdPersonViewDragon = 0;
    private IRenderHandler dreadlandsWeatherRenderer = new RenderDreadlandsWeather();
    private IRenderHandler dreadlandsSkyRenderer = new RenderDreadlandsSky();
    private IRenderHandler dreadlandsAuroraRender = new RenderDreadlandsAurora();

    public static MyrmexHive getReferedClientHive() {
        return referedClientHive;
    }

    public static void setReferedClientHive(MyrmexHive hive) {
        referedClientHive = hive;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    @SuppressWarnings("deprecation")
    public void render() {
        try{
            this.bestiaryFontRenderer = new FontRenderer(Minecraft.getInstance().gameSettings, new ResourceLocation("iceandfire:textures/font/bestiary.png"), Minecraft.getInstance().renderEngine, false);
            ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(this.bestiaryFontRenderer);
        }catch(Exception e){
            this.bestiaryFontRenderer = Minecraft.getInstance().fontRenderer;
        }
        this.particleSpawner = new IceAndFireParticleSpawner();
        ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(this.bestiaryFontRenderer);
        IafKeybindRegistry.init();
        MinecraftForge.EVENT_BUS.register(new RenderModCapes());
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDummyGorgonHead.class, manager -> new RenderGorgonHead(false));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDummyGorgonHeadActive.class, manager -> new RenderGorgonHead(true));
        ForgeHooksClient.registerTESRItemStack(IafItemRegistry.GORGON_HEAD, 0, TileEntityDummyGorgonHead.class);
        ForgeHooksClient.registerTESRItemStack(IafItemRegistry.GORGON_HEAD, 1, TileEntityDummyGorgonHeadActive.class);
        renderEntities();


    }

    @OnlyIn(Dist.CLIENT)
    public void postRender() {
        ClientEvents.initializeStoneLayer();

    }

    @SuppressWarnings("deprecation")
    @OnlyIn(Dist.CLIENT)
    private void renderEntities() {
        EnumDragonAnimations.initializeDragonModels();
        EnumSeaSerpentAnimations.initializeSerpentModels();
        TabulaModel firedragon_model = null;
        TabulaModel icedragon_model = null;
        TabulaModel seaserpent_model = null;

        try {
            firedragon_model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/dragonFireGround"), new FireDragonTabulaModelAnimator());
            icedragon_model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/dragonIceGround"), new IceDragonTabulaModelAnimator());
            seaserpent_model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/iceandfire/models/tabula/seaserpent/seaserpent"), new SeaSerpentTabulaModelAnimator());
        } catch (IOException e) {
            e.printStackTrace();
        }
        RenderingRegistry.registerEntityRenderingHandler(EntityFireDragon.class, manager -> new RenderDragonBase(manager, firedragon_model, true));
        RenderingRegistry.registerEntityRenderingHandler(EntityIceDragon.class, manager -> new RenderDragonBase(manager, icedragon_model, false));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonEgg.class, manager -> new RenderDragonEgg(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonArrow.class, manager -> new RenderDragonArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonSkull.class, manager -> new RenderDragonSkull(manager, firedragon_model, icedragon_model));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonFireCharge.class, manager -> new RenderDragonFireCharge(manager, true));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonIceCharge.class, manager -> new RenderDragonFireCharge(manager, false));
        RenderingRegistry.registerEntityRenderingHandler(EntitySnowVillager.class, manager -> new RenderSnowVillager(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityHippogryphEgg.class, manager -> new RenderSnowball(manager, IafItemRegistry.HIPPOGRYPH_EGG, Minecraft.getInstance().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityHippogryph.class, manager -> new RenderHippogryph(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityStoneStatue.class, manager -> new RenderStoneStatue(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityGorgon.class, manager -> new RenderGorgon(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityPixie.class, manager -> new RenderPixie(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityCyclops.class, manager -> new RenderCyclops(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntitySiren.class, manager -> new RenderSiren(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityHippocampus.class, manager -> new RenderHippocampus(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDeathWorm.class, manager -> new RenderDeathWorm(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDeathWormEgg.class, manager -> new RenderSnowball(manager, IafItemRegistry.DEATHWORM_EGG, Minecraft.getInstance().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityCockatrice.class, manager -> new RenderCockatrice(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityCockatriceEgg.class, manager -> new RenderSnowball(manager, IafItemRegistry.ROTTEN_EGG, Minecraft.getInstance().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityStymphalianBird.class, manager -> new RenderStymphalianBird(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityStymphalianFeather.class, manager -> new RenderStymphalianFeather(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityStymphalianArrow.class, manager -> new RenderStymphalianArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityTroll.class, manager -> new RenderTroll(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexWorker.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexWorker(), 0.5F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexSoldier.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexSoldier(), 0.75F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexQueen.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexQueen(), 1.25F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexEgg.class, manager -> new RenderMyrmexEgg(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexSentinel.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexSentinel(), 0.85F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexRoyal.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.75F));
        RenderingRegistry.registerEntityRenderingHandler(EntityMyrmexSwarmer.class, manager -> new RenderMyrmexBase(manager, new ModelMyrmexRoyal(), 0.25F));
        RenderingRegistry.registerEntityRenderingHandler(EntityAmphithere.class, manager -> new RenderAmphithere(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityAmphithereArrow.class, manager -> new RenderAmphithereArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntitySeaSerpent.class, manager -> new RenderSeaSerpent(manager, seaserpent_model));
        RenderingRegistry.registerEntityRenderingHandler(EntitySeaSerpentBubbles.class, manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntitySeaSerpentArrow.class, manager -> new RenderSeaSerpentArrow(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityChainTie.class, manager -> new RenderChainTie(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityPixieCharge.class, manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityTideTrident.class, manager -> new RenderTideTrident(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityMobSkull.class, manager -> new RenderMobSkull(manager, seaserpent_model));
        RenderingRegistry.registerEntityRenderingHandler(EntityDreadThrall.class, manager -> new RenderDreadThrall(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDreadGhoul.class, manager -> new RenderDreadGhoul(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDreadBeast.class, manager -> new RenderDreadBeast(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDreadScuttler.class, manager -> new RenderDreadScuttler(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDreadLich.class, manager -> new RenderDreadLich(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDreadLichSkull.class, manager -> new RenderDreadLichSkull(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDreadKnight.class, manager -> new RenderDreadKnight(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityDreadHorse.class, manager -> new RenderDreadHorse(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityBlackFrostDragon.class, manager -> new RenderBlackFrostDragon(manager, icedragon_model, false));
        RenderingRegistry.registerEntityRenderingHandler(EntityDreadQueen.class, manager -> new RenderDreadQueen(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityHydra.class, manager -> new RenderHydra(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityHydraBreath.class, manager -> new RenderNothing(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityHydraArrow.class, manager -> new RenderHydraArrow(manager));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPodium.class,  new RenderPodium());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLectern.class,  new RenderLectern());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEggInIce.class,  new RenderEggInIce());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPixieHouse.class,  new RenderPixieHouse());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityJar.class,  new RenderJar());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDreadPortal.class,  new RenderDreadPortal());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDreadSpawner.class, new RenderDreadSpawner());
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnDragonParticle(String name, double x, double y, double z, double motX, double motY, double motZ, EntityDragonBase entityDragonBase) {
        World world = Minecraft.getInstance().world;
        if (world == null) {
            return;
        }
        net.minecraft.client.particle.Particle particle = null;
        if (name.equals("dragonfire")) {
            particle = new ParticleDragonFlame(world, x, y, z, motX, motY, motZ, entityDragonBase, 0);
        }
        if (name.equals("dragonice")) {
            particle = new ParticleDragonFrost(world, x, y, z, motX, motY, motZ, entityDragonBase, 0);
        }
        if (particle != null) {
            particleSpawner.spawnParticle(particle, true, true, false, x, y, z);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnParticle(String name, double x, double y, double z, double motX, double motY, double motZ, float size) {
        World world = Minecraft.getInstance().world;
        if (world == null) {
            return;
        }
        net.minecraft.client.particle.Particle particle = null;
        if (name.equals("dragonfire")) {
            particle = new ParticleDragonFlame(world, x, y, z, motX, motY, motZ, size);
        }
        if (name.equals("dragonice")) {
            particle = new ParticleDragonFrost(world, x, y, z, motX, motY, motZ, size);
        }
        if (name.equals("dread_torch")) {
            particle = new ParticleDreadTorch(world, x, y, z, motX, motY, motZ, size);
        }
        if (name.equals("dread_portal")) {
            particle = new ParticleDreadPortal(world, x, y, z, motX, motY, motZ, size);
        }
        if (name.equals("blood")) {
            particle = new ParticleBlood(world, x, y, z);
        }
        if (name.equals("if_pixie")) {
            particle = new ParticlePixieDust(world, x, y, z, (float) motX, (float) motY, (float) motZ);
        }
        if (name.equals("siren_appearance")) {
            particle = new ParticleSirenAppearance(world, x, y, z);
        }
        if (name.equals("siren_music")) {
            particle = new ParticleSirenMusic(world, x, y, z, motX, motY, motZ);
        }
        if (name.equals("serpent_bubble")) {
            particle = new ParticleSerpentBubble(world, x, y, z, motX, motY, motZ);
        }
        if (name.equals("hydra")) {
            particle = new ParticleHydraBreath(world, x, y, z, (float) motX, (float) motY, (float) motZ);
        }
        if (particle != null) {
            particleSpawner.spawnParticle(particle, false, false, false, x, y, z);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openBestiaryGui(ItemStack book) {
        Minecraft.getInstance().displayGuiScreen(new GuiBestiary(book));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openMyrmexStaffGui(ItemStack staff) {
        Minecraft.getInstance().displayGuiScreen(new GuiMyrmexStaff(staff));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void openMyrmexAddRoomGui(ItemStack staff, BlockPos pos, Direction facing) {
        Minecraft.getInstance().displayGuiScreen(new GuiMyrmexAddRoom(staff, pos, facing));
    }

    @OnlyIn(Dist.CLIENT)
    public Object getArmorModel(int armorId) {
        switch (armorId) {
            case 0:
                return FIRE_DRAGON_SCALE_ARMOR_MODEL;
            case 1:
                return FIRE_DRAGON_SCALE_ARMOR_MODEL_LEGS;
            case 2:
                return ICE_DRAGON_SCALE_ARMOR_MODEL;
            case 3:
                return ICE_DRAGON_SCALE_ARMOR_MODEL_LEGS;
            case 4:
                return DEATHWORM_ARMOR_MODEL;
            case 5:
                return DEATHWORM_ARMOR_MODEL_LEGS;
            case 6:
                return TROLL_ARMOR_MODEL;
            case 7:
                return TROLL_ARMOR_MODEL_LEGS;
            case 8:
                return TIDE_ARMOR_MODEL;
            case 9:
                return TIDE_ARMOR_MODEL_LEGS;
            case 10:
                return DRAGONSTEEL_FIRE_ARMOR_MODEL;
            case 11:
                return DRAGONSTEEL_FIRE_ARMOR_MODEL_LEGS;
            case 12:
                return DRAGONSTEEL_ICE_ARMOR_MODEL;
            case 13:
                return DRAGONSTEEL_ICE_ARMOR_MODEL_LEGS;
            case 14:
                return SILVER_ARMOR_MODEL;
            case 15:
                return SILVER_ARMOR_MODEL_LEGS;
        }
        return null;
    }

    public Object getFontRenderer() {
        return this.bestiaryFontRenderer;
    }

    public int getDragon3rdPersonView() {
        return thirdPersonViewDragon;
    }

    public void setDragon3rdPersonView(int view) {
        thirdPersonViewDragon = view;
    }

    public Object getDreadlandsRender(int i) {
        if(i == 0){
            return dreadlandsSkyRenderer;
        }
        if(i == 1){
            return dreadlandsWeatherRenderer;
        }
        if(i == 2){
            return dreadlandsAuroraRender;
        }
        return null;
    }

    public int getPreviousViewType() {
        return previousViewType;
    }

    public void setPreviousViewType(int view) {
        previousViewType = view;
    }

    public void updateDragonArmorRender(String clear){
        LayerDragonArmor.clearCache(clear);
    }

    public boolean shouldSeeBestiaryContents() {
        return InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), 340) || InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), 344);
    }
}
