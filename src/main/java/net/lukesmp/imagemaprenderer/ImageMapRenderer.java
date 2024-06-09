package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class ImageMapRenderer extends JavaPlugin implements Listener {

    public static JavaPlugin plugin = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        //Update config
        saveDefaultConfig();
        ConfigUpdate();

        //Load images
        ImageManager manager = ImageManager.getInstance();
        manager.init();

        //Register commands
        this.getCommand("map").setExecutor(new MapCommand());
        this.getCommand("map").setTabCompleter(new TabComplete());
//        this.getCommand("invisible").setExecutor(new MapCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void ConfigUpdate() {
        //Load old config
        int configVersion = 5;
        File oldConfigFile = new File(getDataFolder(), "config.yml");
        FileConfiguration oldConfigFileConfiguration = YamlConfiguration.loadConfiguration(oldConfigFile);

        if (oldConfigFileConfiguration.getInt("configVersion") <= 4) {
            imageUpdate();
        }

        if (oldConfigFileConfiguration.getInt("configVersion") < configVersion) {
            File oldConfigFileRenamed = new File(getDataFolder(), "config.yml.old");
            if (!oldConfigFile.renameTo(oldConfigFileRenamed)) {
                Bukkit.getConsoleSender().sendMessage("ImageMapRenderer: Could not rename old config file");
            }

            saveDefaultConfig();

            File newConfigFile = new File(getDataFolder(), "config.yml");
            FileConfiguration newConfig = YamlConfiguration.loadConfiguration(newConfigFile);

            for (String key : oldConfigFileConfiguration.getKeys(true)) {
                if (!key.equals("configVersion")) {
                    newConfig.set(key, oldConfigFileConfiguration.get(key));
                }
            }

            newConfig.set("configVersion", configVersion);

            try {
                newConfig.save(newConfigFile);
                Bukkit.getConsoleSender().sendMessage("ImageMapRenderer: Configuration updated successfully");
                oldConfigFileRenamed.delete();
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("ImageMapRenderer: An error occured when trying to update the config file");
                oldConfigFileRenamed.renameTo(oldConfigFile);
            }
        }
    }

    //upgrade images data from data.yml to images folder
    private void imageUpdate() {
        File dataFile = new File(getDataFolder(), "data.yml");
        File imageFolder = new File("plugins/ImageMapRenderer/images");

        if (!imageFolder.exists()) {
            Bukkit.getConsoleSender().sendMessage(imageFolder.mkdirs() ? "ImageMapRenderer: Created images folder" : "ImageMapRenderer: Could not create images folder");
        }

        if (dataFile.exists()) {
            Bukkit.getConsoleSender().sendMessage("ImageMapRenderer: Updating image files");
            CustomFile dataFileConfiguration = new CustomFile("data.yml");
            int idCount = 0;

            for (String key : dataFileConfiguration.getConfig().getKeys(true)) {
                String imagePath = dataFileConfiguration.getConfig().getString(key);
                key = key.replace("ids.", "");
                File imageFile = new File(imagePath.replace("file://", ""));
                if (imageFile.exists()) {
                    try {
                        imageFile.renameTo(new File(getDataFolder(), "images/" + key + ".png"));
                    } catch (Exception e) {
                        Bukkit.getConsoleSender().sendMessage("An error occured when trying to update the image files");
                    }
                }
                idCount++;
            }
            if (idCount == imageFolder.listFiles().length) {
                Bukkit.getConsoleSender().sendMessage("ImageMapRenderer: Image files updated successfully");
                dataFile.delete();
            } else {
                Bukkit.getConsoleSender().sendMessage("ImageMapRenderer: An error occured when trying to update the image files");
            }
        }
    }

    private class CustomFile {
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
    }
}
