package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class CustomMap {
    private BufferedImage image;
    private int id;

    public CustomMap(BufferedImage image) {
        this.image = image;
    }

    public void createMap() {
        MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));
        map.getRenderers().clear();
        map.addRenderer(new Render());
        map.setScale(MapView.Scale.FARTHEST);
        map.setTrackingPosition(false);
        id = map.getId();
    }
    public void saveImage(Integer id, String url) throws IOException {
        ImageIO.write(image, "png", new File("plugins/ImageMapRenderer/images/" + id + ".png"));
    }
    public void giveMap(Player player) {
        ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta meta = (MapMeta) map.getItemMeta();
        meta.setMapView(Bukkit.getMap(id));
        map.setItemMeta(meta);
        //check to see if player has inventory space
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), map);
        } else {
            player.getInventory().addItem(map);
        }
    }
}
