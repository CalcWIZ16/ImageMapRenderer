package net.lukesmp.custommaps;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Custommaps extends JavaPlugin {

    public static JavaPlugin plugin = null;

    @Override
    public void onEnable() {
        plugin = this;
        ImageManager manager=ImageManager.getInstance();
        manager.init();
        this.getCommand("map").setExecutor(new MapCommand());
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
    }
}
