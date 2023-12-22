package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.inventory.ContainerHippogryph;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

//TODO: We do the same thing here as we do for the other GUI entity screens, that's dumb
public class GuiHippogryph extends AbstractContainerScreen<ContainerHippogryph> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/hippogryph.png");
    private float mousePosx;
    private float mousePosY;

    public GuiHippogryph(ContainerHippogryph dragonInv, Inventory playerInv, Component name) {
        super(dragonInv, playerInv, name);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        Font font = this.getMinecraft().font;
        if (entity instanceof EntityHippogryph) {
            EntityHippogryph hippo = (EntityHippogryph) entity;
            pGuiGraphics.drawString(font, hippo.getDisplayName().getString(), 8, 6, 4210752, false);
        }
        pGuiGraphics.drawString(font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752, false);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        this.mousePosx = mouseX;
        this.mousePosY = mouseY;
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityHippogryph) {
            EntityHippogryph hippo = (EntityHippogryph) entity;
            if (hippo.isChested()) {
                pGuiGraphics.blit(TEXTURE, i + 79, j + 17, 0, this.imageHeight, 5 * 18, 54);
            }
            InventoryScreen.renderEntityInInventoryFollowsMouse(pGuiGraphics,i + 51, j + 60, 17, i + 51 - this.mousePosx, j + 75 - 50 - this.mousePosY,
                hippo);
        }
    }

}