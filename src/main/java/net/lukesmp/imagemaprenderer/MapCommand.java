package net.lukesmp.imagemaprenderer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Objects;

public class MapCommand implements CommandExecutor {
    String prefix = ImageMapRenderer.plugin.getConfig().getString("prefix");

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player=(Player) sender;
            //map command
            if (label.equalsIgnoreCase("map")) {
                //check if player has permission
                if (!player.hasPermission("imagemaprenderer.map")) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You do not have permission to use this command");
                    return true;
                }

                int maxX = ImageMapRenderer.plugin.getConfig().getInt("maxX");
                int maxY = ImageMapRenderer.plugin.getConfig().getInt("maxY");

                int mapArrayWidth = -1;
                int mapArrayHeight = -1;
                boolean giveItemFrames;
                boolean giveGlowItemFrames;
                URL url = null;

                if (args.length<2){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Usage: /map <width> <height> <link> <frame type>");
                    return true;
                } else {
                    if (args[0].equals("~") && args[1].equals("~")) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "Must specify at least one dimension");
                        return true;
                    }
                    if (args[0].equals("~")) {
                        if (!isInt(args[1])) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "Usage: /map <width> <height> <link> <frame type>");
                            return true;
                        } else {
                            mapArrayHeight = Integer.parseInt(args[1]);
                        }
                    }
                    if (args[1].equals("~")) {
                        if (!isInt(args[0])) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "Usage: /map <width> <height> <link> <frame type>");
                            return true;
                        } else {
                            mapArrayWidth = Integer.parseInt(args[0]);
                        }
                    }
                }
                if (args.length==4) {
                    if (args[3].equals("Regular")) {
                        if (!player.hasPermission("imagemaprenderer.itemFrames")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You don't have permission to receive this type of Item Frame");
                            return true;
                        } else { giveItemFrames = true; }
                    } else if (args[3].equals("Glowing")) {
                        if (!player.hasPermission("imagemaprenderer.glowItemFrames")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You don't have permission to receive this type of Item Frame");
                            return true;
                        } else { giveGlowItemFrames = true; }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You must enter a valid type of Item Frame (Regular or Glowing)");
                        return true;
                    }
                }

                try {
                    url = new URL(parseDiscordMetadata(args[2]));
                    BufferedImage image = ImageIO.read(url);
                    CustomMapArray mapArray = new CustomMapArray(mapArrayWidth, mapArrayHeight, image);
                    if (mapArray.getNumMapsWidth() > maxX) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "After calculating the optimal width we found the image is too wide. Max width is " + maxX + " maps");
                        return true;
                    }
                    if (mapArray.getNumMapsHeight() > maxY) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "After calculating the optimal height we found the image is too tall. Max height is " + maxY + " maps");
                        return true;
                    }
                    mapArray.createMaps();
                    mapArray.distributeMaps(player);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "Image could not be loaded. This can happen because the link is not direct. The easiest way to ensure the link is a direct link, is to send the image over discord then use \"copy link\"");
                    return true;
                }
            }
            return true;
        }
        sender.sendMessage("You must be a player to execute this command");
        return true;
    }

    public String parseDiscordMetadata(String url){
        if (url.contains("discordapp.net/attachments/")){
            if (url.contains("&format=")){
                return url.substring(0,url.indexOf("&format=")-2);
            }
        }
        return url;
    }
}
