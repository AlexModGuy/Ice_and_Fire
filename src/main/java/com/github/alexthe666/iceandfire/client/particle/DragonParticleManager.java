package com.github.alexthe666.iceandfire.client.particle;

import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class DragonParticleManager {

    @OnlyIn(Dist.CLIENT)
    public static void translateToDragon(EntityDragonBase dragonBase) {
        RenderLiving render = (RenderLiving) Minecraft.getInstance().getRenderManager().getEntityRenderObject(dragonBase);
        if (render instanceof RenderDragonBase) {
            RenderDragonBase renderDragonBase = (RenderDragonBase) render;
            ((IceAndFireTabulaModel) renderDragonBase.getMainModel()).getCube("BodyUpper").postRender(0.0625F);
            ((IceAndFireTabulaModel) renderDragonBase.getMainModel()).getCube("Neck1").postRender(0.0625F);
            ((IceAndFireTabulaModel) renderDragonBase.getMainModel()).getCube("Neck2").postRender(0.0625F);
            ((IceAndFireTabulaModel) renderDragonBase.getMainModel()).getCube("Neck3").postRender(0.0625F);
            ((IceAndFireTabulaModel) renderDragonBase.getMainModel()).getCube("Head").postRender(0.0625F);
        }
    }
}
