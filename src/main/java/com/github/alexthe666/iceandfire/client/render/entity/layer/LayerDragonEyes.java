package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.model.util.TabulaModelHandlerHelper;
import com.github.alexthe666.iceandfire.client.render.TabulaModelAccessor;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import com.github.alexthe666.iceandfire.enums.EnumDragonTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LayerDragonEyes extends RenderLayer<EntityDragonBase, AdvancedEntityModel<EntityDragonBase>> {
    private final MobRenderer render;
    private TabulaModel fireHead;
    private TabulaModel iceHead;
    private TabulaModel lightningHead;

    public LayerDragonEyes(MobRenderer renderIn) {
        super(renderIn);
        this.render = renderIn;
        try {
            fireHead = onlyKeepCubes(new TabulaModelAccessor(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/firedragon/firedragon_Ground"), null),
                Collections.singletonList("HeadFront"));
            iceHead = onlyKeepCubes(new TabulaModelAccessor(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/icedragon/icedragon_Ground"), null),
                Collections.singletonList("HeadFront"));
            lightningHead = onlyKeepCubes(new TabulaModelAccessor(TabulaModelHandlerHelper.loadTabulaModel("/assets/iceandfire/models/tabula/lightningdragon/lightningdragon_Ground"), null),
                Collections.singletonList("HeadFront"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, EntityDragonBase dragon, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (dragon.shouldRenderEyes()) {
            RenderType eyes = RenderType.eyes(EnumDragonTextures.getEyeTextureFromDragon(dragon));
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(eyes);
            if (dragon instanceof EntityLightningDragon && lightningHead != null) {
                copyPositions(lightningHead, (TabulaModel) this.getParentModel());
                lightningHead.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            } else if (dragon instanceof EntityIceDragon && iceHead != null) {
                copyPositions(iceHead, (TabulaModel) this.getParentModel());
                iceHead.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            } else if (fireHead != null) {
                copyPositions(fireHead, (TabulaModel) this.getParentModel());
                fireHead.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            //Fallback method
            else {
                this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    @Override
    protected @NotNull ResourceLocation getTextureLocation(@NotNull EntityDragonBase entityIn) {
        return null;
    }

    //TODO: do this with hideable/visble/showModel stuff instead
    //Removes all cubes except the cube names specified by the string list and their parents
    //We need to keep the parents to correctly render the head position
    private TabulaModel onlyKeepCubes(TabulaModelAccessor model, List<String> strings){
        List<AdvancedModelBox> keepCubes = new ArrayList<>();
        for (String str : strings){
            AdvancedModelBox cube = model.getCube(str);
            keepCubes.add(cube);
            while (cube.getParent() != null){
                keepCubes.add(cube.getParent());
                cube = cube.getParent();
            }
        }
        removeChildren(model,keepCubes);
        model.getCubes().values().removeIf(advancedModelBox -> !keepCubes.contains(advancedModelBox));
        return model;
    }

    private void removeChildren(TabulaModelAccessor model, List<AdvancedModelBox> keepCubes){
        model.getRootBox().forEach(modelRenderer -> {
            modelRenderer.childModels.removeIf(child -> !keepCubes.contains(child));
            modelRenderer.childModels.forEach(childModel ->{
                removeChildren((AdvancedModelBox) childModel,keepCubes);
            });
        });
    }

    private void removeChildren(AdvancedModelBox modelBox, List<AdvancedModelBox> keepCubes){
        modelBox.childModels.removeIf(modelRenderer -> !keepCubes.contains(modelRenderer));
        modelBox.childModels.forEach(modelRenderer -> {
            removeChildren((AdvancedModelBox)modelRenderer,keepCubes);
        });
    }

    public boolean isAngleEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose != null && pose.rotateAngleX == original.rotateAngleX && pose.rotateAngleY == original.rotateAngleY && pose.rotateAngleZ == original.rotateAngleZ;
    }
    public boolean isPositionEqual(AdvancedModelBox original, AdvancedModelBox pose) {
        return pose.rotationPointX == original.rotationPointX && pose.rotationPointY == original.rotationPointY && pose.rotationPointZ == original.rotationPointZ;
    }

    public void copyPositions(TabulaModel model, TabulaModel modelTo) {
        for (AdvancedModelBox cube : model.getCubes().values()) {
            AdvancedModelBox modelToCube = modelTo.getCube(cube.boxName);
            if (!isAngleEqual(cube,modelToCube)) {
                cube.rotateAngleX = modelToCube.rotateAngleX;
                cube.rotateAngleY = modelToCube.rotateAngleY;
                cube.rotateAngleZ = modelToCube.rotateAngleZ;
            }
            if (!isPositionEqual(cube,modelToCube)) {
                cube.rotationPointX = modelToCube.rotationPointX;
                cube.rotationPointY = modelToCube.rotationPointY;
                cube.rotationPointZ = modelToCube.rotationPointZ;
            }

        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}