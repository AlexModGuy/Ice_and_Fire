package com.github.alexthe666.iceandfire.client.render.item;

import com.github.alexthe666.iceandfire.client.model.ModelDeathWormGauntlet;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDeathWorm;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.ilexiconn.llibrary.client.util.ItemTESRContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderDeathWormGauntlet {
    private static final ModelDeathWormGauntlet MODEL = new ModelDeathWormGauntlet();

    public void renderItem(ItemStack stack, double x, double y, double z, float f, int f1, float alpha) {
        ResourceLocation texture;

        if (stack.getItem() == IafItemRegistry.deathworm_gauntlet_red) {
            texture = RenderDeathWorm.TEXTURE_RED;
        } else if (stack.getItem() == IafItemRegistry.deathworm_gauntlet_white) {
            texture = RenderDeathWorm.TEXTURE_WHITE;
        } else {
            texture = RenderDeathWorm.TEXTURE_YELLOW;
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GL11.glPushMatrix();
        if (ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND ||
                ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND ||
                ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND ||
                ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
            MODEL.animate(stack, Minecraft.getMinecraft().getRenderPartialTicks());
        } else {
            MODEL.resetToDefaultPose();
        }
        MODEL.render(null, 0, 0, 0, 0, 0, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
