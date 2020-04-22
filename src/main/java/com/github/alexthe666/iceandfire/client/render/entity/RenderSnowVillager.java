package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSnowVillager extends RenderVillager {

    public RenderSnowVillager(RenderManager renderManager) {
        super(renderManager);
    }

    protected void preRenderCallback(EntityVillager entity, float partialTickTime) {
        super.preRenderCallback(entity, partialTickTime);
        if (entity.getProfessionForge() == IafVillagerRegistry.INSTANCE.fisherman) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.125F, -1.0F, -0.3F);
            GL11.glRotatef(-80, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(10, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90, 0.0F, 0.0F, 1.0F);
            Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(IafItemRegistry.fishing_spear), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            GL11.glPopMatrix();
        }
    }
}
