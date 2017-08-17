package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.client.gui.bestiary.GuiBestiary;
import com.github.alexthe666.iceandfire.client.model.ModelFireDragon;
import com.github.alexthe666.iceandfire.client.model.ModelFireDragonArmor;
import com.github.alexthe666.iceandfire.client.model.ModelIceDragon;
import com.github.alexthe666.iceandfire.client.model.ModelIceDragonArmor;
import com.github.alexthe666.iceandfire.client.particle.ParticleBlood;
import com.github.alexthe666.iceandfire.client.particle.ParticleDragonFire;
import com.github.alexthe666.iceandfire.client.particle.ParticleDragonIce;
import com.github.alexthe666.iceandfire.client.particle.ParticleSnowflake;
import com.github.alexthe666.iceandfire.client.render.entity.*;
import com.github.alexthe666.iceandfire.client.render.tile.*;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModKeys;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.entity.tile.*;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.event.EventClient;
import com.github.alexthe666.iceandfire.event.EventNewMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    private FontRenderer bestiaryFontRenderer;

    @Override
    public void render() {
        this.bestiaryFontRenderer = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("iceandfire:textures/font/bestiary.png"), Minecraft.getMinecraft().renderEngine, false);
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this.bestiaryFontRenderer);
        ModKeys.init();
        MinecraftForge.EVENT_BUS.register(new RenderModCapes());
        MinecraftForge.EVENT_BUS.register(new EventClient());
        MinecraftForge.EVENT_BUS.register(new EventNewMenu());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDummyGorgonHead.class, new RenderGorgonHead(false));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDummyGorgonHeadActive.class, new RenderGorgonHead(true));
        ForgeHooksClient.registerTESRItemStack(ModItems.gorgon_head, 0, TileEntityDummyGorgonHead.class);
        ForgeHooksClient.registerTESRItemStack(ModItems.gorgon_head, 1, TileEntityDummyGorgonHeadActive.class);
        renderItems();
        renderEntities();
    }

    public void postRender(){
        EventClient.initializeStoneLayer();
    }

    private void renderEntities() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFireDragon.class, new RenderDragonBase(Minecraft.getMinecraft().getRenderManager(), new ModelFireDragon()));
        RenderingRegistry.registerEntityRenderingHandler(EntityIceDragon.class, new RenderDragonBase(Minecraft.getMinecraft().getRenderManager(), new ModelIceDragon()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonEgg.class, new RenderDragonEgg(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonArrow.class, new RenderDragonArrow(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonSkull.class, new RenderDragonSkull(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonFire.class, new RenderNothing(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonFireCharge.class, new RenderDragonFireCharge(Minecraft.getMinecraft().getRenderManager(), true));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonIceProjectile.class, new RenderNothing(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDragonIceCharge.class, new RenderDragonFireCharge(Minecraft.getMinecraft().getRenderManager(), false));
        RenderingRegistry.registerEntityRenderingHandler(EntitySnowVillager.class, new RenderSnowVillager(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityHippogryphEgg.class,  new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ModItems.hippogryph_egg, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityHippogryph.class, new RenderHippogryph(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityStoneStatue.class, new RenderStoneStatue(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityGorgon.class, new RenderGorgon(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityPixie.class, new RenderPixie(Minecraft.getMinecraft().getRenderManager()));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPodium.class, new RenderPodium());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLectern.class, new RenderLectern());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEggInIce.class, new RenderEggInIce());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPixieHouse.class, new RenderPixieHouse());
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 0, TileEntityPixieHouse.class);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 1, TileEntityPixieHouse.class);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 2, TileEntityPixieHouse.class);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 3, TileEntityPixieHouse.class);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 4, TileEntityPixieHouse.class);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.pixieHouse), 5, TileEntityPixieHouse.class);


    }

    public void renderItems() {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.lectern), 0, new ModelResourceLocation("iceandfire:lectern", "inventory"));
        ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocks.podium), new ResourceLocation("iceandfire:podium_oak"), new ResourceLocation("iceandfire:podium_spruce"), new ResourceLocation("iceandfire:podium_birch"), new ResourceLocation("iceandfire:podium_jungle"), new ResourceLocation("iceandfire:podium_acacia"), new ResourceLocation("iceandfire:podium_dark_oak"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.podium), 0, new ModelResourceLocation("iceandfire:podium_oak", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.podium), 1, new ModelResourceLocation("iceandfire:podium_spruce", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.podium), 2, new ModelResourceLocation("iceandfire:podium_birch", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.podium), 3, new ModelResourceLocation("iceandfire:podium_jungle", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.podium), 4, new ModelResourceLocation("iceandfire:podium_acacia", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.podium), 5, new ModelResourceLocation("iceandfire:podium_dark_oak", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.fire_lily), 0, new ModelResourceLocation("iceandfire:fire_lily", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.frost_lily), 0, new ModelResourceLocation("iceandfire:frost_lily", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.goldPile), 0, new ModelResourceLocation("iceandfire:goldpile", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.silverOre), 0, new ModelResourceLocation("iceandfire:silver_ore", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.sapphireOre), 0, new ModelResourceLocation("iceandfire:sapphire_ore", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.silverBlock), 0, new ModelResourceLocation("iceandfire:silver_block", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.sapphireBlock), 0, new ModelResourceLocation("iceandfire:sapphire_block", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.charedDirt), 0, new ModelResourceLocation("iceandfire:chared_dirt", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.charedGrass), 0, new ModelResourceLocation("iceandfire:chared_grass", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.charedStone), 0, new ModelResourceLocation("iceandfire:chared_stone", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.charedCobblestone), 0, new ModelResourceLocation("iceandfire:chared_cobblestone", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.charedGravel), 0, new ModelResourceLocation("iceandfire:chared_gravel", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.charedGrassPath), 0, new ModelResourceLocation("iceandfire:chared_grass_path", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.ash), 0, new ModelResourceLocation("iceandfire:ash", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.frozenDirt), 0, new ModelResourceLocation("iceandfire:frozen_dirt", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.frozenGrass), 0, new ModelResourceLocation("iceandfire:frozen_grass", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.frozenStone), 0, new ModelResourceLocation("iceandfire:frozen_stone", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.frozenCobblestone), 0, new ModelResourceLocation("iceandfire:frozen_cobblestone", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.frozenGravel), 0, new ModelResourceLocation("iceandfire:frozen_gravel", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.frozenGrassPath), 0, new ModelResourceLocation("iceandfire:frozen_grass_path", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.dragon_ice), 0, new ModelResourceLocation("iceandfire:dragon_ice", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.dragon_ice_spikes), 0, new ModelResourceLocation("iceandfire:dragon_ice_spikes", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.eggInIce), 0, new ModelResourceLocation("iceandfire:egginice", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.silverPile), 0, new ModelResourceLocation("iceandfire:silverpile", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.bestiary, 0, new ModelResourceLocation("iceandfire:bestiary", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.manuscript, 0, new ModelResourceLocation("iceandfire:manuscript", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.sapphireGem, 0, new ModelResourceLocation("iceandfire:sapphire_gem", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silverIngot, 0, new ModelResourceLocation("iceandfire:silver_ingot", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silverNugget, 0, new ModelResourceLocation("iceandfire:silver_nugget", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silver_helmet, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_helmet", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silver_chestplate, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_chestplate", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silver_leggings, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_leggings", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silver_boots, 0, new ModelResourceLocation("iceandfire:armor_silver_metal_boots", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silver_sword, 0, new ModelResourceLocation("iceandfire:silver_sword", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silver_shovel, 0, new ModelResourceLocation("iceandfire:silver_shovel", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silver_pickaxe, 0, new ModelResourceLocation("iceandfire:silver_pickaxe", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silver_axe, 0, new ModelResourceLocation("iceandfire:silver_axe", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.silver_hoe, 0, new ModelResourceLocation("iceandfire:silver_hoe", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.frost_stew, 0, new ModelResourceLocation("iceandfire:frost_stew", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.fire_stew, 0, new ModelResourceLocation("iceandfire:fire_stew", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonegg_red, 0, new ModelResourceLocation("iceandfire:dragonegg_red", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonegg_green, 0, new ModelResourceLocation("iceandfire:dragonegg_green", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonegg_bronze, 0, new ModelResourceLocation("iceandfire:dragonegg_bronze", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonegg_gray, 0, new ModelResourceLocation("iceandfire:dragonegg_gray", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonegg_blue, 0, new ModelResourceLocation("iceandfire:dragonegg_blue", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonegg_white, 0, new ModelResourceLocation("iceandfire:dragonegg_white", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonegg_sapphire, 0, new ModelResourceLocation("iceandfire:dragonegg_sapphire", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonegg_silver, 0, new ModelResourceLocation("iceandfire:dragonegg_silver", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonscales_red, 0, new ModelResourceLocation("iceandfire:dragonscales_red", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonscales_green, 0, new ModelResourceLocation("iceandfire:dragonscales_green", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonscales_bronze, 0, new ModelResourceLocation("iceandfire:dragonscales_bronze", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonscales_gray, 0, new ModelResourceLocation("iceandfire:dragonscales_gray", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonscales_blue, 0, new ModelResourceLocation("iceandfire:dragonscales_blue", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonscales_white, 0, new ModelResourceLocation("iceandfire:dragonscales_white", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonscales_sapphire, 0, new ModelResourceLocation("iceandfire:dragonscales_sapphire", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonscales_silver, 0, new ModelResourceLocation("iceandfire:dragonscales_silver", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone, 0, new ModelResourceLocation("iceandfire:dragonbone", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.witherbone, 0, new ModelResourceLocation("iceandfire:witherbone", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.fishing_spear, 0, new ModelResourceLocation("iceandfire:fishing_spear", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.wither_shard, 0, new ModelResourceLocation("iceandfire:wither_shard", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_sword, 0, new ModelResourceLocation("iceandfire:dragonbone_sword", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_shovel, 0, new ModelResourceLocation("iceandfire:dragonbone_shovel", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_pickaxe, 0, new ModelResourceLocation("iceandfire:dragonbone_pickaxe", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_axe, 0, new ModelResourceLocation("iceandfire:dragonbone_axe", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_hoe, 0, new ModelResourceLocation("iceandfire:dragonbone_hoe", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_sword_fire, 0, new ModelResourceLocation("iceandfire:dragonbone_sword_fire", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_sword_ice, 0, new ModelResourceLocation("iceandfire:dragonbone_sword_ice", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_arrow, 0, new ModelResourceLocation("iceandfire:dragonbone_arrow", "inventory"));
        ModelBakery.registerItemVariants(ModItems.dragon_skull, new ResourceLocation("iceandfire:dragonbone_bow"), new ResourceLocation("iceandfire:dragonbone_bow_pulling_0"), new ResourceLocation("iceandfire:dragonbone_bow_pulling_1"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_bow, 0, new ModelResourceLocation("iceandfire:dragonbone_bow", "inventory"));
        ModelBakery.registerItemVariants(ModItems.dragon_skull, new ResourceLocation("iceandfire:dragon_skull_fire"), new ResourceLocation("iceandfire:dragon_skull_ice"));
        renderItem.getItemModelMesher().register(ModItems.dragon_skull, 0, new ModelResourceLocation("iceandfire:dragon_skull_fire", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_skull, 1, new ModelResourceLocation("iceandfire:dragon_skull_ice", "inventory"));
        ModelBakery.registerItemVariants(ModItems.dragon_armor_iron, new ResourceLocation("iceandfire:dragonarmor_iron_head"), new ResourceLocation("iceandfire:dragonarmor_iron_neck"), new ResourceLocation("iceandfire:dragonarmor_iron_body"), new ResourceLocation("iceandfire:dragonarmor_iron_tail"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_iron, 0, new ModelResourceLocation("iceandfire:dragonarmor_iron_head", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_iron, 1, new ModelResourceLocation("iceandfire:dragonarmor_iron_neck", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_iron, 2, new ModelResourceLocation("iceandfire:dragonarmor_iron_body", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_iron, 3, new ModelResourceLocation("iceandfire:dragonarmor_iron_tail", "inventory"));
        ModelBakery.registerItemVariants(ModItems.dragon_armor_gold, new ResourceLocation("iceandfire:dragonarmor_gold_head"), new ResourceLocation("iceandfire:dragonarmor_gold_neck"), new ResourceLocation("iceandfire:dragonarmor_gold_body"), new ResourceLocation("iceandfire:dragonarmor_gold_tail"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_gold, 0, new ModelResourceLocation("iceandfire:dragonarmor_gold_head", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_gold, 1, new ModelResourceLocation("iceandfire:dragonarmor_gold_neck", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_gold, 2, new ModelResourceLocation("iceandfire:dragonarmor_gold_body", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_gold, 3, new ModelResourceLocation("iceandfire:dragonarmor_gold_tail", "inventory"));
        ModelBakery.registerItemVariants(ModItems.dragon_armor_diamond, new ResourceLocation("iceandfire:dragonarmor_diamond_head"), new ResourceLocation("iceandfire:dragonarmor_diamond_neck"), new ResourceLocation("iceandfire:dragonarmor_diamond_body"), new ResourceLocation("iceandfire:dragonarmor_diamond_tail"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_diamond, 0, new ModelResourceLocation("iceandfire:dragonarmor_diamond_head", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_diamond, 1, new ModelResourceLocation("iceandfire:dragonarmor_diamond_neck", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_diamond, 2, new ModelResourceLocation("iceandfire:dragonarmor_diamond_body", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_armor_diamond, 3, new ModelResourceLocation("iceandfire:dragonarmor_diamond_tail", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_meal, 0, new ModelResourceLocation("iceandfire:dragon_meal", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.fire_dragon_flesh, 0, new ModelResourceLocation("iceandfire:fire_dragon_flesh", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.ice_dragon_flesh, 0, new ModelResourceLocation("iceandfire:ice_dragon_flesh", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.fire_dragon_heart, 0, new ModelResourceLocation("iceandfire:fire_dragon_heart", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.ice_dragon_heart, 0, new ModelResourceLocation("iceandfire:ice_dragon_heart", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.fire_dragon_blood, 0, new ModelResourceLocation("iceandfire:fire_dragon_blood", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.ice_dragon_blood, 0, new ModelResourceLocation("iceandfire:ice_dragon_blood", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_stick, 0, new ModelResourceLocation("iceandfire:dragon_stick", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_horn, 0, new ModelResourceLocation("iceandfire:dragon_horn", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragonbone_bow, 0, new ModelResourceLocation("iceandfire:dragonbone_bow", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_horn_fire, 0, new ModelResourceLocation("iceandfire:dragon_horn_fire", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_horn_ice, 0, new ModelResourceLocation("iceandfire:dragon_horn_ice", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.dragon_flute, 0, new ModelResourceLocation("iceandfire:dragon_flute", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.hippogryph_egg, 0, new ModelResourceLocation("iceandfire:hippogryph_egg", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.iron_hippogryph_armor, 0, new ModelResourceLocation("iceandfire:iron_hippogryph_armor", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.gold_hippogryph_armor, 0, new ModelResourceLocation("iceandfire:gold_hippogryph_armor", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.diamond_hippogryph_armor, 0, new ModelResourceLocation("iceandfire:diamond_hippogryph_armor", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.gorgon_head, 0, new ModelResourceLocation("iceandfire:gorgon_head", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.gorgon_head, 1, new ModelResourceLocation("iceandfire:gorgon_head", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.stone_statue, 0, new ModelResourceLocation("iceandfire:stone_statue", "inventory"));
        renderItem.getItemModelMesher().register(ModItems.blindfold, 0, new ModelResourceLocation("iceandfire:blindfold", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.pixieHouse), 0, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.pixieHouse), 1, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.pixieHouse), 2, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.pixieHouse), 3, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.pixieHouse), 4, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(ModBlocks.pixieHouse), 5, new ModelResourceLocation("iceandfire:pixie_house", "inventory"));


    }

    @Override
    public void renderArmors(EnumDragonArmor armor) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(armor.helmet, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_helmet", "inventory"));
        renderItem.getItemModelMesher().register(armor.chestplate, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_chestplate", "inventory"));
        renderItem.getItemModelMesher().register(armor.leggings, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_leggings", "inventory"));
        renderItem.getItemModelMesher().register(armor.boots, 0, new ModelResourceLocation("iceandfire:" + armor.name() + "_boots", "inventory"));
    }

    public void spawnParticle(String name, World world, double x, double y, double z, double motX, double motY, double motZ) {
        Particle particle = null;
        if (name.equals("dragonfire")) {
            particle = new ParticleDragonFire(world, x, y, z, motX, motY, motZ);
        }
        if (name.equals("dragonice")) {
            particle = new ParticleDragonIce(world, x, y, z, motX, motY, motZ);
        }
        if (name.equals("snowflake")) {
            particle = new ParticleSnowflake(world, x, y, z, motX, motY, motZ);
        }
        if (name.equals("blood")) {
            particle = new ParticleBlood(world, x, y, z);
        }
        if (particle != null) {
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        }
    }

    @Override
    public void openBestiaryGui(ItemStack book) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiBestiary(book));
    }

    public Object getArmorModel(int armorId) {
        switch (armorId) {
            case 0:
                return new ModelFireDragonArmor(0.5F);
            case 1:
                return new ModelFireDragonArmor(0.2F);
            case 2:
                return new ModelIceDragonArmor(0.5F);
            case 3:
                return new ModelIceDragonArmor(0.2F);
        }
        return null;
    }

    public Object getFontRenderer() {
        return this.bestiaryFontRenderer;
    }
}
