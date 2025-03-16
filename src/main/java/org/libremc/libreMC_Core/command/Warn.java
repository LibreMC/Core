package org.libremc.libreMC_Core.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.libremc.libreMC_Core.Core;
import org.libremc.libreMC_Core.Punishment;

import java.sql.SQLException;

public class Warn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("libremc_core.warn") && !sender.isOp()){
            sender.sendMessage("Yeah you cant do that pal");
            return true;
        }

        if(args.length < 2){
            sender.sendMessage("warn: correct usage: /warn <player> <reason>");
            return true;
        }

        String player_name = args[0];

        StringBuilder reason_builder = new StringBuilder();
        for(int i = 1; i < args.length; i++){
            reason_builder.append(args[i]).append(" ");
        }
        String reason = reason_builder.toString().trim();

        OfflinePlayer player = Core.getInstance().getServer().getOfflinePlayer(player_name);

        try {
            Punishment.addPunishment(player.getUniqueId(), Punishment.PunishmentType.WARNED, 0, reason);
            sender.sendMessage("Warned player '" + player.getName() + "'. Reason: " + reason);
        } catch (SQLException e) {
            sender.sendMessage("An error occurred while warning the player. Please try again later.");
            e.printStackTrace();
        }

        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + player.getName() + " has been warned. Reason: " + reason + ChatColor.RESET);


        return true;
    }
}