package net.lukesmp.imagemaprenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;


public class CustomMapArray {

    private int width;
    private int height;
    private BufferedImage image;
    private BufferedImage[][] images;

    public CustomMapArray(int width, int height, BufferedImage image) {
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public void splitImage() {
        images = new BufferedImage[width][height];
        int mapWidth = image.getWidth() / width;
        int mapHeight = image.getHeight() / height;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                images[x][y] = image.getSubimage(x * mapWidth, y * mapHeight, mapWidth, mapHeight);
            }
        }
    }
}
