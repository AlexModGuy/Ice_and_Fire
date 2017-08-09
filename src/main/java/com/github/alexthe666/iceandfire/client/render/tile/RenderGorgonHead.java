package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelGorgonHead;
import com.github.alexthe666.iceandfire.client.model.ModelGorgonHeadActive;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderGorgonHead extends TileEntitySpecialRenderer {
    private final boolean active;
    private static final ResourceLocation ACTIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/head_active.png");
    private static final ResourceLocation INACTIVE_TEXTURE = new ResourceLocation("iceandfire:textures/models/gorgon/head_inactive.png");
    private static final ModelBase ACTIVE_MODEL = new ModelGorgonHeadActive();
    private static final ModelBase INACTIVE_MODEL = new ModelGorgonHead();
    public RenderGorgonHead(boolean active) {
        this.active = active;
    }

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f, int f1) {
        ModelBase model = active ? ACTIVE_MODEL : INACTIVE_MODEL;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y - 0.75F, (float) z + 0.5F);
        GL11.glPushMatrix();
        this.bindTexture(active ? ACTIVE_TEXTURE : INACTIVE_TEXTURE);
        GL11.glPushMatrix();
        model.render(null, 0, 0, 0, 0, 0, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();

    }

}
