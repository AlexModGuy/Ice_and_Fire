package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelStymphalianBird;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderStymphalianBird extends RenderLiving<EntityStymphalianBird> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/stymphalianbird/stymphalian_bird.png");

    public RenderStymphalianBird(RenderManager renderManager) {
        super(renderManager, new ModelStymphalianBird(), 0.6F);
    }

    @Override
    public void preRenderCallback(EntityStymphalianBird entitylivingbaseIn, float partialTickTime) {
        GL11.glScalef(0.75F, 0.75F, 0.75F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityStymphalianBird cyclops) {
        return TEXTURE;
    }

}
