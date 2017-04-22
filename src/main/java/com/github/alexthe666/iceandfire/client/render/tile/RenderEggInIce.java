package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.ModelDragonEgg;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityEggInIce;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEggInIce extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float f, int f1) {
        ModelDragonEgg model = new ModelDragonEgg();
        TileEntityEggInIce egg = (TileEntityEggInIce) entity;
        if (egg.type != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5F, (float) y - 0.75F, (float) z + 0.5F);
            GL11.glPushMatrix();
            EnumDragonEgg eggType = egg.type.isFire ? EnumDragonEgg.BLUE : egg.type;
            this.bindTexture(new ResourceLocation(RenderPodium.getTexture(eggType)));
            GL11.glPushMatrix();
            model.renderFrozen(egg);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }

}
