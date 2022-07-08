package net.lukesmp.custommaps;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Render extends MapRenderer {
    public static boolean isValid(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }
    private BufferedImage image;
    private boolean done;
    public Render(){
        done=false;
    }
    public Render(String url){
        load(url);
        done=false;
    }
    public boolean load(String url){
        if(isValid(url)){
            BufferedImage image=null;
            try {
                image = ImageIO.read(new URL(url));
                image = MapPalette.resizeImage(image);
            } catch (IOException e) {
                return false;
            }
            this.image=image;
            return true;
        }
        return false;
    }
    @Override
    public void render(MapView view, MapCanvas canvas, Player player) {
        if (done)
            return;
        canvas.drawImage(0,0,image);
        view.setTrackingPosition(false);
        done=true;
    }
}
