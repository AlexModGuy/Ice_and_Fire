package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelSiren;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderSiren extends RenderLiving<EntitySiren> {

    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/siren/siren_0.png");
    public static final ResourceLocation TEXTURE_0_AGGRESSIVE = new ResourceLocation("iceandfire:textures/models/siren/siren_0_aggressive.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/siren/siren_1.png");
    public static final ResourceLocation TEXTURE_1_AGGRESSIVE = new ResourceLocation("iceandfire:textures/models/siren/siren_1_aggressive.png");
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/siren/siren_2.png");
    public static final ResourceLocation TEXTURE_2_AGGRESSIVE = new ResourceLocation("iceandfire:textures/models/siren/siren_2_aggressive.png");

    public RenderSiren(RenderManager renderManager) {
        super(renderManager, new ModelSiren(), 0.8F);
    }

    @Override
    public void preRenderCallback(EntitySiren LivingEntityIn, float partialTickTime) {
        GL11.glTranslatef(0, 0, -0.5F);

    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySiren siren) {
        switch (siren.getHairColor()) {
            default:
                return siren.isAgressive() ? TEXTURE_0_AGGRESSIVE : TEXTURE_0;
            case 1:
                return siren.isAgressive() ? TEXTURE_1_AGGRESSIVE : TEXTURE_1;
            case 2:
                return siren.isAgressive() ? TEXTURE_2_AGGRESSIVE : TEXTURE_2;
        }
    }

}
