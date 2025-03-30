package org.libremc.libreMC_Core;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Random;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RandomTP implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){

        Resident resident = TownyAPI.getInstance().getResident(event.getPlayer());
        if(!resident.hasTown()){
            // If the player isn't in a town then random teleport him

            if(event.getPlayer().getRespawnLocation() != null){
                // Check if player has a bed
                Location loc = event.getPlayer().getRespawnLocation();
                event.setRespawnLocation(loc);
                event.getPlayer().teleport(loc);
                return;
            }

            Location loc = getRandomLocation();
            event.setRespawnLocation(loc);
            event.getPlayer().teleport(loc);
        }else{
            try {
                // Else teleport him to his towns spawn
                event.setRespawnLocation(resident.getTown().getSpawn());
                event.getPlayer().teleport(resident.getTown().getSpawn());
            } catch (TownyException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void randomTeleportPlayer(Player player){
        player.teleport(getRandomLocation());
    }

    public static Location getRandomLocation(){
        Random rand = new Random();

        int x = rand.nextInt(BorderWraparound.WEST_BORDER + 16 * BorderWraparound.RENDER_DISTANCE, BorderWraparound.EAST_BORDER - 16 * BorderWraparound.RENDER_DISTANCE);
        // Offset random number so the player doesn't end up in Antarctica or northern ice
        int z = rand.nextInt(BorderWraparound.NORTH_BORDER + 16 * BorderWraparound.RENDER_DISTANCE + 6000, BorderWraparound.SOUTH_BORDER - 16 * BorderWraparound.RENDER_DISTANCE - 6000);

        Location loc = new Location(Bukkit.getServer().getWorld("world"), x, 0, z);
        loc.setY(BorderWraparound.findSuitableY(loc));

        // Search for next location until you find one without water beneath the player
        while(new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ()).getBlock().isLiquid()){
            loc = new Location(Bukkit.getServer().getWorld("world"), x, 0, z);
            x = rand.nextInt(BorderWraparound.WEST_BORDER + 16 * BorderWraparound.RENDER_DISTANCE, BorderWraparound.EAST_BORDER - 16 * BorderWraparound.RENDER_DISTANCE);
            z = rand.nextInt(BorderWraparound.NORTH_BORDER + 16 * BorderWraparound.RENDER_DISTANCE + 6000, BorderWraparound.SOUTH_BORDER - 16 * BorderWraparound.RENDER_DISTANCE - 6000);
            loc.setY(BorderWraparound.findSuitableY(loc));
        }

        return loc;
    }
}
