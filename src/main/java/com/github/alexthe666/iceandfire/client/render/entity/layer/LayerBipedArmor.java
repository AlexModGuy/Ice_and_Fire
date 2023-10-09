package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.client.model.ModelBipedBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

//TODO: Consider support for default minecraft armors/ dynamically selecting custom armors

//Base code from minecrafts ArmorBipedLayer

public class LayerBipedArmor<T extends LivingEntity & IAnimatedEntity,
    M extends ModelBipedBase<T>,
    A extends ModelBipedBase<T>> extends RenderLayer<T, M> {

    private final A modelLeggings;
    private final A modelArmor;
    private final ResourceLocation defaultLegArmor;
    private final ResourceLocation defaultArmor;

    public LayerBipedArmor(RenderLayerParent<T, M> mobRenderer, A modelLeggings, A modelArmor, ResourceLocation defaultArmor, ResourceLocation defaultLegArmor) {
        super(mobRenderer);
        this.modelLeggings = modelLeggings;
        this.modelArmor = modelArmor;
        this.defaultLegArmor = defaultLegArmor;
        this.defaultArmor = defaultArmor;
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.renderEquipment(matrixStackIn, bufferIn, entitylivingbaseIn, EquipmentSlot.CHEST, packedLightIn, this.getSlotModel(EquipmentSlot.CHEST));
        this.renderEquipment(matrixStackIn, bufferIn, entitylivingbaseIn, EquipmentSlot.LEGS, packedLightIn, this.getSlotModel(EquipmentSlot.LEGS));
        this.renderEquipment(matrixStackIn, bufferIn, entitylivingbaseIn, EquipmentSlot.FEET, packedLightIn, this.getSlotModel(EquipmentSlot.FEET));
        this.renderEquipment(matrixStackIn, bufferIn, entitylivingbaseIn, EquipmentSlot.HEAD, packedLightIn, this.getSlotModel(EquipmentSlot.HEAD));
    }

    private void renderEquipment(PoseStack matrixStackIn, MultiBufferSource bufferIn, T entityIn, EquipmentSlot slotType, int packedLightIn, A modelIn) {
        ItemStack itemstack = entityIn.getItemBySlot(slotType);
        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem) itemstack.getItem();
            if (armoritem.getEquipmentSlot() == slotType) {
                this.getParentModel().setModelAttributes(modelIn);
                this.setModelSlotVisible(modelIn, slotType);
                boolean flag1 = itemstack.hasFoil();
                this.renderArmorItem(matrixStackIn, bufferIn, packedLightIn, flag1, modelIn, 1.0F, 1.0F, 1.0F, this.getArmorResource(entityIn, itemstack, slotType, null));
            }
        }
    }

    protected void setModelSlotVisible(A modelIn, EquipmentSlot slotIn) {
        modelIn.setVisible(false);
        switch (slotIn) {
            case HEAD:
                modelIn.head.invisible = false;
                modelIn.headware.invisible = false;
                break;
            case CHEST:
                modelIn.body.invisible = false;
                modelIn.armRight.invisible = false;
                modelIn.armLeft.invisible = false;
                break;
            case LEGS:
                modelIn.body.invisible = false;
                modelIn.legRight.invisible = false;
                modelIn.legLeft.invisible = false;
                break;
            case FEET:
                modelIn.legRight.invisible = false;
                modelIn.legLeft.invisible = false;
        }
    }

    private void renderArmorItem(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean p_241738_5_, A modelIn, float red, float green, float blue, ResourceLocation armorResource) {
        VertexConsumer ivertexbuilder = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(armorResource), false, p_241738_5_);
        modelIn.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    private A getSlotModel(EquipmentSlot equipmentSlotType) {
        return this.isLegSlot(equipmentSlotType) ? this.modelLeggings : this.modelArmor;
    }

    protected boolean isLegSlot(EquipmentSlot slotIn) {
        return slotIn == EquipmentSlot.LEGS;
    }

    public ResourceLocation getArmorResource(T entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        if (isLegSlot(slot))
            return defaultLegArmor;
        return defaultArmor;
    }

}
