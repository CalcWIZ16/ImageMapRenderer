package net.lukesmp.imagemaprenderer;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.System.load;

public class ImageRenderer extends MapRenderer {
    private BufferedImage image;
    private boolean done;
    public ImageRenderer(){
        done=false;
    }

    public ImageRenderer(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean load(String imagePath) {

    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (image != null) {
            mapCanvas.drawImage(0, 0, image);
        }
    }
}