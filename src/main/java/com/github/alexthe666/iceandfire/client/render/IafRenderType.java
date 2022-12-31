package com.github.alexthe666.iceandfire.client.render;

import com.github.alexthe666.iceandfire.client.IafClientSetup;
import com.github.alexthe666.iceandfire.client.render.tile.RenderDreadPortal;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class IafRenderType extends RenderType {

    private static final ResourceLocation STONE_TEXTURE = new ResourceLocation("textures/block/stone.png");
    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_DREAD_PORTAL_SHADER = new RenderStateShard.ShaderStateShard(IafClientSetup::getRendertypeDreadPortalShader);
    private static final RenderType DREADLANDS_PORTAL = create("dreadlands_portal", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_DREAD_PORTAL_SHADER).setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(RenderDreadPortal.DREAD_PORTAL_BACKGROUND, false, false).add(RenderDreadPortal.DREAD_PORTAL, false, false).build()).createCompositeState(false));


    protected static final RenderStateShard.TransparencyStateShard GHOST_TRANSPARANCY = new RenderStateShard.TransparencyStateShard("translucent_ghost_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public IafRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType getGhost(ResourceLocation locationIn) {
        TextureStateShard lvt_1_1_ = new TextureStateShard(locationIn, false, false);
        return create("ghost_iaf", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER).setTextureState(lvt_1_1_).setTransparencyState(GHOST_TRANSPARANCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true));
    }

    public static RenderType getGhostDaytime(ResourceLocation locationIn) {
        TextureStateShard lvt_1_1_ = new TextureStateShard(locationIn, false, false);
        return create("ghost_iaf_day", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER).setTextureState(lvt_1_1_).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true));
    }

    public static RenderType getDreadlandsPortal() {
        return DREADLANDS_PORTAL;
    }

    public static RenderType getStoneMobRenderType(float x, float y) {
        RenderStateShard.TextureStateShard textureState = new TextureStateShard(STONE_TEXTURE, false, false);
        RenderType.CompositeState rendertype = RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_ENTITY_CUTOUT_SHADER).setTextureState(textureState).setTransparencyState(NO_TRANSPARENCY).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true);
        return create("stone_entity_type", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, rendertype);
    }

    public static RenderType getIce(ResourceLocation locationIn) {
        TextureStateShard lvt_1_1_ = new TextureStateShard(locationIn, false, false);
        return create("ice_texture", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_BEACON_BEAM_SHADER).setTextureState(lvt_1_1_).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true));
    }

    public static RenderType getStoneCrackRenderType(ResourceLocation crackTex) {
        RenderStateShard.TextureStateShard textureState = new TextureStateShard(crackTex, false, false);
        RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder().setTextureState(textureState).setShaderState(RenderType.RENDERTYPE_ENTITY_CUTOUT_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(EQUAL_DEPTH_TEST).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(false);
        return create("stone_entity_type_crack", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, rendertype$state);
    }


}
