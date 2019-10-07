package com.github.alexthe666.iceandfire.client.texture;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ArrayLayeredTexture extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    public final List<String> layeredTextureNames;

    public ArrayLayeredTexture(List<String> textureNames) {
        this.layeredTextureNames = textureNames;
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        BufferedImage bufferedimage = null;

        for (String s : this.layeredTextureNames) {
            IResource iresource = null;

            try {
                if (s != null) {
                    iresource = resourceManager.getResource(new ResourceLocation(s));
                    BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(iresource.getInputStream());

                    if (bufferedimage == null) {
                        bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), 2);
                    }

                    bufferedimage.getGraphics().drawImage(bufferedimage1, 0, 0, null);
                }

                continue;
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't load layered image", ioexception);
            } finally {
                IOUtils.closeQuietly(iresource);
            }

            return;
        }

        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
    }
}

