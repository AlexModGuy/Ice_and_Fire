package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.BlockPixieHouse;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityGhostChest;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class IceAndFireTEISR extends BlockEntityWithoutLevelRenderer {

    private final RenderPixieHouse PIXIE_HOUSE_RENDERER;
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final EntityModelSet entityModelSet;
    private final TileEntityGhostChest chest = new TileEntityGhostChest(BlockPos.ZERO, IafBlockRegistry.GHOST_CHEST.get().defaultBlockState());
    private final TileEntityDreadPortal portal = new TileEntityDreadPortal(BlockPos.ZERO, IafBlockRegistry.DREAD_PORTAL.get().defaultBlockState());

    public IceAndFireTEISR() {
        this(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    public IceAndFireTEISR(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
        this.blockEntityRenderDispatcher = dispatcher;
        this.entityModelSet = modelSet;
        PIXIE_HOUSE_RENDERER = new RenderPixieHouse(null);

    }

    @Override
    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext type, @NotNull PoseStack stackIn, @NotNull MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (stack.getItem() == IafBlockRegistry.GHOST_CHEST.get().asItem()) {
            blockEntityRenderDispatcher.renderItem(chest, stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() == IafBlockRegistry.DREAD_PORTAL.get()) {
            blockEntityRenderDispatcher.renderItem(portal, stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }
        if (stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof BlockPixieHouse) {
            PIXIE_HOUSE_RENDERER.metaOverride = (BlockItem) stack.getItem();
            PIXIE_HOUSE_RENDERER.render(null, 0, stackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        }

    }
}
