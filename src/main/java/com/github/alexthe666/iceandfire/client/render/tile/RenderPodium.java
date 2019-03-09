package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderMyrmexEgg;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderPodium extends TileEntitySpecialRenderer {

    protected static ResourceLocation getEggTexture(EnumDragonEgg type) {
        switch (type) {
            default:
                return RenderDragonEgg.EGG_RED;
            case GREEN:
                return RenderDragonEgg.EGG_GREEN;
            case BRONZE:
                return RenderDragonEgg.EGG_BRONZE;
            case GRAY:
                return RenderDragonEgg.EGG_GREY;
            case BLUE:
                return RenderDragonEgg.EGG_BLUE;
            case WHITE:
                return RenderDragonEgg.EGG_WHITE;
            case SAPPHIRE:
                return RenderDragonEgg.EGG_SAPPHIRE;
            case SILVER:
                return RenderDragonEgg.EGG_SILVER;
        }
    }

    @Override
    public void render(TileEntity entity, double x, double y, double z, float f, int f1, float alpha) {
        ModelDragonEgg model = new ModelDragonEgg();
        TileEntityPodium podium = (TileEntityPodium) entity;

        if (!podium.getStackInSlot(0).isEmpty()) {
            if (podium.getStackInSlot(0).getItem() instanceof ItemDragonEgg) {
                ItemDragonEgg item = (ItemDragonEgg) podium.getStackInSlot(0).getItem();
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x + 0.5F, (float) y + 0.475F, (float) z + 0.5F);
                GL11.glPushMatrix();
                this.bindTexture(getEggTexture(item.type));
                GL11.glPushMatrix();
                model.renderPodium();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
            }
            if (podium.getStackInSlot(0).getItem() instanceof ItemMyrmexEgg) {
                boolean jungle = podium.getStackInSlot(0).getItem() == ModItems.myrmex_jungle_egg;
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x + 0.5F, (float) y + 0.475F, (float) z + 0.5F);
                GL11.glPushMatrix();
                this.bindTexture(jungle ? RenderMyrmexEgg.EGG_JUNGLE : RenderMyrmexEgg.EGG_DESERT);
                GL11.glPushMatrix();
                model.renderPodium();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
            }
        }

    }
}
