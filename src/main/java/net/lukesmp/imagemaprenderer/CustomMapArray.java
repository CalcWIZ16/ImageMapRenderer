package net.lukesmp.imagemaprenderer;

import java.awt.image.BufferedImage;

public class CustomMapArray {

    private int numMapsWidth;
    private int numMapsHeight;
    private int mapWidth;
    private int mapHeight;
    private BufferedImage image;
    private BufferedImage[] images;

    /**
     * Constructor for CustomMapArray
     * @param numMapsWidth width (in maps) of the array (if -1, the optimal width will be calculated)
     * @param numMapsHeight height (in maps) of the array (if -1, the optimal height will be calculated)
     * @param image image to make up the array
     */
    public CustomMapArray(int numMapsWidth, int numMapsHeight, BufferedImage image) {
        this.image = image;
        if (numMapsWidth == -1 && numMapsHeight == -1) {
                this.numMapsWidth = 1;
                this.numMapsHeight = 1;
                this.mapWidth = image.getWidth();
                this.mapHeight = image.getHeight();
        } else if (numMapsWidth == -1) {
            this.mapWidth = image.getWidth() / numMapsHeight;
            this.mapHeight = image.getHeight() / numMapsHeight;

            this.numMapsWidth = image.getWidth() / mapWidth;
            this.numMapsHeight = numMapsHeight;
        } else if (numMapsHeight == -1) {
            this.mapWidth = image.getWidth() / numMapsWidth;
            this.mapHeight = image.getHeight() / numMapsWidth;

            this.numMapsWidth = numMapsWidth;
            this.numMapsHeight = image.getHeight() / mapHeight;

        } else {
            this.numMapsWidth = numMapsWidth;
            this.numMapsHeight = numMapsHeight;
            this.mapWidth = image.getWidth() / numMapsWidth;
            this.mapHeight = image.getHeight() / numMapsHeight;
        }
    }

    public BufferedImage[] splitImage() {
        images = new BufferedImage[numMapsWidth*numMapsHeight];
        for (int i = 0; i < numMapsWidth; i++) {
            for (int j = 0; j < numMapsHeight; j++) {
                images[i + j * numMapsWidth] = image.getSubimage(i * mapWidth, j * mapHeight, mapWidth, mapHeight);
            }
        }
        return images;
    }

    public int getNumMapsWidth() {
        return numMapsWidth;
    }

    public int getNumMapsHeight() {
        return numMapsHeight;
    }
}
