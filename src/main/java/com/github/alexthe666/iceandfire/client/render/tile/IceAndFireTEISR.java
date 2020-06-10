package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDeathwormGauntlet;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IceAndFireTEISR extends ItemStackTileEntityRenderer {

    private RenderTrollWeapon renderTrollWeapon = new RenderTrollWeapon();
    private RenderDeathWormGauntlet renderDeathWormGauntlet = new RenderDeathWormGauntlet();
    private RenderDreadPortal renderDreadPortal = new RenderDreadPortal(TileEntityRendererDispatcher.instance);
    private RenderGorgonHead renderGorgonHead = new RenderGorgonHead(true);
    private RenderGorgonHead renderGorgonHeadDead = new RenderGorgonHead(false);
    private TileEntityDreadPortal dreadPortalDummy = new TileEntityDreadPortal();
    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (itemStackIn.getItem() == IafItemRegistry.GORGON_HEAD) {
            if(itemStackIn.getTag().getBoolean("Active")){
                renderGorgonHead.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
            }else{
                renderGorgonHeadDead.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
            }
        }

        if (itemStackIn.getItem() instanceof ItemTrollWeapon) {
            ItemTrollWeapon weaponItem = (ItemTrollWeapon) itemStackIn.getItem();
            renderTrollWeapon.renderItem(weaponItem.weapon, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() instanceof ItemDeathwormGauntlet) {
            renderDeathWormGauntlet.renderItem(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(IafBlockRegistry.DREAD_PORTAL)) {
            renderDreadPortal.render(dreadPortalDummy, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    }
}
