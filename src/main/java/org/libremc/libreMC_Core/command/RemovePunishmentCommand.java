package org.libremc.libreMC_Core.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.libremc.libreMC_Core.Core;
import org.libremc.libreMC_Core.Punishment;

import java.sql.SQLException;

public class RemovePunishmentCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("libremc_core.removepunishment") && !sender.isOp()){
            sender.sendMessage("Yeah you cant do that pal");
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("removepunishment: /removepunishment <player> <id>");
            return false;
        }

        OfflinePlayer player = Core.getInstance().getServer().getOfflinePlayer(args[0]);

        if(!player.hasPlayedBefore() && !player.isOnline()){
            sender.sendMessage("Player has not played before!");
            return true;
        }

        int punishmentId;
        try {
            punishmentId = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Punishment ID must be a number!");
            return false;
        }


        try {
            Punishment.removePunishment(punishmentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sender.sendMessage("Removed punishment " + punishmentId + "from " + player.getName());
        return true;

    }
}
