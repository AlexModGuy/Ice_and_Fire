package com.github.alexthe666.iceandfire.compat.tinkers;

import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;

public class TinkersCompatClient {

    public static void preInit() {
        MaterialRenderInfo boneInfo = new MaterialRenderInfo.Default(0XB2AD98);
        boneInfo.setTextureSuffix("bone_base");
        TinkersCompat.MATERIAL_DRAGONBONE.setRenderInfo(boneInfo);
        MaterialRenderInfo desertChitinInfo = new MaterialRenderInfo.BlockTexture(new ResourceLocation("iceandfire:tinkers/desert_myrmex"));
        TinkersCompat.MATERIAL_DESERT_MYRMEX.setRenderInfo(desertChitinInfo);
        MaterialRenderInfo jungleChitinInfo = new MaterialRenderInfo.BlockTexture(new ResourceLocation("iceandfire:tinkers/jungle_myrmex"));
        TinkersCompat.MATERIAL_JUNGLE_MYRMEX.setRenderInfo(jungleChitinInfo);
        MaterialRenderInfo dragonsteelFireInfo = new MaterialRenderInfo.BlockTexture(new ResourceLocation("iceandfire:tinkers/dragonsteel_fire"));
        TinkersCompat.MATERIAL_DRAGONSTEEL_FIRE.setRenderInfo(dragonsteelFireInfo);
        MaterialRenderInfo dragonsteelIceInfo = new MaterialRenderInfo.BlockTexture(new ResourceLocation("iceandfire:tinkers/dragonsteel_ice"));
        TinkersCompat.MATERIAL_DRAGONSTEEL_ICE.setRenderInfo(dragonsteelIceInfo);
    }
}
