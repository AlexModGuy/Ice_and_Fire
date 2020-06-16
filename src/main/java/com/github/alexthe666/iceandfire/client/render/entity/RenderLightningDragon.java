package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.particle.LightningBoltData;
import com.github.alexthe666.iceandfire.client.particle.LightningRender;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class RenderLightningDragon extends RenderDragonBase {

    private LightningRender lightningRender = new LightningRender();

    public RenderLightningDragon(EntityRendererManager manager, SegmentedModel model, int dragonType) {
        super(manager, model, dragonType);
    }

    public boolean shouldRender(EntityDragonBase livingEntityIn, ClippingHelperImpl camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            EntityLightningDragon lightningDragon = (EntityLightningDragon)livingEntityIn;
            if (lightningDragon.hasLightningTarget()) {
                Vec3d vec3d1 = lightningDragon.getHeadPosition();
                Vec3d vec3d = new Vec3d(lightningDragon.getLightningTargetX(), lightningDragon.getLightningTargetY(), lightningDragon.getLightningTargetZ());
                return camera.isBoundingBoxInFrustum(new AxisAlignedBB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z));
            }
            return false;
        }
    }

    public void render(EntityDragonBase entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        EntityLightningDragon lightningDragon = (EntityLightningDragon)entityIn;
        matrixStackIn.push();
        if (lightningDragon.hasLightningTarget()) {

            Vec3d vec3d1 = lightningDragon.getHeadPosition();
            Vec3d vec3d = new Vec3d(lightningDragon.getLightningTargetX(), lightningDragon.getLightningTargetY(), lightningDragon.getLightningTargetZ());
            float energyScale = 0.3F * lightningDragon.getRenderScale();
            LightningBoltData bolt = new LightningBoltData(LightningBoltData.BoltRenderInfo.electricity(), vec3d1, vec3d, 15)
                    .size(0.05F * getBoundedScale(energyScale, 0.5F, 2))
                    .lifespan(3)
                    .spawn(LightningBoltData.SpawnFunction.NO_DELAY);
            lightningRender.update(null, bolt, partialTicks);
            matrixStackIn.translate(-lightningDragon.getPosX(), -lightningDragon.getPosY(), -lightningDragon.getPosZ());
            lightningRender.render(partialTicks, matrixStackIn, bufferIn);
        }
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

    }

    private static float getBoundedScale(float scale, float min, float max) {
        return min + scale * (max - min);
    }
}
