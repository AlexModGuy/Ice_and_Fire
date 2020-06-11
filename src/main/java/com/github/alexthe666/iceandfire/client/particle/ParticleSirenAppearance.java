package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.client.model.ModelSiren;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.ElderGuardianRenderer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleSirenAppearance extends Particle {
    private final Model field_228342_a_ = new ModelSiren();
    private final RenderType field_228341_A_ = RenderType.getEntityTranslucent(ElderGuardianRenderer.GUARDIAN_ELDER_TEXTURE);

    public ParticleSirenAppearance(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.particleGravity = 0.0F;
        this.maxAge = 30;
    }

    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks) {
        float f = ((float) this.age + partialTicks) / (float) this.maxAge;
        float f1 = 0.05F + 0.5F * MathHelper.sin(f * (float) Math.PI);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.rotate(renderInfo.getRotation());
        matrixstack.rotate(Vector3f.XP.rotationDegrees(150.0F * f - 60.0F));
        matrixstack.scale(-1.0F, -1.0F, 1.0F);
        matrixstack.translate(0.0D, -1.101F, 1.5D);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        IVertexBuilder ivertexbuilder = irendertypebuffer$impl.getBuffer(this.field_228341_A_);
        this.field_228342_a_.render(matrixstack, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, f1);
        irendertypebuffer$impl.finish();
    }
}
