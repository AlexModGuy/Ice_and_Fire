package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.ModelTroll;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntityCrack;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollEyes;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollStone;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerTrollWeapon;
import com.github.alexthe666.iceandfire.entity.EntityTroll;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class RenderTroll extends RenderLiving<EntityTroll> implements ICustomStoneLayer {

    public RenderTroll(RenderManager renderManager) {
        super(renderManager, new ModelTroll(), 0.9F);
        this.layerRenderers.add(new LayerTrollWeapon(this));
        this.layerRenderers.add(new LayerTrollEyes(this));
    }

    @Override
    public void preRenderCallback(EntityTroll LivingEntityIn, float partialTickTime) {
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTroll troll) {
        return troll.getType().TEXTURE;
    }

    @Override
    public LayerRenderer getStoneLayer(RenderLivingBase render) {
        return new LayerTrollStone(render);
    }

    @Override
    public LayerRenderer getCrackLayer(RenderLivingBase render) {
        return new LayerStoneEntityCrack(render);
    }
}
