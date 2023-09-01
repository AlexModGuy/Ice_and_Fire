package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.model.ModelMyrmexPupa;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerMyrmexItem;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public class RenderMyrmexBase extends MobRenderer<EntityMyrmexBase, AdvancedEntityModel<EntityMyrmexBase>> {

    private static final AdvancedEntityModel<EntityMyrmexBase> LARVA_MODEL = new ModelMyrmexPupa();
    private static final AdvancedEntityModel<EntityMyrmexBase> PUPA_MODEL = new ModelMyrmexPupa();
    private final AdvancedEntityModel<EntityMyrmexBase> adultModel;

    public RenderMyrmexBase(EntityRendererProvider.Context context, AdvancedEntityModel<EntityMyrmexBase> model, float shadowSize) {
        super(context, model, shadowSize);
        this.addLayer(new LayerMyrmexItem(this));
        this.adultModel = model;
    }

    @Override
    public void render(EntityMyrmexBase entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.getGrowthStage() == 0) {
            model = LARVA_MODEL;
        } else if (entityIn.getGrowthStage() == 1) {
            model = PUPA_MODEL;
        } else {
            model = adultModel;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

    }

    @Override
    protected void scale(EntityMyrmexBase myrmex, @NotNull PoseStack matrixStackIn, float partialTickTime) {
        float scale = myrmex.getModelScale();
        if (myrmex.getGrowthStage() == 0) {
            scale /= 2;
        }
        if (myrmex.getGrowthStage() == 1) {
            scale /= 1.5F;
        }
        matrixStackIn.scale(scale, scale, scale);
        if (myrmex.isPassenger() && myrmex.getGrowthStage() < 2) {
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityMyrmexBase myrmex) {
        return myrmex.getTexture();
    }

}
