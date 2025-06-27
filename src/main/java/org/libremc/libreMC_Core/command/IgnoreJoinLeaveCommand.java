package org.libremc.libreMC_Core.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.List;

public class IgnoreJoinLeaveCommand implements CommandExecutor {
    public static HashSet<String> list = new HashSet<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!sender.hasPermission("libremc_core.ignorejoinleave") && !sender.isOp()){
            sender.sendMessage("No permission!");
            return true;
        }

        if(list.contains(args[0])){
            sender.sendMessage("Removed " + args[0] + " from the list");
            list.remove(args[0]);
            return true;
        }

        list.add(args[0]);

        return true;
    }
}