package org.libremc.libreMC_Core.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(ChatColor.BOLD + "Planetminecraft: " + ChatColor.LIGHT_PURPLE + "https://www.planetminecraft.com/server/libremc-geopolitical-towny-earth/vote/" + ChatColor.RESET);
        commandSender.sendMessage(ChatColor.BOLD + "minecraft-mp: " + ChatColor.LIGHT_PURPLE + "https://minecraft-mp.com/server/342430/vote/" + ChatColor.RESET);
        return true;
    }
}
