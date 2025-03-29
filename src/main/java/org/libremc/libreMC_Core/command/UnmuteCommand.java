package org.libremc.libreMC_Core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.libremc.libreMC_Core.Core;
import org.libremc.libreMC_Core.Punishment;

import java.sql.SQLException;

public class UnmuteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("libremc_core.unmute") && !sender.isOp()){
            sender.sendMessage("Yeah you cant do that pal");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("unmute: correct usage: /unmute <player>");
            return true;
        }

        Player player = (Player) Core.getInstance().getServer().getOfflinePlayer(args[0]);

        try {
            if(Punishment.isMuted(player)){
                Punishment.removePunishment(player.getUniqueId(), Punishment.PunishmentType.MUTED);
                sender.sendMessage("Player '"+ player.getName() + "' is unmuted");
            }else{
                sender.sendMessage("Player '"+ player.getName() + "' is not muted!");
            }

            return true;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}