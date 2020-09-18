package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ChangePageButton extends Button {
    private final boolean right;
    public int lastpage = 1;
    private int page;
    private int color;

    public ChangePageButton(int x, int y, boolean right, int bookpage, int color, IPressable press) {
        super(x, y, 23, 10, new StringTextComponent(""), press);
        this.right = right;
        page = bookpage;
        this.color = color;
    }

    @Override
    public void func_230431_b_(MatrixStack p_230430_1_,  int mouseX, int mouseY, float partial) {
        if (this.field_230693_o_) {
            boolean flag = mouseX >= this.field_230690_l_ && mouseY >= this.field_230691_m_ && mouseX < this.field_230690_l_ + this.field_230688_j_ && mouseY < this.field_230691_m_ + this.field_230689_k_;
            Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("iceandfire:textures/gui/bestiary/widgets.png"));
            int i = 0;
            int j = 64;
            if (flag) {
                i += 23;
            }

            if (!this.right) {
                j += 13;
            }
            j += color * 23;

            this.func_238474_b_(p_230430_1_, this.field_230690_l_, this.field_230691_m_, i, j, field_230688_j_, field_230689_k_);
        }
    }
}