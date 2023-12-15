package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.LivingEntity;

public class ModelDragonEgg<T extends LivingEntity> extends AdvancedEntityModel<T> {

    public AdvancedModelBox Egg1;
    public AdvancedModelBox Egg2;
    public AdvancedModelBox Egg3;
    public AdvancedModelBox Egg4;

    public ModelDragonEgg() {
        this.texWidth = 64;
        this.texHeight = 32;
        this.Egg3 = new AdvancedModelBox(this, 0, 0);
        this.Egg3.setPos(0.0F, 0.0F, 0.0F);
        this.Egg3.addBox(-2.5F, -4.6F, -2.5F, 5, 5, 5, 0.0F);
        this.Egg2 = new AdvancedModelBox(this, 22, 2);
        this.Egg2.setPos(0.0F, 0.0F, 0.0F);
        this.Egg2.addBox(-2.5F, -0.6F, -2.5F, 5, 5, 5, 0.0F);
        this.Egg1 = new AdvancedModelBox(this, 0, 12);
        this.Egg1.setPos(0.0F, 19.6F, 0.0F);
        this.Egg1.addBox(-3.0F, -2.8F, -3.0F, 6, 6, 6, 0.0F);
        this.Egg4 = new AdvancedModelBox(this, 28, 16);
        this.Egg4.setPos(0.0F, -0.9F, 0.0F);
        this.Egg4.addBox(-2.0F, -4.8F, -2.0F, 4, 4, 4, 0.0F);
        this.Egg1.addChild(this.Egg3);
        this.Egg1.addChild(this.Egg2);
        this.Egg3.addChild(this.Egg4);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(Egg1);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(Egg1, Egg2, Egg3, Egg4);
    }

    @Override
    public void setupAnim(LivingEntity entity, float f, float f1, float f2, float f3, float f4) {
        this.resetToDefaultPose();
        this.Egg1.setPos(0.0F, 19.6F, 0.0F);
        this.Egg4.setPos(0.0F, -0.9F, 0.0F);
        if (entity instanceof EntityDragonEgg egg) {
            boolean isLocationValid = false;
            if (egg.getEggType().dragonType == DragonType.FIRE) {
                isLocationValid = egg.level().getBlockState(egg.blockPosition()).isBurning(entity.level(), egg.blockPosition());
            } else if (egg.getEggType().dragonType == DragonType.LIGHTNING) {
                isLocationValid = egg.level().isRainingAt(egg.blockPosition());
            }
            if (isLocationValid) {
                this.walk(Egg1, 0.3F, 0.3F, true, 1, 0, f2, 1);
                this.flap(Egg1, 0.3F, 0.3F, false, 0, 0, f2, 1);
            }
        }
    }

    public void renderPodium() {
        Egg1.rotateAngleX = (float) Math.toRadians(-180);

    }

    public void renderFrozen(TileEntityEggInIce tile) {
        this.resetToDefaultPose();
        Egg1.rotateAngleX = (float) Math.toRadians(-180);
        this.walk(Egg1, 0.3F, 0.1F, true, 1, 0, tile.ticksExisted, 1);
        this.flap(Egg1, 0.3F, 0.1F, false, 0, 0, tile.ticksExisted, 1);
    }
}
