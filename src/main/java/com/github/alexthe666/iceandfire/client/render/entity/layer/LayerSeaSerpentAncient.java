package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.MobRendererBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;

public class LayerSeaSerpentAncient extends LayerRenderer<EntitySeaSerpent, SegmentedModel<EntitySeaSerpent>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/seaserpent/ancient_overlay.png");
    private static final ResourceLocation TEXTURE_BLINK = new ResourceLocation("iceandfire:textures/models/seaserpent/ancient_overlay_blink.png");
    private MobRenderer<EntitySeaSerpent, SegmentedModel<EntitySeaSerpent>> renderer;

    public LayerSeaSerpentAncient(MobRenderer<EntitySeaSerpent, SegmentedModel<EntitySeaSerpent>> renderer) {
        super(renderer);
        this.renderer = renderer;
    }

    @Override
    public void render(EntitySeaSerpent serpent, float f, float f1, float i, float f2, float f3, float f4, float f5) {
        if (serpent.isAncient()) {
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            if (serpent.isBlinking()) {
                this.renderer.bindTexture(TEXTURE_BLINK);
            } else {
                this.renderer.bindTexture(TEXTURE);
            }
            this.renderer.getMainModel().render(serpent, f, f1, f2, f3, f4, f5);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
