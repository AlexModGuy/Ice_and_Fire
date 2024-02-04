package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ModelDeathWormGauntlet extends ModelDragonBase {
    public AdvancedModelBox Head;
    public AdvancedModelBox JawExtender;
    public AdvancedModelBox HeadInner;
    public AdvancedModelBox ToothB;
    public AdvancedModelBox ToothT;
    public AdvancedModelBox ToothL;
    public AdvancedModelBox ToothL_1;
    public AdvancedModelBox JawExtender2;
    public AdvancedModelBox JawExtender3;
    public AdvancedModelBox JawExtender4;
    public AdvancedModelBox TopJaw;
    public AdvancedModelBox BottomJaw;
    public AdvancedModelBox JawHook;

    public ModelDeathWormGauntlet() {
        this.texWidth = 128;
        this.texHeight = 64;
        this.Head = new AdvancedModelBox(this, 0, 29);
        this.Head.setPos(0.0F, 0.0F, 1.5F);
        this.Head.addBox(-5.0F, -5.0F, -8.0F, 10, 10, 8, 0.0F);
        this.TopJaw = new AdvancedModelBox(this, 19, 7);
        this.TopJaw.setPos(0.0F, -0.2F, -11.4F);
        this.TopJaw.addBox(-2.0F, -1.5F, -6.4F, 4, 2, 6, 0.0F);
        this.setRotateAngle(TopJaw, 0.091106186954104F, 0.0F, 0.0F);
        this.JawHook = new AdvancedModelBox(this, 0, 7);
        this.JawHook.setPos(0.0F, -0.3F, -6.0F);
        this.JawHook.addBox(-0.5F, -0.7F, -2.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(JawHook, 1.730144887501979F, 0.0F, 0.0F);
        this.ToothL = new AdvancedModelBox(this, 52, 34);
        this.ToothL.setPos(4.5F, 0.0F, -7.5F);
        this.ToothL.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(ToothL, -3.141592653589793F, 0.3490658503988659F, 0.0F);
        this.ToothB = new AdvancedModelBox(this, 52, 34);
        this.ToothB.setPos(0.0F, 4.5F, -7.5F);
        this.ToothB.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(ToothB, 2.7930504019665254F, -0.0F, 0.0F);
        this.JawExtender = new AdvancedModelBox(this, 0, 7);
        this.JawExtender.setPos(0.0F, 0.0F, 10.0F);
        this.JawExtender.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.BottomJaw = new AdvancedModelBox(this, 40, 7);
        this.BottomJaw.setPos(0.0F, 0.8F, -12.3F);
        this.BottomJaw.addBox(-2.0F, 0.2F, -4.9F, 4, 1, 5, 0.0F);
        this.setRotateAngle(BottomJaw, -0.045553093477052F, 0.0F, 0.0F);
        this.ToothT = new AdvancedModelBox(this, 52, 34);
        this.ToothT.setPos(0.0F, -4.5F, -7.5F);
        this.ToothT.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(ToothT, -2.7930504019665254F, -0.0F, 0.0F);
        this.HeadInner = new AdvancedModelBox(this, 0, 48);
        this.HeadInner.setPos(0.0F, 0.0F, -0.3F);
        this.HeadInner.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, 0.0F);
        this.ToothL_1 = new AdvancedModelBox(this, 52, 34);
        this.ToothL_1.setPos(-4.5F, 0.0F, -7.5F);
        this.ToothL_1.addBox(-0.5F, -0.4F, 0.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(ToothL_1, -3.141592653589793F, -0.3490658503988659F, 0.0F);
        this.JawExtender2 = new AdvancedModelBox(this, 0, 7);
        this.JawExtender2.setPos(0.0F, 0.0F, 0.0F);
        this.JawExtender2.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.JawExtender3 = new AdvancedModelBox(this, 0, 7);
        this.JawExtender3.setPos(0.0F, 0.0F, 0.0F);
        this.JawExtender3.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.JawExtender4 = new AdvancedModelBox(this, 0, 7);
        this.JawExtender4.setPos(0.0F, 0.0F, 0.0F);
        this.JawExtender4.addBox(-1.5F, -1.5F, -13.0F, 3, 3, 13, 0.0F);
        this.TopJaw.addChild(this.JawHook);
        this.Head.addChild(this.ToothL);
        this.Head.addChild(this.ToothB);
        this.Head.addChild(this.ToothT);
        this.Head.addChild(this.HeadInner);
        this.Head.addChild(this.ToothL_1);
        this.JawExtender.addChild(this.JawExtender2);
        this.JawExtender2.addChild(this.JawExtender3);
        this.JawExtender3.addChild(this.JawExtender4);
        this.JawExtender4.addChild(this.TopJaw);
        this.JawExtender4.addChild(this.BottomJaw);
        this.updateDefaultPose();
    }

    @Override
    public void setupAnim(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(Head, JawExtender);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(Head, JawExtender, HeadInner, ToothB, ToothT, ToothL, ToothL_1, JawExtender2, JawExtender3, JawExtender4, TopJaw, BottomJaw, JawHook);
    }

    public void animate(ItemStack stack, float partialTick) {
        this.resetToDefaultPose();
        CompoundTag tag = stack.getOrCreateTag();
        Entity holder = Minecraft.getInstance().level.getEntity(tag.getInt("HolderID"));

        if (!(holder instanceof LivingEntity)) {
            return;
        }

        EntityDataProvider.getCapability(holder).ifPresent(data -> {
            float lungeTicks = data.miscData.lungeTicks + partialTick;
            progressRotation(TopJaw, lungeTicks, (float) Math.toRadians(-30), 0, 0);
            progressRotation(BottomJaw, lungeTicks, (float) Math.toRadians(30), 0, 0);
            progressPosition(JawExtender, lungeTicks, 0, 0, -4);
            progressPosition(JawExtender2, lungeTicks, 0, 0, -10);
            progressPosition(JawExtender3, lungeTicks, 0, 0, -10);
            progressPosition(JawExtender4, lungeTicks, 0, 0, -10);
        });

        /*animator.setAnimation(EntityDeathWorm.ANIMATION_BITE);
        animator.startKeyframe(3);
        this.rotate(animator, TopJaw, -20, 0, 0);
        this.rotate(animator, BottomJaw, 20, 0, 0);
        animator.move(JawExtender, 0, 0, -8);
        animator.move(JawExtender2, 0, 0, -8);
        animator.endKeyframe();
        animator.startKeyframe(3);
        this.rotate(animator, TopJaw, -40, 0, 0);
        this.rotate(animator, BottomJaw, 40, 0, 0);
        animator.move(JawExtender, 0, 0, -10);
        animator.move(JawExtender2, 0, 0, -10);
        animator.endKeyframe();
        animator.startKeyframe(2);
        this.rotate(animator, TopJaw, 5, 0, 0);
        this.rotate(animator, BottomJaw, -5, 0, 0);
        animator.move(JawExtender, 0, 0, -7);
        animator.move(JawExtender2, 0, 0, -7);
        animator.endKeyframe();
        animator.resetKeyframe(2);*/
    }

    @Override
    public void renderStatue(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, Entity living) {
        this.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
