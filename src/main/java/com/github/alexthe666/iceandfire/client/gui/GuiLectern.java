package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.glu.Project;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class GuiLectern extends GuiContainer {
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("iceandfire:textures/gui/lectern.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("iceandfire:textures/models/lectern_book.png");
    private static final ModelBook MODEL_BOOK = new ModelBook();
    private final InventoryPlayer playerInventory;
    private final Random random = new Random();
    private final ContainerLectern container;
    private final IInventory nameable;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;

    public GuiLectern(InventoryPlayer playerInv, IInventory furnaceInv) {
        super(new ContainerLectern(playerInv, furnaceInv));
        this.playerInventory = playerInv;
        this.container = (ContainerLectern) this.inventorySlots;
        this.nameable = furnaceInv;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.nameable.getDisplayName().getUnformattedText(), 12, 5, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    public void updateScreen() {
        super.updateScreen();
        this.container.onUpdate();
        this.tickBook();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        for (int k = 0; k < 3; ++k) {
            int l = mouseX - (i + 60);
            int i1 = mouseY - (j + 14 + 19 * k);

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.container.enchantItem(this.mc.player, k)) {
                this.mc.playerController.sendEnchantPacket(this.container.windowId, k);
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        GlStateManager.viewport((scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
        GlStateManager.translate(-0.34F, 0.23F, 0.0F);
        Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
        float f = 1.0F;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0F, 3.3F, -16.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        float f1 = 5.0F;
        GlStateManager.scale(5.0F, 5.0F, 5.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        float f2 = this.oOpen + (this.open - this.oOpen) * partialTicks;
        GlStateManager.translate((1.0F - f2) * 0.2F, (1.0F - f2) * 0.1F, (1.0F - f2) * 0.25F);
        GlStateManager.rotate(-(1.0F - f2) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        float f3 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.25F;
        float f4 = this.oFlip + (this.flip - this.oFlip) * partialTicks + 0.75F;
        f3 = (f3 - (float) MathHelper.fastFloor((double) f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float) MathHelper.fastFloor((double) f4)) * 1.6F - 0.3F;

        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        if (f4 < 0.0F) {
            f4 = 0.0F;
        }

        if (f3 > 1.0F) {
            f3 = 1.0F;
        }

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        GlStateManager.enableRescaleNormal();
        MODEL_BOOK.render(null, 0.0F, f3, f4, f2, 0.0F, 0.0625F);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        //EnchantmentNameParts.getInstance().reseedRandomGenerator((long) this.container.xpSeed);
        int k = this.container.getManuscriptAmount();

        for (int l = 0; l < 3; ++l) {
            int i1 = i + 60;
            int j1 = i1 + 20;
            this.zLevel = 0.0F;
            this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
            int k1 = this.container.getPossiblePages()[l] == null ? -1 : this.container.getPossiblePages()[l].ordinal();//enchantment level
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (k1 == -1) {
                this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 185, 108, 19);
            } else {
                int l1 = 86;
                String s1 = I18n.format("bestiary." + this.container.getPossiblePages()[l].toString().toLowerCase());//EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRenderer, l1);
                FontRenderer fontrenderer = this.mc.fontRenderer;
                float textScale = 1.0F;
                if (fontrenderer.getStringWidth(s1) > 80) {
                    textScale = 1.0F - (fontrenderer.getStringWidth(s1) - 80) * 0.02F;
                }
                int i2 = 6839882;
                int j2 = mouseX - (i + 60);
                int k2 = mouseY - (j + 14 + 19 * l);
                if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19) {
                    this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 204, 108, 19);
                    i2 = 16777088;
                } else {
                    this.drawTexturedModalRect(i1, j + 14 + 19 * l, 0, 166, 108, 19);
                }
                this.drawTexturedModalRect(i1 + 1, j + 15 + 19 * l, 16 * l, 223, 16, 16);
                GlStateManager.pushMatrix();
                GlStateManager.translate(width / 2 - 10, height / 2 - 83 + (1.0F - textScale) * 45, 2);
                GlStateManager.scale(textScale, textScale, 1);
                fontrenderer.drawString(s1, 0, 20 + 19 * l, i2);
                GlStateManager.popMatrix();
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        partialTicks = this.mc.getTickLength();
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        boolean flag = this.mc.player.capabilities.isCreativeMode;
        int i = this.container.getManuscriptAmount();

        for (int j = 0; j < 3; ++j) {
            int k = 1;
            EnumBestiaryPages enchantment = this.container.getPossiblePages()[j];
            int i1 = 3;

            if (this.isPointInRegion(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0) {
                List<String> list = Lists.newArrayList();

                if (enchantment == null){
                    list.add(TextFormatting.RED + I18n.format("container.lectern.no_bestiary"));
                }
                else if (!flag) {
                    list.add("" + TextFormatting.WHITE + TextFormatting.ITALIC + I18n.format(enchantment == null ? "" : "bestiary." + enchantment.name().toLowerCase()));
                    TextFormatting textformatting = i >= i1 ? TextFormatting.GRAY : TextFormatting.RED;
                    list.add(textformatting + "" + I18n.format("container.lectern.costs"));
                    String s = I18n.format("container.lectern.manuscript.many", i1);
                    list.add(textformatting + "" + s);
                }

                this.drawHoveringText(list, mouseX, mouseY);
                break;
            }
        }
    }

    public void tickBook() {
        ItemStack itemstack = this.inventorySlots.getSlot(0).getStack();

        if (!ItemStack.areItemStacksEqual(itemstack, this.last)) {
            this.last = itemstack;

            while (true) {
                this.flipT += (float) (this.random.nextInt(4) - this.random.nextInt(4));

                if (this.flip > this.flipT + 1.0F || this.flip < this.flipT - 1.0F) {
                    break;
                }
            }
        }

        ++this.ticks;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;

        for (int i = 0; i < 3; ++i) {
            if (this.container.getPossiblePages()[i] != null) {
                flag = true;
            }
        }

        if (flag) {
            this.open += 0.2F;
        } else {
            this.open -= 0.2F;
        }

        this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        float f = 0.2F;
        f1 = MathHelper.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }

}