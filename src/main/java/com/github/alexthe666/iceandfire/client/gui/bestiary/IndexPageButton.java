package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IndexPageButton extends Button {

    public IndexPageButton(int id, int x, int y, String buttonText, net.minecraft.client.gui.widget.button.Button.IPressable butn) {
        super(x, y, 160, 32, buttonText, butn);
        this.width = 160;
        this.height = 32;
    }

    public void render(int mouseX, int mouseY, float partial) {
        if (this.visible) {
            FontRenderer fontrenderer = IafConfig.useVanillaFont ? Minecraft.getInstance().fontRenderer : (FontRenderer) IceAndFire.PROXY.getFontRenderer();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png"));
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            this.blit(this.x, this.y, 0, this.isHovered ? 32 : 0, this.width, this.height);
            fontrenderer.drawString(getMessage(), (float) (this.x + this.width / 2 - fontrenderer.getStringWidth(this.getMessage()) / 2), (float) this.y + (this.height - 8) / 2, this.isHovered ? 0XFAE67D : 0X303030);
        }
    }
}
