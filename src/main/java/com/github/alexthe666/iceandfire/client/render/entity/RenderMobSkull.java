package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;

public class RenderMobSkull extends EntityRenderer<EntityMobSkull> {

    private static final Map<String, ResourceLocation> SKULL_TEXTURE_CACHE = Maps.newHashMap();
    private final ModelHippogryph hippogryphModel;
    private final ModelCyclops cyclopsModel;
    private final ModelCockatrice cockatriceModel;
    private final ModelStymphalianBird stymphalianBirdModel;
    private final ModelTroll trollModel;
    private final ModelAmphithere amphithereModel;
    private final ModelHydraHead hydraModel;
    private final TabulaModel seaSerpentModel;

    public RenderMobSkull(EntityRendererProvider.Context context, AdvancedEntityModel seaSerpentModel) {
        super(context);
        this.hippogryphModel = new ModelHippogryph();
        this.cyclopsModel = new ModelCyclops();
        this.cockatriceModel = new ModelCockatrice();
        this.stymphalianBirdModel = new ModelStymphalianBird();
        this.trollModel = new ModelTroll();
        this.amphithereModel = new ModelAmphithere();
        this.seaSerpentModel = (TabulaModel) seaSerpentModel;
        this.hydraModel = new ModelHydraHead(0);
    }

    private static void setRotationAngles(BasicModelPart cube, float rotX, float rotY, float rotZ) {
        cube.rotateAngleX = rotX;
        cube.rotateAngleY = rotY;
        cube.rotateAngleZ = rotZ;
    }

    @Override
    public void render(@NotNull EntityMobSkull entity, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-180.0F));
        matrixStackIn.mulPose(Axis.YN.rotationDegrees(180.0F - entity.getYaw()));
        float f = 0.0625F;
        float size = 1.0F;
        matrixStackIn.scale(size, size, size);
        matrixStackIn.translate(0, entity.isOnWall() ? -0.24F : -0.12F, 0.5F);
        renderForEnum(entity.getSkullType(), entity.isOnWall(), matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

    private void renderForEnum(EnumSkullType skull, boolean onWall, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(getSkullTexture(skull)));
        switch (skull) {
            case HIPPOGRYPH:
                matrixStackIn.translate(0, -0.0F, -0.2F);
                matrixStackIn.scale(1.2F, 1.2F, 1.2F);
                hippogryphModel.resetToDefaultPose();
                setRotationAngles(hippogryphModel.Head, onWall ? (float) Math.toRadians(50F) : (float) Math.toRadians(-5), 0, 0);
                hippogryphModel.Head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                break;
            case CYCLOPS:
                matrixStackIn.translate(0, 1.8F, -0.5F);
                matrixStackIn.scale(2.25F, 2.25F, 2.25F);
                cyclopsModel.resetToDefaultPose();
                setRotationAngles(cyclopsModel.Head, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                cyclopsModel.Head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                break;
            case COCKATRICE:
                if (onWall) {
                    matrixStackIn.translate(0, 0F, 0.35F);
                }
                cockatriceModel.resetToDefaultPose();
                setRotationAngles(cockatriceModel.head, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                cockatriceModel.head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                break;
            case STYMPHALIAN:
                if (!onWall) {
                    matrixStackIn.translate(0, 0F, -0.35F);
                }
                stymphalianBirdModel.resetToDefaultPose();
                setRotationAngles(stymphalianBirdModel.HeadBase, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                stymphalianBirdModel.HeadBase.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                break;
            case TROLL:
                matrixStackIn.translate(0, 1F, -0.35F);
                if (onWall) {
                    matrixStackIn.translate(0, 0F, 0.35F);
                }
                trollModel.resetToDefaultPose();
                setRotationAngles(trollModel.head, onWall ? (float) Math.toRadians(50F) : (float) Math.toRadians(-20), 0, 0);
                trollModel.head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                break;
            case AMPHITHERE:
                matrixStackIn.translate(0, -0.2F, 0.7F);
                matrixStackIn.scale(2.0F, 2.0F, 2.0F);
                amphithereModel.resetToDefaultPose();
                setRotationAngles(amphithereModel.Head, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                amphithereModel.Head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                break;
            case SEASERPENT:
                matrixStackIn.translate(0, -0.35F, 0.8F);
                matrixStackIn.scale(2.5F, 2.5F, 2.5F);
                seaSerpentModel.resetToDefaultPose();
                setRotationAngles(seaSerpentModel.getCube("Head"), onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                seaSerpentModel.getCube("Head").render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                break;
            case HYDRA:
                matrixStackIn.translate(0, -0.2F, -0.1F);
                matrixStackIn.scale(2.0F, 2.0F, 2.0F);
                hydraModel.resetToDefaultPose();
                setRotationAngles(hydraModel.Head1, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                hydraModel.Head1.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

                break;
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityMobSkull entity) {
        return getSkullTexture(entity.getSkullType());
    }

    public ResourceLocation getSkullTexture(EnumSkullType skull) {
        String s = "iceandfire:textures/models/skulls/skull_" + skull.name().toLowerCase(Locale.ROOT) + ".png";
        ResourceLocation resourcelocation = SKULL_TEXTURE_CACHE.get(s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            SKULL_TEXTURE_CACHE.put(s, resourcelocation);
        }
        return resourcelocation;
    }

}
