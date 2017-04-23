package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderDragonSkull extends RenderLiving {

    public RenderDragonSkull(RenderManager renderManager) {
        super(renderManager, new ModelFireSkull(), 0.3F);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {
        if (par1EntityLivingBase instanceof EntityDragonSkull) {
            GL11.glScalef(((EntityDragonSkull) par1EntityLivingBase).getDragonSize(), 1, ((EntityDragonSkull) par1EntityLivingBase).getDragonSize());
            GL11.glScalef(1, -((EntityDragonSkull) par1EntityLivingBase).getDragonSize(), 1);
            GL11.glRotatef(par1EntityLivingBase.rotationYaw, 0, 1, 0);
            super.preRenderCallback(par1EntityLivingBase, par2);
        }

        if (((EntityDragonSkull) par1EntityLivingBase).getType() == 0) {
            if (this.mainModel.getClass() != ModelFireSkull.class) {
                this.mainModel = new ModelFireSkull();
            }
        } else if (this.mainModel.getClass() != ModelIceSkull.class) {
            this.mainModel = new ModelIceSkull();
        }

    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        if (entity instanceof EntityDragonSkull && ((EntityDragonSkull) entity).getType() == 0) {
            return new ResourceLocation("iceandfire:textures/models/firedragon/skeleton.png");
        } else {
            return new ResourceLocation("iceandfire:textures/models/icedragon/skeleton.png");
        }
    }

}
