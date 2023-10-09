package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.model.ICustomStatueModel;
import com.github.alexthe666.iceandfire.client.model.ModelHydraBody;
import com.github.alexthe666.iceandfire.client.model.ModelStonePlayer;
import com.github.alexthe666.iceandfire.client.render.IafRenderType;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerHydraHead;
import com.github.alexthe666.iceandfire.entity.EntityHydra;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RenderStoneStatue extends EntityRenderer<EntityStoneStatue> {

    protected static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]{new ResourceLocation("textures/block/destroy_stage_0.png"), new ResourceLocation("textures/block/destroy_stage_1.png"), new ResourceLocation("textures/block/destroy_stage_2.png"), new ResourceLocation("textures/block/destroy_stage_3.png"), new ResourceLocation("textures/block/destroy_stage_4.png"), new ResourceLocation("textures/block/destroy_stage_5.png"), new ResourceLocation("textures/block/destroy_stage_6.png"), new ResourceLocation("textures/block/destroy_stage_7.png"), new ResourceLocation("textures/block/destroy_stage_8.png"), new ResourceLocation("textures/block/destroy_stage_9.png")};
    private final Map<String, EntityModel> modelMap = new HashMap();
    private final Map<String, Entity> hollowEntityMap = new HashMap();
    private final EntityRendererProvider.Context context;

    public RenderStoneStatue(EntityRendererProvider.Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityStoneStatue entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    protected void preRenderCallback(EntityStoneStatue entity, PoseStack matrixStackIn, float partialTickTime) {
        float scale = entity.getScale() < 0.01F ? 1F : entity.getScale();
        matrixStackIn.scale(scale, scale, scale);
    }

    @Override
    public void render(EntityStoneStatue entityIn, float entityYaw, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn) {
        EntityModel model = new PigModel(context.bakeLayer(ModelLayers.PIG));

        // Get the correct model
        if (modelMap.get(entityIn.getTrappedEntityTypeString()) != null) {
            model = modelMap.get(entityIn.getTrappedEntityTypeString());
        } else {
            EntityRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(entityIn.getTrappedEntityType());

            if (renderer instanceof RenderLayerParent) {
                model = ((RenderLayerParent<?, ?>) renderer).getModel();
            } else if (entityIn.getTrappedEntityType() == EntityType.PLAYER) {
                model = new ModelStonePlayer(context.bakeLayer(ModelLayers.PLAYER));
            }
            modelMap.put(entityIn.getTrappedEntityTypeString(), model);
        }
        if (model == null)
            return;

        Entity fakeEntity = null;
        if (this.hollowEntityMap.get(entityIn.getTrappedEntityTypeString()) == null) {
            Entity build = entityIn.getTrappedEntityType().create(Minecraft.getInstance().level);
            if (build != null) {
                try {
                    build.load(entityIn.getTrappedTag());
                } catch (Exception e) {
                    IceAndFire.LOGGER.warn("Mob " + entityIn.getTrappedEntityTypeString() + " could not build statue NBT");
                }
                fakeEntity = this.hollowEntityMap.putIfAbsent(entityIn.getTrappedEntityTypeString(), build);
            }
        } else {
            fakeEntity = this.hollowEntityMap.get(entityIn.getTrappedEntityTypeString());
        }
        RenderType tex = IafRenderType.getStoneMobRenderType(200, 200);
        if (fakeEntity instanceof EntityTroll) {
            tex = RenderType.entityCutout(((EntityTroll) fakeEntity).getTrollType().TEXTURE_STONE);
        }

        VertexConsumer ivertexbuilder = bufferIn.getBuffer(tex);


        matrixStackIn.pushPose();
        float yaw = entityIn.yRotO + (entityIn.getYRot() - entityIn.yRotO) * partialTicks;
        boolean shouldSit = entityIn.isPassenger() && (entityIn.getVehicle() != null && entityIn.getVehicle().shouldRiderSit());
        model.young = entityIn.isBaby();
        model.riding = shouldSit;
        model.attackTime = entityIn.getAttackAnim(partialTicks);
        if (model instanceof AdvancedEntityModel) {
            ((AdvancedEntityModel) model).resetToDefaultPose();
        } else if (fakeEntity != null) {
            model.setupAnim(fakeEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F);
        }
        preRenderCallback(entityIn, matrixStackIn, partialTicks);
        matrixStackIn.translate(0, 1.5F, 0);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(yaw));
        if (model instanceof ICustomStatueModel && fakeEntity != null) {
            ((ICustomStatueModel) model).renderStatue(matrixStackIn, ivertexbuilder, packedLightIn, fakeEntity);
            if (model instanceof ModelHydraBody && fakeEntity instanceof EntityHydra) {
                LayerHydraHead.renderHydraHeads((ModelHydraBody) model, true, matrixStackIn, bufferIn, packedLightIn, (EntityHydra) fakeEntity, 0, 0, partialTicks, 0, 0, 0);
            }
        } else {
            model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        matrixStackIn.popPose();

        if (entityIn.getCrackAmount() >= 1) {
            int i = Mth.clamp(entityIn.getCrackAmount() - 1, 0, DESTROY_STAGES.length - 1);
            RenderType crackTex = IafRenderType.getStoneCrackRenderType(DESTROY_STAGES[i]);
            VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(crackTex);
            matrixStackIn.pushPose();
            matrixStackIn.pushPose();
            preRenderCallback(entityIn, matrixStackIn, partialTicks);
            matrixStackIn.translate(0, 1.5F, 0);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(yaw));
            if (model instanceof ICustomStatueModel) {
                ((ICustomStatueModel) model).renderStatue(matrixStackIn, ivertexbuilder2, packedLightIn, fakeEntity);
            } else {
                model.renderToBuffer(matrixStackIn, ivertexbuilder2, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            matrixStackIn.popPose();
            matrixStackIn.popPose();
        }
        //super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}