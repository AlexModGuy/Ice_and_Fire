package com.github.alexthe666.iceandfire.client.model.util;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author gegy1000
 * @since 1.0.0
 */
@SideOnly(Side.CLIENT)
@FunctionalInterface
public interface IIceAndFireTabulaModelAnimator<T extends Entity> {
    void setRotationAngles(IceAndFireTabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale);
}