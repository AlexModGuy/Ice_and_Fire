package com.github.alexthe666.iceandfire.client.texture;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.entity.HorseRenderer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ArrayLayeredTexture extends Texture {
    private static final Logger LOGGER = LogManager.getLogger();
    public final List<String> layeredTextureNames;

    public ArrayLayeredTexture(List<String> textureNames) {
        this.layeredTextureNames = textureNames;
    }

    public void loadTexture(IResourceManager manager) throws IOException {
        Iterator<String> iterator = this.layeredTextureNames.iterator();
        String s = iterator.next();

        try (IResource iresource = manager.getResource(new ResourceLocation(s))) {
            NativeImage nativeimage = net.minecraftforge.client.MinecraftForgeClient.getImageLayer(new ResourceLocation(s), manager);

            while (iterator.hasNext()) {
                String s1 = iterator.next();
                if (s1 != null) {
                    try (
                            IResource iresource1 = manager.getResource(new ResourceLocation(s1));
                            NativeImage nativeimage1 = NativeImage.read(iresource1.getInputStream());
                    ) {
                        for (int i = 0; i < nativeimage1.getHeight(); ++i) {
                            for (int j = 0; j < nativeimage1.getWidth(); ++j) {
                                nativeimage.blendPixel(j, i, nativeimage1.getPixelRGBA(j, i));
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
            LOGGER.error("Couldn't load layered image", (Throwable) ioexception);
        }

    }

    private void loadImage(NativeImage imageIn) {
        TextureUtil.prepareImage(this.getGlTextureId(), imageIn.getWidth(), imageIn.getHeight());
        imageIn.uploadTextureSub(0, 0, 0, true);
    }
}
