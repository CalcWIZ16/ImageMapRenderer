package net.lukesmp.imagemaprenderer;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageRenderer extends MapRenderer {
    private BufferedImage image;
    private boolean done;

    public ImageRenderer() {
        this.done = false;
    }

    public ImageRenderer(String imagePath) {
        load(imagePath);
        this.done = false;
    }

    public boolean load(String imagePath) {
        if (isValid(imagePath)) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(new URL(imagePath));
                image = MapPalette.resizeImage(image);
            } catch (IOException e) {
                return false;
            }
            this.image = image;
            return true;
        }
        return false;
    }

    public static boolean isValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (done) {
            mapCanvas.drawImage(0, 0, image);
            mapView.setTrackingPosition(false);
            done = true;
        }
    }
}