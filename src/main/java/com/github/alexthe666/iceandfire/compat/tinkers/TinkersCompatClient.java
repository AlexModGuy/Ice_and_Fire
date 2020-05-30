package com.github.alexthe666.iceandfire.compat.tinkers;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.book.TinkerBook;
import slimeknights.tconstruct.library.client.MaterialRenderInfo;

public class TinkersCompatClient {

    public static void preInit() {
        TinkerBook.INSTANCE.addRepository(new FileRepository("iceandfire:tinkers/book"));
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
        MaterialRenderInfo weezerInfo = new MaterialRenderInfo.BlockTexture(new ResourceLocation("iceandfire:tinkers/weezer"));
        TinkersCompat.MATERIAL_WEEZER.setRenderInfo(weezerInfo);
        ModelRegisterUtil.registerModifierModel(TinkersCompat.BURN_I, new ResourceLocation("iceandfire:models/item/tinkers/flame"));
        ModelRegisterUtil.registerModifierModel(TinkersCompat.FREEZE_I, new ResourceLocation("iceandfire:models/item/tinkers/frost"));

    }

    public static void registerModels(ModelRegistryEvent event) {
        /*ModelResourceLocation liquidModel = new ModelResourceLocation(new ResourceLocation("iceandfire:tinkers_fluid"), "fluid");
        ModelLoader.setCustomStateMapper(TinkersCompat.MOLTEN_FIRE_DRAGONSTEEL.getBlock(), new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return liquidModel;
            }
        });
        ModelLoader.setCustomStateMapper(TinkersCompat.MOLTEN_ICE_DRAGONSTEEL.getBlock(), new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return liquidModel;
            }
        });*/
    }

}
