package net.lukesmp.imagemaprenderer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                if (args.length>2) {
                    //get max dimensions
                    int maxX = ImageMapRenderer.plugin.getConfig().getInt("maxX");
                    int maxY = ImageMapRenderer.plugin.getConfig().getInt("maxY");
                    boolean giveFrames = ImageMapRenderer.plugin.getConfig().getBoolean("giveItemFrames");

                    if (args.length>3){
                        boolean giveItemFrames = ImageMapRenderer.plugin.getConfig().getBoolean("giveItemFrames");
                        boolean giveGlowItemFrames = ImageMapRenderer.plugin.getConfig().getBoolean("giveGlowItemFrames");
                        if (Objects.equals(args[3], "Regular")) {
                            if (!giveItemFrames || !player.hasPermission("imagemaprenderer.itemFrames")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You cannot receive this type of Item Frame");
                                return true;
                            }
                        } else if (Objects.equals(args[3], "Glowing")) {
                            if (!giveGlowItemFrames || !player.hasPermission("imagemaprenderer.glowItemFrames")) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You cannot receive this type of Item Frame");
                                return true;
                            }
                        } else {
                            if (giveItemFrames || giveGlowItemFrames) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You must enter a valid type of Item Frame (Regular or Glowing)");
                                return true;
                            }
                        }
                    }

                    //Y value given, calculate X value
                    if (args[0].equals("~")&&isInt(args[1])){
                        int height = Integer.parseInt(args[1]);
                        if (height<=maxY){
                            try {
                                URL url = new URL(args[2]);
                                BufferedImage image = ImageIO.read(url);
                                CustomMapArray mapArray = new CustomMapArray(-1, height, image);
                                if (mapArray.getNumMapsWidth()<=maxX) {
                                    mapArray.createMaps();
                                    mapArray.distributeMaps(player);
                                    return true;
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "After calculating the optimal width we found the image is too wide. Max width is " + maxX + " maps");
                                    return true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Image could not be loaded. This can happen because the link is not direct. The easiest way to ensure the link is a direct link, is to send the image over discord then use \"copy link\"");
                                return true;
                            }
                        }else{
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED+"Dimensions are too large, please specify smaller numbers");
                            return true;
                        }
                    }
                    //X value given, calculate Y value
                    else if (isInt(args[0])&&args[1].equals("~")){
                        int width = Integer.parseInt(args[0]);
                        if (width<=maxX){
                            try {
                                URL url = new URL(args[2]);
                                BufferedImage image = ImageIO.read(url);
                                CustomMapArray mapArray = new CustomMapArray(width, -1, image);
                                if (mapArray.getNumMapsHeight()<=maxY) {
                                    mapArray.createMaps();
                                    mapArray.distributeMaps(player);
                                    return true;
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "After calculating the optimal height we found the image is too tall. Max height is " + maxY + " maps");
                                    return true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Image could not be loaded. This can happen because the link is not direct. The easiest way to ensure the link is a direct link, is to send the image over discord then use \"copy link\"");
                                return true;
                            }
                        }else{
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED+"Dimensions are too large, please specify smaller numbers");
                            return true;
                        }
                    }
                    //both arguments are given a value
                    else if (isInt(args[0])&&(isInt(args[1]))) {
                        // The arguments are integers
                        int width = Integer.parseInt(args[0]);
                        int height = Integer.parseInt(args[1]);
                        if (width<=maxX&&height<=maxY) {
                            try {
                                URL url = new URL(args[2]);
                                BufferedImage image = ImageIO.read(url);
                                CustomMapArray mapArray = new CustomMapArray(width, height, image);
                                mapArray.createMaps();
                                mapArray.distributeMaps(player);
                                return true;
                            } catch (Exception e) {
                                e.printStackTrace();
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Image could not be loaded. This can happen because the link is not direct. The easiest way to ensure the link is a direct link, is to send the image over discord then use \"copy link\"");
                                return true;
                            }
                        }else{
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED+"Dimensions are too large, please specify smaller numbers");
                            return true;
                        }
                    }
                    else if (args[0].equals("~")&&args[1].equals("~")){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Must specify at least one dimension");
                        return true;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Usage: /map <width> <height> <link> <frame type>");
                    return true;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Usage: /map <width> <height> <link> <frame type>");
                return true;
            }
            return true;
        }
        sender.sendMessage("You must be a player to execute this command");
        return true;
    }
}
