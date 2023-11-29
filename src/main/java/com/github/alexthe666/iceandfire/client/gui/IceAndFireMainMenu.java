package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class IceAndFireMainMenu extends TitleScreen {
    public static final int LAYER_COUNT = 2;
    public static final ResourceLocation splash = new ResourceLocation(IceAndFire.MODID, "splashes.txt");
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation BESTIARY_TEXTURE = new ResourceLocation("iceandfire:textures/gui/main_menu/bestiary_menu.png");
    private static final ResourceLocation TABLE_TEXTURE = new ResourceLocation("iceandfire:textures/gui/main_menu/table.png");
    public static ResourceLocation[] pageFlipTextures;
    public static ResourceLocation[] drawingTextures = new ResourceLocation[22];
    private int layerTick;
    private String splashText;
    private boolean isFlippingPage = false;
    private int pageFlip = 0;
    private Picture[] drawnPictures;
    private Enscription[] drawnEnscriptions;
    private float globalAlpha = 1F;

    public IceAndFireMainMenu() {
        pageFlipTextures = new ResourceLocation[]{new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_1.png"),
            new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_2.png"),
            new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_3.png"),
            new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_4.png"),
            new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_5.png"),
            new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/page_6.png")};
        for (int i = 0; i < drawingTextures.length; i++) {
            drawingTextures[i] = new ResourceLocation(IceAndFire.MODID, "textures/gui/main_menu/drawing_" + (i + 1) + ".png");
        }
        resetDrawnImages();
        final String branch = "1.17";
        try (final BufferedReader reader = getURLContents("https://raw.githubusercontent.com/Alex-the-666/Ice_and_Fire/"
            + branch + "/src/main/resources/assets/iceandfire/splashes.txt", "assets/iceandfire/splashes.txt")) {
            List<String> list = IOUtils.readLines(reader);

            if (!list.isEmpty()) {
                do {
                    this.splashText = list.get(ThreadLocalRandom.current().nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        } catch (IOException e) {
            IceAndFire.LOGGER.error("Exception trying to collect splash screen lines: ", e);
        }
    }

    public static BufferedReader getURLContents(String urlString, String backupFileLoc) {
        BufferedReader reader = null;
        boolean useBackup = false;
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            url = null;
            useBackup = true;
        }
        if (url != null) {
            URLConnection connection = null;
            try {
                connection = url.openConnection();
                connection.setConnectTimeout(200);
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
            } catch (IOException e) {
                IceAndFire.LOGGER.warn("Ice and Fire couldn't download splash texts for main menu");
                useBackup = true;
            }
        }
        if (useBackup) {
            InputStream is = IceAndFireMainMenu.class.getClassLoader().getResourceAsStream(backupFileLoc);
            if (is != null) {
                reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            }
        }
        return reader;
    }


    private void resetDrawnImages() {
        globalAlpha = 0;
        Random random = java.util.concurrent.ThreadLocalRandom.current();
        drawnPictures = new Picture[1 + random.nextInt(2)];
        boolean left = random.nextBoolean();
        for (int i = 0; i < drawnPictures.length; i++) {
            left = !left;
            int x;
            int y = random.nextInt(25);
            if (left) {
                x = -15 - random.nextInt(20) - 128;
            } else {
                x = 30 + random.nextInt(20);
            }
            drawnPictures[i] = new Picture(random.nextInt(drawingTextures.length - 1), x, y, 0.5F, random.nextFloat() * 0.5F + 0.5F);
        }
        drawnEnscriptions = new Enscription[4 + random.nextInt(8)];
        for (int i = 0; i < drawnEnscriptions.length; i++) {
            left = !left;
            int x;
            int y = 10 + random.nextInt(130);
            if (left) {
                x = -30 - random.nextInt(30) - 50;
            } else {
                x = 30 + random.nextInt(30);
            }
            String s1 = "missingno";
            drawnEnscriptions[i] = new Enscription(s1, x, y, random.nextFloat() * 0.5F + 0.5F, 0X9C8B7B);
        }
    }

    @Override
    public void tick() {
        super.tick();
        float flipTick = layerTick % 40;
        if (globalAlpha < 1 && !isFlippingPage && flipTick < 30) {
            globalAlpha += 0.1F;
        }

        if (globalAlpha > 0 && flipTick > 30) {
            globalAlpha -= 0.1F;
        }
        if (flipTick == 0 && !isFlippingPage) {
            isFlippingPage = true;
        }
        if (isFlippingPage) {
            if (layerTick % 2 == 0) {
                pageFlip++;
            }
            if (pageFlip == 6) {
                pageFlip = 0;
                isFlippingPage = false;
                resetDrawnImages();
            }
        }

        this.layerTick++;
    }

    @Override
    public void render(@NotNull PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, TABLE_TEXTURE);
        int width = this.width;
        int height = this.height;
        blit(ms, 0, 0, 0, 0, width, height, width, height);
        RenderSystem.setShaderTexture(0, BESTIARY_TEXTURE);
        blit(ms, 50, 0, 0, 0, width - 100, height, width - 100, height);
        float f11 = 1.0F;
        int l = Mth.ceil(f11 * 255.0F) << 24;
        if (this.isFlippingPage) {
            RenderSystem.setShaderTexture(0, pageFlipTextures[Math.min(5, pageFlip)]);
            blit(ms, 50, 0, 0, 0, width - 100, height, width - 100, height);
        } else {
            int middleX = width / 2;
            int middleY = height / 5;
            float widthScale = width / 427F;
            float heightScale = height / 427F;
            float imageScale = Math.min(widthScale, heightScale) * 192;
            for (Picture picture : drawnPictures) {
//                float alpha = (picture.alpha * globalAlpha + 0.01F);
                RenderSystem.enableBlend();
                RenderSystem.setShaderTexture(0, drawingTextures[picture.image]);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                blit(ms, (int) (picture.x * widthScale) + middleX, (int) ((picture.y * heightScale) + middleY), 0, 0, (int) imageScale, (int) imageScale, (int) imageScale, (int) imageScale);
                RenderSystem.disableBlend();
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager._enableBlend();
        this.getMinecraft().font.draw(ms, "Ice and Fire " + ChatFormatting.YELLOW + IceAndFire.VERSION, 2, height - 10, 0xFFFFFFFF);
        RenderSystem.setShaderTexture(0, MINECRAFT_TITLE_TEXTURES);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        blit(ms, width / 2 - 256 / 2, 10, 0, 0, 256, 64, 256, 64);

        ForgeHooksClient.renderMainMenu(this, ms, this.getMinecraft().font, width, height, l);
        if (this.splashText != null) {
            ms.pushPose();
            ms.translate((this.width / 2 + 90), 70.0D, 0.0D);
            ms.mulPose(Vector3f.ZP.rotationDegrees(-20.0F));
            float f2 = 1.8F - Mth.abs(Mth.sin((float) (Util.getMillis() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
            f2 = f2 * 100.0F / (float) (this.font.width(this.splashText) + 32);
            ms.scale(f2, f2, f2);
            drawCenteredString(ms, this.font, this.splashText, 0, -8, 16776960 | l);
            ms.popPose();
        }


        String s1 = "Copyright Mojang AB. Do not distribute!";
        Font font = this.getMinecraft().font;
        drawString(ms, font, s1, width - this.getMinecraft().font.width(s1) - 2, height - 10, 0xFFFFFFFF);
        for (int i = 0; i < this.renderables.size(); ++i) {
            this.renderables.get(i).render(ms, mouseX, mouseY, partialTicks);
        }
        for (int i = 0; i < this.renderables.size(); i++) {
            renderables.get(i).render(ms, mouseX, mouseY, getMinecraft().getFrameTime());
        }
    }

    private class Picture {
        int image;
        int x;
        int y;
        float alpha;

        public Picture(int image, int x, int y, float alpha, float scale) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.alpha = alpha;
        }
    }

    private class Enscription {
        public Enscription(String text, int x, int y, float alpha, int color) {
        }
    }
}

