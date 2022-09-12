package com.github.alexthe666.iceandfire.client.model.util;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

// The AdvancedModelRenderer/ModelBox uses a child-parent structure
// Meaning that if you change a parents showModel field to false all the children also
// don't get rendered. This is a workaround for that

public class HideableModelRenderer extends AdvancedModelBox {

    public boolean invisible;

    public HideableModelRenderer(AdvancedEntityModel model, String name) {
        super(model, name);
    }

    public HideableModelRenderer(AdvancedEntityModel model, int i, int i1) {
        super(model, i, i1);
    }

    @Override
    public void render(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (invisible) {
            invisibleRender(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        } else {
            super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        }

    }

    public void copyFrom(BasicModelPart currentModel) {
        this.copyModelAngles(currentModel);
        this.rotationPointX = currentModel.rotationPointX;
        this.rotationPointY = currentModel.rotationPointY;
        this.rotationPointZ = currentModel.rotationPointZ;
    }

    public void invisibleRender(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.showModel && (!this.cubeList.isEmpty() || !this.childModels.isEmpty())) {
            matrixStackIn.pushPose();
            this.translateAndRotate(matrixStackIn);
            if (!this.scaleChildren) {
                matrixStackIn.scale(1.0F / Math.max(this.scaleX, 1.0E-4F), 1.0F / Math.max(this.scaleY, 1.0E-4F), 1.0F / Math.max(this.scaleZ, 1.0E-4F));
            }
            for (BasicModelPart renderer : this.childModels) {
                renderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            }

            matrixStackIn.popPose();
        }
    }
}
