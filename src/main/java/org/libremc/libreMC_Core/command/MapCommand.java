package org.libremc.libreMC_Core.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MapCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        commandSender.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "Map: " + ChatColor.RESET + "https://map.libremc.net");
        return true;
    }
}
