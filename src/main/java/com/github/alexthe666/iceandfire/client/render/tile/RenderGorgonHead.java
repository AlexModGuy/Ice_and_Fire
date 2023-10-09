package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.iceandfire.client.model.ModelGorgonHead;
import com.github.alexthe666.iceandfire.client.model.ModelGorgonHeadActive;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderGorgonHead extends BlockEntityWithoutLevelRenderer {

    private static final RenderType ACTIVE_TEXTURE = RenderType.entityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/gorgon/head_active.png"), false);
    private static final RenderType INACTIVE_TEXTURE = RenderType.entityCutoutNoCull(new ResourceLocation("iceandfire:textures/models/gorgon/head_inactive.png"), false);
    private static final AdvancedEntityModel ACTIVE_MODEL = new ModelGorgonHeadActive();
    private static final AdvancedEntityModel INACTIVE_MODEL = new ModelGorgonHead();

    public RenderGorgonHead(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
        super(dispatcher, set);
    }

    @Override
    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext type, @NotNull PoseStack stackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        boolean active = false;
        if (stack.getItem() == IafItemRegistry.GORGON_HEAD.get()) {
            if (stack.getTag() != null) {
                if (stack.getTag().getBoolean("Active"))
                    active = true;
            }
        }
        AdvancedEntityModel model = active ? ACTIVE_MODEL : INACTIVE_MODEL;
        stackIn.pushPose();
        stackIn.translate(0.5F, active ? 1.5F : 1.25F, 0.5F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(active ? ACTIVE_TEXTURE : INACTIVE_TEXTURE);
        model.renderToBuffer(stackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        stackIn.popPose();
    }

}
