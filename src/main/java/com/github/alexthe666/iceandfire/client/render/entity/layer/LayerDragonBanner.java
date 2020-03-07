package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.iceandfire.client.model.ModelMyrmexBase;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

public class LayerDragonBanner implements LayerRenderer<EntityDragonBase> {

    protected final RenderLivingBase render;
    private final ModelBanner bannerModel = new ModelBanner();
    private final TileEntityBanner banner = new TileEntityBanner();

    public LayerDragonBanner(RenderLivingBase livingEntityRendererIn) {
        this.render = livingEntityRendererIn;
    }

    public void doRenderLayer(EntityDragonBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack itemstack = entity.getHeldItem(EnumHand.OFF_HAND);
        GlStateManager.pushMatrix();
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemBanner) {
            this.banner.setItemValues(itemstack, false);
            float f = (entity.getRenderSize() / 3F);
            float f2 = 1F / f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, -0.125F, 0.4F);
            postRender(((IceAndFireTabulaModel) this.render.getMainModel()).getCube("BodyUpper"),  0.0625F);
            renderBanner(banner, 0, 0, 0, partialTicks, 0, f, false);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
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

    public void renderBanner(TileEntityBanner te, double x, double y, double z, float partialTicks, int destroyStage, float scale, boolean pole) {
        boolean flag = te.getWorld() != null;
        int i = flag ? te.getBlockMetadata() : 0;
        long j = flag ? te.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        float reverseScale = 1F / scale;
        float f = 0.6666667F;
        if (!pole) {
            this.bannerModel.bannerStand.showModel = true;
        } else {
            float f2 = 0.0F;

            if (i == 2) {
                f2 = 180.0F;
            }

            if (i == 4) {
                f2 = 90.0F;
            }

            if (i == 5) {
                f2 = -90.0F;
            }
            this.bannerModel.bannerStand.showModel = false;
        }

        BlockPos blockpos = te.getPos();
        float f3 = (float) (blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + (float) j + partialTicks;
        this.bannerModel.bannerSlate.rotateAngleX = (-0.0125F + 0.01F * MathHelper.cos(f3 * (float) Math.PI * 0.02F)) * (float) Math.PI;
        GlStateManager.enableRescaleNormal();
        ResourceLocation resourcelocation = this.getBannerResourceLocation(te);
        if (resourcelocation != null) {
            render.bindTexture(resourcelocation);
            GlStateManager.pushMatrix();
            GlStateManager.scale(reverseScale,  reverseScale, reverseScale);
            this.bannerModel.renderBanner();
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public boolean shouldCombineTextures() {
        return false;
    }

    public static final BannerTextures.Cache BANNER_DESIGNS = new BannerTextures.Cache("B", new ResourceLocation("textures/entity/banner_base.png"), "textures/entity/banner/");

    @Nullable
    private ResourceLocation getBannerResourceLocation(TileEntityBanner bannerObj) {
        return BANNER_DESIGNS.getResourceLocation(bannerObj.getPatternResourceLocation(), bannerObj.getPatternList(), bannerObj.getColorList());
    }


}