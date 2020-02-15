package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadLich;
import com.github.alexthe666.iceandfire.client.model.ModelDreadThrall;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadLich;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderDreadLich extends RenderLiving<EntityDreadLich> {
    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_eyes.png");
    public static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_0.png");
    public static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_1.png");
    public static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_2.png");
    public static final ResourceLocation TEXTURE_3 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_3.png");
    public static final ResourceLocation TEXTURE_4 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_4.png");
    public static final ResourceLocation TEXTURE_5 = new ResourceLocation("iceandfire:textures/models/dread/dread_lich_5.png");

    public RenderDreadLich(RenderManager renderManager) {
        super(renderManager, new ModelDreadLich(0.0F, false), 0.6F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
        this.addLayer(new LayerHeldItem(this) {
            protected void translateToHand(EnumHandSide p_191361_1_) {
                ((ModelDreadLich) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
                if (p_191361_1_ == EnumHandSide.LEFT) {
                    GL11.glTranslatef(-0.05F, 0, 0);
                } else {
                    GL11.glTranslatef(0.05F, 0, 0);
                }
            }
        });
    }

    @Override
    protected void preRenderCallback(EntityDreadLich entity, float f) {
        GL11.glScalef(0.95F, 0.95F, 0.95F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityDreadLich entity) {
        switch (entity.getVariant()){
            default:
                return TEXTURE_0;
            case 1:
                return TEXTURE_1;
            case 2:
                return TEXTURE_2;
            case 3:
                return TEXTURE_3;
            case 4:
                return TEXTURE_4;
            case 5:
                return TEXTURE_5;
        }
    }
}
