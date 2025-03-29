package org.libremc.libreMC_Core.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ReplyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(args.length < 1){
            sender.sendMessage(label + ": correct usage: /" + label + " <message>");

            return true;
        }

        HashMap<String, String> hashmap = MessageCommand.getReplyHashmap();

        if(hashmap.get(sender.getName()) == null){
            sender.sendMessage(label + ": you have no one to reply to!");
            return true;
        }

        Player player = Bukkit.getServer().getPlayer(hashmap.get(sender.getName()));


        if(player == null){
            sender.sendMessage(label + ": player is no longer online");
            return true;
        }

        String message = "";

        for (String arg : args) {
            message = message.concat(arg + " ");
        }

        player.sendMessage(ChatColor.LIGHT_PURPLE + "[" + sender.getName() + "] -> [You]: " + message);
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "[You] -> [" + player.getName() + "]: " + message);

        return true;
    }
}
