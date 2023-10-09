package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelTideTrident;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderTideTridentItem extends BlockEntityWithoutLevelRenderer {
    private static final ModelTideTrident MODEL = new ModelTideTrident();

    public RenderTideTridentItem(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext type, PoseStack stackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        stackIn.translate(0.5F, 0.5f, 0.5f);
        if (type == ItemDisplayContext.GUI || type == ItemDisplayContext.FIXED || type == ItemDisplayContext.NONE || type == ItemDisplayContext.GROUND) {
            ItemStack tridentInventory = new ItemStack(IafItemRegistry.TIDE_TRIDENT_INVENTORY.get());
            if (stack.isEnchanted()) {
                ListTag enchantments = stack.getTag().getList("Enchantments", 10);
                tridentInventory.addTagElement("Enchantments", enchantments);
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(tridentInventory, type, type == ItemDisplayContext.GROUND ? combinedLightIn : 240, combinedOverlayIn, stackIn, bufferIn, Minecraft.getInstance().level, 0);
        } else {
            stackIn.pushPose();
            stackIn.translate(0, 0.2F, -0.15F);
            if (type.firstPerson()) {
                stackIn.translate(type == ItemDisplayContext.FIRST_PERSON_LEFT_HAND ? -0.3F : 0.3F, 0.2F, -0.2F);
            } else {
                stackIn.translate(0, 0.6F, 0.0F);
            }
            stackIn.mulPose(Axis.XP.rotationDegrees(160.0F));
            VertexConsumer glintVertexBuilder = ItemRenderer.getFoilBufferDirect(bufferIn, RenderType.entityCutoutNoCull(RenderTideTrident.TRIDENT), false, stack.hasFoil());
            MODEL.renderToBuffer(stackIn,
                glintVertexBuilder, combinedLightIn,
                combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            stackIn.popPose();
        }

    }

}
