package org.libremc.libreMC_Core.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.libremc.libreMC_Core.Core;
import org.libremc.libreMC_Core.Punishment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class PstatCommand implements CommandExecutor {

    void parseResultSet(CommandSender sender, ResultSet set) throws SQLException {
        while (set.next()) {
            String type = set.getString("punishment_type");
            long date_issued = set.getLong("date_issued");
            long date_of_expiry = set.getLong("date_of_expiry");
            String reason = set.getString("reason");
            int punishment_id = set.getInt("punishment_id");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(date_issued));
            String date_string = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" +calendar.get(Calendar.YEAR);

            long milliseconds = date_of_expiry - date_issued;

            long seconds = milliseconds / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            long remaining_hours = hours % 24;
            long remaining_minutes = minutes % 60;

            StringBuilder expire_length = new StringBuilder();
            if (days > 0) {
                expire_length.append(days).append(" day").append(days > 1 ? "s" : "").append(", ");
            }
            if (remaining_hours > 0 || days > 0) {
                expire_length.append(remaining_hours).append(" hour").append(remaining_hours > 1 ? "s" : "").append(", ");
            }
            expire_length.append(remaining_minutes).append(" minute").append(remaining_minutes > 1 ? "s" : "");

            String expire_string = expire_length.toString();

            if(type.compareTo(Punishment.PunishmentType.BANNED.name()) == 0){
                sender.sendMessage(punishment_id + ": " + date_string + ": " + ChatColor.RED + ChatColor.BOLD  + "BANNED" + ChatColor.RESET + "- Reason: " + ChatColor.ITALIC +reason + ChatColor.RESET + "\nLength: " + expire_string + "\n");
            }else if(type.compareTo(Punishment.PunishmentType.WARNED.name()) == 0){
                sender.sendMessage(punishment_id + ": " + date_string + ": " + ChatColor.GOLD + ChatColor.BOLD  + "WARNED" + ChatColor.RESET + "- Reason: " + ChatColor.ITALIC +reason + ChatColor.RESET + "\n");
            }else if(type.compareTo(Punishment.PunishmentType.MUTED.name()) == 0){
                sender.sendMessage(punishment_id + ": " + date_string + ": " + ChatColor.BLUE + ChatColor.BOLD  + "MUTED" + ChatColor.RESET + "- Reason: " + ChatColor.ITALIC +reason + ChatColor.RESET + "\nLength: " + expire_string + "\n");
            }else{
                sender.sendMessage("pstat: bad error!!!");
            }
        }

        set.close();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            sender.sendMessage("pstat: correct usage /pstat <player>/none");
            return true;
        }

        OfflinePlayer player;
        if (args.length < 1) {
            player = (OfflinePlayer) sender;
        } else {
            player = Core.getInstance().getServer().getOfflinePlayer(args[0]);
        }

        if(!player.hasPlayedBefore()){
            sender.sendMessage("Player has not played before!");
            return true;
        }

        ResultSet set;
        try {
            set = Punishment.getPunishments(player.getUniqueId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sender.sendMessage("Player stats for: " + ChatColor.RED + player.getName() + ChatColor.RESET + "\n");
        sender.sendMessage("Joined: " + new Date(player.getFirstPlayed()));
        sender.sendMessage("Punishments:");

        try {
            parseResultSet(sender, set);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            set.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
