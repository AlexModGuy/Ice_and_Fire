package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.world.entity.LivingEntity;

abstract class ModelDreadBase<T extends LivingEntity & IAnimatedEntity> extends ModelBipedBase<T> {

    ModelDreadBase() {
        super();
    }

    public abstract Animation getSpawnAnimation();

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        setRotationAnglesSpawn(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public void setRotationAnglesSpawn(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn.getAnimation() == getSpawnAnimation()) {
            if (entityIn.getAnimationTick() < 30) {
                this.flap(armRight, 0.5F, 0.5F, false, 2, -0.7F, entityIn.tickCount, 1);
                this.flap(armLeft, 0.5F, 0.5F, true, 2, -0.7F, entityIn.tickCount, 1);
                this.walk(armRight, 0.5F, 0.5F, true, 1, 0, entityIn.tickCount, 1);
                this.walk(armLeft, 0.5F, 0.5F, true, 1, 0, entityIn.tickCount, 1);
            }
        }
    }

    @Override
    public void animate(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animator.update(entity);
        if (animator.setAnimation(getSpawnAnimation())) {
            animator.startKeyframe(0);
            animator.move(this.body, 0, 35, 0);
            rotate(animator, this.armLeft, -180, 0, 0);
            rotate(animator, this.armRight, -180, 0, 0);
            animator.endKeyframe();
            animator.startKeyframe(30);
            animator.move(this.body, 0, 0, 0);
            rotate(animator, this.armLeft, -180, 0, 0);
            rotate(animator, this.armRight, -180, 0, 0);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
    }

}
