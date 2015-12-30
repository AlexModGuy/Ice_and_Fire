package com.github.alexthe666.iceandfire.client.render.entity.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.client.model.ModelFireDragon;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

@SideOnly(Side.CLIENT)
public class LayerDragonArmor implements LayerRenderer
{
	private final RenderDragonBase renderer;
	private final ModelFireDragon fireModel = new ModelFireDragon();

	private int slot;
	private String dragonType;

	public LayerDragonArmor(RenderDragonBase renderer, int slot, String dragonType)
	{
		this.renderer = renderer;
		this.slot = slot;
		this.dragonType = dragonType;
	}

	public void doRenderLayer(EntityDragonBase entity, float f, float f1, float i, float f2, float f3, float f4, float f5)
	{

		if(entity.inv.getStackInSlot(slot) != null){
			if(entity.getArmorIndex(entity.inv.getStackInSlot(slot)) != 0){
				this.renderer.bindTexture(new ResourceLocation("iceandfire:textures/models/" + dragonType + "/armor_" + armorPart() + "_" + entity.getArmorIndex(entity.inv.getStackInSlot(slot)) + ".png"));
				this.renderer.getMainModel().render(entity, f, f1, f2, f3, f4, f5);
			}
		}
	}

	public String armorPart(){
		switch(slot){
		case 2:
			return "neck";
		case 3:
			return "body";
		case 4:
			return "tail";
		default:
			return "head";
		}
	}
	public boolean shouldCombineTextures()
	{
		return false;
	}

	public void doRenderLayer(EntityLivingBase entity, float f, float f1, float f2, float f3, float f4, float f5, float f6)
	{
		this.doRenderLayer((EntityDragonBase)entity, f, f1, f2, f3, f4, f5, f6);
	}
}