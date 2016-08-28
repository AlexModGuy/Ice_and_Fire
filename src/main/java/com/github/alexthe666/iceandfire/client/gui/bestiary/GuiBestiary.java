package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModBlocks;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef(cornerX, cornerY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) short1 / 1.0F, (float) short2 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        if(!index){
            drawPerPage(bookPages);
            int pageLeft = bookPages * 2 + 1;
            int pageRight = pageLeft + 1;
            font.drawString("" + pageLeft, this.X / 4, this.Y - (int)(this.Y * 0.13), 0X303030, false);
            font.drawString("" + pageRight,this.X - (int)(this.X * 0.24), this.Y - (int)(this.Y * 0.13), 0X303030, false);
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderHelper.enableStandardItemLighting();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawPerPage(int bookPages){
        switch(this.pageType){
            case INTRODUCTION:
                if(bookPages == 0) {
                    drawItemStack(new ItemStack(ModItems.manuscript), 30, 14, 2.5F);
                    drawItemStack(new ItemStack(ModBlocks.silverOre), 110, 15, 2.5F);
                    drawItemStack(new ItemStack(ModItems.silverIngot), 140, 48, 2F);
                    int i = 133;
                    drawItemStack(new ItemStack(ModItems.silver_shovel), i += 16, 85, 1.51F);
                    drawItemStack(new ItemStack(ModItems.silver_pickaxe), i += 16, 85, 1.5F);
                    drawItemStack(new ItemStack(ModItems.silver_axe), i += 16, 85, 1.5F);
                    drawItemStack(new ItemStack(ModItems.silver_sword), i += 16, 85, 1.5F);
                    drawItemStack(new ItemStack(ModItems.silver_hoe), i += 16, 85, 1.5F);
                    drawItemStack(new ItemStack(ModItems.silverNugget), i += 16, 85, 1.5F);
                    int j = 148;
                    drawItemStack(new ItemStack(ModItems.silver_helmet), j += 16, 100, 1.5F);
                    drawItemStack(new ItemStack(ModItems.silver_chestplate), j += 16, 100, 1.5F);
                    drawItemStack(new ItemStack(ModItems.silver_leggings), j += 16, 100, 1.5F);
                    drawItemStack(new ItemStack(ModItems.silver_boots), j += 16, 100, 1.5F);
                }
                if(bookPages == 1) {
                    drawItemStack(new ItemStack(ModBlocks.sapphireOre), 30, 20, 2.5F);
                    drawItemStack(new ItemStack(ModItems.sapphireGem), 40, 60, 2F);
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 0, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    boolean drawGold = Minecraft.getMinecraft().thePlayer.ticksExisted % 20 < 10;
                    drawItemStack(new ItemStack(drawGold ? Items.GOLD_NUGGET : ModItems.silverNugget), 144, 34, 1.5F);
                    drawItemStack(new ItemStack(drawGold ? Items.GOLD_NUGGET : ModItems.silverNugget), 161, 34, 1.5F);
                    drawItemStack(new ItemStack(drawGold ? ModBlocks.goldPile : ModBlocks.silverPile), 151, 7, 2F);
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 90, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(Blocks.PLANKS), 161, 124, 1.5F);
                    drawItemStack(new ItemStack(Blocks.PLANKS), 161, 107, 1.5F);
                    drawItemStack(new ItemStack(Items.BOOK), 161, 91, 1.5F);
                    drawItemStack(new ItemStack(ModBlocks.lectern), 151, 78, 2F);


                }
                if(bookPages == 2) {


                }
                writeFromTxt();
                break;
            case FIREDRAGON:
                break;
            case FIREDRAGONEGG:
                break;
            case ICEDRAGON:
                break;
            case ICEDRAGONEGG:
                break;
            case TAMEDDRAGONS:
                break;
            case MATERIALS:
                break;
            case ALCHEMY:
                break;
            case VILLAGERS:
                break;
        }
    }

    public void writeFromTxt(){
        String filePath = "assets/iceandfire/lang/bestiary/" + Minecraft.getMinecraft().gameSettings.language + "/";
        if (getClass().getClassLoader().getResourceAsStream(filePath) == null) {
            filePath = "assets/iceandfire/lang/bestiary/en_US/";
        }
        String fileName = this.pageType.toString().toLowerCase() + "_" + this.bookPages + ".txt";
        InputStream fileReader = getClass().getClassLoader().getResourceAsStream(filePath + fileName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileReader));
            String line = null;
            int linenumber = 0;
            while ((line = bufferedReader.readLine()) != null) {
                GL11.glPushMatrix();
                if (linenumber <= 19) {
                    font.drawString(line, 15, 20 + linenumber * 10, 0X303030, false);
                } else {
                    font.drawString(line, 220, (linenumber  - 19) * 10, 0X303030, false);
                }
                linenumber++;
                GL11.glPopMatrix();
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GL11.glPushMatrix();
        GL11.glScalef(2, 2, 2);
        font.drawString(StatCollector.translateToLocal("bestiary." + this.pageType.toString().toLowerCase()), 10, 2, 0X7A756A, false);
        GL11.glPopMatrix();
    }
    public void drawImage(ResourceLocation texture, int x, int y, int u, int v, int width, int height, float scale){
        this.mc.renderEngine.bindTexture(texture);
        drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, scale, scale);
    }

    private void drawItemStack(ItemStack stack, int x, int y, float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GlStateManager.translate(0, 0, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = null;
        if (stack != null) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRendererObj;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, null);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GL11.glPopMatrix();
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
