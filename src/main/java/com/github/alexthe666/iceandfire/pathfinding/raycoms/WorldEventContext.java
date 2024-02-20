package com.github.alexthe666.iceandfire.pathfinding.raycoms;

import com.github.alexthe666.iceandfire.client.render.pathfinding.PathfindingDebugRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public class WorldEventContext {
    public static final WorldEventContext INSTANCE = new WorldEventContext();

    private WorldEventContext()
    {
        // singleton
    }

    public MultiBufferSource.BufferSource bufferSource;
    public PoseStack poseStack;
    public float partialTicks;
    public ClientLevel clientLevel;
    public LocalPlayer clientPlayer;
    public ItemStack mainHandItem;


    /**
     * In chunks
     */
    int clientRenderDist;

    public void renderWorldLastEvent(final RenderLevelStageEvent event)
    {
        bufferSource = WorldRenderMacros.getBufferSource();
        poseStack = event.getPoseStack();
        partialTicks = event.getPartialTick();
        clientLevel = Minecraft.getInstance().level;
        clientPlayer = Minecraft.getInstance().player;
        mainHandItem = clientPlayer.getMainHandItem();
        clientRenderDist = Minecraft.getInstance().options.renderDistance().get();

        final Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS)
        {
            PathfindingDebugRenderer.render(this);

            bufferSource.endBatch();
        }
        else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS)
        {
            bufferSource.endBatch();
        }

        poseStack.popPose();
    }

}
