package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;
import org.checkerframework.checker.optional.qual.Present;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CustomMapArray {

    private final String prefix = ImageMapRenderer.plugin.getConfig().getString("prefix");

    private final int numMapsWidth;
    private final int numMapsHeight;
    private BufferedImage image;
    private CustomMap[] maps;

    /**
     * Constructor for CustomMapArray
     * @param numMapsWidth width (in maps) of the array (if -1, the optimal width will be calculated)
     * @param numMapsHeight height (in maps) of the array (if -1, the optimal height will be calculated)
     * @param image image to make up the array
     */
    public CustomMapArray(int numMapsWidth, int numMapsHeight, BufferedImage image) {;
        if (numMapsWidth == -1 && numMapsHeight == -1) {
            this.numMapsWidth = 1;
            this.numMapsHeight = 1;
        } else if (numMapsWidth == -1) {
            this.numMapsHeight = numMapsHeight;
            this.numMapsWidth = image.getWidth() / (image.getWidth()/numMapsHeight);
        } else if (numMapsHeight == -1) {
            this.numMapsWidth = numMapsWidth;
            this.numMapsHeight = image.getHeight() / (image.getHeight() / numMapsWidth);
        } else {
            this.numMapsWidth = numMapsWidth;
            this.numMapsHeight = numMapsHeight;
        }
        this.image = resize(image, 128*numMapsWidth, 128*numMapsHeight);
    }

    public void createMaps() throws IOException {
        maps = new CustomMap[numMapsWidth*numMapsHeight];
        for (int i = 0; i < numMapsWidth; i++) {
            for (int j = 0; j < numMapsHeight; j++) {
                maps[i + j * numMapsWidth] = new CustomMap(image.getSubimage(i*128, j*128, 128, 128));
            }
        }
    }

    public void distributeMaps(Player player) {
        for (CustomMap map : maps) {
            map.giveMap(player);
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.GREEN + "Map(s) Created!");
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return newImage;
    }

    public int getNumMapsWidth() {
        return numMapsWidth;
    }

    public int getNumMapsHeight() {
        return numMapsHeight;
    }
}
