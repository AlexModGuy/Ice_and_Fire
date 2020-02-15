package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;

public class RenderDreadSpawner extends TileEntitySpecialRenderer<TileEntityDreadSpawner> {

    public static void renderMob(MobSpawnerBaseLogic mobSpawnerLogic, double posX, double posY, double posZ, float partialTicks) {
        Entity entity = mobSpawnerLogic.getCachedEntity();

        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.width, entity.height);

            if ((double) f1 > 1.0D) {
                f /= f1;
            }

            GlStateManager.translate(0.0F, 0.4F, 0.0F);
            GlStateManager.rotate((float) (mobSpawnerLogic.getPrevMobRotation() + (mobSpawnerLogic.getMobRotation() - mobSpawnerLogic.getPrevMobRotation()) * (double) partialTicks) * 10.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.2F, 0.0F);
            GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(f, f, f);
            entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
            Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
        }
    }

    public void render(TileEntityDreadSpawner te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        renderMob(te.getSpawnerBaseLogic(), x, y, z, partialTicks);
        GlStateManager.popMatrix();
    }
}