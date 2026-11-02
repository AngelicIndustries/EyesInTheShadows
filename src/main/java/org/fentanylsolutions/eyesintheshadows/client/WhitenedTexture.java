package org.fentanylsolutions.eyesintheshadows.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

/**
 * A texture turned white, each pixel as bright as its brightest channel. Made from the texture as loaded, so it follows
 * resource packs.
 */
public class WhitenedTexture extends AbstractTexture {

    private final ResourceLocation source;

    private WhitenedTexture(ResourceLocation source) {
        this.source = source;
    }

    /** The white version of a texture, loaded the first time it is asked for. */
    public static ResourceLocation of(ResourceLocation texture) {
        ResourceLocation white = new ResourceLocation(
            texture.getResourceDomain(),
            texture.getResourcePath() + "#white");
        TextureManager textures = Minecraft.getMinecraft()
            .getTextureManager();
        if (textures.getTexture(white) == null) textures.loadTexture(white, new WhitenedTexture(texture));
        return white;
    }

    @Override
    public void loadTexture(IResourceManager manager) throws IOException {
        deleteGlTexture();
        BufferedImage image;
        try (InputStream in = manager.getResource(source)
            .getInputStream()) {
            image = ImageIO.read(in);
        }
        BufferedImage white = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int color = image.getRGB(x, y);
                int value = Math.max(color >> 16 & 255, Math.max(color >> 8 & 255, color & 255));
                white.setRGB(x, y, color & 0xFF000000 | value << 16 | value << 8 | value);
            }
        }
        TextureUtil.uploadTextureImageAllocate(getGlTextureId(), white, false, false);
    }
}
