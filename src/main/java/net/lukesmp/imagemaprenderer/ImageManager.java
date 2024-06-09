package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class ImageManager implements Listener {
    private static ImageManager instance = null;

    public static ImageManager getInstance() {
        if (instance == null)
            instance = new ImageManager();
        return instance;
    }

//    private CustomFile dataFile = new CustomFile("data.yml");
//    private Map<Integer, String> savedImages = new HashMap<Integer, String>();
    private final File imageFolder = new File("plugins/ImageMapRenderer/images");
    private final ArrayList<Integer> managedMapIds = new ArrayList<>();

    @EventHandler
    public void onMapInitEvent(MapInitializeEvent event) {
        if (managedMapIds.contains(event.getMap().getId())) {
            MapView view = event.getMap();
            view.getRenderers().clear();
            view.addRenderer(new ImageRenderer("file:///" + new File("plugins/ImageMapRenderer/images/" + view.getId() + ".png").getAbsolutePath()));
            view.setScale(MapView.Scale.FARTHEST);
            view.setTrackingPosition(false);
        }
    }

    public void init() {
        Bukkit.getPluginManager().registerEvents(this, ImageMapRenderer.getPlugin(ImageMapRenderer.class));
        loadImages();
    }

    private void loadImages() {
        File imageFolder = new File("plugins/ImageMapRenderer/images");
        if (imageFolder.exists() && imageFolder.isDirectory()) {
            for (String fileName : Objects.requireNonNull(imageFolder.list())) {
                managedMapIds.add(Integer.parseInt(fileName.replace(".png", "")));
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("ImageMapRenderer: Could not find images folder");
        }
    }

//    public String getImage(int id) {
//        return savedImages.get(id);
//    }

//    public boolean hasImage(int id) {
//        return savedImages.containsKey(id);
//    }
//    public FileConfiguration getData() {
//        return dataFile.getConfig();
//    }
//    public void saveData() {
//        dataFile.saveConfig();
//    }
//    class CustomFile {
//        private final ImageMapRenderer plugin = ImageMapRenderer.getPlugin(ImageMapRenderer.class);
//        private FileConfiguration dataConfig = null;
//        private File dataConfigFile = null;
//        private final String name;
//
//        public CustomFile(String name) {
//            this.name = name;
//            saveDefaultConfig();
//        }
//        public void reloadConfig() {
//            if (dataConfigFile == null)
//                dataConfigFile = new File(plugin.getDataFolder(),name);
//            this.dataConfig = YamlConfiguration
//                    .loadConfiguration(dataConfigFile);
//            InputStream defConfigStream = plugin.getResource(name);
//            if (defConfigStream != null) {
//                YamlConfiguration defConfig = YamlConfiguration
//                        .loadConfiguration(new InputStreamReader(defConfigStream));
//                this.dataConfig.setDefaults(defConfig);
//            }
//        }
//        public FileConfiguration getConfig() {
//            if (this.dataConfig == null)
//                reloadConfig();
//            return this.dataConfig;
//        }
//        public void saveConfig() {
//            if ((dataConfig == null) || (dataConfigFile == null))
//                return;
//            try {
//                getConfig().save(dataConfigFile);
//            } catch (IOException e) {
//                plugin.getLogger().log(Level.SEVERE, "Could not save config to "
//                        + dataConfigFile, e);
//            }
//        }
//        public void saveDefaultConfig() {
//            if (dataConfigFile == null)
//                dataConfigFile = new File(plugin.getDataFolder(), name);
//            if (!dataConfigFile.exists())
//                plugin.saveResource(name, false);
//        }
//
//    }
}
