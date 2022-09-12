package com.github.alexthe666.iceandfire.client.render.entity.layer;

import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.client.model.ModelBipedBase;
import com.github.alexthe666.iceandfire.entity.util.IHasArmorVariant;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class LayerBipedArmorMultiple<R extends MobRenderer & IHasArmorVariantResource,
    T extends LivingEntity & IHasArmorVariant & IAnimatedEntity,
    M extends ModelBipedBase<T>,
    A extends ModelBipedBase<T>> extends LayerBipedArmor<T, M, A> {

    R mobRenderer;

    public LayerBipedArmorMultiple(R mobRenderer, A modelLeggings, A modelArmor,
                                   ResourceLocation defaultArmor, ResourceLocation defaultLegArmor) {
        super(mobRenderer, modelLeggings, modelArmor, defaultArmor, defaultLegArmor);
        this.mobRenderer = mobRenderer;
    }

    @Override
    public ResourceLocation getArmorResource(T entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        return this.mobRenderer.getArmorResource(entity.getBodyArmorVariant(), slot);
    }
}
