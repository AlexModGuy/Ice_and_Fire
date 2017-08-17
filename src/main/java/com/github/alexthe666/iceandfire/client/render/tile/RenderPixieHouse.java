package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.client.model.ModelPixieHouse;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import net.ilexiconn.llibrary.client.util.ItemTESRContext;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderPixieHouse extends TileEntitySpecialRenderer<TileEntityPixieHouse> {

    private static final ModelPixieHouse MODEL = new ModelPixieHouse();
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_3.png");
    private static final ResourceLocation TEXTURE_4 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_4.png");
    private static final ResourceLocation TEXTURE_5 = new ResourceLocation("iceandfire:textures/models/pixie/house/pixie_house_5.png");

    @Override
    public void renderTileEntityAt(TileEntityPixieHouse entity, double x, double y, double z, float f, int f1) {
        int rotation = 0;
        int meta = 0;

        if(entity != null && entity.getWorld() != null){
            meta = entity.houseType;
            if(entity.getWorld().getBlockState(entity.getPos()).getValue(BlockPixieHouse.FACING) == EnumFacing.NORTH){
                rotation = 180;
            }
            if(entity.getWorld().getBlockState(entity.getPos()).getValue(BlockPixieHouse.FACING) == EnumFacing.EAST){
                rotation = -90;
            }
            if(entity.getWorld().getBlockState(entity.getPos()).getValue(BlockPixieHouse.FACING) == EnumFacing.WEST){
                rotation = 90;
            }
        } else if (ItemTESRContext.INSTANCE.getCurrentStack() != null) {
            meta = ItemTESRContext.INSTANCE.getCurrentStack().getItemDamage();
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 1.501F, (float) z + 0.5F);
        GL11.glPushMatrix();
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glRotatef(rotation, 0, 1, 0);
        switch(meta){
            case 0:
                this.bindTexture(TEXTURE_0);
                break;
            case 1:
                this.bindTexture(TEXTURE_1);
                break;
            case 2:
                this.bindTexture(TEXTURE_2);
                break;
            case 3:
                this.bindTexture(TEXTURE_3);
                break;
            case 4:
                this.bindTexture(TEXTURE_4);
                break;
            case 5:
                this.bindTexture(TEXTURE_5);
                break;
        }
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        MODEL.render(0.0625F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();

    }

}
