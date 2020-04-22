package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderDragonEgg;
import com.github.alexthe666.iceandfire.client.render.entity.RenderMyrmexEgg;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPodium;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
            } else if (podium.getStackInSlot(0).getItem() instanceof ItemMyrmexEgg) {
                boolean jungle = podium.getStackInSlot(0).getItem() == IafItemRegistry.myrmex_jungle_egg;
                GL11.glPushMatrix();
                GL11.glTranslatef((float) x + 0.5F, (float) y + 0.475F, (float) z + 0.5F);
                GL11.glPushMatrix();
                this.bindTexture(jungle ? RenderMyrmexEgg.EGG_JUNGLE : RenderMyrmexEgg.EGG_DESERT);
                GL11.glPushMatrix();
                model.renderPodium();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                GL11.glPopMatrix();
            } else if (!podium.getStackInSlot(0).isEmpty()) {
                //if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new RenderPodiumItemEvent(this, podium, f, x, y, z))) {
                    GL11.glPushMatrix();
                    float f2 = ((float) podium.prevTicksExisted + (podium.ticksExisted - podium.prevTicksExisted) * f);
                    float f3 = MathHelper.sin(f2 / 10.0F) * 0.1F + 0.1F;
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 1.55F + f3, (float) z + 0.5F);
                    float f4 = (f2 / 20.0F) * (180F / (float) Math.PI);
                    GlStateManager.rotate(f4, 0.0F, 1.0F, 0.0F);
                    GL11.glPushMatrix();
                    Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().player, podium.getStackInSlot(0), ItemCameraTransforms.TransformType.GROUND);
                    GL11.glPopMatrix();
                    GL11.glPopMatrix();
                //}
            }
        }

    }
}
