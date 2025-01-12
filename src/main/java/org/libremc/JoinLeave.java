package org.libremc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeave implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(player.hasPlayedBefore()){
            event.setJoinMessage(ChatColor.AQUA + "Welcome back, " + player.getName());
        }else{
            event.setJoinMessage(ChatColor.YELLOW + "Welcome to the server, " + player.getName() + "!");
        }
    }
}
