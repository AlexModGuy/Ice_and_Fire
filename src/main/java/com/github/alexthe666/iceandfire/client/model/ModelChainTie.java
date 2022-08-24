package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelChainTie extends SegmentedModel<EntityChainTie> {
    public ModelRenderer knotRenderer;

    public ModelChainTie() {
        this(0, 0, 32, 32);
    }

    public ModelChainTie(int width, int height, int texWidth, int texHeight) {
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.knotRenderer = new ModelRenderer(this, width, height);
        this.knotRenderer.addBox(-4.0F, 2.0F, -4.0F, 8, 12, 8, 1.0F);
        this.knotRenderer.setPos(0.0F, 0.0F, 0.0F);
    }

    public void setupAnim(EntityChainTie entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.knotRenderer.yRot = netHeadYaw * 0.017453292F;
        this.knotRenderer.xRot = headPitch * 0.017453292F;
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(knotRenderer);
    }
}
