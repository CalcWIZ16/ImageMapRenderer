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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ImageManager implements Listener {
    private static ImageManager instance = null;
    public static ImageManager getInstance() {
        if (instance == null)
            instance = new ImageManager();
        return instance;
    }
    private CustomFile dataFile = new CustomFile("data.yml");
    private Map<Integer, String> savedImages = new HashMap<Integer, String>();

    @EventHandler
    public void onMapInitEvent(MapInitializeEvent event) {
        if (hasImage(event.getMap().getId())) {
            MapView view = event.getMap();
            view.getRenderers().clear();
            view.addRenderer(new Render(getImage(view.getId())));
            view.setScale(MapView.Scale.FARTHEST);
            view.setTrackingPosition(false);
        }
    }
    public void saveImage(Integer id, String url) {
        getData().set("ids." + id, url);
        saveData();
    }
    public void loadImages() {
        if (getData().contains("ids"))
            getData().getConfigurationSection("ids").getKeys(false).forEach(id -> {
                savedImages.put(Integer.parseInt(id), getData().getString("ids." + id));
            });
    }
    public boolean hasImage(int id) {
        return savedImages.containsKey(id);
    }
    public String getImage(int id) {
        return savedImages.get(id);
    }
    public FileConfiguration getData() {
        return dataFile.getConfig();
    }
    public void saveData() {
        dataFile.saveConfig();
    }
    class CustomFile {
        private final ImageMapRenderer plugin = ImageMapRenderer.getPlugin(ImageMapRenderer.class);
        private FileConfiguration dataConfig = null;
        private File dataConfigFile = null;
        private final String name;
        public CustomFile(String name) {
            this.name = name;
            saveDefaultConfig();
        }
        public void reloadConfig() {
            if (dataConfigFile == null)
                dataConfigFile = new File(plugin.getDataFolder(),name);
            this.dataConfig = YamlConfiguration
                    .loadConfiguration(dataConfigFile);
            InputStream defConfigStream = plugin.getResource(name);
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration
                        .loadConfiguration(new InputStreamReader(defConfigStream));
                this.dataConfig.setDefaults(defConfig);
            }
        }
        public FileConfiguration getConfig() {
            if (this.dataConfig == null)
                reloadConfig();
            return this.dataConfig;
        }
        public void saveConfig() {
            if ((dataConfig == null) || (dataConfigFile == null))
                return;
            try {
                getConfig().save(dataConfigFile);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to "
                        + dataConfigFile, e);
            }
        }
        public void saveDefaultConfig() {
            if (dataConfigFile == null)
                dataConfigFile = new File(plugin.getDataFolder(), name);
            if (!dataConfigFile.exists())
                plugin.saveResource(name, false);
        }

    }
}
