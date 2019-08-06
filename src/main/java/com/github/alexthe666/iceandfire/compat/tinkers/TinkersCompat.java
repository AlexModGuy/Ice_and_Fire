package com.github.alexthe666.iceandfire.compat.tinkers;

import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.tools.TinkerMaterials;
import slimeknights.tconstruct.tools.traits.TraitBonusDamage;
import slimeknights.tconstruct.tools.traits.TraitSplintering;
import slimeknights.tconstruct.tools.traits.TraitSplinters;

import static slimeknights.tconstruct.library.materials.MaterialTypes.HEAD;
import static slimeknights.tconstruct.library.materials.MaterialTypes.SHAFT;
import static slimeknights.tconstruct.tools.TinkerTraits.*;

public class TinkersCompat {
    private static final TinkersCompat INSTANCE = new TinkersCompat();
    public static final Material MATERIAL_DRAGONBONE = new Material("dragonbone", 0XB7B29D);
    public static final Material MATERIAL_DESERT_MYRMEX = new Material("desert_myrmex", 0X783B0C);
    public static final Material MATERIAL_JUNGLE_MYRMEX = new Material("jungle_myrmex", 0X267A72);
    public static final Material MATERIAL_DRAGONSTEEL_FIRE = new Material("dragonsteel_fire", 0XCCBBC4);
    public static final Material MATERIAL_DRAGONSTEEL_ICE = new Material("dragonsteel_ice", 0XBBE4FD);
    public static final AbstractTrait SPLINTERING_II = new TraitSplinteringII();
    public static final AbstractTrait SPLINTERS_II = new TraitSplintersII();
    public static final AbstractTrait FRACTURED_II = new TraitBonusDamage("fractured2", 3f);
    public static final AbstractTrait HIVE_DEFENDER = new TraitHiveDefender();
    public static final AbstractTrait FREEZE_II = new TraitFreeze(2);
    public static final AbstractTrait BURN_II = new TraitBurn(2);
    private static boolean registered = false;

    public static void register() {
        if (!registered) {
            registered = true;
            MinecraftForge.EVENT_BUS.register(INSTANCE);
            init();
        } else {
            throw new RuntimeException("You can only call TinkersCompat.register() once");
        }
    }

    private static void init() {
        TinkerMaterials.materials.add(MATERIAL_DRAGONBONE);
        TinkerRegistry.integrate(MATERIAL_DRAGONBONE).preInit();
        MATERIAL_DRAGONBONE.addItem(ModItems.dragonbone, 1, Material.VALUE_Ingot);
        MATERIAL_DRAGONBONE.setRepresentativeItem(ModItems.dragonbone);
        MATERIAL_DRAGONBONE.setCraftable(true);
        MATERIAL_DRAGONBONE.setCastable(false);
        TinkerRegistry.addMaterialStats(MATERIAL_DRAGONBONE,
                new HeadMaterialStats(300, 3.00f, 6.00f, HarvestLevels.OBSIDIAN),
                new HandleMaterialStats(1.3F, 130),
                new ExtraMaterialStats(200));
        TinkerRegistry.addMaterialStats(MATERIAL_DRAGONBONE, new BowMaterialStats(0.6f, 1.2f, 4F));
        MATERIAL_DRAGONBONE.addTrait(SPLINTERING_II, HEAD);
        MATERIAL_DRAGONBONE.addTrait(SPLINTERS_II, SHAFT);
        MATERIAL_DRAGONBONE.addTrait(FRACTURED_II);

        TinkerMaterials.materials.add(MATERIAL_DESERT_MYRMEX);
        TinkerRegistry.integrate(MATERIAL_DESERT_MYRMEX).preInit();
        MATERIAL_DESERT_MYRMEX.addItem(ModItems.myrmex_desert_chitin, 1, Material.VALUE_Ingot);
        MATERIAL_DESERT_MYRMEX.setRepresentativeItem(ModItems.myrmex_desert_chitin);
        MATERIAL_DESERT_MYRMEX.setCraftable(true);
        MATERIAL_DESERT_MYRMEX.setCastable(false);
        TinkerRegistry.addMaterialStats(MATERIAL_DESERT_MYRMEX,
                new HeadMaterialStats(400, 4.00f, 5.50f, HarvestLevels.DIAMOND),
                new HandleMaterialStats(0.7F, 300),
                new ExtraMaterialStats(150));
        TinkerRegistry.addMaterialStats(MATERIAL_DESERT_MYRMEX, new BowMaterialStats(0.5f, 0.7F, 2F));
        MATERIAL_DESERT_MYRMEX.addTrait(HIVE_DEFENDER, HEAD);
        MATERIAL_DESERT_MYRMEX.addTrait(poisonous);

        TinkerMaterials.materials.add(MATERIAL_JUNGLE_MYRMEX);
        TinkerRegistry.integrate(MATERIAL_JUNGLE_MYRMEX).preInit();
        MATERIAL_JUNGLE_MYRMEX.addItem(ModItems.myrmex_jungle_chitin, 1, Material.VALUE_Ingot);
        MATERIAL_JUNGLE_MYRMEX.setRepresentativeItem(ModItems.myrmex_jungle_chitin);
        TinkerRegistry.addMaterialStats(MATERIAL_JUNGLE_MYRMEX,
                new HeadMaterialStats(400, 4.00f, 5.50f, HarvestLevels.DIAMOND),
                new HandleMaterialStats(0.7F, 300),
                new ExtraMaterialStats(150));
        TinkerRegistry.addMaterialStats(MATERIAL_JUNGLE_MYRMEX, new BowMaterialStats(0.5f, 0.7F, 2F));
        MATERIAL_JUNGLE_MYRMEX.addTrait(HIVE_DEFENDER, HEAD);
        MATERIAL_JUNGLE_MYRMEX.addTrait(poisonous);

        TinkerMaterials.materials.add(MATERIAL_DRAGONSTEEL_FIRE);
        TinkerRegistry.integrate(MATERIAL_DRAGONSTEEL_FIRE).preInit();
        MATERIAL_DRAGONSTEEL_FIRE.addItem(ModItems.dragonsteel_fire_ingot, 1, Material.VALUE_Ingot);
        MATERIAL_DRAGONSTEEL_FIRE.setRepresentativeItem(ModItems.dragonsteel_fire_ingot);
        TinkerRegistry.addMaterialStats(MATERIAL_DRAGONSTEEL_FIRE,
                new HeadMaterialStats(1500, 7.00f, 10.0f, HarvestLevels.COBALT),
                new HandleMaterialStats(0.4F, 400),
                new ExtraMaterialStats(510));
        TinkerRegistry.addMaterialStats(MATERIAL_DRAGONSTEEL_FIRE, new BowMaterialStats(0.9f, 3.0F, 6F));
        MATERIAL_DRAGONSTEEL_FIRE.addTrait(BURN_II, HEAD);
        MATERIAL_DRAGONSTEEL_FIRE.addTrait(sharp);

        TinkerMaterials.materials.add(MATERIAL_DRAGONSTEEL_ICE);
        TinkerRegistry.integrate(MATERIAL_DRAGONSTEEL_ICE).preInit();
        MATERIAL_DRAGONSTEEL_ICE.addItem(ModItems.dragonsteel_ice_ingot, 1, Material.VALUE_Ingot);
        MATERIAL_DRAGONSTEEL_ICE.setRepresentativeItem(ModItems.dragonsteel_ice_ingot);
        MATERIAL_DRAGONSTEEL_ICE.setCraftable(false);
        MATERIAL_DRAGONSTEEL_ICE.setCastable(true);
        TinkerRegistry.addMaterialStats(MATERIAL_DRAGONSTEEL_ICE,
                new HeadMaterialStats(1500, 7.00f, 10.0f, HarvestLevels.COBALT),
                new HandleMaterialStats(0.4F, 400),
                new ExtraMaterialStats(510));
        TinkerRegistry.addMaterialStats(MATERIAL_DRAGONSTEEL_ICE, new BowMaterialStats(0.9f, 3.0F, 6F));
        MATERIAL_DRAGONSTEEL_ICE.addTrait(FREEZE_II, HEAD);
        MATERIAL_DRAGONSTEEL_ICE.addTrait(sharp);
    }

    public static void post() {
    }
}
