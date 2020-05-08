package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadKnight;
import com.github.alexthe666.iceandfire.client.model.ModelDreadLich;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderDreadKnight extends RenderLiving<EntityDreadKnight> {
    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_knight_eyes.png");
    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/dread/dread_knight_1.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/dread/dread_knight_2.png");
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/dread/dread_knight_3.png");

    public RenderDreadKnight(RenderManager renderManager) {
        super(renderManager, new ModelDreadKnight(0.0F, false), 0.6F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
        this.addLayer(new LayerHeldItem(this) {
            protected void translateToHand(HandSide p_191361_1_) {
                ((ModelDreadKnight) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
                if (p_191361_1_ == HandSide.LEFT) {
                    GL11.glTranslatef(-0.05F, 0, 0);
                } else {
                    GL11.glTranslatef(0.05F, 0, 0);
                }
            }
        });
    }

    @Override
    protected void preRenderCallback(EntityDreadKnight entity, float f) {
        GL11.glScalef(0.95F, 0.95F, 0.95F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityDreadKnight entity) {
        switch (entity.getArmorVariant()){
            case 0:
                return TEXTURE_0;
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
                default:
                return TEXTURE_0;
        }
    }
}
