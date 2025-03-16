package org.libremc.libreMC_Core.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.libremc.libreMC_Core.RandomTP;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.hasPlayedBefore()){
            event.setJoinMessage(ChatColor.AQUA + "Welcome back, " + player.getName());
        }else{
            RandomTP.randomTeleportPlayer(player);
            event.setJoinMessage(ChatColor.YELLOW + "Welcome to the server, " + player.getName() + "!");
        }

        // Set the players TAB list
        player.setPlayerListFooter("  \n"+ChatColor.DARK_PURPLE + ChatColor.BOLD + "  LibreMC Earth  \n\n" + ChatColor.RESET + "  Website: libremc.net  \n" + "  Map: map.libremc.net  \n" + "  Follow the rules!  \n");

    }
}
