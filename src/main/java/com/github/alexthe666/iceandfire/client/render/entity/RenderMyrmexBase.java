package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerMyrmexItem;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderMyrmexBase extends MobRenderer<EntityMyrmexBase, SegmentedModel<EntityMyrmexBase>> {

    public RenderMyrmexBase(EntityRendererManager renderManager, SegmentedModel model, float shadowSize) {
        super(renderManager, model, shadowSize);
        this.addLayer(new LayerMyrmexItem(this));
    }

    @Override
    protected void preRenderCallback(EntityMyrmexBase myrmex, MatrixStack matrixStackIn, float partialTickTime) {
        float scale = myrmex.getModelScale();
        if (myrmex.getGrowthStage() == 0) {
            scale /= 2;
        }
        if (myrmex.getGrowthStage() == 1) {
            scale /= 1.5F;
        }
        matrixStackIn.scale(scale, scale, scale);
        if (myrmex.isPassenger() && myrmex.getGrowthStage() < 2) {
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, 90, true));
        }
    }

    @Override
    public ResourceLocation getEntityTexture(EntityMyrmexBase myrmex) {
        return myrmex.getTexture();
    }

}
