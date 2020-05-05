package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadGhoul;
import com.github.alexthe666.iceandfire.client.model.ModelPixie;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerPixieGlow;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerPixieItem;
import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderDreadGhoul extends RenderLiving<EntityDreadGhoul> {

    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_eyes.png");

    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_closed_1.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_closed_2.png");
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_closed_3.png");
    public static final ResourceLocation TEXTURE_0_MID = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_mid_1.png");
    public static final ResourceLocation TEXTURE_1_MID = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_mid_2.png");
    public static final ResourceLocation TEXTURE_2_MID = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_mid_3.png");
    public static final ResourceLocation TEXTURE_0_OPEN = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_open_1.png");
    public static final ResourceLocation TEXTURE_1_OPEN = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_open_2.png");
    public static final ResourceLocation TEXTURE_2_OPEN = new ResourceLocation("iceandfire:textures/models/dread/dread_ghoul_open_3.png");

    public RenderDreadGhoul(RenderManager renderManager) {
        super(renderManager, new ModelDreadGhoul(), 0.5F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
    }

    @Override
    public void preRenderCallback(EntityDreadGhoul LivingEntityIn, float partialTickTime) {
        GlStateManager.scale(LivingEntityIn.getScale(), LivingEntityIn.getScale(), LivingEntityIn.getScale());
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDreadGhoul ghoul) {
        if(ghoul.getScreamStage() == 2){
            switch (ghoul.getVariant()) {
                default:
                    return TEXTURE_0_OPEN;
                case 1:
                    return TEXTURE_1_OPEN;
                case 2:
                    return TEXTURE_2_OPEN;
            }
        }else if(ghoul.getScreamStage() == 1){
            switch (ghoul.getVariant()) {
                default:
                    return TEXTURE_0_MID;
                case 1:
                    return TEXTURE_1_MID;
                case 2:
                    return TEXTURE_2_MID;
            }
        }else{
            switch (ghoul.getVariant()) {
                default:
                    return TEXTURE_0;
                case 1:
                    return TEXTURE_1;
                case 2:
                    return TEXTURE_2;
            }
        }

    }

}
