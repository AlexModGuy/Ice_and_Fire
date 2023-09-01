package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDreadQueen;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class LayerDragonRider extends RenderLayer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private final MobRenderer render;
    private final boolean excludeDreadQueenMob;

    public LayerDragonRider(MobRenderer renderIn, boolean excludeDreadQueenMob) {
        super(renderIn);
        this.render = renderIn;
        this.excludeDreadQueenMob = excludeDreadQueenMob;
    }

    @Override
    public void render(PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.pushPose();
        if (!dragon.getPassengers().isEmpty()) {
            float dragonScale = dragon.getRenderSize() / 3;
            for (Entity passenger : dragon.getPassengers()) {
                boolean prey = dragon.getControllingPassenger() == null || dragon.getControllingPassenger().getId() != passenger.getId();
                if (excludeDreadQueenMob && passenger instanceof EntityDreadQueen) {
                    prey = false;
                }
                ClientProxy.currentDragonRiders.remove(passenger.getUUID());
                float riderRot = passenger.yRotO + (passenger.getYRot() - passenger.yRotO) * partialTicks;
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
                        if ((passenger.getBbHeight() > passenger.getBbWidth() || modelBase instanceof HumanoidModel) && !(modelBase instanceof QuadrupedModel) && !(modelBase instanceof HorseModel)) {
                            matrixStackIn.translate(-0.15F * passenger.getBbHeight(), 0.1F * dragonScale - 0.1F * passenger.getBbHeight(), -0.1F * dragonScale - 0.1F * passenger.getBbWidth());
                            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(90.0F));
                            matrixStackIn.mulPose(Axis.YP.rotationDegrees(45.0F));
                        } else {
                            boolean horse = modelBase instanceof HorseModel;
                            matrixStackIn.translate((horse ? -0.08F : -0.15F) * passenger.getBbWidth(), 0.1F * dragonScale - 0.15F * passenger.getBbWidth(), -0.1F * dragonScale - 0.1F * passenger.getBbWidth());
                            matrixStackIn.mulPose(Axis.XN.rotationDegrees(90.0F));
                        }
                    } else {
                        matrixStackIn.translate(0, 0.555F * dragonScale, -0.5F * dragonScale);
                    }

                } else {
                    matrixStackIn.translate(0, -0.01F * dragonScale, -0.035F * dragonScale);
                }
                matrixStackIn.pushPose();
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(riderRot+180));
                matrixStackIn.scale(1 / dragonScale, 1 / dragonScale, 1 / dragonScale);
                matrixStackIn.translate(0, -0.25F, 0);
                renderEntity(passenger, 0, 0, 0, 0.0F, partialTicks, matrixStackIn, bufferIn, packedLightIn);
                matrixStackIn.popPose();
                ClientProxy.currentDragonRiders.add(passenger.getUUID());
            }
        }
        matrixStackIn.popPose();
    }

    protected void translateToBody(PoseStack stack) {
        postRender(((TabulaModel) this.render.getModel()).getCube("BodyUpper"), stack, 0.0625F);
        postRender(((TabulaModel) this.render.getModel()).getCube("Neck1"), stack, 00.0625F);
    }

    protected void translateToHead(PoseStack stack) {
        postRender(((TabulaModel) this.render.getModel()).getCube("Neck2"), stack, 0.0625F);
        postRender(((TabulaModel) this.render.getModel()).getCube("Neck3"), stack, 0.0625F);
        postRender(((TabulaModel) this.render.getModel()).getCube("Head"), stack, 0.0625F);
    }

    protected void postRender(AdvancedModelBox renderer, PoseStack matrixStackIn, float scale) {
        if (renderer.rotateAngleX == 0.0F && renderer.rotateAngleY == 0.0F && renderer.rotateAngleZ == 0.0F) {
            if (renderer.rotationPointX != 0.0F || renderer.rotationPointY != 0.0F || renderer.rotationPointZ != 0.0F) {
                matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);
            }
        } else {
            matrixStackIn.translate(renderer.rotationPointX * scale, renderer.rotationPointY * scale, renderer.rotationPointZ * scale);

            if (renderer.rotateAngleZ != 0.0F) {
                matrixStackIn.mulPose(Axis.ZP.rotation(renderer.rotateAngleZ));
            }

            if (renderer.rotateAngleY != 0.0F) {
                matrixStackIn.mulPose(Axis.YP.rotation(renderer.rotateAngleY));
            }

            if (renderer.rotateAngleX != 0.0F) {
                matrixStackIn.mulPose(Axis.XP.rotation(renderer.rotateAngleX));
            }
        }
    }

    private void offsetPerDragonType(DragonType dragonType, PoseStack stackIn) {
        if (dragonType == DragonType.LIGHTNING) {
            stackIn.translate(0.1F, -0.2F, -0.1F);
        }
    }


    public <E extends Entity> void renderEntity(E entityIn, int x, int y, int z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
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
            crashreportcategory1.setDetail("Location", new BlockPos(x, y, z));
            crashreportcategory1.setDetail("Rotation", yaw);
            crashreportcategory1.setDetail("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }


}