package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class IndexPageButton extends Button {

    public IndexPageButton(int x, int y, Component buttonText,
                           net.minecraft.client.gui.components.Button.OnPress butn) {
        super(x, y, 160, 32, buttonText, butn);
        this.width = 160;
        this.height = 32;
    }

    @Override
    public void renderButton(PoseStack ms, int mouseX, int mouseY, float partial) {
        if (this.active) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            Font font = IafConfig.useVanillaFont ? Minecraft.getInstance().font : (Font) IceAndFire.PROXY.getFontRenderer();
            boolean flag = isHoveredOrFocused();
            RenderSystem.setShaderTexture(0, new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png"));
            blit(ms, this.x, this.y, 0, flag ? 32 : 0, this.width, this.height);
            int i = flag ? 0XFAE67D : 0X303030;
            font.draw(ms, this.getMessage().getVisualOrderText(), (this.x + this.width / 2 - font.width(this.getMessage().getString()) / 2), this.y + (this.height - 8) / 2, i | Mth.ceil(this.alpha * 255.0F) << 24);
        }
    }
}
