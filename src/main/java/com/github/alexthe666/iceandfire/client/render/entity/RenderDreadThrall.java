package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDeathWorm;
import com.github.alexthe666.iceandfire.client.model.ModelDreadThrall;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderDreadThrall extends RenderLiving<EntityDreadThrall> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/dread_thrall.png");
    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_thrall_eyes.png");

    public RenderDreadThrall(RenderManager renderManager) {
        super(renderManager, new ModelDreadThrall(0.0F, false), 0.6F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
        this.addLayer(new LayerHeldItem(this) {
            protected void translateToHand(EnumHandSide p_191361_1_) {
                ((ModelDreadThrall) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
            }
        });
        this.addLayer(new LayerArmorBase<ModelDreadThrall>(this) {
            protected void initArmor() {
                this.modelLeggings = new ModelDreadThrall(0.5F, true);
                this.modelArmor = new ModelDreadThrall(1.0F, true);
            }

            @Override
            protected void setModelSlotVisible(ModelDreadThrall p_188359_1_, EntityEquipmentSlot slotIn) {
                this.setModelVisible(p_188359_1_);

                switch (slotIn) {
                    case HEAD:
                        p_188359_1_.bipedHead.showModel = true;
                        p_188359_1_.bipedHeadwear.showModel = true;
                        break;
                    case CHEST:
                        p_188359_1_.bipedBody.showModel = true;
                        p_188359_1_.bipedRightArm.showModel = true;
                        p_188359_1_.bipedLeftArm.showModel = true;
                        break;
                    case LEGS:
                        p_188359_1_.bipedBody.showModel = true;
                        p_188359_1_.bipedRightLeg.showModel = true;
                        p_188359_1_.bipedLeftLeg.showModel = true;
                        break;
                    case FEET:
                        p_188359_1_.bipedRightLeg.showModel = true;
                        p_188359_1_.bipedLeftLeg.showModel = true;
                }
            }

            protected void setModelVisible(ModelDreadThrall model) {
                model.setVisible(false);
            }
        });
    }

    @Override
    protected void preRenderCallback(EntityDreadThrall entity, float f) {
        GL11.glScalef(0.95F, 0.95F, 0.95F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityDreadThrall entity) {
        return TEXTURE;
    }
}
