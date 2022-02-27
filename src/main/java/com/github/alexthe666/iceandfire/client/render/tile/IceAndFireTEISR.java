package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.model.ModelTideTrident;
import com.github.alexthe666.iceandfire.client.model.ModelTrollWeapon;
import com.github.alexthe666.iceandfire.client.render.entity.RenderTideTrident;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityGhostChest;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDeathwormGauntlet;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IceAndFireTEISR extends ItemStackTileEntityRenderer {

    private static final ModelTideTrident TIDE_TRIDENT_MODEL = new ModelTideTrident();
    private final RenderTrollWeapon renderTrollWeapon = new RenderTrollWeapon();
    private final RenderDeathWormGauntlet renderDeathWormGauntlet = new RenderDeathWormGauntlet();
    private final RenderDreadPortal renderDreadPortal = new RenderDreadPortal(TileEntityRendererDispatcher.instance);
    private final RenderGorgonHead renderGorgonHead = new RenderGorgonHead(true);
    private final RenderGorgonHead renderGorgonHeadDead = new RenderGorgonHead(false);
    private final RenderPixieHouse renderPixieHouse = new RenderPixieHouse(TileEntityRendererDispatcher.instance);
    private final TileEntityDreadPortal dreadPortalDummy = new TileEntityDreadPortal();
    private final RenderGhostChest renderGhostChest = new RenderGhostChest(TileEntityRendererDispatcher.instance);
    private final TileEntityGhostChest ghostChestDummy = new TileEntityGhostChest();

    @Override
    public void func_239207_a_(ItemStack itemStackIn, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (itemStackIn.getItem() == IafItemRegistry.GORGON_HEAD) {
            if (itemStackIn.getTag() != null) {
                if (itemStackIn.getTag().getBoolean("Active")) {
                    renderGorgonHead.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
                } else {
                    renderGorgonHeadDead.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
                }
            }
        }
        if (itemStackIn.getItem() == IafBlockRegistry.GHOST_CHEST.asItem()) {
            renderGhostChest.render(ghostChestDummy, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }

        if (itemStackIn.getItem() instanceof ItemTrollWeapon) {
            ItemTrollWeapon weaponItem = (ItemTrollWeapon) itemStackIn.getItem();
            renderTrollWeapon.renderItem(weaponItem.weapon, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() instanceof ItemDeathwormGauntlet) {
            renderDeathWormGauntlet.renderItem(itemStackIn, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() instanceof BlockItem && ((BlockItem) itemStackIn.getItem()).getBlock() == IafBlockRegistry.DREAD_PORTAL) {
            renderDreadPortal.render(dreadPortalDummy, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() instanceof BlockItem && ((BlockItem) itemStackIn.getItem()).getBlock() instanceof BlockPixieHouse) {
            renderPixieHouse.metaOverride = (BlockItem) itemStackIn.getItem();
            renderPixieHouse.render(null, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() == IafItemRegistry.TIDE_TRIDENT) {
            matrixStackIn.translate(0.5F, 0.5f, 0.5f);
            if (p_239207_2_ == ItemCameraTransforms.TransformType.GUI || p_239207_2_ == ItemCameraTransforms.TransformType.FIXED || p_239207_2_ == ItemCameraTransforms.TransformType.NONE || p_239207_2_ == ItemCameraTransforms.TransformType.GROUND) {
                Minecraft.getInstance().getItemRenderer().renderItem(new ItemStack(IafItemRegistry.TIDE_TRIDENT_INVENTORY), p_239207_2_, p_239207_2_ == ItemCameraTransforms.TransformType.GROUND ? combinedLightIn : 240, combinedOverlayIn, matrixStackIn, bufferIn);
            } else {
                matrixStackIn.push();
                matrixStackIn.translate(0, 0.2F, -0.15F);
                if(p_239207_2_.isFirstPerson()){
                    matrixStackIn.translate(p_239207_2_ == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? -0.3F : 0.3F, 0.2F, -0.2F);
                }else{
                    matrixStackIn.translate(0, 0.6F, 0.0F);
                }
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(160));
                TIDE_TRIDENT_MODEL.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(RenderTideTrident.TRIDENT)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.pop();
            }

        }
    }
}
