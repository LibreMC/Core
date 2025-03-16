package org.libremc.libreMC_Core;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.sql.SQLException;

public class BorderWraparound implements Listener {

    public static final int RENDER_DISTANCE = 7;
    public static final int NORTH_BORDER = -12287;
    public static final int SOUTH_BORDER = 12286;
    public static final int WEST_BORDER = -24573;
    public static final int EAST_BORDER = 24573;

    @EventHandler
    void onPlayerMove(PlayerMoveEvent event){
        Location loc = event.getTo();
        double x = loc.getX();
        double z = loc.getZ();

        // Check if player moved past the north/south border
        if(z <= NORTH_BORDER + 16 * RENDER_DISTANCE){
            // Invert signage (south/north movement works different from east/west) and move him a little bit back
            loc.setX(-loc.getX());
            loc.setZ(loc.getZ() + 5);
            loc.setY(findSuitableY(loc));
            event.setTo(loc);
            loc.setYaw(0);
            event.getPlayer().sendMessage("You have passed the world border");
            return;
        }else if(z >= SOUTH_BORDER - 16 * RENDER_DISTANCE){
            loc.setX(-loc.getX());
            loc.setZ(loc.getZ() - 5);
            loc.setY(findSuitableY(loc));
            loc.setYaw(180);
            event.setTo(loc);
            event.getPlayer().sendMessage("You have passed the world border");
            return;
        }

        // Check if player moved past the east/west border
        if(x <= WEST_BORDER + 16 * RENDER_DISTANCE){
            // Invert signage
            loc.setX(-loc.getX() - 5);
            loc.setY(findSuitableY(loc));
            loc.setYaw(90);
            event.setTo(loc);
            event.getPlayer().sendMessage("You have passed the world border");
            return;
        }else if(x >= EAST_BORDER - 16 * RENDER_DISTANCE){
            loc.setX(-loc.getX() + 5);
            loc.setY(findSuitableY(loc));
            loc.setYaw(270);
            event.setTo(loc);
            event.getPlayer().sendMessage("You have passed the world border");
            return;
        }
    }

    // Find suitable Y coordinate to place the player
    public static int findSuitableY(Location l){
        for(int i = 320; i >= 0; i--){
            l.setY(i);
            if(!l.getBlock().getType().isAir()){
                return i + 1;
            }
        }

        return -1;
    }

}
