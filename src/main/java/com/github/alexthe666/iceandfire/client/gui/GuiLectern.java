package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class GuiLectern extends AbstractContainerScreen<ContainerLectern> {
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("iceandfire:textures/gui/lectern.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("iceandfire:textures/models/lectern_book.png");
    private static BookModel bookModel;
    private final Random random = new Random();
    private final Component nameable;
    public int ticks;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;
    private int flapTimer = 0;

    public GuiLectern(ContainerLectern container, Inventory inv, Component name) {
        super(container, inv, name);
        bookModel = new BookModel(this.minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
        this.nameable = name;
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        Font font = this.getMinecraft().font;
        font.draw(matrixStack, this.nameable.getString(), 12, 4, 4210752);
        font.draw(matrixStack, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.menu.onUpdate();
        this.tickBook();
    }

    @SuppressWarnings("resource")
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        for (int k = 0; k < 3; ++k) {
            double l = mouseX - (i + 60);
            double i1 = mouseY - (j + 14 + 19 * k);

            if (l >= 0 && i1 >= 0 && l < 108 && i1 < 19 && this.menu.clickMenuButton(getMinecraft().player, k)) {
                flapTimer = 5;
                this.getMinecraft().gameMode.handleInventoryButtonClick(this.menu.containerId, k);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        Lighting.setupForFlatItems();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ENCHANTMENT_TABLE_GUI_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int k = (int) this.minecraft.getWindow().getGuiScale();
        RenderSystem.viewport((this.width - 320) / 2 * k, (this.height - 240) / 2 * k, 320 * k, 240 * k);
        Matrix4f matrix4f = Matrix4f.createTranslateMatrix(-0.34F, 0.23F, 0.0F);
        matrix4f.multiply(Matrix4f.perspective(90.0D, 1.3333334F, 9.0F, 80.0F));
        RenderSystem.backupProjectionMatrix();
        RenderSystem.setProjectionMatrix(matrix4f);
        matrixStack.pushPose();
        PoseStack.Pose posestack$pose = matrixStack.last();
        posestack$pose.pose().setIdentity();
        posestack$pose.normal().setIdentity();
        matrixStack.translate(0.0D, 3.3F, 1984.0D);
        float f = 5.0F;
        matrixStack.scale(5.0F, 5.0F, 5.0F);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
        float f1 = Mth.lerp(partialTicks, this.oOpen, this.open);
        matrixStack.translate(((1.0F - f1) * 0.2F), ((1.0F - f1) * 0.1F), ((1.0F - f1) * 0.25F));
        float f2 = -(1.0F - f1) * 90.0F - 90.0F;
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(f2));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        float f3 = Mth.lerp(partialTicks, this.oFlip, this.flip) + 0.25F;
        float f4 = Mth.lerp(partialTicks, this.oFlip, this.flip) + 0.75F;
        f3 = (f3 - (float) Mth.fastFloor(f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float) Mth.fastFloor(f4)) * 1.6F - 0.3F;
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

        bookModel.setupAnim(0, f3, f4, f1);
        MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(bookModel.renderType(ENCHANTMENT_TABLE_BOOK_TEXTURE));
        bookModel.renderToBuffer(matrixStack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        multibuffersource$buffersource.endBatch();
        matrixStack.popPose();
        RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
        RenderSystem.restoreProjectionMatrix();
        Lighting.setupFor3DItems();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.menu.getManuscriptAmount();

        for (int i1 = 0; i1 < 3; ++i1) {
            int j1 = i + 60;
            int k1 = j1 + 20;
            this.setBlitOffset(0);
            RenderSystem.setShaderTexture(0, ENCHANTMENT_TABLE_GUI_TEXTURE);
            int l1 = this.menu.getPossiblePages()[i1] == null ? -1 : this.menu.getPossiblePages()[i1].ordinal();//enchantment level
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            if (l1 == -1) {
                this.blit(matrixStack, j1, j + 14 + 19 * i1, 0, 185, 108, 19);
            } else {
                String s = "" + 3;
                Font fontrenderer = this.getMinecraft().font;
                String s1 = "";
                float textScale = 1.0F;
                EnumBestiaryPages enchantment = this.menu.getPossiblePages()[i1];
                if (enchantment != null) {
                    s1 = I18n.get("bestiary." + enchantment.toString().toLowerCase());//EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRenderer, l1);
                    if (fontrenderer.width(s1) > 80) {
                        textScale = 1.0F - (fontrenderer.width(s1) - 80) * 0.01F;
                    }
                }
                int j2 = 6839882;
                if (IceAndFire.PROXY.getRefrencedTE() instanceof TileEntityLectern) {
                    IceAndFire.PROXY.getRefrencedTE();
                    if (menu.getSlot(0).getItem().getItem() == IafItemRegistry.BESTIARY.get()) { // Forge: render buttons as disabled when enchantable but enchantability not met on lower levels
                        int k2 = mouseX - (i + 60);
                        int l2 = mouseY - (j + 14 + 19 * i1);
                        int j3 = 0X9F988C;
                        if (k2 >= 0 && l2 >= 0 && k2 < 108 && l2 < 19) {
                            this.blit(matrixStack, j1, j + 14 + 19 * i1, 0, 204, 108, 19);
                            j2 = 16777088;
                            j3 = 16777088;
                        } else {
                            this.blit(matrixStack, j1, j + 14 + 19 * i1, 0, 166, 108, 19);
                        }

                        this.blit(matrixStack, j1 + 1, j + 15 + 19 * i1, 16 * i1, 223, 16, 16);
                        matrixStack.pushPose();
                        matrixStack.translate(width / 2F - 10, height / 2F - 83 + (1.0F - textScale) * 55, 2);
                        matrixStack.scale(textScale, textScale, 1);
                        fontrenderer.draw(matrixStack, s1, 0, 20 + 19 * i1, j2);
                        matrixStack.popPose();
                        fontrenderer = this.getMinecraft().font;
                        fontrenderer.drawShadow(matrixStack, s, k1 + 84 - fontrenderer.width(s),
                            j + 13 + 19 * i1 + 7, j3);
                    } else {
                        this.blit(matrixStack, j1, j + 14 + 19 * i1, 0, 185, 108, 19);
                        this.blit(matrixStack, j1 + 1, j + 15 + 19 * i1, 16 * i1, 239, 16, 16);
                    }
                }
            }
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        boolean flag = this.getMinecraft().player.isCreative();
        int i = this.menu.getManuscriptAmount();

        for (int j = 0; j < 3; ++j) {
            int k = 1;
            EnumBestiaryPages enchantment = this.menu.getPossiblePages()[j];
            int i1 = 3;

            if (this.isHovering(60, 14 + 19 * j, 108, 17, mouseX, mouseY) && k > 0) {
                List<FormattedCharSequence> list = Lists.newArrayList();

                if (enchantment == null) {
                    list.add(new TextComponent(ChatFormatting.RED + I18n.get("container.lectern.no_bestiary")).getVisualOrderText());
                } else if (!flag) {
                    list.add(new TextComponent("" + ChatFormatting.WHITE + ChatFormatting.ITALIC + I18n.get(enchantment == null ? "" : "bestiary." + enchantment.name().toLowerCase())).getVisualOrderText());
                    ChatFormatting textformatting = i >= i1 ? ChatFormatting.GRAY : ChatFormatting.RED;
                    list.add(new TextComponent(textformatting + "" + I18n.get("container.lectern.costs")).getVisualOrderText());
                    String s = I18n.get("container.lectern.manuscript.many", i1);
                    list.add(new TextComponent(textformatting + "" + s).getVisualOrderText());
                }

                this.renderTooltip(matrixStack, list, mouseX, mouseY);
                break;
            }
        }
    }

    public void tickBook() {
        ItemStack itemstack = this.menu.getSlot(0).getItem();

        if (!ItemStack.matches(itemstack, this.last)) {
            this.last = itemstack;

            while (true) {
                this.flipT += this.random.nextInt(4) - this.random.nextInt(4);

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
            if (this.menu.getPossiblePages()[i] != null) {
                flag = true;
            }
        }

        if (flag) {
            this.open += 0.2F;
        } else {
            this.open -= 0.2F;
        }

        this.open = Mth.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        if (flapTimer > 0) {
            f1 = (ticks + this.getMinecraft().getFrameTime()) * 0.5F;
            flapTimer--;
        }
        f1 = Mth.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }

}