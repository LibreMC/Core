package org.libremc.libreMC_Core;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.libremc.libreMC_Core.listener.DynmapListener;

import java.util.Random;

public class EndPortalManager implements Listener, CommandExecutor {

    public static Location current_location;
    public static long timestamp_remaining;
    public static final long PORTAL_LIFE_MS = 604800000L; // 7 days


    // Finds a suitable place to create an end portal and returns its location
    public static Location createPortal(){
        Location loc = getRandomLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        World world = loc.getWorld();

        // Create a 3x3 end portal frame
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                if (dx == 0 && dz == 0) continue; // Skip the center block
                Block block = world.getBlockAt(x + dx, y, z + dz);
                block.setType(Material.END_PORTAL_FRAME);
            }
        }

        // Place end portal blocks in the center
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                Block block = world.getBlockAt(x + dx, y, z + dz);
                block.setType(Material.END_PORTAL);
            }
        }

        Bukkit.broadcastMessage("A new end portal has been opened at " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "!");


        DynmapListener.moveEndPortalMarker(loc);

        return loc;
    }

    public static void startEndPortalManager(){
        FileConfiguration file = Core.getInstance().getConfig();

        if(file.contains("portal.timestamp")){

            long timestamp = file.getLong("portal.timestamp");
            timestamp_remaining = timestamp;
            long time_remaining = timestamp - System.currentTimeMillis();

            current_location = new Location(Bukkit.getWorld("world"), file.getDouble("portal.location.x"), file.getDouble("portal.location.y"), file.getDouble("portal.location.z"));

            if(time_remaining > 0){
                schedulePortalDeletion(time_remaining / 50);
            }else{
                schedulePortalDeletion(50);
            }

            return;
        }
        Bukkit.getLogger().info("Created portal for first time");
        long timestamp = System.currentTimeMillis() + PORTAL_LIFE_MS;
        file.set("portal.timestamp", timestamp);
        timestamp_remaining = timestamp;
        current_location = createPortal();
        file.set("portal.location.x", current_location.getX());
        file.set("portal.location.y", current_location.getY());
        file.set("portal.location.z", current_location.getZ());

        schedulePortalDeletion(PORTAL_LIFE_MS / 50);

        Core.getInstance().saveConfig();

    }

    public static void deletePortal(Location loc){
        World world = loc.getWorld();

        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                Block block = world.getBlockAt(loc.getBlockX() + dx, loc.getBlockY(), loc.getBlockZ() + dz);
                if(block.getType() == Material.END_PORTAL || block.getType() == Material.END_PORTAL_FRAME){
                    block.setType(Material.AIR);
                }
            }
        }
    }

    private static void schedulePortalDeletion(long delayInTicks) {
        new BukkitRunnable() {
            @Override
            public void run() {
                FileConfiguration file = Core.getInstance().getConfig();

                deletePortal(current_location);
                // Create new portal
                current_location = createPortal();

                timestamp_remaining = System.currentTimeMillis() + PORTAL_LIFE_MS;

                file.set("portal.location.x", current_location.getX());
                file.set("portal.location.y", current_location.getY());
                file.set("portal.location.z", current_location.getZ());
                file.set("portal.timestamp", timestamp_remaining);

                Core.getInstance().saveConfig();

                schedulePortalDeletion(PORTAL_LIFE_MS / 50);

            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("LibreMC_Core"), delayInTicks);
    }

    public static Location getRandomLocation(){
        Random rand = new Random();

        int x = rand.nextInt(-24000, 24000);
        // Offset random number so the player doesn't end up in Antarctica or northern ice
        int z = rand.nextInt(11000, 12000);

        Location loc = new Location(Bukkit.getServer().getWorld("world"), x, 0, z);
        loc.setY(BorderWraparound.findSuitableY(loc));

        // Search for next location until you find one without water beneath the player
        while(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ()).getBlock().isLiquid()){
            loc = new Location(Bukkit.getServer().getWorld("world"), x, 0, z);
            x = rand.nextInt(-24000, 24000);
            z = rand.nextInt(11000, 12000);
            loc.setY(BorderWraparound.findSuitableY(loc));
        }

        return loc;
    }

    // /endportal
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        sender.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "End Portal" + ChatColor.RESET);
        sender.sendMessage("Location: " + current_location.getBlockX() + " " + current_location.getBlockY() + " " +current_location.getBlockZ());
        sender.sendMessage("Time remaining: " + Punishment.formatDuration(timestamp_remaining - System.currentTimeMillis()));

        return true;
    }
}
