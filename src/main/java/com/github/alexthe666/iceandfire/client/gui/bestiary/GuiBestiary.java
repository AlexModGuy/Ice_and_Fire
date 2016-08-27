package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiBestiary extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/bestiary/bestiary.png");
    private static final ResourceLocation DRAWINGS_0 = new ResourceLocation("iceandfire:textures/gui/bestiary/drawings_0.png");
    protected static final int X = 390;
    protected static final int Y = 245;
    public List<EnumBestiaryPages> allPageTypes = new ArrayList<EnumBestiaryPages>();
    public EnumBestiaryPages pageType;
    public List<IndexPageButton> indexButtons = new  ArrayList<IndexPageButton>();
    public ChangePageButton previousPage;
    public ChangePageButton nextPage;
    public int bookPages;
    public int bookPagesTotal = 1;
    public int indexPages;
    public int indexPagesTotal = 1;
    protected ItemStack book;
    protected boolean index;
    protected FontRenderer font = (FontRenderer) IceAndFire.PROXY.getFontRenderer();

    public GuiBestiary(ItemStack book){
        this.book = book;
        if(book != null && book.getItem() != null && book.getItem() == ModItems.bestiary){
            if(book.getTagCompound() != null){
                List<EnumBestiaryPages> pages = EnumBestiaryPages.containedPages(EnumBestiaryPages.toList(book.getTagCompound().getIntArray("Pages")));
                allPageTypes.addAll(pages);
                indexPagesTotal = pages.size() % 10;
            }
        }
        index = true;
    }

    public void initGui(){
        super.initGui();
        int centerX = (this.width - this.X) / 2;
        int centerY = (this.height - this.Y) / 2;
        this.buttonList.add(this.previousPage = new ChangePageButton(0, centerX + 15, centerY + 215, false, bookPages));
        this.buttonList.add(this.nextPage = new ChangePageButton(1, centerX + 357, centerY + 215, true, bookPages));

        if(!allPageTypes.isEmpty()) {
            for (int i = 0; i < allPageTypes.size(); i++) {
                int xIndex = i % 2;
                int yIndex = i % 10;
                IndexPageButton button = new IndexPageButton(2 + i, centerX + 15 + (xIndex * 200), centerY + 10 + (yIndex * 20) - (xIndex == 1 ? 20 : 0), StatCollector.translateToLocal("bestiary." + EnumBestiaryPages.values()[allPageTypes.get(i).ordinal()].toString().toLowerCase()));
                this.indexButtons.add(button);
                this.buttonList.add(button);
            }
        }

    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for(GuiButton button : this.buttonList){
            if(button.id >= 2){
                button.enabled = index;
                button.visible = index;
            }
        }
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(TEXTURE);
        int cornerX = (this.width - this.X) / 2;
        int cornerY = (this.height - this.Y) / 2;
        drawModalRectWithCustomSizedTexture(cornerX, cornerY, 0, 0, this.X, this.Y, 390F, 390F);
        if(!index){
            this.mc.renderEngine.bindTexture(DRAWINGS_0);
            drawModalRectWithCustomSizedTexture(cornerX, cornerY, 0, 0, this.X, this.Y, 126F, 126F);
            int pageLeft = bookPages + 1;
            int pageRight = bookPages + 2;
            font.drawString("" + pageLeft, cornerX + this.X / 4, cornerY + this.Y - (int)(this.Y * 0.13), 0X303030, false);
            font.drawString("" + pageRight, cornerX + this.X - (int)(this.X * 0.24), cornerY + this.Y - (int)(this.Y * 0.13), 0X303030, false);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.id == 0 && (this.index ? this.indexPagesTotal > 1 && this.indexPages > 0 : this.pageType != null && this.bookPages > 0)){
            if(this.index){
                this.indexPages--;
            }else{
                this.bookPages--;
            }        }
        if(button.id == 1 && (this.index ? this.indexPagesTotal > 1 && this.indexPages < this.indexPagesTotal : this.pageType != null && this.bookPages < this.pageType.pages)) {
            if(this.index){
                this.indexPages++;
            }else{
                this.bookPages++;
            }
        }
        if(button.id >= 2 && this.indexButtons.get(button.id - 2) != null && allPageTypes.get(button.id - 2) != null && button instanceof IndexPageButton){
            this.index = false;
            this.indexPages = 0;
            this.bookPages = 0;
            this.pageType = allPageTypes.get(button.id - 2);
        }
    }

}
