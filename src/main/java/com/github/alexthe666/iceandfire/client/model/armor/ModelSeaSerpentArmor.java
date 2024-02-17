package com.github.alexthe666.iceandfire.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ModelSeaSerpentArmor extends ArmorModelBase {
    private static final ModelPart INNER_MODEL = createMesh(CubeDeformation.NONE.extend(INNER_MODEL_OFFSET), 0.0F).getRoot().bake(64, 64);
    private static final ModelPart OUTER_MODEL = createMesh(CubeDeformation.NONE.extend(OUTER_MODEL_OFFSET), 0.0F).getRoot().bake(64, 64);

    public ModelSeaSerpentArmor(boolean inner) {
        super(getBakedModel(inner));
    }

    public static MeshDefinition createMesh(CubeDeformation deformation, float offset) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(deformation, offset);
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.getChild("head").addOrReplaceChild("headFin", CubeListBuilder.create().texOffs(0, 32).addBox(-0.5F, -8.4F, -7.9F, 1, 16, 14), PartPose.offsetAndRotation(-3.5F, -8.8F, 3.5F, 3.141592653589793F, -0.5235987755982988F, 0.0F));
        partdefinition.getChild("head").addOrReplaceChild("headFin2", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-0.5F, -8.4F, -7.9F, 1, 16, 14), PartPose.offsetAndRotation(3.5F, -8.8F, 3.5F, 3.141592653589793F, 0.5235987755982988F, 0.0F));

        partdefinition.getChild("right_arm").addOrReplaceChild("armFinR", CubeListBuilder.create().texOffs(30, 32).addBox(-0.5F, -5.4F, -6.0F, 1, 7, 5), PartPose.offsetAndRotation(-1.5F, 4.0F, -0.4F, 3.141592653589793F, -1.3089969389957472F, -0.003490658503988659F));
        partdefinition.getChild("left_arm").addOrReplaceChild("armFinL", CubeListBuilder.create().texOffs(30, 32).mirror().addBox(-0.5F, -5.4F, -6.0F, 1, 7, 5), PartPose.offsetAndRotation(1.5F, 4.0F, -0.4F, 3.141592653589793F, 1.3089969389957472F, 0.0F));

        partdefinition.getChild("right_leg").addOrReplaceChild("legFinR", CubeListBuilder.create().texOffs(45, 31).addBox(-0.5F, -5.4F, -6.0F, 1, 7, 6), PartPose.offsetAndRotation(-1.5F, 5.2F, 1.6F, 3.141592653589793F, -1.3089969389957472F, 0.0F));
        partdefinition.getChild("left_leg").addOrReplaceChild("legFinL", CubeListBuilder.create().texOffs(45, 31).mirror().addBox(-0.5F, -5.4F, -6.0F, 1, 7, 6), PartPose.offsetAndRotation(1.5F, 5.2F, 1.6F, 3.141592653589793F, 1.3089969389957472F, 0.0F));

        partdefinition.getChild("right_arm").addOrReplaceChild("shoulderR", CubeListBuilder.create().texOffs(38, 46).addBox(-3.5F, -2.0F, -2.5F, 5, 12, 5), PartPose.offset(0.0F, -0.5F, 0.0F));
        partdefinition.getChild("left_arm").addOrReplaceChild("shoulderL", CubeListBuilder.create().texOffs(38, 46).mirror().addBox(-1.5F, -2.0F, -2.5F, 5, 12, 5), PartPose.offset(0.0F, -0.5F, 0.0F));

        return meshdefinition;
    }

    public static ModelPart getBakedModel(boolean inner) {
        return inner ? INNER_MODEL : OUTER_MODEL;
    }
}
