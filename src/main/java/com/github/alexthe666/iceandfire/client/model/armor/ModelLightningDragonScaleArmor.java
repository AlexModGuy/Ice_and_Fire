package com.github.alexthe666.iceandfire.client.model.armor;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelLightningDragonScaleArmor extends ArmorModelBase {
    // TODO: Make the inner model and outer model separate/ make them use normal minecraft armor dimensions while still looking good
    private static final ModelPart INNER_MODEL = createMesh(CubeDeformation.NONE, 0.0F).getRoot().bake(64, 64);
    private static final ModelPart OUTER_MODEL = createMesh(CubeDeformation.NONE, 0.0F).getRoot().bake(64, 64);
    public AdvancedModelBox HornL;
    public AdvancedModelBox HornR;
    public AdvancedModelBox HornL3;
    public AdvancedModelBox HornR3;
    public AdvancedModelBox Jaw;
    public AdvancedModelBox HornR4;
    public AdvancedModelBox HornL4;
    public AdvancedModelBox HeadFront;
    public AdvancedModelBox HornL2;
    public AdvancedModelBox HornR2;
    public AdvancedModelBox Teeth4;
    public AdvancedModelBox Teeth3;
    public AdvancedModelBox Teeth1;
    public AdvancedModelBox Teeth2;
    public AdvancedModelBox RightShoulderSpike1;
    public AdvancedModelBox RightShoulderSpike2;
    public AdvancedModelBox LeftLegSpike;
    public AdvancedModelBox LeftLegSpike2;
    public AdvancedModelBox LeftLegSpike3;
    public AdvancedModelBox BackSpike1;
    public AdvancedModelBox BackSpike2;
    public AdvancedModelBox BackSpike3;
    public AdvancedModelBox LeftShoulderSpike1;
    public AdvancedModelBox LeftShoulderSpike2;
    public AdvancedModelBox RightLegSpike;
    public AdvancedModelBox RightLegSpike2;
    public AdvancedModelBox RightLegSpike3;


    public ModelLightningDragonScaleArmor(ModelPart modelPart) {
        super(modelPart);
    }

    public ModelLightningDragonScaleArmor(boolean inner) {
        super(getBakedModel(inner));
    }

    public static MeshDefinition createMesh(CubeDeformation deformation, float offset) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, offset);
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.getChild("right_leg").addOrReplaceChild("RightLegSpike3", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1), PartPose.offsetAndRotation(-0.8F, 0.0F, -0.8F, -1.2217304763960306F, 1.2217304763960306F, -0.17453292519943295F));
        partdefinition.getChild("right_leg").addOrReplaceChild("RightLegSpike2", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1), PartPose.offsetAndRotation(-0.7F, 3.6F, -0.4F, -1.4114477660878142F, 0.0F, 0.0F));
        partdefinition.getChild("right_leg").addOrReplaceChild("RightLegSpike", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, 5.0F, 0.4F, -1.4114477660878142F, 0.0F, 0.0F));
        partdefinition.getChild("left_leg").addOrReplaceChild("LeftLegSpike3", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1), PartPose.offsetAndRotation(0.8F, 0.0F, -0.8F, -1.2217304763960306F, -1.2217304763960306F, 0.17453292519943295F));
        partdefinition.getChild("left_leg").addOrReplaceChild("LeftLegSpike2", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1), PartPose.offsetAndRotation(0.7F, 3.6F, -0.4F, -1.4114477660878142F, 0.0F, 0.0F));
        partdefinition.getChild("left_leg").addOrReplaceChild("LeftLegSpike", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, 0.0F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, 5.0F, 0.4F, -1.4114477660878142F, 0.0F, 0.0F));

        partdefinition.getChild("head").addOrReplaceChild("HornR", CubeListBuilder.create().texOffs(48, 44).addBox(-1.0F, -0.5F, 0.0F, 2, 3, 5), PartPose.offsetAndRotation(-3.6F, -8.0F, 0.0F, 0.4363323129985824F, -0.33161255787892263F, -0.19198621771937624F));
        partdefinition.getChild("head").addOrReplaceChild("HornL", CubeListBuilder.create().texOffs(48, 44).mirror().addBox(-1.0F, -0.5F, 0.0F, 2, 3, 5), PartPose.offsetAndRotation(3.6F, -8.0F, 0.0F, 0.4363323129985824F, 0.33161255787892263F, 0.19198621771937624F));

        partdefinition.getChild("head").addOrReplaceChild("HornR3", CubeListBuilder.create().texOffs(47, 37).mirror().addBox(-0.5F, -0.8F, 0.0F, 1, 2, 4), PartPose.offsetAndRotation(-4.0F, -3.0F, 0.7F, -0.06981317007977318F, -0.4886921905584123F, -0.08726646259971647F));
        partdefinition.getChild("head").addOrReplaceChild("HornL3", CubeListBuilder.create().texOffs(47, 37).mirror().addBox(-0.5F, -0.8F, 0.0F, 1, 2, 4), PartPose.offsetAndRotation(4.0F, -3.0F, 0.7F, -0.06981317007977318F, 0.4886921905584123F, 0.08726646259971647F));

        partdefinition.getChild("head").addOrReplaceChild("HeadFront", CubeListBuilder.create().texOffs(6, 44).addBox(-3.5F, -2.8F, -8.8F, 7, 2, 5), PartPose.offsetAndRotation(0.0F, -5.6F, 0.0F, 0.045553093477052F, 0.0F, 0.0F));
        partdefinition.getChild("head").addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(6, 51).addBox(-3.5F, 4.0F, -7.4F, 7, 2, 5), PartPose.offsetAndRotation(0.0F, -5.4F, 0.0F, -0.091106186954104F, 0.0F, 0.0F));

        partdefinition.getChild("head").addOrReplaceChild("HornR4", CubeListBuilder.create().texOffs(46, 36).mirror().addBox(-0.5F, -0.8F, 0.0F, 1, 2, 5), PartPose.offsetAndRotation(-4.0F, -5.1F, 0.1F, 0.12217304763960307F, -0.3141592653589793F, -0.03490658503988659F));
        partdefinition.getChild("head").addOrReplaceChild("HornL4", CubeListBuilder.create().texOffs(46, 36).mirror().addBox(-0.5F, -0.8F, 0.0F, 1, 2, 5), PartPose.offsetAndRotation(4.0F, -5.1F, -0.1F, 0.12217304763960307F, 0.3141592653589793F, 0.03490658503988659F));

        partdefinition.getChild("head").addOrReplaceChild("Teeth1", CubeListBuilder.create().texOffs(6, 34).addBox(-3.6F, 0.1F, -8.9F, 4, 1, 5), PartPose.offset(0.0F, -6.2F, 0.0F));

        partdefinition.getChild("right_arm").addOrReplaceChild("RightShoulderSpike1", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(-0.5F, -1.2F, 0.0F, -3.141592653589793F, 0.0F, -0.17453292519943295F));
        partdefinition.getChild("left_arm").addOrReplaceChild("LeftShoulderSpike1", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.5F, -1.2F, 0.0F, -3.141592653589793F, 0.0F, 0.17453292519943295F));

        partdefinition.getChild("right_arm").addOrReplaceChild("RightShoulderSpike2", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(-1.8F, -0.1F, 0.0F, -3.141592653589793F, 0.0F, -0.2617993877991494F));
        partdefinition.getChild("left_arm").addOrReplaceChild("LeftShoulderSpike2", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(1.8F, -0.1F, 0.0F, -3.141592653589793F, 0.0F, 0.2617993877991494F));

        partdefinition.getChild("body").addOrReplaceChild("BackSpike1", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, 0.9F, 0.2F, 1.1838568316277536F, 0.0F, 0.0F));
        partdefinition.getChild("body").addOrReplaceChild("BackSpike2", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, 3.5F, 0.6F, 1.1838568316277536F, 0.0F, 0.0F));
        partdefinition.getChild("body").addOrReplaceChild("BackSpike3", CubeListBuilder.create().texOffs(0, 34).addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1), PartPose.offsetAndRotation(0.0F, 6.4F, 0.0F, 1.1838568316277536F, 0.0F, 0.0F));

        partdefinition.getChild("head").getChild("HornR").addOrReplaceChild("HornR2", CubeListBuilder.create().texOffs(46, 36).mirror().addBox(-0.5F, -0.8F, 0.0F, 1, 2, 5), PartPose.offsetAndRotation(0.0F, 0.3F, 4.5F, -0.07504915783575616F, 0.5009094953223726F, 0.0F));
        partdefinition.getChild("head").getChild("HornL").addOrReplaceChild("HornL2", CubeListBuilder.create().texOffs(46, 36).mirror().addBox(-0.5F, -0.8F, 0.0F, 1, 2, 5), PartPose.offsetAndRotation(0.0F, 0.3F, 4.5F, -0.07504915783575616F, -0.5009094953223726F, 0.0F));

        partdefinition.getChild("head").getChild("HeadFront").addOrReplaceChild("Teeth2", CubeListBuilder.create().texOffs(6, 34).mirror().addBox(-0.4F, 0.1F, -8.9F, 4, 1, 5), PartPose.offset(0.0F, -1.0F, 0.0F));

        partdefinition.getChild("head").getChild("Jaw").addOrReplaceChild("Teeth3", CubeListBuilder.create().texOffs(6, 34).addBox(-3.6F, 0.1F, -8.9F, 4, 1, 5), PartPose.offset(0.0F, 3.0F, 1.4F));
        partdefinition.getChild("head").getChild("Jaw").addOrReplaceChild("Teeth4", CubeListBuilder.create().texOffs(6, 34).mirror().addBox(-0.4F, 0.1F, -8.9F, 4, 1, 5), PartPose.offset(0.0F, 3.0F, 1.4F));

        return meshdefinition;
    }

    public static ModelPart getBakedModel(boolean inner) {
        return inner ? INNER_MODEL : OUTER_MODEL;
    }

}