package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderMyrmexEgg;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;

public class RenderPodium<T extends TileEntityPodium> implements BlockEntityRenderer<T> {

    public RenderPodium(BlockEntityRendererProvider.Context context) {

    }

    protected static RenderType getEggTexture(EnumDragonEgg type) {
        return switch (type) {
            default -> RenderType.entityCutout(RenderDragonEgg.EGG_RED);
            case GREEN -> RenderType.entityCutout(RenderDragonEgg.EGG_GREEN);
            case BRONZE -> RenderType.entityCutout(RenderDragonEgg.EGG_BRONZE);
            case GRAY -> RenderType.entityCutout(RenderDragonEgg.EGG_GREY);
            case BLUE -> RenderType.entityCutout(RenderDragonEgg.EGG_BLUE);
            case WHITE -> RenderType.entityCutout(RenderDragonEgg.EGG_WHITE);
            case SAPPHIRE -> RenderType.entityCutout(RenderDragonEgg.EGG_SAPPHIRE);
            case SILVER -> RenderType.entityCutout(RenderDragonEgg.EGG_SILVER);
            case ELECTRIC -> RenderType.entityCutout(RenderDragonEgg.EGG_ELECTRIC);
            case AMYTHEST -> RenderType.entityCutout(RenderDragonEgg.EGG_AMYTHEST);
            case COPPER -> RenderType.entityCutout(RenderDragonEgg.EGG_COPPER);
            case BLACK -> RenderType.entityCutout(RenderDragonEgg.EGG_BLACK);
        };
    }

    @Override
    public void render(@NotNull T entity, float partialTicks, @NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ModelDragonEgg model = new ModelDragonEgg();
        TileEntityPodium podium = entity;

        if (!podium.getItem(0).isEmpty()) {
            if (podium.getItem(0).getItem() instanceof ItemDragonEgg) {
                ItemDragonEgg item = (ItemDragonEgg) podium.getItem(0).getItem();
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.5F, 0.475F, 0.5F);
                matrixStackIn.pushPose();
                matrixStackIn.pushPose();
                model.renderPodium();
                model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderPodium.getEggTexture(item.type)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.popPose();
                matrixStackIn.popPose();
                matrixStackIn.popPose();
            } else if (podium.getItem(0).getItem() instanceof ItemMyrmexEgg) {
                boolean jungle = podium.getItem(0).getItem() == IafItemRegistry.MYRMEX_JUNGLE_EGG.get();
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.5F, 0.475F, 0.5F);
                matrixStackIn.pushPose();
                matrixStackIn.pushPose();
                model.renderPodium();
                model.renderToBuffer(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutout(jungle ? RenderMyrmexEgg.EGG_JUNGLE : RenderMyrmexEgg.EGG_DESERT)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.popPose();
                matrixStackIn.popPose();
                matrixStackIn.popPose();
            } else if (!podium.getItem(0).isEmpty()) {
                //if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new RenderPodiumItemEvent(this, podium, f, x, y, z))) {
                matrixStackIn.pushPose();
                float f2 = ((float) podium.prevTicksExisted + (podium.ticksExisted - podium.prevTicksExisted) * partialTicks);
                float f3 = Mth.sin(f2 / 10.0F) * 0.1F + 0.1F;
                matrixStackIn.translate(0.5F, 1.55F + f3, 0.5F);
                float f4 = (f2 / 20.0F);
                matrixStackIn.mulPose(Axis.YP.rotation(f4));
                matrixStackIn.pushPose();
                matrixStackIn.translate(0, 0.2F, 0);
                matrixStackIn.scale(0.65F, 0.65F, 0.65F);
                Minecraft.getInstance().getItemRenderer().renderStatic(podium.getItem(0), ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, podium.getLevel(), 0);
                matrixStackIn.popPose();
                matrixStackIn.popPose();
                //}
            }
        }

    }
}
