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
    public CustomMapArray(int numMapsWidth, int numMapsHeight, BufferedImage image) {;
        if (numMapsWidth == -1 && numMapsHeight == -1) {
            this.numMapsWidth = 1;
            this.numMapsHeight = 1;

            this.image = resize(image, 128, 128);
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
                maps[i + j * numMapsWidth] = new CustomMap(x * 128, y * 128, 128, 128));
            }
        }
    }

    public void distributeMaps(Player player) {
        MapView mapView = Bukkit.createMap(player.getWorld());
        mapView.getRenderers().clear();
        ImageRenderer renderer = new ImageRenderer();
        for (CustomMap map : maps) {
            map.giveMap(player, mapView, renderer);
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.GREEN + "Map(s) Created!");
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public int getNumMapsWidth() {
        return numMapsWidth;
    }

    public int getNumMapsHeight() {
        return numMapsHeight;
    }
}
