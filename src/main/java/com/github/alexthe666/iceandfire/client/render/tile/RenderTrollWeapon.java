package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelTrollWeapon;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RenderTrollWeapon extends BlockEntityWithoutLevelRenderer {
    private static final ModelTrollWeapon MODEL = new ModelTrollWeapon();

    public RenderTrollWeapon(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
        super(dispatcher, set);
    }

    @Override
    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext type, @NotNull PoseStack stackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        EnumTroll.Weapon weapon = EnumTroll.Weapon.AXE;
        if (stack.getItem() instanceof ItemTrollWeapon)
            weapon = ((ItemTrollWeapon) stack.getItem()).weapon;

        stackIn.pushPose();
        stackIn.translate(0.5F, -0.75F, 0.5F);
        MODEL.renderToBuffer(stackIn, bufferIn.getBuffer(RenderType.entityCutout(weapon.TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        stackIn.popPose();
    }
}
