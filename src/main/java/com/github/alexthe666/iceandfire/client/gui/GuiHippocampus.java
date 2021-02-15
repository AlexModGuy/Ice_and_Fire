package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.inventory.ContainerHippocampus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiHippocampus extends ContainerScreen<ContainerHippocampus> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/hippogryph.png");
    private final PlayerInventory playerInventory;
    private final ContainerHippocampus hippogryphInv;
    private float mousePosx;
    private float mousePosY;

    public GuiHippocampus(ContainerHippocampus dragonInv, PlayerInventory playerInv, ITextComponent name) {
        super(dragonInv, playerInv, name);
        this.playerInventory = playerInv;
        this.hippogryphInv = dragonInv;
    }

    protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY) {
        int k = 0;
        int l = 0;
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        FontRenderer font = this.getMinecraft().fontRenderer;
        if (entity instanceof EntityHippocampus) {
            EntityHippocampus hippo = (EntityHippocampus) entity;
            font.func_238421_b_(matrixStack, hippo.getDisplayName().getString(), l+8, 6, 4210752);
        }
        font.func_238421_b_(matrixStack, this.playerInventory.getDisplayName().getString(), k+ 8, l + this.ySize - 96 + 2, 4210752);
    }

    public void func_230430_a_(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.func_230446_a_(p_230430_1_);
        this.mousePosx = (float)p_230430_2_;
        this.mousePosY = (float)p_230430_3_;
        super.func_230430_a_(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.func_238474_b_(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityHippocampus) {
            EntityHippocampus hippo = (EntityHippocampus) entity;
            if (hippo.isChested()) {
                this.func_238474_b_(matrixStack, i + 79, j + 17, 0, this.ySize, 5 * 18, 54);
            }
            GuiDragon.drawEntityOnScreen(i + 51, j + 60, 17, (float) (i + 51) - this.mousePosx, (float) (j + 75 - 50) - this.mousePosY, hippo);
        }
    }
}