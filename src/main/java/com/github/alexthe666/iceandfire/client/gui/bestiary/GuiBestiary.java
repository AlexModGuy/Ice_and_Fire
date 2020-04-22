package com.github.alexthe666.iceandfire.client.gui.bestiary;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumSeaSerpent;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.IResource;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiBestiary extends GuiScreen {
    protected static final int X = 390;
    protected static final int Y = 245;
    private static final ResourceLocation TEXTURE = new ResourceLocation("iceandfire:textures/gui/bestiary/bestiary.png");
    private static final ResourceLocation DRAWINGS_0 = new ResourceLocation("iceandfire:textures/gui/bestiary/drawings_0.png");
    private static final ResourceLocation DRAWINGS_1 = new ResourceLocation("iceandfire:textures/gui/bestiary/drawings_1.png");
    private static final ResourceLocation DRAWINGS_2 = new ResourceLocation("iceandfire:textures/gui/bestiary/drawings_2.png");
    private static final Map<String, ResourceLocation> PICTURE_LOCATION_CACHE = Maps.newHashMap();
    public List<EnumBestiaryPages> allPageTypes = new ArrayList<EnumBestiaryPages>();
    public EnumBestiaryPages pageType;
    public List<IndexPageButton> indexButtons = new ArrayList<IndexPageButton>();
    public ChangePageButton previousPage;
    public ChangePageButton nextPage;
    public int bookPages;
    public int bookPagesTotal = 1;
    public int indexPages;
    public int indexPagesTotal = 1;
    protected ItemStack book;
    protected boolean index;
    protected FontRenderer font = getFont();

    public GuiBestiary(ItemStack book) {
        this.book = book;
        int indexPageTotal = 0;
        if (!book.isEmpty() && book.getItem() != null && book.getItem() == IafItemRegistry.bestiary) {
            if (book.getTagCompound() != null) {
                List<EnumBestiaryPages> pages = EnumBestiaryPages.containedPages(EnumBestiaryPages.toList(book.getTagCompound().getIntArray("Pages")));
                allPageTypes.addAll(pages);
                indexPagesTotal = (int) Math.ceil(pages.size() / 10D);
            }
        }
        index = true;
    }

    private FontRenderer getFont() {
        FontRenderer font;
        if (IceAndFire.CONFIG.useVanillaFont || !Minecraft.getMinecraft().gameSettings.language.equalsIgnoreCase("en_us")) {
            font = Minecraft.getMinecraft().fontRenderer;
        } else {
            font = (FontRenderer) IceAndFire.PROXY.getFontRenderer();
        }
        return font;
    }

    public void initGui() {
        super.initGui();
        int centerX = (this.width - X) / 2;
        int centerY = (this.height - Y) / 2;
        this.buttonList.add(this.previousPage = new ChangePageButton(0, centerX + 15, centerY + 215, false, bookPages, 0));
        this.buttonList.add(this.nextPage = new ChangePageButton(1, centerX + 357, centerY + 215, true, bookPages, 0));
        if (!allPageTypes.isEmpty()) {
            for (int i = 0; i < allPageTypes.size(); i++) {
                int xIndex = i % -2;
                int yIndex = i % 10;
                IndexPageButton button = new IndexPageButton(2 + i, centerX + 15 + (xIndex * 200), centerY + 10 + (yIndex * 20) - (xIndex == 1 ? 20 : 0), StatCollector.translateToLocal("bestiary." + EnumBestiaryPages.values()[allPageTypes.get(i).ordinal()].toString().toLowerCase()));
                this.indexButtons.add(button);
                this.buttonList.add(button);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (GuiButton button : this.buttonList) {
            if (button.id >= 2) {
                button.enabled = index;
                button.visible = index;
            }
        }
        for (int i = 0; i < this.indexButtons.size(); i++) {
            this.indexButtons.get(i).visible = i < 10 * (this.indexPages + 1) && i >= 10 * (this.indexPages) && this.index;
        }
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(TEXTURE);
        int cornerX = (this.width - X) / 2;
        int cornerY = (this.height - Y) / 2;
        drawModalRectWithCustomSizedTexture(cornerX, cornerY, 0, 0, X, Y, 390F, 390F);
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
        if (!index) {
            drawPerPage(bookPages);
            int pageLeft = bookPages * 2 + 1;
            int pageRight = pageLeft + 1;
            font.drawString("" + pageLeft, X / 4, Y - (int) (Y * 0.13), 0X303030, false);
            font.drawString("" + pageRight, X - (int) (X * 0.24), Y - (int) (Y * 0.13), 0X303030, false);
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

    public void drawPerPage(int bookPages) {
        imageFromTxt();
        switch (this.pageType) {
            case INTRODUCTION:
                if (bookPages == 1) {
                    drawItemStack(new ItemStack(IafBlockRegistry.sapphireOre), 30, 20, 2.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.sapphireGem), 40, 60, 2F);
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 0, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    boolean drawGold = Minecraft.getMinecraft().player.ticksExisted % 20 < 10;
                    drawItemStack(new ItemStack(drawGold ? Items.GOLD_NUGGET : IafItemRegistry.silverNugget), 144, 34, 1.5F);
                    drawItemStack(new ItemStack(drawGold ? Items.GOLD_NUGGET : IafItemRegistry.silverNugget), 161, 34, 1.5F);
                    drawItemStack(new ItemStack(drawGold ? IafBlockRegistry.goldPile : IafBlockRegistry.silverPile), 151, 7, 2F);
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 90, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(Blocks.PLANKS), 161, 124, 1.5F);
                    drawItemStack(new ItemStack(Blocks.PLANKS), 161, 107, 1.5F);
                    drawItemStack(new ItemStack(Items.BOOK), 161, 91, 1.5F);
                    drawItemStack(new ItemStack(IafBlockRegistry.lectern), 151, 78, 2F);
                }
                break;
            case FIREDRAGON:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 20, 50, 127, 0, 88, 62, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_0, 38, 60, 0, 0, 127, 62, 512F);
                    drawImage(DRAWINGS_0, 240, 5, 0, 185, 114, 62, 512F);
                    drawImage(DRAWINGS_0, 240, 150, 0, 62, 99, 37, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 2) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_0, 42, 80, 0, 99, 100, 41, 512F);
                    drawImage(DRAWINGS_0, 44, 160, 0, 140, 95, 45, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 3) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_0, 45, 50, 214, 0, 87, 63, 512F);
                    drawImage(DRAWINGS_0, 45, 110, 214, 62, 89, 62, 512F);
                    drawImage(DRAWINGS_0, 245, 10, 214, 124, 89, 63, 512F);
                    drawImage(DRAWINGS_0, 245, 70, 214, 184, 89, 64, 512F);
                    drawImage(DRAWINGS_0, 245, 130, 300, 0, 88, 62, 512F);
                    GL11.glPopMatrix();
                }
                break;
            case FIREDRAGONEGG:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_0, 20, 95, 327, 118, 24, 33, 512F);
                    drawImage(DRAWINGS_0, 60, 95, 303, 118, 24, 33, 512F);
                    drawImage(DRAWINGS_0, 95, 95, 351, 118, 24, 33, 512F);
                    drawImage(DRAWINGS_0, 135, 95, 375, 118, 24, 33, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 25, 20, 303, 62, 71, 56, 512F);
                    GL11.glPopMatrix();
                }
                break;
            case ICEDRAGON:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 20, 50, 127, 248, 87, 62, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_0, 38, 60, 0, 247, 126, 61, 512F);
                    drawImage(DRAWINGS_0, 240, 5, 0, 432, 114, 62, 512F);
                    drawImage(DRAWINGS_0, 240, 150, 0, 309, 99, 37, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 2) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_0, 42, 80, 0, 346, 100, 41, 512F);
                    drawImage(DRAWINGS_0, 44, 160, 0, 387, 95, 45, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 3) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_0, 50, 52, 214, 248, 86, 62, 512F);
                    drawImage(DRAWINGS_0, 50, 112, 214, 310, 87, 62, 512F);
                    drawImage(DRAWINGS_0, 250, 12, 214, 372, 87, 63, 512F);
                    drawImage(DRAWINGS_0, 250, 72, 214, 432, 87, 64, 512F);
                    drawImage(DRAWINGS_0, 250, 132, 300, 248, 71, 62, 512F);
                    GL11.glPopMatrix();
                }
                break;
            case ICEDRAGONEGG:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_0, 20, 95, 327, 366, 24, 33, 512F);
                    drawImage(DRAWINGS_0, 60, 95, 303, 366, 24, 33, 512F);
                    drawImage(DRAWINGS_0, 95, 95, 351, 366, 24, 33, 512F);
                    drawImage(DRAWINGS_0, 135, 95, 375, 366, 24, 33, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 25, 20, 303, 309, 70, 56, 512F);
                    GL11.glPopMatrix();
                }
                break;
            case TAMEDDRAGONS:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 90, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(Items.BONE), 145, 124, 1.5F);
                    drawItemStack(new ItemStack(Items.PORKCHOP), 145, 107, 1.5F);
                    drawItemStack(new ItemStack(Items.BONE), 145, 91, 1.5F);
                    drawItemStack(new ItemStack(Items.PORKCHOP), 161, 124, 1.5F);
                    drawItemStack(new ItemStack(Items.BONE), 161, 107, 1.5F);
                    drawItemStack(new ItemStack(Items.PORKCHOP), 161, 91, 1.5F);
                    drawItemStack(new ItemStack(Items.BONE), 177, 124, 1.5F);
                    drawItemStack(new ItemStack(Items.PORKCHOP), 177, 107, 1.5F);
                    drawItemStack(new ItemStack(Items.BONE), 177, 91, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_meal), 151, 78, 2F);
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 0, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_skull), 161, 17, 1.5F);
                    drawItemStack(new ItemStack(Items.STICK), 161, 32, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_stick), 151, 10, 2F);
                }
                if (bookPages == 2) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafBlockRegistry.fire_lily), 5, 14, 2.5F);
                    drawItemStack(new ItemStack(IafBlockRegistry.frost_lily), 30, 14, 2.5F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 0, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    boolean drawFire = Minecraft.getMinecraft().player.ticksExisted % 40 < 20;
                    drawItemStack(new ItemStack(drawFire ? IafBlockRegistry.fire_lily : IafBlockRegistry.frost_lily), 161, 17, 1.5F);
                    drawItemStack(new ItemStack(Items.BOWL), 161, 32, 1.5F);
                    drawItemStack(new ItemStack(drawFire ? Items.BLAZE_ROD : Items.PRISMARINE_CRYSTALS), 177, 17, 1.5F);
                    drawItemStack(new ItemStack(drawFire ? IafItemRegistry.fire_stew : IafItemRegistry.frost_stew), 151, 10, 2F);
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 65, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(Items.STICK), 144, 97, 1.5F);
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragonbone), 180, 110, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragonbone), 180, 92, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragonbone), 198, 92, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragonbone), 198, 74, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_horn), 151, 60, 2F);

                }
                if (bookPages == 3) {
                    int j = 18;
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_armor_iron, 1, 0), j += 16, 60, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_armor_iron, 1, 1), j += 16, 60, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_armor_iron, 1, 2), j += 16, 60, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_armor_iron, 1, 3), j += 16, 60, 1.5F);

                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 10, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragonbone), 160, 12, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragonbone), 180, 31, 1.5F);
                    drawItemStack(new ItemStack(Items.IRON_INGOT), 199, 50, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_flute), 151, 18, 2F);
                }
                break;
            case MATERIALS:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragonscales_red), 18, 16, 2.5F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragonbone), 70, 10, 2.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.witherbone), 112, 70, 2.5F);
                    {
                        int j = 18;
                        drawItemStack(new ItemStack(EnumDragonArmor.armor_red.helmet), j += 16, 115, 1.5F);
                        drawItemStack(new ItemStack(EnumDragonArmor.armor_red.chestplate), j += 16, 115, 1.5F);
                        drawItemStack(new ItemStack(EnumDragonArmor.armor_red.leggings), j += 16, 115, 1.5F);
                        drawItemStack(new ItemStack(EnumDragonArmor.armor_red.boots), j += 16, 115, 1.5F);
                    }
                }
                if (bookPages == 1) {
                    {
                        int j = 1;
                        drawItemStack(new ItemStack(IafItemRegistry.dragonbone_sword), j += 16, 14, 1.5F);
                        drawItemStack(new ItemStack(IafItemRegistry.dragonbone_pickaxe), j += 16, 14, 1.5F);
                        drawItemStack(new ItemStack(IafItemRegistry.dragonbone_axe), j += 16, 14, 1.5F);
                        drawItemStack(new ItemStack(IafItemRegistry.dragonbone_shovel), j += 16, 14, 1.5F);
                        drawItemStack(new ItemStack(IafItemRegistry.dragonbone_hoe), j += 16, 14, 1.5F);
                        drawItemStack(new ItemStack(IafItemRegistry.dragonbone_bow), j += 16, 14, 1.5F);
                    }
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.fire_dragon_flesh), 18, 24, 2.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.fire_dragon_heart), 70, 14, 2.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.dragon_skull), 70, 39, 2.5F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 2) {

                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.fire_dragon_blood), 18, 24, 2.5F);
                    GL11.glPopMatrix();
                }
                break;
            case ALCHEMY:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.fire_dragon_blood), 10, 24, 2.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.ice_dragon_blood), 26, 24, 2.5F);
                    GL11.glPopMatrix();
                    boolean drawFire = Minecraft.getMinecraft().player.ticksExisted % 40 < 20;
                    drawItemStack(new ItemStack(IafItemRegistry.dragonbone_sword), 161, 17, 1.5F);
                    drawItemStack(new ItemStack(drawFire ? IafItemRegistry.fire_dragon_blood : IafItemRegistry.ice_dragon_blood), 161, 32, 1.5F);
                    drawItemStack(new ItemStack(drawFire ? IafItemRegistry.dragonbone_sword_fire : IafItemRegistry.dragonbone_sword_ice), 151, 10, 2F);
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 0, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                }
                break;
            case VILLAGERS:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 35, 50, 388, 52, 58, 36, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.fishing_spear), 70, 2, 2.5F);
                    GL11.glPopMatrix();
                }
                break;
            case HIPPOGRYPH:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.8F, 0.8F, 1F);
                    drawImage(DRAWINGS_0, 29, 150, 303, 151, 61, 36, 512F);
                    drawImage(DRAWINGS_0, 91, 150, 364, 151, 61, 36, 512F);
                    drawImage(DRAWINGS_0, 151, 150, 425, 151, 61, 36, 512F);
                    drawImage(DRAWINGS_0, 29, 190, 303, 187, 61, 36, 512F);
                    drawImage(DRAWINGS_0, 91, 190, 364, 187, 61, 36, 512F);
                    drawImage(DRAWINGS_0, 151, 190, 425, 187, 61, 36, 512F);
                    drawImage(DRAWINGS_0, 90, 230, 425, 223, 61, 35, 512F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(Items.RABBIT_FOOT), 70, 20, 2.5F);
                    GL11.glPopMatrix();
                    GL11.glPopMatrix();
                }

                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(Items.STICK), 16, 24, 2.5F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 10, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Items.FEATHER), 160, 31, 1.5F);
                    int drawType = Minecraft.getMinecraft().player.ticksExisted % 60 > 40 ? 2 : Minecraft.getMinecraft().player.ticksExisted % 60 > 20 ? 1 : 0;
                    drawItemStack(new ItemStack(drawType == 0 ? Items.IRON_HORSE_ARMOR : drawType == 1 ? Items.GOLDEN_HORSE_ARMOR : Items.DIAMOND_HORSE_ARMOR), 180, 31, 1.5F);
                    drawItemStack(new ItemStack(Items.FEATHER), 199, 31, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(drawType == 0 ? IafItemRegistry.iron_hippogryph_armor : drawType == 1 ? IafItemRegistry.gold_hippogryph_armor : IafItemRegistry.diamond_hippogryph_armor), 151, 18, 2F);

                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(Items.RABBIT_STEW), 70, 23, 2.5F);
                    GL11.glPopMatrix();

                }
                break;
            case GORGON:
                if (bookPages == 0) {

                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 10, 89, 473, 117, 19, 34, 512F);
                    drawImage(DRAWINGS_0, 50, 78, 399, 106, 28, 45, 512F);
                    drawImage(DRAWINGS_0, 100, 89, 455, 117, 18, 34, 512F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 70, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Items.STRING), 160, 97, 1.5F);
                    drawItemStack(new ItemStack(Items.LEATHER), 180, 97, 1.5F);
                    drawItemStack(new ItemStack(Items.STRING), 199, 97, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.blindfold), 171, 65, 2F);

                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.gorgon_head), 16, 12, 2.5F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(1.7F, 1.7F, 1F);
                    drawImage(DRAWINGS_0, 37, 95, 473, 117, 19, 34, 512F);
                    drawImage(DRAWINGS_0, 60, 95, 455, 117, 18, 34, 512F);
                    GL11.glPopMatrix();

                }
                break;
            case PIXIE:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_0, 20, 60, 371, 258, 47, 35, 512F);
                    drawImage(DRAWINGS_0, 42, 95, 416, 258, 45, 35, 512F);
                    drawImage(DRAWINGS_0, 67, 60, 462, 258, 47, 35, 512F);
                    drawImage(DRAWINGS_0, 88, 95, 370, 293, 47, 35, 512F);
                    drawImage(DRAWINGS_0, 110, 60, 416, 293, 47, 35, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.pixie_dust), 70, 10, 2.5F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    GL11.glTranslatef(20, 24, 0);
                    GL11.glPushMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 100, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Blocks.GLASS), 160, 113, 1.5F);
                    drawItemStack(new ItemStack(Blocks.GLASS), 199, 113, 1.5F);
                    drawItemStack(new ItemStack(Blocks.PLANKS), 180, 113, 1.5F);
                    drawItemStack(new ItemStack(Blocks.GLASS), 160, 131, 1.5F);
                    drawItemStack(new ItemStack(Blocks.GLASS), 199, 131, 1.5F);
                    drawItemStack(new ItemStack(Blocks.GLASS), 180, 150, 1.5F);
                    drawItemStack(new ItemStack(Blocks.GLASS), 160, 150, 1.5F);
                    drawItemStack(new ItemStack(Blocks.GLASS), 199, 150, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafBlockRegistry.jar_empty), 171, 85, 2F);
                    GL11.glPopMatrix();
                    GL11.glPopMatrix();

                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.ambrosia), 19, 22, 2.5F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 100, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.pixie_dust), 180, 131, 1.5F);
                    drawItemStack(new ItemStack(Items.GOLDEN_CARROT), 160, 131, 1.5F);
                    drawItemStack(new ItemStack(Items.SPECKLED_MELON), 199, 131, 1.5F);
                    drawItemStack(new ItemStack(Items.BOWL), 180, 150, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.ambrosia), 171, 85, 2F);
                    GL11.glPopMatrix();
                }
                break;
            case CYCLOPS:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1.5F);
                    drawImage(DRAWINGS_0, 185, 8, 399, 328, 24, 63, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1.5F);
                    drawImage(DRAWINGS_0, 50, 35, 423, 328, 24, 63, 512F);
                    //drawImage(DRAWINGS_0, 68, 60, 447, 328, 24, 63, 512F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 50, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Items.LEATHER_HELMET), 180, 76, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 160, 76, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 199, 76, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 160, 57, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 180, 57, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 199, 57, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.sheep_helmet), 165, 45, 2F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 144, 95, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Items.LEATHER_CHESTPLATE), 180, 126, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 160, 126, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 199, 126, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 160, 107, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 199, 107, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 160, 145, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 180, 145, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 199, 145, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.sheep_chestplate), 165, 95, 2F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 2) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1.5F);
                    drawImage(DRAWINGS_0, 185, 30, 447, 328, 24, 63, 512F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 13, 24, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Items.LEATHER_LEGGINGS), 34, 46, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 14, 46, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 53, 46, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 14, 27, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 34, 27, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 53, 27, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 14, 65, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 53, 65, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.sheep_leggings), 64, 27, 2F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 13, 84, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Items.LEATHER_BOOTS), 34, 94, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 14, 113, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 53, 113, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 14, 94, 1.5F);
                    drawItemStack(new ItemStack(Blocks.WOOL), 53, 94, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.sheep_boots), 64, 73, 2F);
                    GL11.glPopMatrix();
                }
                break;
            case SIREN:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.25F, 1.25F, 1.25F);
                    drawImage(DRAWINGS_1, 190, 25, 0, 0, 25, 42, 512F);
                    drawImage(DRAWINGS_1, 220, 15, 25, 0, 25, 42, 512F);
                    drawImage(DRAWINGS_1, 255, 25, 50, 0, 25, 42, 512F);

                    drawImage(DRAWINGS_1, 190, 135, 0, 42, 26, 28, 512F);
                    drawImage(DRAWINGS_1, 220, 125, 26, 42, 26, 28, 512F);
                    drawImage(DRAWINGS_1, 255, 135, 52, 42, 26, 28, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.earplugs), 18, 40, 2.5F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    GL11.glPushMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 160, 0, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Item.getItemFromBlock(Blocks.WOODEN_BUTTON)), 180, 20, 1.5F);
                    drawItemStack(new ItemStack(Item.getItemFromBlock(Blocks.WOODEN_BUTTON)), 215, 20, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.earplugs), 170, 10, 2F);
                    GL11.glPopMatrix();
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.shiny_scales), 123, 75, 1.5F);
                    GL11.glPopMatrix();
                }
                break;
            case HIPPOCAMPUS:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_1, 210, 25, 0, 70, 57, 49, 512F);
                    drawImage(DRAWINGS_1, 265, 25, 57, 70, 57, 49, 512F);
                    drawImage(DRAWINGS_1, 320, 25, 0, 119, 57, 49, 512F);
                    drawImage(DRAWINGS_1, 210, 80, 57, 119, 57, 49, 512F);
                    drawImage(DRAWINGS_1, 265, 80, 0, 168, 57, 49, 512F);
                    drawImage(DRAWINGS_1, 320, 80, 57, 168, 57, 49, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(Blocks.SPONGE), 37, 33, 1.5F);
                    drawItemStack(new ItemStack(Items.PRISMARINE_CRYSTALS), 37, 73, 1.5F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 2) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(Items.STICK), 35, 25, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.shiny_scales), 35, 75, 1.5F);
                    GL11.glPopMatrix();
                }
                break;
            case DEATHWORM:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_1, 230, 25, 0, 217, 133, 16, 512F);
                    drawImage(DRAWINGS_1, 230, 50, 0, 233, 133, 16, 512F);
                    drawImage(DRAWINGS_1, 230, 75, 0, 249, 133, 16, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    drawImage(DRAWINGS_1, 25, 95, 0, 265, 148, 44, 512F);
                    drawImage(DRAWINGS_1, 250, 5, 0, 309, 81, 162, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 2) {
                    int drawType = Minecraft.getMinecraft().player.ticksExisted % 60 > 40 ? 2 : Minecraft.getMinecraft().player.ticksExisted % 60 > 20 ? 1 : 0;
                    GL11.glPushMatrix();
                    GL11.glScalef(2.5F, 2.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.deathworm_chitin, 1, drawType), 17, 30, 1.5F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(drawType == 2 ? IafItemRegistry.deathworm_red_helmet : drawType == 1 ? IafItemRegistry.deathworm_white_helmet : IafItemRegistry.deathworm_yellow_helmet), 92, 8, 1.5F);
                    drawItemStack(new ItemStack(drawType == 2 ? IafItemRegistry.deathworm_red_chestplate : drawType == 1 ? IafItemRegistry.deathworm_white_chestplate : IafItemRegistry.deathworm_yellow_chestplate), 112, 8, 1.5F);
                    drawItemStack(new ItemStack(drawType == 2 ? IafItemRegistry.deathworm_red_leggings : drawType == 1 ? IafItemRegistry.deathworm_white_leggings : IafItemRegistry.deathworm_yellow_leggings), 132, 8, 1.5F);
                    drawItemStack(new ItemStack(drawType == 2 ? IafItemRegistry.deathworm_red_boots : drawType == 1 ? IafItemRegistry.deathworm_white_boots : IafItemRegistry.deathworm_yellow_boots), 152, 8, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.deathworm_egg), 125, 42, 1.5F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 3) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawItemStack(new ItemStack(IafItemRegistry.deathworm_egg, 1, 1), 125, 4, 1.5F);
                    drawItemStack(new ItemStack(Items.FISHING_ROD), 115, 55, 1.5F);
                    drawItemStack(new ItemStack(Items.FISHING_ROD), 135, 55, 1.5F);

                    GL11.glPopMatrix();
                }
                break;
            case COCKATRICE:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_1, 155, 10, 114, 0, 88, 36, 512F);
                    drawImage(DRAWINGS_1, 155, 45, 114, 36, 88, 36, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 18, 10, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Items.STRING), 20, 30, 1.5F);
                    drawItemStack(new ItemStack(Items.LEATHER), 40, 30, 1.5F);
                    drawItemStack(new ItemStack(Items.STRING), 59, 30, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.blindfold), 60, 18, 2F);
                    drawItemStack(new ItemStack(IafItemRegistry.witherbone), 30, 58, 2.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.rotten_egg), 109, 18, 2.5F);
                }
                break;
            case STYMPHALIANBIRD:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_1, 34, 46, 114, 72, 59, 37, 512F);
                    drawImage(DRAWINGS_1, 155, 35, 114, 109, 67, 35, 512F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.stymphalian_bird_feather), 109, 60, 2.5F);
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 18, 10, 389, 1, 50, 50, 512F);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glScalef(0.9F, 0.9F, 1F);
                    drawItemStack(new ItemStack(Items.FLINT), 40, 13, 1.5F);
                    drawItemStack(new ItemStack(Items.STICK), 40, 30, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.stymphalian_bird_feather), 40, 49, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.stymphalian_arrow), 60, 18, 2F);

                }
                break;
            case TROLL:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_1, 15, 60, 156, 211, 25, 58, 512F);
                    drawImage(DRAWINGS_1, 50, 55, 181, 211, 25, 58, 512F);
                    drawImage(DRAWINGS_1, 85, 60, 206, 211, 25, 58, 512F);
                    drawImage(DRAWINGS_1, 155, 22, 114, 145, 24, 66, 512F);
                    drawImage(DRAWINGS_1, 190, 19, 188, 142, 47, 69, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    int i = (Minecraft.getMinecraft().player.ticksExisted % (EnumTroll.Weapon.values().length * 20)) / 20;
                    drawItemStack(new ItemStack(EnumTroll.Weapon.values()[i].item), 30, 7, 2.5F);
                    int j = (Minecraft.getMinecraft().player.ticksExisted % (EnumTroll.values().length * 20)) / 20;
                    drawItemStack(new ItemStack(EnumTroll.values()[j].leather), 100, 30, 2.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.troll_tusk), 120, 30, 2.5F);
                }
                if (bookPages == 2) {
                    int j = (Minecraft.getMinecraft().player.ticksExisted % (EnumTroll.values().length * 20)) / 20;
                    drawItemStack(new ItemStack(EnumTroll.values()[j].helmet), 27, 15, 1.5F);
                    drawItemStack(new ItemStack(EnumTroll.values()[j].chestplate), 47, 15, 1.5F);
                    drawItemStack(new ItemStack(EnumTroll.values()[j].leggings), 67, 15, 1.5F);
                    drawItemStack(new ItemStack(EnumTroll.values()[j].boots), 87, 15, 1.5F);
                }
                break;
            case MYRMEX:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.51F, 1.51F, 1F);
                    drawImage(DRAWINGS_1, 137, 10, 202, 16, 57, 21, 512F);
                    drawImage(DRAWINGS_1, 195, 10, 278, 16, 57, 21, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.51F, 1.51F, 1F);
                    drawImage(DRAWINGS_1, 7, 17, 202, 37, 59, 21, 512F);
                    drawImage(DRAWINGS_1, 65, 17, 278, 37, 59, 21, 512F);
                    drawImage(DRAWINGS_1, 7, 77, 202, 58, 59, 21, 512F);
                    drawImage(DRAWINGS_1, 65, 77, 278, 58, 59, 21, 512F);
                    drawImage(DRAWINGS_1, 145, 20, 278, 103, 43, 45, 512F);
                    drawImage(DRAWINGS_1, 195, 20, 321, 103, 43, 45, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 2) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.51F, 1.51F, 1F);
                    drawImage(DRAWINGS_1, 25, 13, 202, 79, 76, 24, 512F);
                    drawImage(DRAWINGS_1, 25, 40, 278, 79, 76, 24, 512F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.myrmex_desert_chitin), 125, 43, 2F);
                    drawItemStack(new ItemStack(IafItemRegistry.myrmex_jungle_chitin), 155, 43, 2F);
                    int i = 133;
                    boolean jungle = Minecraft.getMinecraft().player.ticksExisted % 60 > 30;
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_shovel : IafItemRegistry.myrmex_desert_shovel), i += 16, 100, 1.51F);
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_pickaxe : IafItemRegistry.myrmex_desert_pickaxe), i += 16, 100, 1.5F);
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_axe : IafItemRegistry.myrmex_desert_axe), i += 16, 100, 1.5F);
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_sword : IafItemRegistry.myrmex_desert_sword), i += 16, 100, 1.5F);
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_sword_venom : IafItemRegistry.myrmex_desert_sword_venom), i += 16, 100, 1.5F);
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_hoe : IafItemRegistry.myrmex_desert_hoe), i += 16, 100, 1.5F);
                    int j = 148;
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_helmet : IafItemRegistry.myrmex_desert_helmet), j += 16, 115, 1.5F);
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_chestplate : IafItemRegistry.myrmex_desert_chestplate), j += 16, 115, 1.5F);
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_leggings : IafItemRegistry.myrmex_desert_leggings), j += 16, 115, 1.5F);
                    drawItemStack(new ItemStack(jungle ? IafItemRegistry.myrmex_jungle_boots : IafItemRegistry.myrmex_desert_boots), j += 16, 115, 1.5F);
                }
                if (bookPages == 3) {
                    drawItemStack(new ItemStack(IafItemRegistry.myrmex_stinger), 35, 22, 2.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.myrmex_desert_resin), 25, 64, 2F);
                    drawItemStack(new ItemStack(IafItemRegistry.myrmex_jungle_resin), 55, 64, 2F);
                }
                if (bookPages == 4) {
                    drawItemStack(new ItemStack(IafItemRegistry.myrmex_desert_staff), 25, 73, 2F);
                    drawItemStack(new ItemStack(IafItemRegistry.myrmex_jungle_staff), 55, 73, 2F);

                    drawItemStack(new ItemStack(IafItemRegistry.myrmex_desert_egg), 125, 90, 2F);
                    drawItemStack(new ItemStack(IafItemRegistry.myrmex_jungle_egg), 155, 90, 2F);
                }
                break;
            case AMPHITHERE:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.75F, 0.75F, 0.75F);
                    drawImage(DRAWINGS_1, 70, 97, 257, 163, 136, 93, 512F);
                    drawImage(DRAWINGS_1, 270, 50, 148, 267, 120, 51, 512F);
                    drawImage(DRAWINGS_1, 380, 50, 148, 318, 120, 51, 512F);
                    drawImage(DRAWINGS_1, 270, 100, 148, 369, 120, 51, 512F);
                    drawImage(DRAWINGS_1, 380, 100, 148, 420, 120, 51, 512F);
                    drawImage(DRAWINGS_1, 330, 150, 268, 267, 120, 51, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 2) {
                    drawItemStack(new ItemStack(IafItemRegistry.amphithere_feather), 30, 20, 2.5F);
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 18, 70, 389, 1, 50, 50, 512F);
                    GL11.glScalef(0.65F, 0.65F, 0.65F);
                    drawItemStack(new ItemStack(Items.FLINT), 36, 73, 1.5F);
                    drawItemStack(new ItemStack(Items.STICK), 36, 89, 1.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.amphithere_feather), 36, 106, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.amphithere_arrow), 60, 65, 2F);
                }
                break;
            case SEASERPENT:
                if (bookPages == 0) {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.75F, 0.75F, 0.75F);
                    drawImage(DRAWINGS_1, 290, 5, 422, 0, 90, 64, 512F);
                    drawImage(DRAWINGS_1, 380, 5, 422, 64, 90, 64, 512F);
                    drawImage(DRAWINGS_1, 290, 70, 422, 128, 90, 64, 512F);
                    drawImage(DRAWINGS_1, 380, 70, 422, 192, 90, 64, 512F);
                    drawImage(DRAWINGS_1, 290, 140, 422, 256, 90, 64, 512F);
                    drawImage(DRAWINGS_1, 380, 140, 422, 320, 90, 64, 512F);
                    drawImage(DRAWINGS_1, 345, 210, 422, 384, 90, 64, 512F);
                    GL11.glPopMatrix();
                }
                if (bookPages == 1) {
                    drawImage(DRAWINGS_1, 60, 90, 337, 0, 70, 83, 512F);
                    int j = (Minecraft.getMinecraft().player.ticksExisted % (EnumSeaSerpent.values().length * 20)) / 20;
                    drawItemStack(new ItemStack(EnumSeaSerpent.values()[j].scale), 130, 40, 2.5F);
                    drawItemStack(new ItemStack(IafItemRegistry.sea_serpent_fang), 90, 40, 2.5F);
                }
                if (bookPages == 2) {
                    GL11.glPushMatrix();
                    GL11.glScalef(1.5F, 1.5F, 1F);
                    drawImage(DRAWINGS_0, 18, 30, 389, 1, 50, 50, 512F);
                    GL11.glScalef(0.65F, 0.65F, 0.65F);
                    int j = (Minecraft.getMinecraft().player.ticksExisted % (EnumSeaSerpent.values().length * 20)) / 20;
                    drawItemStack(new ItemStack(IafItemRegistry.sea_serpent_fang), 36, 32, 1.5F);
                    drawItemStack(new ItemStack(Items.STICK), 36, 48, 1.5F);
                    drawItemStack(new ItemStack(EnumSeaSerpent.values()[j].scale), 36, 66, 1.5F);
                    drawItemStack(new ItemStack(EnumSeaSerpent.values()[j].helmet), 34, 125, 1.5F);
                    drawItemStack(new ItemStack(EnumSeaSerpent.values()[j].chestplate), 50, 125, 1.5F);
                    drawItemStack(new ItemStack(EnumSeaSerpent.values()[j].leggings), 66, 125, 1.5F);
                    drawItemStack(new ItemStack(EnumSeaSerpent.values()[j].boots), 82, 125, 1.5F);
                    GL11.glPopMatrix();
                    drawItemStack(new ItemStack(IafItemRegistry.sea_serpent_arrow), 60, 33, 2F);
                }
                break;
        }
        writeFromTxt();
    }

    public void imageFromTxt() {
        String fileName = this.pageType.toString().toLowerCase() + "_" + this.bookPages + ".txt";
        ResourceLocation fileLoc = new ResourceLocation("iceandfire:lang/bestiary/" + Minecraft.getMinecraft().gameSettings.language + "_0/" + fileName);
        ResourceLocation backupLoc = new ResourceLocation("iceandfire:lang/bestiary/en_us_0/" + fileName);
        IResource resource = null;

        try {
            resource = Minecraft.getMinecraft().getResourceManager().getResource(fileLoc);
        } catch (IOException e) {
            try {
                resource = Minecraft.getMinecraft().getResourceManager().getResource(backupLoc);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            Iterator iterator = IOUtils.readLines(resource.getInputStream(), "UTF-8").iterator();
            String line = null;
            int linenumber = 0;
            while (iterator.hasNext()) {
                line = ((String) iterator.next()).trim();
                if (line.contains("<") || line.contains(">")) {
                    if (line.contains("<image>")) {
                        line = line.substring(8, line.length() - 1);
                        String[] split = line.split(" ");
                        String texture = "iceandfire:textures/gui/bestiary/" + split[0];
                        ResourceLocation resourcelocation = PICTURE_LOCATION_CACHE.get(texture);
                        if (resourcelocation == null) {
                            resourcelocation = new ResourceLocation(texture);
                            PICTURE_LOCATION_CACHE.put(texture, resourcelocation);
                        }
                        GL11.glPushMatrix();
                        drawImage(resourcelocation, Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]), Integer.parseInt(split[7]) * 512F);
                        GL11.glPopMatrix();
                    }
                }
                if (line.contains("<item>")) {
                    line = line.substring(7, line.length() - 1);
                    String[] split = line.split(" ");
                    RenderHelper.enableGUIStandardItemLighting();
                    drawItemStack(new ItemStack(Item.getByNameOrId(split[0]), 1, Integer.parseInt(split[1])), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Float.parseFloat(split[4]) * 2F);
                }
                if (line.contains("<recipe>")) {
                    line = line.substring(9, line.length() - 1);
                    String[] split = line.split(" ");
                    RenderHelper.enableGUIStandardItemLighting();
                    float scale = Float.parseFloat(split[split.length - 1]);
                    int x = Integer.parseInt(split[split.length - 3]);
                    int y = Integer.parseInt(split[split.length - 2]);
                    ItemStack result = new ItemStack(Item.getByNameOrId(split[0]), 1, Integer.parseInt(split[1]));
                    ItemStack[] ingredients = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY};
                    int j = 8;
                    for (int i = split.length - 5; i >= 2; i -= 2) {
                        ingredients[j] = new ItemStack(Item.getByNameOrId(split[i]), 1, Integer.parseInt(split[i + 1]));
                        j--;
                    }
                    RenderHelper.enableGUIStandardItemLighting();
                    GL11.glPushMatrix();
                    GL11.glTranslatef(x, y, 0);
                    GL11.glScalef(scale, scale, 0);
                    drawRecipe(result, ingredients);
                    GL11.glPopMatrix();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawRecipe(ItemStack result, ItemStack[] ingredients) {
        drawItemStack(result, 62, 17, 2F);
        for (int i = 0; i < 9; i++) {
            drawItemStack(ingredients[i], ((i % 3) * 22 + 30), ((i / 3) * 22 + 10), 1.25F);
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(37F, 13, 1F);
        GL11.glScalef(1.5F, 1.5F, 1F);
        drawImage(DRAWINGS_0, 0, 0, 389, 1, 50, 50, 512F);
        GL11.glPopMatrix();

    }

    public void writeFromTxt() {
        String fileName = this.pageType.toString().toLowerCase() + "_" + this.bookPages + ".txt";
        ResourceLocation fileLoc = new ResourceLocation("iceandfire:lang/bestiary/" + Minecraft.getMinecraft().gameSettings.language + "_0/" + fileName);
        ResourceLocation backupLoc = new ResourceLocation("iceandfire:lang/bestiary/en_us_0/" + fileName);
        IResource resource = null;

        try {
            resource = Minecraft.getMinecraft().getResourceManager().getResource(fileLoc);
        } catch (IOException e) {
            try {
                resource = Minecraft.getMinecraft().getResourceManager().getResource(backupLoc);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            Iterator iterator = IOUtils.readLines(resource.getInputStream(), "UTF-8").iterator();
            String line = null;
            int linenumber = 0;
            while (iterator.hasNext()) {
                line = ((String) iterator.next()).trim();
                if (line.contains("<") || line.contains(">")) {
                    continue;
                }
                GL11.glPushMatrix();
                if (usingVanillaFont()) {
                    GL11.glScalef(0.945F, 0.945F, 0.945F);
                    GL11.glTranslatef(0, 5.5F, 0);
                }
                if (linenumber <= 19) {
                    font.drawString(line, 15, 20 + linenumber * 10, 0X303030, false);
                } else {
                    font.drawString(line, 220, (linenumber - 19) * 10, 0X303030, false);
                }
                linenumber++;
                GL11.glPopMatrix();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GL11.glPushMatrix();
        GL11.glScalef(2, 2, 2);
        font.drawString(StatCollector.translateToLocal("bestiary." + this.pageType.toString().toLowerCase()), 10, 2, 0X7A756A, false);
        GL11.glPopMatrix();
    }

    private boolean usingVanillaFont() {
        return font == Minecraft.getMinecraft().fontRenderer;
    }

    public void drawImage(ResourceLocation texture, int x, int y, int u, int v, int width, int height, float scale) {
        this.mc.renderEngine.bindTexture(texture);
        drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, scale, scale);
    }

    private void drawItemStack(ItemStack stack, int x, int y, float scale) {
        GL11.glPushMatrix();
        GlStateManager.translate(0, 0, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = null;
        if (!stack.isEmpty()) font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        GL11.glScalef(scale, scale, scale);
        this.itemRender.zLevel = 5;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GL11.glPopMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0 && (this.index ? this.indexPages > 0 : this.pageType != null)) {
            if (this.index) {
                this.indexPages--;
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(IafSoundRegistry.BESTIARY_PAGE, 1.0F));
            } else {
                if (this.bookPages > 0) {
                    this.bookPages--;
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(IafSoundRegistry.BESTIARY_PAGE, 1.0F));
                } else {
                    this.index = true;
                }
            }
        }
        if (button.id == 1 && (this.index ? this.indexPages < this.indexPagesTotal - 1 : this.pageType != null && this.bookPages < this.pageType.pages)) {
            if (this.index) {
                this.indexPages++;
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(IafSoundRegistry.BESTIARY_PAGE, 1.0F));
            } else {
                this.bookPages++;
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(IafSoundRegistry.BESTIARY_PAGE, 1.0F));
            }
        }
        if (button.id >= 2 && this.indexButtons.get(button.id - 2) != null && allPageTypes.get(button.id - 2) != null && button instanceof IndexPageButton) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(IafSoundRegistry.BESTIARY_PAGE, 1.0F));
            this.index = false;
            this.indexPages = 0;
            this.bookPages = 0;
            this.pageType = allPageTypes.get(button.id - 2);
        }
    }

}
