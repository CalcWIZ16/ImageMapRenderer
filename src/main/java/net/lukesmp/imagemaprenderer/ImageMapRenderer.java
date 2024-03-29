package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import sun.security.krb5.Config;

import java.io.File;
import java.io.IOException;

public final class ImageMapRenderer extends JavaPlugin {

    public static JavaPlugin plugin = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        ImageManager manager=ImageManager.getInstance();
        manager.init();
        ConfigUpdate();
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
        int configVersion = 3;
        File oldConfigFile = new File(getDataFolder(), "config.yml");
        FileConfiguration oldConfigFileConfiguration = YamlConfiguration.loadConfiguration(oldConfigFile);
        if (oldConfigFileConfiguration.getInt("configVersion") == configVersion) {
            saveDefaultConfig();
        } else {
            //Delete old config
            oldConfigFile.delete();

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
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage("An error occured when trying to update the config file");
            }
        }
    }
}
