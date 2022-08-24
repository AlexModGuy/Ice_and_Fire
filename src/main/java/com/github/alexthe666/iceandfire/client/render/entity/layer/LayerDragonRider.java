package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;


public class LayerDragonRider extends LayerRenderer<EntityDragonBase, SegmentedModel<EntityDragonBase>> {
    private final MobRenderer render;
    private final boolean excludeDreadQueenMob;

    public LayerDragonRider(MobRenderer renderIn, boolean excludeDreadQueenMob) {
        super(renderIn);
        this.render = renderIn;
        this.excludeDreadQueenMob = excludeDreadQueenMob;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.pushPose();
        if (!dragon.getPassengers().isEmpty()) {
            float dragonScale = dragon.getRenderSize() / 3;
            for (Entity passenger : dragon.getPassengers()) {
                boolean prey = dragon.getControllingPassenger() == null || dragon.getControllingPassenger().getId() != passenger.getId();
                if (excludeDreadQueenMob && passenger instanceof EntityDreadQueen) {
                    prey = false;
                }
                ClientProxy.currentDragonRiders.remove(passenger.getUUID());
                float riderRot = passenger.yRotO + (passenger.yRot - passenger.yRotO) * partialTicks;
                int animationTicks = 0;
                if (dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
                    animationTicks = dragon.getAnimationTick();
                }
                if (animationTicks == 0 || animationTicks >= 15) {
                    translateToBody(matrixStackIn);
                }
                if (prey) {
                    if (animationTicks == 0 || animationTicks >= 15 || dragon.isFlying()) {
                        translateToHead(matrixStackIn);
                        offsetPerDragonType(dragon.dragonType, matrixStackIn);
                        EntityRenderer render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(passenger);
                        EntityModel modelBase = null;
                        if (render instanceof MobRenderer) {
                            modelBase = ((MobRenderer) render).getModel();
                        }
                        if ((passenger.getBbHeight() > passenger.getBbWidth() || modelBase instanceof BipedModel) && !(modelBase instanceof QuadrupedModel) && !(modelBase instanceof HorseModel)) {
                            matrixStackIn.translate(-0.15F * passenger.getBbHeight(), 0.1F * dragonScale - 0.1F * passenger.getBbHeight(), -0.1F * dragonScale - 0.1F * passenger.getBbWidth());
                            matrixStackIn.mulPose(new Quaternion(Vector3f.ZP, 90, true));
                            matrixStackIn.mulPose(new Quaternion(Vector3f.YP, 45, true));
                        } else {
                            boolean horse = modelBase instanceof HorseModel;
                            matrixStackIn.translate((horse ? -0.08F : -0.15F) * passenger.getBbWidth(), 0.1F * dragonScale - 0.15F * passenger.getBbWidth(), -0.1F * dragonScale - 0.1F * passenger.getBbWidth());
                            matrixStackIn.mulPose(new Quaternion(Vector3f.XN, 90, true));
                        }
                    } else {
                        matrixStackIn.translate(0, 0.555F * dragonScale, -0.5F * dragonScale);
                    }

                } else {
                    matrixStackIn.translate(0, -0.01F * dragonScale, -0.035F * dragonScale);
                }
                matrixStackIn.pushPose();
                matrixStackIn.mulPose(new Quaternion(Vector3f.ZP, 180, true));
                matrixStackIn.mulPose(new Quaternion(Vector3f.YP, riderRot + 180, true));
                matrixStackIn.scale(1 / dragonScale, 1 / dragonScale, 1 / dragonScale);
                matrixStackIn.translate(0, -0.25F, 0);
                renderEntity(passenger, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, packedLightIn);
                matrixStackIn.popPose();
                ClientProxy.currentDragonRiders.add(passenger.getUUID());
            }
        }
        matrixStackIn.popPose();
    }

    protected void translateToBody(MatrixStack stack) {
        postRender(((TabulaModel) this.render.getModel()).getCube("BodyUpper"), stack, 0.0625F);
        postRender(((TabulaModel) this.render.getModel()).getCube("Neck1"), stack, 00.0625F);
    }

    protected void translateToHead(MatrixStack stack) {
        postRender(((TabulaModel) this.render.getModel()).getCube("Neck2"), stack, 0.0625F);
        postRender(((TabulaModel) this.render.getModel()).getCube("Neck3"), stack, 0.0625F);
        postRender(((TabulaModel) this.render.getModel()).getCube("Head"), stack, 0.0625F);
    }

    protected void postRender(AdvancedModelBox renderer, MatrixStack matrixStackIn, float scale) {
        if (renderer.xRot == 0.0F && renderer.yRot == 0.0F && renderer.zRot == 0.0F) {
            if (renderer.x != 0.0F || renderer.y != 0.0F || renderer.z != 0.0F) {
                matrixStackIn.translate(renderer.x * scale, renderer.y * scale, renderer.z * scale);
            }
        } else {
            matrixStackIn.translate(renderer.x * scale, renderer.y * scale, renderer.z * scale);

            if (renderer.zRot != 0.0F) {
                matrixStackIn.mulPose(Vector3f.ZP.rotation(renderer.zRot));
            }

            if (renderer.yRot != 0.0F) {
                matrixStackIn.mulPose(Vector3f.YP.rotation(renderer.yRot));
            }

            if (renderer.xRot != 0.0F) {
                matrixStackIn.mulPose(Vector3f.XP.rotation(renderer.xRot));
            }
        }
    }

    private void offsetPerDragonType(DragonType dragonType, MatrixStack stackIn){
        if(dragonType == DragonType.LIGHTNING){
            stackIn.translate(0.1F, -0.2F, -0.1F);
        }
    }


    public <E extends Entity> void renderEntity(E entityIn, double x, double y, double z, float yaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRendererManager manager = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entityIn);

            if (render != null) {
                try {
                    render.render(entityIn, 0, partialTicks, matrixStack, bufferIn, packedLight);
                } catch (Throwable throwable1) {
                    throw new ReportedException(CrashReport.forThrowable(throwable1, "Rendering entity in world"));
                }
            }
        } catch (Throwable throwable3) {
            CrashReport crashreport = CrashReport.forThrowable(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            entityIn.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Assigned renderer", render);
            crashreportcategory1.setDetail("Location", CrashReportCategory.formatLocation(x, y, z));
            crashreportcategory1.setDetail("Rotation", Float.valueOf(yaw));
            crashreportcategory1.setDetail("Delta", Float.valueOf(partialTicks));
            throw new ReportedException(crashreport);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }


}