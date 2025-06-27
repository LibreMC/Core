package org.libremc.libreMC_Core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

public class Broadcasts {

    final static String[] messages = {"Join our discord: https://discord.gg/cCuC4SmeGx", "Vote for our servers and get gold! /vote"};

    static BukkitTask broadcast_task;
    static int index = 0;

    public static void startBroadcasts(){
        long ticks = 17 * 60 * 20L;

        broadcast_task = Bukkit.getScheduler().runTaskTimer(Core.getInstance(), () -> {
            Bukkit.broadcastMessage(ChatColor.AQUA + messages[index] + ChatColor.RESET);
            index++;
            if(index == messages.length){
                index = 0;
            }
        }, ticks, ticks);
    }
}

