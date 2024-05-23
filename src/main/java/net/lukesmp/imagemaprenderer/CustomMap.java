package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CustomMap {
    private final BufferedImage image;
    private final MapView mapView;

    public CustomMap(BufferedImage image) throws IOException {
        this.image = image;
        this.mapView = Bukkit.createMap(Bukkit.getWorlds().get(0));
        this.mapView.getRenderers().clear();
        this.mapView.addRenderer(new ImageRenderer());
        this.mapView.setScale(MapView.Scale.FARTHEST);
        this.mapView.setTrackingPosition(false);
        //save the image to the images folder
        ImageIO.write(image, "png", new File("plugins/ImageMapRenderer/images/" + mapView.getId() + ".png"));
    }

    public void giveMap(Player player) {
        this.mapView.getRenderers().clear();
        ImageRenderer renderer = new ImageRenderer();
        if (renderer.load("file:///" + new File("plugins/ImageMapRenderer/images/" + mapView.getId() + ".png").getAbsolutePath())) {
            this.mapView.addRenderer(renderer);
            ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
            MapMeta meta = (MapMeta) map.getItemMeta();
            meta.setMapView(mapView);
            map.setItemMeta(meta);
            //check to see if player has inventory space
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItem(player.getLocation(), map);
            } else {
                player.getInventory().addItem(map);
            }
        }
    }
}
