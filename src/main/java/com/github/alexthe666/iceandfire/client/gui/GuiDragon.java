package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class GuiDragon extends AbstractContainerScreen<ContainerDragon> {
    private static final ResourceLocation texture = new ResourceLocation("iceandfire:textures/gui/dragon.png");

    public GuiDragon(ContainerDragon dragonInv, Inventory playerInv, Component name) {
        super(dragonInv, playerInv, name);
        this.imageHeight = 214;
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics matrixStack, int mouseX, int mouseY) {

    }

    @Override
    public void render(@NotNull GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        matrixStack.blit(texture, k, l, 0, 0, this.imageWidth, this.imageHeight);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            float dragonScale = 1F / Math.max(0.0001F, dragon.getScale());
            Quaternionf quaternionf = (new Quaternionf()).rotateY((float) Mth.lerp((float) mouseX / this.width, 0, Math.PI)).rotateZ((float) Mth.lerp((float) mouseY / this.width, Math.PI, Math.PI + 0.2));
            InventoryScreen.renderEntityInInventory(matrixStack, k + 88, l + (int) (0.5F * (dragon.flyProgress)) + 55, (int) (dragonScale * 23F), quaternionf, null, dragon);
        }
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;

            Font font = this.getMinecraft().font;
            String s3 = dragon.getCustomName() == null ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + " " + dragon.getCustomName().getString();
            font.drawInBatch(s3, k + this.imageWidth / 2 - font.width(s3) / 2, l + 75, 0XFFFFFF, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s2 = StatCollector.translateToLocal("dragon.health") + " " + Math.floor(Math.min(dragon.getHealth(), dragon.getMaxHealth())) + " / " + dragon.getMaxHealth();
            font.drawInBatch(s2, k + this.imageWidth / 2 - font.width(s2) / 2, l + 84, 0XFFFFFF, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s5 = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal((dragon.isMale() ? "dragon.gender.male" : "dragon.gender.female"));
            font.drawInBatch(s5, k + this.imageWidth / 2 - font.width(s5) / 2, l + 93, 0XFFFFFF, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s6 = StatCollector.translateToLocal("dragon.hunger") + dragon.getHunger() + "/100";
            font.drawInBatch(s6, k + this.imageWidth / 2 - font.width(s6) / 2, l + 102, 0XFFFFFF, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s4 = StatCollector.translateToLocal("dragon.stage") + " " + dragon.getDragonStage() + " " + StatCollector.translateToLocal("dragon.days.front") + dragon.getAgeInDays() + " " + StatCollector.translateToLocal("dragon.days.back");
            font.drawInBatch(s4, k + this.imageWidth / 2 - font.width(s4) / 2, l + 111, 0XFFFFFF, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
            String s7 = dragon.getOwner() != null ? StatCollector.translateToLocal("dragon.owner") + dragon.getOwner().getName().getString() : StatCollector.translateToLocal("dragon.untamed");
            font.drawInBatch(s7, k + this.imageWidth / 2 - font.width(s7) / 2, l + 120, 0XFFFFFF, false, matrixStack.pose().last().pose(), matrixStack.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }


}