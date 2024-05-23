package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ImageMapRenderer extends JavaPlugin implements Listener {

    public static JavaPlugin plugin = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        //Load images
        ImageManager manager = ImageManager.getInstance();
        manager.init();

        //Update config
        ConfigUpdate();

        //Register commands
        this.getCommand("map").setExecutor(new MapCommand());
        this.getCommand("map").setTabCompleter(new TabComplete());
//        this.getCommand("invisible").setExecutor(new MapCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void ConfigUpdate() {
        //Load old config
        int configVersion = 4;
        File oldConfigFile = new File(getDataFolder(), "config.yml");
        FileConfiguration oldConfigFileConfiguration = YamlConfiguration.loadConfiguration(oldConfigFile);
        if (oldConfigFileConfiguration.getInt("configVersion") == configVersion) {
            saveDefaultConfig();
        } else {
            //Delete old config
            oldConfigFile.renameTo(new File("config.yml.old"));

            //create and load new config file
            saveDefaultConfig();
            File newConfigFile = new File(getDataFolder(), "config.yml");
            FileConfiguration newConfigFileConfiguration = YamlConfiguration.loadConfiguration(newConfigFile);

            //replace new file values with old file values (except configVersion)
            for (String key : oldConfigFileConfiguration.getKeys(true)) {
                newConfigFileConfiguration.set(key, oldConfigFileConfiguration.get(key));
            }
            newConfigFileConfiguration.set("configVersion", configVersion);
            try {
                newConfigFileConfiguration.save(newConfigFile);
                Bukkit.getConsoleSender().sendMessage("Configuration updated successfully");
                oldConfigFile.delete();
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("An error occured when trying to update the config file");
                oldConfigFile.renameTo(new File("config.yml"));

            }
        }
    }
}
