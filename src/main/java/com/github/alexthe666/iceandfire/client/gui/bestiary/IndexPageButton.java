package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IndexPageButton extends Button {

    public IndexPageButton(int id, int x, int y, ITextComponent buttonText, net.minecraft.client.gui.widget.button.Button.IPressable butn) {
        super(x, y, 160, 32, buttonText, butn);
        this.field_230688_j_ = 160;
        this.field_230689_k_ = 32;
    }

    public void func_230431_b_(MatrixStack p_230430_1_, int mouseX, int mouseY, float partial) {
        if (this.field_230693_o_) {
            FontRenderer fontrenderer = IafConfig.useVanillaFont ? Minecraft.getInstance().fontRenderer : (FontRenderer) IceAndFire.PROXY.getFontRenderer();
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png"));
            boolean flag = mouseX >= this.field_230690_l_ && mouseY >= this.field_230691_m_ && mouseX < this.field_230690_l_ + this.field_230688_j_ && mouseY < this.field_230691_m_ + this.field_230689_k_;
            this.func_238474_b_(p_230430_1_, this.field_230690_l_, this.field_230691_m_, 0, flag ? 32 : 0, this.field_230688_j_, this.field_230689_k_);
            fontrenderer.func_238421_b_(p_230430_1_, func_230458_i_().toString(), (float) (this.field_230690_l_ + this.field_230688_j_ / 2 - fontrenderer.getStringWidth(this.func_230458_i_().toString()) / 2), (float) this.field_230691_m_ + (this.field_230689_k_ - 8) / 2, flag ? 0XFAE67D : 0X303030);
        }
    }
}
