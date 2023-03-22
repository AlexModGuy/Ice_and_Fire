package com.github.alexthe666.iceandfire.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelDeathWormArmor extends ArmorModelBase {
    public ModelPart spineH1;
    public ModelPart spineH2;
    public ModelPart spineH3;
    public ModelPart spineH4;
    public ModelPart spineH5;
    public ModelPart spineH6;
    public ModelPart spineH7;
    public ModelPart spineR1;
    public ModelPart spineR2;
    public ModelPart spineL1;
    public ModelPart spineL2;
    // TODO: Make the inner model and outer model separate/ make them use normal minecraft armor dimensions while still looking good
    private static final ModelPart INNER_MODEL = createMesh(CubeDeformation.NONE, 0.0F).getRoot().bake(64, 64);
    private static final ModelPart OUTER_MODEL = createMesh(CubeDeformation.NONE, 0.0F).getRoot().bake(64, 64);

    public ModelDeathWormArmor(ModelPart modelPart) {
        super(modelPart);
        spineH1 = modelPart.getChild("hat").getChild("spineH1");
        spineH2 = modelPart.getChild("hat").getChild("spineH2");
        spineH3 = modelPart.getChild("hat").getChild("spineH3");
        spineH4 = modelPart.getChild("hat").getChild("spineH4");
        spineH5 = modelPart.getChild("hat").getChild("spineH5");
        spineH6 = modelPart.getChild("hat").getChild("spineH6");
        spineH7 = modelPart.getChild("hat").getChild("spineH7");
        spineR1 = modelPart.getChild("right_arm").getChild("spineR1");
        spineR2 = modelPart.getChild("right_arm").getChild("spineR2");
        spineL1 = modelPart.getChild("left_arm").getChild("spineL1");
        spineL2 = modelPart.getChild("left_arm").getChild("spineL2");
    }

    public static MeshDefinition createMesh(CubeDeformation deformation, float offset) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, offset);
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.getChild("right_arm").addOrReplaceChild("spineR1", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(-1.0F, -2.7F, 0.0F, -0.4914847173616032F, 0.0F, 0.0F));
        partdefinition.getChild("left_arm").addOrReplaceChild("spineL1", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(1.0F, -2.7F, 0.0F, -0.4914847173616032F, 0.0F, 0.0F));

        partdefinition.getChild("right_arm").addOrReplaceChild("spineR2", CubeListBuilder.create().texOffs(32, 40).addBox(-0.6F, -1.7F, -0.5F, 1, 2, 1), PartPose.offsetAndRotation(-2.5F, -1.6F, 0.0F, -0.4914847173616032F, 0.0F, 0.0F));
        partdefinition.getChild("left_arm").addOrReplaceChild("spineL2", CubeListBuilder.create().texOffs(32, 40).addBox(-0.4F, -1.7F, -0.5F, 1, 2, 1), PartPose.offsetAndRotation(2.5F, -1.6F, 0.0F, -0.4914847173616032F, 0.0F, 0.0F));

        partdefinition.getChild("hat").addOrReplaceChild("spineH1", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, -9.0F, -3.0F, -0.4914847173616032F, 0.0F, 0.0F));
        partdefinition.getChild("hat").addOrReplaceChild("spineH2", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, -2.7F, -0.5F, 1, 4, 1), PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.4914847173616032F, 0.0F, 0.0F));
        partdefinition.getChild("hat").addOrReplaceChild("spineH3", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, -9.0F, 3.0F, -0.8651597102135892F, 0.0F, 0.0F));
        partdefinition.getChild("hat").addOrReplaceChild("spineH4", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, -2.7F, -0.5F, 1, 4, 1), PartPose.offsetAndRotation(0.0F, -8.0F, 5.0F, -1.5481070465189704F, 0.0F, 0.0F));
        partdefinition.getChild("hat").addOrReplaceChild("spineH5", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, -6.0F, 5.0F, -1.8212510744560826F, 0.0F, 0.0F));
        partdefinition.getChild("hat").addOrReplaceChild("spineH6", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, -2.7F, -0.5F, 1, 5, 1), PartPose.offsetAndRotation(0.0F, -3.5F, 5.0F, -2.0032889154390916F, 0.0F, 0.0F));
        partdefinition.getChild("hat").addOrReplaceChild("spineH7", CubeListBuilder.create().texOffs(32, 40).addBox(-0.5F, -1.7F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, -1.3F, 4.5F, -2.0032889154390916F, 0.0F, 0.0F));

        return meshdefinition;
    }

    public static ModelPart getBakedModel(boolean inner) {
        return inner ? INNER_MODEL : OUTER_MODEL;
    }

}
