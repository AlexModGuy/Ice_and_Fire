package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelDreadThrall;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerGenericGlowing;
import com.github.alexthe666.iceandfire.entity.EntityDreadThrall;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class RenderDreadThrall extends RenderLiving<EntityDreadThrall> {
    public static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/models/dread/dread_thrall.png");
    public static final ResourceLocation TEXTURE_EYES = new ResourceLocation("iceandfire:textures/models/dread/dread_thrall_eyes.png");
    public static final ResourceLocation TEXTURE_LEG_ARMOR = new ResourceLocation("iceandfire:textures/models/dread/thrall_legs.png");
    public static final ResourceLocation TEXTURE_ARMOR_0 = new ResourceLocation("iceandfire:textures/models/dread/thrall_chest_1.png");
    public static final ResourceLocation TEXTURE_ARMOR_1 = new ResourceLocation("iceandfire:textures/models/dread/thrall_chest_2.png");
    public static final ResourceLocation TEXTURE_ARMOR_2 = new ResourceLocation("iceandfire:textures/models/dread/thrall_chest_3.png");
    public static final ResourceLocation TEXTURE_ARMOR_3 = new ResourceLocation("iceandfire:textures/models/dread/thrall_chest_4.png");
    public static final ResourceLocation TEXTURE_ARMOR_4 = new ResourceLocation("iceandfire:textures/models/dread/thrall_chest_5.png");
    public static final ResourceLocation TEXTURE_ARMOR_5 = new ResourceLocation("iceandfire:textures/models/dread/thrall_chest_6.png");
    public static final ResourceLocation TEXTURE_ARMOR_6 = new ResourceLocation("iceandfire:textures/models/dread/thrall_chest_7.png");
    public static final ResourceLocation TEXTURE_ARMOR_7 = new ResourceLocation("iceandfire:textures/models/dread/thrall_chest_8.png");

    public RenderDreadThrall(RenderManager renderManager) {
        super(renderManager, new ModelDreadThrall(0.0F, false), 0.6F);
        this.addLayer(new LayerGenericGlowing(this, TEXTURE_EYES));
        this.addLayer(new LayerHeldItem(this) {
            protected void translateToHand(HandSide p_191361_1_) {
                ((ModelDreadThrall) this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
                if (p_191361_1_ == HandSide.LEFT) {
                    GL11.glTranslatef(-0.05F, 0, 0);
                } else {
                    GL11.glTranslatef(0.05F, 0, 0);
                }
            }
        });
        this.addLayer(new LayerArmorBase<ModelDreadThrall>(this) {
            ModelDreadThrall modelHead = new ModelDreadThrall(1.0F, true);
            ModelDreadThrall modelBoots = new ModelDreadThrall(1.0F, true);

            protected void initArmor() {
                this.modelLeggings = new ModelDreadThrall(0.5F, true);
                this.modelArmor = new ModelDreadThrall(1.0F, true);
            }

            public ModelDreadThrall getModelFromSlot(EquipmentSlotType slotIn) {
                switch (slotIn) {
                    case HEAD:
                        return modelHead;
                    case CHEST:
                        return modelArmor;
                    case LEGS:
                        return modelLeggings;
                    case FEET:
                        return modelBoots;
                }
                return modelArmor;
            }

            @Override
            protected void setModelSlotVisible(ModelDreadThrall model, EquipmentSlotType slotIn) {
                this.setModelVisible(model);
                switch (slotIn) {
                    case HEAD:
                        model.bipedHead.invisible = false;
                        model.bipedHeadwear.invisible = false;
                        break;
                    case CHEST:
                        model.bipedBody.invisible = false;
                        model.bipedRightArm.invisible = false;
                        model.bipedLeftArm.invisible = false;
                        break;
                    case LEGS:
                        model.bipedBody.invisible = false;
                        model.bipedRightLeg.invisible = false;
                        model.bipedLeftLeg.invisible = false;
                        break;
                    case FEET:
                        model.bipedRightLeg.invisible = false;
                        model.bipedLeftLeg.invisible = false;
                }
            }

            protected void setModelVisible(ModelDreadThrall model) {
                model.setVisible(false);
            }

            @Override
            public ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EquipmentSlotType slot, String type){
                if(entity instanceof EntityDreadThrall){
                    EntityDreadThrall dreadThrall = (EntityDreadThrall)entity;
                    if(dreadThrall.hasCustomArmorHead() && slot == EquipmentSlotType.HEAD){
                        return getArmorTexture(dreadThrall.getArmorVariant());
                    }
                    if(dreadThrall.hasCustomArmorChest() && slot == EquipmentSlotType.CHEST){
                        return getArmorTexture(dreadThrall.getArmorVariant());
                    }
                    if(dreadThrall.hasCustomArmorLegs() && slot == EquipmentSlotType.LEGS){
                        return RenderDreadThrall.TEXTURE_LEG_ARMOR;
                    }
                    if(dreadThrall.hasCustomArmorFeet() && slot == EquipmentSlotType.FEET){
                        return getArmorTexture(dreadThrall.getArmorVariant());
                    }
                }
                return super.getArmorResource(entity, stack, slot, type);
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

    private static ResourceLocation getArmorTexture(int index){
        switch (index){
            case 0:
                return TEXTURE_ARMOR_0;
            case 1:
                return TEXTURE_ARMOR_1;
            case 2:
                return TEXTURE_ARMOR_2;
            case 3:
                return TEXTURE_ARMOR_3;
            case 4:
                return TEXTURE_ARMOR_4;
            case 5:
                return TEXTURE_ARMOR_5;
            case 6:
                return TEXTURE_ARMOR_6;
            case 7:
                return TEXTURE_ARMOR_7;
        }
        return TEXTURE_ARMOR_0;
    }
}
