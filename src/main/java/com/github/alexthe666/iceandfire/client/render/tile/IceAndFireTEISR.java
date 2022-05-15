package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.model.ModelTideTrident;
import com.github.alexthe666.iceandfire.client.render.entity.RenderTideTrident;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityGhostChest;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDeathwormGauntlet;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.vector.Vector3f;

import java.util.function.Supplier;

import static net.minecraftforge.common.util.Lazy.of;

public class IceAndFireTEISR extends ItemStackTileEntityRenderer {

    private static final Supplier<ModelTideTrident> TIDE_TRIDENT_MODEL = of(ModelTideTrident::new);
    private final Supplier<RenderTrollWeapon> renderTrollWeapon = of(RenderTrollWeapon::new);
    private final Supplier<RenderDeathWormGauntlet> renderDeathWormGauntlet = of(RenderDeathWormGauntlet::new);
    private final Supplier<RenderDreadPortal> renderDreadPortal = of(
        () -> new RenderDreadPortal(TileEntityRendererDispatcher.instance));
    private final Supplier<RenderGorgonHead> renderGorgonHead = of(() -> new RenderGorgonHead(true));
    private final Supplier<RenderGorgonHead> renderGorgonHeadDead = of(() -> new RenderGorgonHead(false));
    private final Supplier<RenderPixieHouse<?>> renderPixieHouse = of(
        () -> new RenderPixieHouse<>(TileEntityRendererDispatcher.instance));
    private final Supplier<TileEntityDreadPortal> dreadPortalDummy = of(TileEntityDreadPortal::new);
    private final Supplier<RenderGhostChest> renderGhostChest = of(
        () -> new RenderGhostChest(TileEntityRendererDispatcher.instance));
    private final Supplier<TileEntityGhostChest> ghostChestDummy = of(TileEntityGhostChest::new);

    @Override
    public void func_239207_a_(ItemStack itemStackIn, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (itemStackIn.getItem() == IafItemRegistry.GORGON_HEAD) {
            if (itemStackIn.getTag() != null) {
                if (itemStackIn.getTag().getBoolean("Active")) {
                    renderGorgonHead.get().render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
                } else {
                    renderGorgonHeadDead.get().render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
                }
            }
        }
        if (itemStackIn.getItem() == IafBlockRegistry.GHOST_CHEST.asItem()) {
            renderGhostChest.get().render(ghostChestDummy.get(), 0, matrixStackIn, bufferIn, combinedLightIn,
                combinedOverlayIn);
        }

        if (itemStackIn.getItem() instanceof ItemTrollWeapon) {
            ItemTrollWeapon weaponItem = (ItemTrollWeapon) itemStackIn.getItem();
            renderTrollWeapon.get().renderItem(weaponItem.weapon, matrixStackIn, bufferIn, combinedLightIn,
                combinedOverlayIn);
        }
        if (itemStackIn.getItem() instanceof ItemDeathwormGauntlet) {
            renderDeathWormGauntlet.get().renderItem(itemStackIn, matrixStackIn, bufferIn, combinedLightIn,
                combinedOverlayIn);
        }
        if (itemStackIn.getItem() instanceof BlockItem && ((BlockItem) itemStackIn.getItem()).getBlock() == IafBlockRegistry.DREAD_PORTAL) {
            renderDreadPortal.get().render(dreadPortalDummy.get(), 0, matrixStackIn, bufferIn, combinedLightIn,
                combinedOverlayIn);
        }
        if (itemStackIn.getItem() instanceof BlockItem && ((BlockItem) itemStackIn.getItem()).getBlock() instanceof BlockPixieHouse) {
            renderPixieHouse.get().metaOverride = (BlockItem) itemStackIn.getItem();
            renderPixieHouse.get().render(null, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (itemStackIn.getItem() == IafItemRegistry.TIDE_TRIDENT) {
            matrixStackIn.translate(0.5F, 0.5f, 0.5f);
            if (p_239207_2_ == ItemCameraTransforms.TransformType.GUI || p_239207_2_ == ItemCameraTransforms.TransformType.FIXED || p_239207_2_ == ItemCameraTransforms.TransformType.NONE || p_239207_2_ == ItemCameraTransforms.TransformType.GROUND) {
                ItemStack tridentInventory = new ItemStack(IafItemRegistry.TIDE_TRIDENT_INVENTORY);
                if (itemStackIn.isEnchanted()) {
                    ListNBT enchantments = itemStackIn.getTag().getList("Enchantments", 10);
                    tridentInventory.setTagInfo("Enchantments", enchantments);
                }
                Minecraft.getInstance().getItemRenderer().renderItem(tridentInventory, p_239207_2_, p_239207_2_ == ItemCameraTransforms.TransformType.GROUND ? combinedLightIn : 240, combinedOverlayIn, matrixStackIn, bufferIn);
            } else {
                matrixStackIn.push();
                matrixStackIn.translate(0, 0.2F, -0.15F);
                if(p_239207_2_.isFirstPerson()){
                    matrixStackIn.translate(p_239207_2_ == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? -0.3F : 0.3F, 0.2F, -0.2F);
                }else{
                    matrixStackIn.translate(0, 0.6F, 0.0F);
                }
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(160));
                IVertexBuilder glintVertexBuilder = ItemRenderer.getEntityGlintVertexBuilder(bufferIn, RenderType.getEntityCutoutNoCull(RenderTideTrident.TRIDENT), false, itemStackIn.hasEffect());
                TIDE_TRIDENT_MODEL.get().render(matrixStackIn,
                    glintVertexBuilder, combinedLightIn,
                    combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.pop();
            }

        }
    }
}
