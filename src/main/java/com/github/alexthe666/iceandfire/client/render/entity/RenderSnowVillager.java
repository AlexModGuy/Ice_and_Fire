package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.core.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.*;
import org.lwjgl.opengl.*;

public class RenderSnowVillager extends RenderVillager {

	public RenderSnowVillager (RenderManager renderManager) {
		super (renderManager);
	}

	protected void preRenderCallback (EntityVillager entity, float partialTickTime) {
		super.preRenderCallback (entity, partialTickTime);
		if (entity.getProfessionForge () == ModVillagers.INSTANCE.fisherman) {
			GL11.glPushMatrix ();
			GL11.glTranslatef (0.125F, -1.0F, -0.3F);
			GL11.glRotatef (-80, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef (10, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef (90, 0.0F, 0.0F, 1.0F);
			Minecraft.getMinecraft ().getRenderItem ().renderItem (new ItemStack (ModItems.fishing_spear), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
			GL11.glPopMatrix ();
		}
	}
}
