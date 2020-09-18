package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.iceandfire.client.model.ModelHydraBody;
import com.github.alexthe666.iceandfire.client.model.ModelHydraHead;
import com.github.alexthe666.iceandfire.client.render.entity.RenderHydra;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerHydraHead extends LayerRenderer<EntityHydra, ModelHydraBody> {
    public static final ResourceLocation TEXTURE_STONE = new ResourceLocation("iceandfire:textures/models/hydra/stone.png");
    private static final float[][] TRANSLATE = new float[][]{
            {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
            {-0.15F, 0.15F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
            {-0.3F, 0F, 0.3F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
            {-0.4F, -0.1F, 0.1F, 0.4F, 0F, 0F, 0F, 0F, 0F},//etc...
            {-0.5F, -0.2F, 0F, 0.2F, 0.5F, 0F, 0F, 0F, 0F},
            {-0.7F, -0.4F, -0.2F, 0.2F, 0.4F, 0.7F, 0F, 0F, 0F},
            {-0.7F, -0.4F, -0.2F, 0, 0.2F, 0.4F, 0.7F, 0F, 0F},
            {-0.6F, -0.4F, -0.2F, -0.1F, 0.1F, 0.2F, 0.4F, 0.6F, 0F},
            {-0.6F, -0.4F, -0.2F, -0.1F, 0.0F, 0.1F, 0.2F, 0.4F, 0.6F},
    };
    private static final float[][] ROTATE = new float[][]{
            {0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 1 total heads
            {10F, -10F, 0F, 0F, 0F, 0F, 0F, 0F, 0F},// 2 total heads
            {10F, 0F, -10F, 0F, 0F, 0F, 0F, 0F, 0F},// 3 total heads
            {25F, 10F, -10F, -25F, 0F, 0F, 0F, 0F, 0F},//etc...
            {30F, 15F, 0F, -15F, -30F, 0F, 0F, 0F, 0F},
            {40F, 25F, 5F, -5F, -25F, -40F, 0F, 0F, 0F},
            {40F, 30F, 15F, 0F, -15F, -30F, -40F, 0F, 0F},
            {45F, 30F, 20F, 5F, -5F, -20F, -30F, -45F, 0F},
            {50F, 37F, 25F, 15F, 0, -15F, -25F, -37F, -50F},
    };
    private final RenderHydra renderer;
    private ModelHydraHead[] modelArr;

    public LayerHydraHead(RenderHydra renderer) {
        super(renderer);
        this.renderer = renderer;
        modelArr = new ModelHydraHead[EntityHydra.HEADS];
        for (int i = 0; i < modelArr.length; i++) {
            modelArr[i] = new ModelHydraHead(i);
        }
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityHydra entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        int heads = entity.getHeadCount();
        boolean stone = EntityGorgon.isStoneMob(entity);
        if (entity.isInvisible() && !stone) {
            return;
        }
        matrixStackIn.push();
        translateToBody(matrixStackIn);
        for (int head = 1; head <= heads; head++) {
            matrixStackIn.push();
            float bodyWidth = 0.5F;
            matrixStackIn.translate(TRANSLATE[heads - 1][head - 1] * bodyWidth, 0, 0);
            matrixStackIn.rotate(new Quaternion(Vector3f.YP, ROTATE[heads - 1][head - 1], true));
            ResourceLocation tex = stone ? TEXTURE_STONE : getEntityTexture(entity);
            modelArr[head - 1].setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            modelArr[head - 1].render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityCutout(tex)), packedLightIn, LivingRenderer.getPackedOverlay(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
        matrixStackIn.pop();
    }

    public ResourceLocation getEntityTexture(EntityHydra gorgon) {
        switch (gorgon.getVariant()) {
            default:
                return RenderHydra.TEXUTURE_0;
            case 1:
                return RenderHydra.TEXUTURE_1;
            case 2:
                return RenderHydra.TEXUTURE_2;
        }
    }

    protected void translateToBody(MatrixStack stack) {
        postRender(this.renderer.getEntityModel().BodyUpper, stack, 0.0625F);
    }

    protected void postRender(AdvancedModelBox renderer, MatrixStack matrixStackIn, float scale) {
        if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
            if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.rotationPointZ != 0.0F) {
                matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);
            }
        } else {
            matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);
            if (renderer.rotateAngleZ != 0.0F) {
                matrixStackIn.rotate(Vector3f.ZP.rotation(renderer.rotateAngleZ));
            }

            if (renderer.rotateAngleY != 0.0F) {
                matrixStackIn.rotate(Vector3f.YP.rotation(renderer.rotateAngleY));
            }

            if (renderer.rotateAngleX != 0.0F) {
                matrixStackIn.rotate(Vector3f.XP.rotation(renderer.rotateAngleX));
            }
        }
    }
}