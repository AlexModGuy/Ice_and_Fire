package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelHydraBody;
import com.github.alexthe666.iceandfire.client.model.ModelHydraHead;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.client.render.entity.RenderHydra;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class LayerHydraHead implements LayerRenderer {
    private final RenderHydra renderer;
    private ModelHydraHead[] modelArr;
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
    public LayerHydraHead(RenderHydra renderer) {
        this.renderer = renderer;
        modelArr = new ModelHydraHead[EntityHydra.HEADS];
        for (int i = 0; i < modelArr.length; i++) {
            modelArr[i] = new ModelHydraHead(i);
        }
    }

    public void doRenderLayer(EntityHydra entity, float f, float f1, float i, float f2, float f3, float f4, float f5) {
        int heads = entity.getHeadCount();
        boolean stone = EntityGorgon.isStoneMob(entity);
        if(entity.isInvisible() && !stone){
            return;
        }
        GlStateManager.pushMatrix();
        translateToBody();
        for (int head = 1; head <= heads; head++) {
            GlStateManager.pushMatrix();
            float bodyWidth = 0.5F;
            GL11.glTranslatef(TRANSLATE[heads - 1][head - 1] * bodyWidth, 0, 0);
            GL11.glRotatef(ROTATE[heads - 1][head - 1], 0, 1, 0);
            if(stone){
                renderer.bindTexture(TEXTURE_STONE);
                f2 = 0;
            }else{
                renderer.bindTexture(getEntityTexture(entity));

            }
            modelArr[head - 1].render(entity, f, f1, f2, f3, f4, f5);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    protected ResourceLocation getEntityTexture(EntityHydra gorgon) {
        switch (gorgon.getVariant()) {
            default:
                return RenderHydra.TEXUTURE_0;
            case 1:
                return RenderHydra.TEXUTURE_1;
            case 2:
                return RenderHydra.TEXUTURE_2;
        }
    }

    protected void translateToBody() {
        postRender(((ModelHydraBody) this.renderer.getMainModel()).BodyUpper, 0.0625F);
    }

    protected void postRender(AdvancedModelRenderer renderer, float scale) {
        if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
            if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.rotationPointZ != 0.0F) {
                GlStateManager.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);
            }
        } else {
            GlStateManager.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);

            if (renderer.rotateAngleZ != 0.0F) {
                GlStateManager.rotate(renderer.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
            }

            if (renderer.rotateAngleY != 0.0F) {
                GlStateManager.rotate(renderer.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
            }

            if (renderer.rotateAngleX != 0.0F) {
                GlStateManager.rotate(renderer.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6) {
        this.doRenderLayer((EntityHydra) entity, f, f1, f2, f3, f4, f5, f6);
    }
}