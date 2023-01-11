package com.github.alexthe666.iceandfire.client.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ArrayLayeredTexture extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    public final List<String> layeredTextureNames;

    public ArrayLayeredTexture(List<String> textureNames) {
        this.layeredTextureNames = textureNames;
    }

    @Override
    public void load(@NotNull ResourceManager manager) {
        Iterator<String> iterator = this.layeredTextureNames.iterator();
        String s = iterator.next();

        try (Resource iresource = manager.getResource(new ResourceLocation(s))) {
            NativeImage nativeimage = net.minecraftforge.client.MinecraftForgeClient.getImageLayer(new ResourceLocation(s), manager);
            while (iterator.hasNext()) {
                String s1 = iterator.next();
                if (s1 != null) {
                    try (
                        Resource iresource1 = manager.getResource(new ResourceLocation(s1));
                        NativeImage nativeimage1 = NativeImage.read(iresource1.getInputStream())
                    ) {
                        for (int i = 0; i < Math.min(nativeimage1.getHeight(), nativeimage.getHeight()); i++) {
                            for (int j = 0; j < Math.min(nativeimage1.getWidth(), nativeimage.getWidth()); j++) {
                                blendPixel(nativeimage, nativeimage1, j, i, nativeimage1.getPixelRGBA(j, i));
                            }
                        }
                    }
                }
            }

            if (!RenderSystem.isOnRenderThreadOrInit()) {
                RenderSystem.recordRenderCall(() -> {
                    this.loadImage(nativeimage);
                });
            } else {
                this.loadImage(nativeimage);
            }
        } catch (IOException ioexception) {
            LOGGER.error("Couldn't load layered image", ioexception);
        }

    }

    public static void blendPixel(NativeImage nativeimage, NativeImage nativeimage1, int xIn, int yIn, int colIn) {
        int i = nativeimage.getPixelRGBA(xIn, yIn);
        float f = NativeImage.getA(colIn) / 255.0F;
        float f1 = NativeImage.getB(colIn) / 255.0F;
        float f2 = NativeImage.getG(colIn) / 255.0F;
        float f3 = NativeImage.getR(colIn) / 255.0F;
        float f4 = NativeImage.getA(i) / 255.0F;
        float f5 = NativeImage.getB(i) / 255.0F;
        float f6 = NativeImage.getG(i) / 255.0F;
        float f7 = NativeImage.getR(i) / 255.0F;
        float f8 = 1.0F - f;
        float f9 = f * f + f4 * f8;
        float f10 = f1 * f + f5 * f8;
        float f11 = f2 * f + f6 * f8;
        float f12 = f3 * f + f7 * f8;
        if (f9 > 1.0F) {
            f9 = 1.0F;
        }

        if (f10 > 1.0F) {
            f10 = 1.0F;
        }

        if (f11 > 1.0F) {
            f11 = 1.0F;
        }

        if (f12 > 1.0F) {
            f12 = 1.0F;
        }

        int j = (int) (f9 * 255.0F);
        int k = (int) (f10 * 255.0F);
        int l = (int) (f11 * 255.0F);
        int i1 = (int) (f12 * 255.0F);
        nativeimage.setPixelRGBA(xIn, yIn, NativeImage.combine(j, k, l, i1));

    }

    private void loadImage(NativeImage imageIn) {
        TextureUtil.prepareImage(this.getId(), imageIn.getWidth(), imageIn.getHeight());
        imageIn.upload(0, 0, 0, true);
    }
}
