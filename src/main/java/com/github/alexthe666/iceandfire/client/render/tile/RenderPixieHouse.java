package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.model.ModelPixieHouse;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import org.jetbrains.annotations.NotNull;

public class RenderPixieHouse<T extends TileEntityPixieHouse> implements BlockEntityRenderer<T> {

    private static final ModelPixieHouse MODEL = new ModelPixieHouse();
    private static ModelPixie MODEL_PIXIE;
    private static final RenderType TEXTURE_0 = RenderType.entityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_0.png"), false);
    private static final RenderType TEXTURE_1 = RenderType.entityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_1.png"), false);
    private static final RenderType TEXTURE_2 = RenderType.entityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_2.png"), false);
    private static final RenderType TEXTURE_3 = RenderType.entityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_3.png"), false);
    private static final RenderType TEXTURE_4 = RenderType.entityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_4.png"), false);
    private static final RenderType TEXTURE_5 = RenderType.entityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_5.png"), false);
    public BlockItem metaOverride;

    public RenderPixieHouse(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(@NotNull T entity, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        int rotation = 0;
        int meta = 0;
        if (MODEL_PIXIE == null) {
            MODEL_PIXIE = new ModelPixie();
        }
        if (entity != null && entity.getLevel() != null && entity.getLevel().getBlockState(entity.getBlockPos()).getBlock() instanceof BlockPixieHouse) {
            meta = TileEntityPixieHouse.getHouseTypeFromBlock(entity.getLevel().getBlockState(entity.getBlockPos()).getBlock());
            // Apparently with Optifine/other optimizations mods, this code path can get run before the block
            // has been destroyed/possibly created, causing the BlockState to be an air block,
            // which is missing the below property, causing a crash. If this property is missing,
            // let's just silently fail.
            if (!entity.getLevel().getBlockState(entity.getBlockPos()).hasProperty(BlockPixieHouse.FACING)) {
                return;
            }
            Direction facing = entity.getLevel().getBlockState(entity.getBlockPos()).getValue(BlockPixieHouse.FACING);
            if (facing == Direction.NORTH) {
                rotation = 180;
            }
            else if (facing == Direction.EAST) {
                rotation = -90;
            } else if (facing == Direction.WEST) {
                rotation = 90;
            }

        }
        if (entity == null) {
            meta = TileEntityPixieHouse.getHouseTypeFromBlock(metaOverride.getBlock());
        }
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5F, 1.501F, 0.5F);
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(rotation));
        if (entity != null && entity.getLevel() != null && entity.hasPixie) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0F, 0.95F, 0F);
            matrixStackIn.scale(0.55F, 0.55F, 0.55F);
            matrixStackIn.pushPose();
            //GL11.glRotatef(MathHelper.clampAngle(entity.ticksExisted * 3), 0, 1, 0);
            RenderType type = RenderJar.TEXTURE_0;
            RenderType type2 = RenderJar.TEXTURE_0_GLO;
            switch (entity.pixieType) {
                default:
                    type = RenderJar.TEXTURE_0;
                    type2 = RenderJar.TEXTURE_0_GLO;
                    break;
                case 1:
                    type = RenderJar.TEXTURE_1;
                    type2 = RenderJar.TEXTURE_1_GLO;
                    break;
                case 2:
                    type = RenderJar.TEXTURE_2;
                    type2 = RenderJar.TEXTURE_2_GLO;
                    break;
                case 3:
                    type = RenderJar.TEXTURE_3;
                    type2 = RenderJar.TEXTURE_3_GLO;
                    break;
                case 4:
                    type = RenderJar.TEXTURE_4;
                    type2 = RenderJar.TEXTURE_4_GLO;
                    break;
                case 5:
                    type = RenderJar.TEXTURE_5;
                    type2 = RenderJar.TEXTURE_5_GLO;
                    break;
            }
            matrixStackIn.pushPose();
            MODEL_PIXIE.animateInHouse(entity);
            MODEL_PIXIE.renderToBuffer(matrixStackIn, bufferIn.getBuffer(type), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            MODEL_PIXIE.renderToBuffer(matrixStackIn, bufferIn.getBuffer(type2), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
            matrixStackIn.popPose();
            matrixStackIn.popPose();
        }
        RenderType pixieType = TEXTURE_0;
        switch (meta) {
            case 0:
                pixieType = TEXTURE_0;
                break;
            case 1:
                pixieType = TEXTURE_1;
                break;
            case 2:
                pixieType = TEXTURE_2;
                break;
            case 3:
                pixieType = TEXTURE_3;
                break;
            case 4:
                pixieType = TEXTURE_4;
                break;
            case 5:
                pixieType = TEXTURE_5;
                break;
        }
        matrixStackIn.pushPose();
        MODEL.renderToBuffer(matrixStackIn, bufferIn.getBuffer(pixieType), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }
}
