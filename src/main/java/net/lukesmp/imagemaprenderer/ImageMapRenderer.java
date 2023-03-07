package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ImageMapRenderer extends JavaPlugin {

    public static JavaPlugin plugin = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        ImageManager manager=ImageManager.getInstance();
        manager.init();
        this.getCommand("map").setExecutor(new MapCommand());
//        this.getCommand("invisible").setExecutor(new MapCommand());
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
