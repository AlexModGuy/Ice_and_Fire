package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.client.model.ModelSiren;
import com.github.alexthe666.iceandfire.client.render.entity.RenderSiren;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ParticleSirenAppearance extends Particle {
    private final Model model = new ModelSiren();
    private final int sirenType;

    public ParticleSirenAppearance(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int sirenType) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.gravity = 0.0F;
        this.lifetime = 30;
        this.sirenType = sirenType;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        float f = ((float) this.age + partialTicks) / (float) this.lifetime;
        float f1 = 0.05F + 0.5F * Mth.sin(f * (float) Math.PI);
        PoseStack matrixstack = new PoseStack();
        matrixstack.mulPose(renderInfo.rotation());
        matrixstack.mulPose(Axis.XP.rotationDegrees(150.0F * f - 60.0F));
        matrixstack.scale(-1.0F, -1.0F, 1.0F);
        matrixstack.translate(0.0D, -1.101F, 1.5D);
        MultiBufferSource.BufferSource irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer ivertexbuilder = irendertypebuffer$impl.getBuffer(RenderType.entityTranslucent(RenderSiren.getSirenOverlayTexture(sirenType)));
        this.model.renderToBuffer(matrixstack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, f1);
        irendertypebuffer$impl.endBatch();
    }
}

