package net.lukesmp.imagemaprenderer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    List<String> arguments4 = Arrays.asList("Regular", "Glowing");
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> results = new ArrayList<String>();
        if (args.length==1 || args.length==2) {
            return Collections.singletonList("~");
        }
        if (args.length==4) {
            boolean glowing = false;
            boolean regular = false;
            if (sender.hasPermission("imagemaprenderer.glowItemFrames")&&ImageMapRenderer.plugin.getConfig().getBoolean("giveGlowItemFrames")) {
                glowing = true;
            }
            if (sender.hasPermission("imagemaprenderer.itemFrames")&&ImageMapRenderer.plugin.getConfig().getBoolean("giveItemFrames")) {
                regular = true;
            }
            if (glowing && regular) {
                for (String a : arguments4) {
                    if (a.toLowerCase().startsWith(args[3].toLowerCase())) {
                        results.add(a);
                    }
                }
                return results;
            } else if (glowing) {
                return Collections.singletonList("Glowing");
            } else if (regular) {
                return Collections.singletonList("Regular");
            } else {
                return null;
            }
        }
        return null;
    }
}
