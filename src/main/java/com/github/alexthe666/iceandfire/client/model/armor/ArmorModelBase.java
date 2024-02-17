package com.github.alexthe666.iceandfire.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.jetbrains.annotations.NotNull;


public class ArmorModelBase extends HumanoidModel<LivingEntity> {
    protected static float INNER_MODEL_OFFSET = 0.38F;
    protected static float OUTER_MODEL_OFFSET = 0.45F;
    public ArmorModelBase(ModelPart p_170677_) {
        super(p_170677_);
    }

    @Override
    public void setupAnim(@NotNull LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn instanceof ArmorStand armorStand) {
            this.head.xRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getX();
            this.head.yRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getY();
            this.head.zRot = ((float) Math.PI / 180F) * armorStand.getHeadPose().getZ();
            this.body.xRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getX();
            this.body.yRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getY();
            this.body.zRot = ((float) Math.PI / 180F) * armorStand.getBodyPose().getZ();
            this.leftArm.xRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getX();
            this.leftArm.yRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getY();
            this.leftArm.zRot = ((float) Math.PI / 180F) * armorStand.getLeftArmPose().getZ();
            this.rightArm.xRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getX();
            this.rightArm.yRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getY();
            this.rightArm.zRot = ((float) Math.PI / 180F) * armorStand.getRightArmPose().getZ();
            this.leftLeg.xRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getX();
            this.leftLeg.yRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getY();
            this.leftLeg.zRot = ((float) Math.PI / 180F) * armorStand.getLeftLegPose().getZ();
            this.rightLeg.xRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getX();
            this.rightLeg.yRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getY();
            this.rightLeg.zRot = ((float) Math.PI / 180F) * armorStand.getRightLegPose().getZ();
            this.hat.copyFrom(this.head);
        } else {
            super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }

    //this.(?<name>.*).addChild\(this.(?<name2>.*)\);
    //partdefinition.getChild("${name}").addOrReplaceChild("${name2},

    //this.(?<name>.*) = new AdvancedModelBox\(.*, (?<texX>[0-9]*), (?<texY>[0-9]*)\);
    //.addOrReplaceChild("${name}", CubeListBuilder.create().texOffs(${texX}, ${texY})

    //this.(?<name>.*).setPos\((?<x>.*), (?<y>.*), (?<z>.*)\);
    //PartPose.offsetAndRotation(${x}, ${y}, ${z},

    //this.(?<name>.*).addBox\((?<x>.*), (?<y>.*), (?<z>.*), (?<u>.*), (?<v>.*), (?<w>.*), 0.0F\);
    //.addBox(${x}, ${y}, ${z}, ${u}, ${v}, ${w})

    //(?<main>.addOrReplaceChild\("(?<name>.*)", Cube.*)\n.*(?<part>PartPose.*)\n.*(?<box>addBox.*)\n.*this.setRotateAngle\(.*\k<name>.*, (?<aX>.*), (?<aY>.*), (?<aZ>.*)\);
    //${main}.${box}, ${part}${aX}, ${aY}, ${aZ}));

    //(?<main>.addOrReplaceChild\("(?<name>.*)", Cube.*)\n.*(?<part>PartPose.*)\n.*(?<box>addBox.*\));\n
    //${main}.${box}, ${part});
}
