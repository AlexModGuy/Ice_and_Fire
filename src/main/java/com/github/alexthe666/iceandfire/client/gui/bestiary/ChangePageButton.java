package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ChangePageButton extends Button {
    private final boolean right;
    public int lastpage = 1;
    private final int color;

    public ChangePageButton(int x, int y, boolean right, int color, OnPress press) {
        super(x, y, 23, 10, Component.empty(), press);
        this.right = right;
        this.color = color;
    }

    @Override
    public void renderButton(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partial) {
        if (this.active) {
            ResourceLocation resourceLocation = new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png");
            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            RenderSystem.setShaderTexture(0, resourceLocation);
            int i = 0;
            int j = 64;
            if (flag) {
                i += 23;
            }

            if (!this.right) {
                j += 13;
            }
            j += color * 23;

            blit(matrixStack, this.x, this.y, i, j, width, height);
        }
    }
}