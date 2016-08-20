package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModVillagers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RenderSnowVillager extends RenderVillager{

    public RenderSnowVillager(RenderManager renderManager) {
        super(renderManager);
    }

    protected void preRenderCallback(EntityVillager entity, float partialTickTime) {
        super.preRenderCallback(entity, partialTickTime);
        if(entity.getProfessionForge() == ModVillagers.INSTANCE.fisherman) {
            GL11.glRotatef(-180, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0F, -1.02F, 0.23F);
            GL11.glScalef(0.4F, 0.4F, 0.4F);
            Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(ModItems.fishing_spear), ItemCameraTransforms.TransformType.FIXED);
        }
    }
}
