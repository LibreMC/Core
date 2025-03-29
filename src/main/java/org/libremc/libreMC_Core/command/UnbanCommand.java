package org.libremc.libreMC_Core.command;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.libremc.libreMC_Core.Core;
import org.libremc.libreMC_Core.Punishment;

import java.sql.SQLException;


public class UnbanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!sender.hasPermission("libremc_core.unban") && !sender.isOp()){
            sender.sendMessage("Yeah you cant do that pal");
            return true;
        }

        if(args.length < 1){
            sender.sendMessage("unban: correct usage: /unban <player>");
            return true;
        }

        OfflinePlayer player = Core.getInstance().getServer().getOfflinePlayer(args[0]);

        if(!player.isBanned()){
            sender.sendMessage("Player " + player.getName() + " is not banned!");
            return true;
        }

        try {
            Punishment.removePunishment(player.getUniqueId(), Punishment.PunishmentType.BANNED);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Bukkit.getBanList(BanList.Type.NAME).pardon(player.getName());

        sender.sendMessage("Unbanned player " + player.getName());

        return true;
    }
}
