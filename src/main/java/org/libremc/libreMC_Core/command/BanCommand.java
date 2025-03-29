package org.libremc.libreMC_Core.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.libremc.libreMC_Core.Core;
import org.libremc.libreMC_Core.Punishment;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Date;

public class BanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("libremc_core.ban")){
            sender.sendMessage("Yeah you cant do that pal");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("ban: correct usage: /ban <player> <reason> <time>");
            return true;
        }

        // Parse the player
        OfflinePlayer player = Core.getInstance().getServer().getOfflinePlayer(args[0]);

        if (player.isBanned()) {
            sender.sendMessage("ban: player '" + args[0] + "' is already banned!");
            return true;
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 1; i < args.length - 1; i++) {
            reasonBuilder.append(args[i]).append(" ");
        }
        String reason = reasonBuilder.toString().trim();

        // Parse the time dynamically
        long ban_duration_millis;
        try {
            ban_duration_millis = Punishment.parseDynamicTime(args[args.length - 1]); // Last argument is the time
        } catch (IllegalArgumentException e) {
            sender.sendMessage("ban: invalid time format");
            return true;
        }

        Date banDate = new Date(System.currentTimeMillis());
        Date banExpiryDate = new Date(System.currentTimeMillis() + ban_duration_millis);

        player.ban(ChatColor.RED + "" + ChatColor.BOLD + "You are banned from LibreMC\nFrom: " + banDate + "\nTo: " + banExpiryDate, Duration.ofMillis(ban_duration_millis), reason);

        if (player.isOnline()) {
            Player onlinePlayer = player.getPlayer();
            assert onlinePlayer != null;
            onlinePlayer.kickPlayer("You have been banned!");
        }

        try {
            Punishment.addPunishment(player.getUniqueId(), Punishment.PunishmentType.BANNED, ban_duration_millis, reason);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sender.sendMessage("Banned player '" + player.getName() + "' for " + Punishment.formatDuration(ban_duration_millis) + ". Reason: " + reason);
        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + player.getName() + " has been banned for " + Punishment.formatDuration(ban_duration_millis) + ". Reason: " + reason + ChatColor.RESET);

        return true;
    }
}

