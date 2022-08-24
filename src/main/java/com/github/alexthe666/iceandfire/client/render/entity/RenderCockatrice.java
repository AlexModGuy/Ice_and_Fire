package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelCockatrice;
import com.github.alexthe666.iceandfire.client.model.ModelCockatriceChick;
import com.github.alexthe666.iceandfire.client.particle.CockatriceBeamRender;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

public class RenderCockatrice extends MobRenderer<EntityCockatrice, SegmentedModel<EntityCockatrice>> {

    public static final ResourceLocation TEXTURE_ROOSTER = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_0.png");
    public static final ResourceLocation TEXTURE_HEN = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_1.png");
    public static final ResourceLocation TEXTURE_ROOSTER_CHICK = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_0_chick.png");
    public static final ResourceLocation TEXTURE_HEN_CHICK = new ResourceLocation("iceandfire:textures/models/cockatrice/cockatrice_1_chick.png");
    public static final ModelCockatrice ADULT_MODEL = new ModelCockatrice();
    public static final ModelCockatriceChick BABY_MODEL = new ModelCockatriceChick();

    public RenderCockatrice(EntityRendererManager renderManager) {
        super(renderManager, new ModelCockatrice(), 0.6F);
    }



    private Vector3d getPosition(LivingEntity LivingEntityIn, double p_177110_2_, float p_177110_4_) {
        double d0 = LivingEntityIn.xOld + (LivingEntityIn.getX() - LivingEntityIn.xOld) * (double) p_177110_4_;
        double d1 = p_177110_2_ + LivingEntityIn.yOld + (LivingEntityIn.getY() - LivingEntityIn.yOld) * (double) p_177110_4_;
        double d2 = LivingEntityIn.zOld + (LivingEntityIn.getZ() - LivingEntityIn.zOld) * (double) p_177110_4_;
        return new Vector3d(d0, d1, d2);
    }

    public boolean shouldRender(EntityCockatrice livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            if (livingEntityIn.hasTargetedEntity()) {
                LivingEntity livingentity = livingEntityIn.getTargetedEntity();
                if (livingentity != null) {
                    Vector3d Vector3d = this.getPosition(livingentity, (double) livingentity.getBbHeight() * 0.5D, 1.0F);
                    Vector3d Vector3d1 = this.getPosition(livingEntityIn, livingEntityIn.getEyeHeight(), 1.0F);
                    return camera.isVisible(new AxisAlignedBB(Vector3d1.x, Vector3d1.y, Vector3d1.z, Vector3d.x, Vector3d.y, Vector3d.z));
                }
            }

            return false;
        }
    }

    public void render(EntityCockatrice entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isBaby()) {
            model = BABY_MODEL;
        } else {
            model = ADULT_MODEL;
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        LivingEntity livingentity = entityIn.getTargetedEntity();
        boolean blindness = entityIn.hasEffect(Effects.BLINDNESS) || livingentity != null && livingentity.hasEffect(Effects.BLINDNESS);
        if (!blindness && livingentity != null && EntityGorgon.isEntityLookingAt(entityIn, livingentity, EntityCockatrice.VIEW_RADIUS) && EntityGorgon.isEntityLookingAt(livingentity, entityIn, EntityCockatrice.VIEW_RADIUS)) {
            if (livingentity != null) {
                CockatriceBeamRender.render(entityIn, livingentity, matrixStackIn, bufferIn, partialTicks);
            }
        }

    }

    @Override
    protected void scale(EntityCockatrice entity, MatrixStack matrixStackIn, float partialTickTime) {
        if (entity.isBaby()) {
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCockatrice cockatrice) {
        if (cockatrice.isBaby()) {
            return cockatrice.isHen() ? TEXTURE_HEN_CHICK : TEXTURE_ROOSTER_CHICK;
        } else {
            return cockatrice.isHen() ? TEXTURE_HEN : TEXTURE_ROOSTER;
        }
    }

}
