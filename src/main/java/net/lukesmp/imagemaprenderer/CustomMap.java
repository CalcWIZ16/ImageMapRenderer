package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;

public class CustomMap {
    private ImageIO image;
    private int id;

    public CustomMap(ImageIO image) {
        this.image = image;


    }

    public void createMap() {
        MapView view = Bukkit.createMap(Bukkit.getWorlds().get(0));
        view.getRenderers().clear();
        view.addRenderer(new Render(getImage(view.getId())));
        view.setScale(MapView.Scale.FARTHEST);
        view.setTrackingPosition(false);
    }
    public void saveImage(Integer id, String url) {

    }
//    public void giveMap(Player player) {
//        ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
//        MapMeta meta = (MapMeta) map.getItemMeta();
//        meta.setMapView(Bukkit.getMap(id));
//        map.setItemMeta(meta);
//        player.getInventory().addItem(map);
//    }
}
