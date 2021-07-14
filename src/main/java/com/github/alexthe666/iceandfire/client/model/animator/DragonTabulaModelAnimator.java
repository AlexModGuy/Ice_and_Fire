package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.client.model.util.DragonAnimationsLibrary;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonAnimations;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonModelTypes;
import com.github.alexthe666.iceandfire.client.model.util.EnumDragonPoses;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;

public abstract class DragonTabulaModelAnimator<T extends EntityDragonBase> extends IceAndFireTabulaModelAnimator implements ITabulaModelAnimator<T> {

    protected TabulaModel[] walkPoses;
    protected TabulaModel[] flyPoses;
    protected TabulaModel[] swimPoses;
    protected AdvancedModelBox[] neckParts;
    protected AdvancedModelBox[] tailParts;
    protected AdvancedModelBox[] tailPartsWBody;
    protected AdvancedModelBox[] toesPartsL;
    protected AdvancedModelBox[] toesPartsR;
    protected AdvancedModelBox[] clawL;
    protected AdvancedModelBox[] clawR;
    protected ModelAnimator bakedAnimation;

    public DragonTabulaModelAnimator(TabulaModel baseModel) {
        super(baseModel);
    }

    public void init(TabulaModel model) {
        neckParts = new AdvancedModelBox[]{model.getCube("Neck1"), model.getCube("Neck2"), model.getCube("Neck3"), model.getCube("Neck3"), model.getCube("Head")};
        tailParts = new AdvancedModelBox[]{model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        tailPartsWBody = new AdvancedModelBox[]{model.getCube("BodyLower"), model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        toesPartsL = new AdvancedModelBox[]{model.getCube("ToeL1"), model.getCube("ToeL2"), model.getCube("ToeL3")};
        toesPartsR = new AdvancedModelBox[]{model.getCube("ToeR1"), model.getCube("ToeR2"), model.getCube("ToeR3")};
        clawL = new AdvancedModelBox[]{model.getCube("ClawL")};
        clawR = new AdvancedModelBox[]{model.getCube("ClawR")};
    }

    @Override
    public void setRotationAngles(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {

    }

    protected boolean isWing(TabulaModel model, AdvancedModelBox modelRenderer) {
        return model.getCube("armL1") == modelRenderer || model.getCube("armR1") == modelRenderer || model.getCube("armL1").childModels.contains(modelRenderer) || model.getCube("armR1").childModels.contains(modelRenderer);
    }

    protected boolean isHorn(AdvancedModelBox modelRenderer) {
        return modelRenderer.boxName.contains("Horn");
    }


    public void animate(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        if(bakedAnimation == null)
            bakeAnimation(model, entity, limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale);
        else
            model.llibAnimator = bakedAnimation;
    }

    protected void genderMob(T entity, AdvancedModelBox cube) {
        if (!entity.isMale()) {
            TabulaModel maleModel = getModel(EnumDragonPoses.MALE);
            TabulaModel femaleModel = getModel(EnumDragonPoses.FEMALE);
            AdvancedModelBox femaleModelCube = femaleModel.getCube(cube.boxName);
            AdvancedModelBox maleModelCube = maleModel.getCube(cube.boxName);
            float x = femaleModelCube.rotateAngleX;
            float y = femaleModelCube.rotateAngleY;
            float z = femaleModelCube.rotateAngleZ;
            if (x != maleModelCube.rotateAngleX || y != maleModelCube.rotateAngleY || z != maleModelCube.rotateAngleZ) {
                this.setRotateAngle(cube, 1F, x, y, z);
            }
        }
    }

    protected abstract TabulaModel getModel(EnumDragonPoses pose);

    protected abstract void bakeAnimation(TabulaModel model, T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale);
}
