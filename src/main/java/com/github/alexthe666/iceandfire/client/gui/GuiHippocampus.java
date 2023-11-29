package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.inventory.HippocampusContainerMenu;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public class GuiHippocampus extends AbstractContainerScreen<HippocampusContainerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/hippogryph.png");
    private float mousePosx;
    private float mousePosY;

    public GuiHippocampus(HippocampusContainerMenu dragonInv, Inventory playerInv, Component name) {
        super(dragonInv, playerInv, name);
    }

    @Override
    protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
        int k = 0;
        int l = 0;
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        Font font = this.getMinecraft().font;
        if (entity instanceof EntityHippocampus) {
            EntityHippocampus hippo = (EntityHippocampus) entity;
            font.draw(ms, hippo.getDisplayName().getString(), l + 8, 6, 4210752);
        }
        font.draw(ms, this.playerInventoryTitle, k + 8, l + this.imageHeight - 96 + 2, 4210752);
    }

    @Override
    public void render(PoseStack ms,  int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        this.mousePosx = mouseX;
        this.mousePosY = mouseY;
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack ms, float partialTicks, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(ms, i, j, 0, 0, this.imageWidth, this.imageHeight);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityHippocampus hippo) {
            if (hippo.isChested()) {
                blit(ms, i + 79, j + 17, 0, this.imageHeight, 5 * 18, 54);
            }
            InventoryScreen.renderEntityInInventory(i + 51, j + 60, 17, i + 51 - this.mousePosx, j + 75 - 50 - this.mousePosY, hippo);
        }
    }
}