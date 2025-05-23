package org.libremc.libreMC_Core.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Links:" + ChatColor.RESET);
        sender.sendMessage("Website - https://libremc.net");
        sender.sendMessage("Map - https://map.libremc.net");
        sender.sendMessage("Discord - https://discord.gg/cCuC4SmeGx");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Commands:" + ChatColor.RESET);
        sender.sendMessage("/pstat <player> - Views players stats");
        sender.sendMessage("/ignore <player> - Prevents you from seeing a players messages");
        sender.sendMessage("/endportal - Shows you info about the current end portal");
        sender.sendMessage("/msg <player> <message> - Privately messages a player");
        sender.sendMessage("/r <message> - Replies to the player previously messaging you");
        sender.sendMessage("/vote - Get all the places where you can vote");
        return true;
    }
}

