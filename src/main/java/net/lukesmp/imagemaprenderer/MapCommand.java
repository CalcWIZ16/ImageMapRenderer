package net.lukesmp.imagemaprenderer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
            //invisible command
//            if (label.equalsIgnoreCase("invisible")) {
//                if (args.length==0) {
//
//                } else {
//                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Usage: /invisible");
//                }
//                return true;
//            }
            //map command
            if (label.equalsIgnoreCase("map")) {
                if (args.length>2) {
                    //get max dimensions
                    int maxX = ImageMapRenderer.plugin.getConfig().getInt("maxX");
                    int maxY = ImageMapRenderer.plugin.getConfig().getInt("maxY");
                    boolean giveFrames = ImageMapRenderer.plugin.getConfig().getBoolean("giveItemFrames");
                    //Y value given, calculate X value
                    if (args[0].equals("~")&&isInt(args[1])){
                        int height = Integer.parseInt(args[1]);
                        if (height<=maxY){
                            try {
                                URL url = new URL(args[2]);
                                BufferedImage image = ImageIO.read(url);
                                float pixelsPerMapEdge = (float) image.getHeight() /height;
                                int width = Math.round(image.getWidth()/pixelsPerMapEdge);
                                if (width<=maxX) {
                                    distributeMaps(player, args, height, width, image);
                                    return true;
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "After calculating the optimal width we found the image is too wide. Max width is " + maxX + " maps");
                                }
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
                    //X value given, calculate Y value
                    else if (isInt(args[0])&&args[1].equals("~")){
                        int width = Integer.parseInt(args[0]);
                        if (width<=maxX){
                            try {
                                URL url = new URL(args[2]);
                                BufferedImage image = ImageIO.read(url);
                                float pixelsPerMapEdge = (float) image.getWidth() /width;
                                int height = Math.round(image.getHeight()/pixelsPerMapEdge);
                                if (height<=maxY) {
                                    distributeMaps(player, args, height, width, image);
                                    } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "After calculating the optimal height we found the image is too tall. Max height is " + maxY + " maps");
                                }
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
                    //both arguments are given a value
                    else if (isInt(args[0])&&(isInt(args[1]))) {
                        // The arguments are integers
                        int width = Integer.parseInt(args[0]);
                        int height = Integer.parseInt(args[1]);
                        if (width<=maxX&&height<=maxY) {
                            try {
                                URL url = new URL(args[2]);
                                BufferedImage image = ImageIO.read(url);
                                distributeMaps(player, args, height, width, image);
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
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Usage: /map <width> <height> <link> <frame>");
                    return true;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',prefix)+ChatColor.RED + "Usage: /map <width> <height> <link> <frame>");
                return true;
            }
            return true;
        }
        sender.sendMessage("You must be a player to execute this command");
        return true;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public void distributeMaps (Player player, String[] args, Integer height, Integer width, BufferedImage image) throws IOException {
        int maxX = ImageMapRenderer.plugin.getConfig().getInt("maxX");
        int maxY = ImageMapRenderer.plugin.getConfig().getInt("maxY");
        boolean giveFrames = ImageMapRenderer.plugin.getConfig().getBoolean("giveItemFrames");

        image = resize(image, 128 * width, 128 * height);
        int lastUsedNumber = -1;
        boolean foundNumber = false;
        Path folderPath = ImageMapRenderer.plugin.getDataFolder().toPath();
        while (!foundNumber) {
            lastUsedNumber++;
            Path filePath = Paths.get(folderPath.toString(), String.valueOf(lastUsedNumber) + ".png");
            if (!filePath.toFile().exists()) {
                foundNumber = true;
            }
        }
        ArrayList<String> links = new ArrayList<String>();
        for (int x = 0; x < image.getWidth() / 128; x++) {
            for (int y = 0; y < image.getHeight() / 128; y++) {
                File file = Paths.get(folderPath.toString(), String.valueOf(lastUsedNumber) + ".png").toFile();
                links.add(file.toPath().toUri().toString());
                ImageIO.write(image.getSubimage(x * 128, y * 128, 128, 128), "png", file);
                lastUsedNumber++;
            }
        }
        if (args.length>3){
            Boolean giveItemFrames = ImageMapRenderer.plugin.getConfig().getBoolean("giveItemFrames");
            Boolean giveGlowItemFrames = ImageMapRenderer.plugin.getConfig().getBoolean("giveGlowItemFrames");
            int mapAmount = width * height;
            if (Objects.equals(args[3], "Regular")) {
                if (giveItemFrames && player.hasPermission("imagemaprenderer.itemFrames")) {
                    HashMap<Integer, ItemStack> failedFrames = player.getInventory().addItem(new ItemStack(Material.ITEM_FRAME, mapAmount));
                    for (Map.Entry<Integer, ItemStack> entry : failedFrames.entrySet()) {
                        player.getWorld().dropItem(player.getLocation(), entry.getValue());
                    }
                }else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You cannot receive this type of Item Frame");
                    return;
                }
            } else if (Objects.equals(args[3], "Glowing")) {
                if (giveGlowItemFrames && player.hasPermission("imagemaprenderer.glowItemFrames")) {
                    HashMap<Integer, ItemStack> failedFrames = player.getInventory().addItem(new ItemStack(Material.GLOW_ITEM_FRAME, mapAmount));
                    for (Map.Entry<Integer, ItemStack> entry : failedFrames.entrySet()) {
                        player.getWorld().dropItem(player.getLocation(), entry.getValue());
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You cannot receive this type of Item Frame");
                    return;
                }
            } else {
                if (giveItemFrames || giveGlowItemFrames) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.RED + "You must enter a valid type of Item Frame (Regular or Glowing)");
                    return;
                }
            }
        }
        for (String str : links) {
            MapView view = Bukkit.createMap(player.getWorld());
            view.getRenderers().clear();
            Render renderer = new Render();
            if (renderer.load(str)) {
                view.addRenderer(renderer);
                ItemStack map = new ItemStack(Material.FILLED_MAP);
                MapMeta meta = (MapMeta) map.getItemMeta();
                assert meta != null;
                meta.setMapView(view);
                map.setItemMeta(meta);
                //Give maps and drop if inventory is full
                HashMap<Integer, ItemStack> failedItems = player.getInventory().addItem(map);
                for (Map.Entry<Integer, ItemStack> entry : failedItems.entrySet()) {
                    player.getWorld().dropItem(player.getLocation(), entry.getValue());
                }
                ImageManager manager = ImageManager.getInstance();
                manager.saveImage(view.getId(), str);
            }
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.GREEN + "Map Created!");
    }
}
