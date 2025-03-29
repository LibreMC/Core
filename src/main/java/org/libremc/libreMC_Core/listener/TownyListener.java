package org.libremc.libreMC_Core.listener;

import com.palmergames.bukkit.towny.event.PlotPreChangeTypeEvent;
import com.palmergames.bukkit.towny.event.TownClaimEvent;
import com.palmergames.bukkit.towny.event.TownPreClaimEvent;
import com.palmergames.bukkit.towny.event.plot.PlayerChangePlotTypeEvent;
import com.palmergames.bukkit.towny.event.plot.toggle.PlotTogglePvpEvent;
import com.palmergames.bukkit.towny.event.town.toggle.TownTogglePVPEvent;
import com.palmergames.bukkit.towny.object.TownBlockType;
import com.palmergames.bukkit.towny.tasks.TownClaim;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyListener implements Listener {

    // Cancel any event to toggle pvp in a town, we don't want users to be able to do that
    @EventHandler
    public static void onTownTogglePvP(TownTogglePVPEvent event){
        if(!event.isAdminAction()){
            event.setCancelMessage("You can't enable PVP town-wide");
            event.setCancelled(true);
        }
    }
    @EventHandler
    public static void onPlotTogglePVP(PlotTogglePvpEvent event){
        if(!event.getTown().getMayor().isAdmin()){
            if(event.getTownBlock().isHomeBlock()){
                event.setCancelMessage("You can't enable PVP in your homeblock");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public static void onPlotChangeType(PlotPreChangeTypeEvent event){
        if(event.getNewType().equals(TownBlockType.ARENA)){
            event.setCancelMessage("You can't set your homeblock to an arena");
            event.setCancelled(true);
        }
    }

    // Prevent people from claiming deep Antarctica (also where end portals spawn)
    @EventHandler
    public static void onTownClaim(TownPreClaimEvent event){
        if(event.getPlayer().getLocation().getBlockZ() > 10800){
            event.setCancelled(true);
            event.setCancelMessage("This part of Antarctica is unclaimable");
        }
    }

}
