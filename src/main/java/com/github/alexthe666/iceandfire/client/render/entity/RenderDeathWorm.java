package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderDeathWorm extends RenderLiving<EntityDeathWorm> {
    public static final ResourceLocation TEXTURE_WHITE = new ResourceLocation("iceandfire:textures/models/deathworm/deathworm_red.png");
    public static final ResourceLocation TEXTURE_RED = new ResourceLocation("iceandfire:textures/models/deathworm/deathworm_white.png");
    public static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation("iceandfire:textures/models/deathworm/deathworm_yellow.png");

    public RenderDeathWorm(RenderManager renderManager) {
        super(renderManager, new ModelDeathWorm(), 0);
    }

    @Override
    protected void preRenderCallback(EntityDeathWorm entity, float f) {
        this.shadowSize = entity.getScale() / 3;
        GL11.glScalef(entity.getScale(), entity.getScale(), entity.getScale());
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityDeathWorm entity) {
        return entity.getVariant() == 2 ? TEXTURE_RED : entity.getVariant() == 1 ? TEXTURE_WHITE : TEXTURE_YELLOW;
    }
}
