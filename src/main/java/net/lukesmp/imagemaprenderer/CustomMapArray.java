package net.lukesmp.imagemaprenderer;

import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class CustomMapArray {

    private final int numMapsWidth;
    private final int numMapsHeight;
    private final int mapWidth;
    private final int mapHeight;
    private BufferedImage image;
    private CustomMap[] maps;

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

    public void createMaps() throws IOException {
        maps = new CustomMap[numMapsWidth*numMapsHeight];
        for (int i = 0; i < numMapsWidth; i++) {
            for (int j = 0; j < numMapsHeight; j++) {
                maps[i + j * numMapsWidth] = new CustomMap(image.getSubimage(i * mapWidth, j * mapHeight, mapWidth, mapHeight));
            }
        }
    }

    public void distributeMaps(Player player) {
        for (CustomMap map : maps) {
            map.giveMap(player);
        }
    }

    public int getNumMapsWidth() {
        return numMapsWidth;
    }

    public int getNumMapsHeight() {
        return numMapsHeight;
    }
}
