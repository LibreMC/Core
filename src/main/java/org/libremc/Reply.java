package org.libremc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class Reply implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args){
        if(args.length < 1){
            sender.sendMessage("r: correct usage: /r <message>");
            return true;
        }

        HashMap<String, String> hashmap = Message.getReplyHashmap();

        if(hashmap.get(sender.getName()) == null){
            sender.sendMessage("r: you have no one to reply to!");
            return true;
        }

        Player player = Bukkit.getServer().getPlayer(hashmap.get(sender.getName()));


        if(player == null){
            sender.sendMessage("r: player is no longer online");
            return true;
        }

        String message = "";

        for (String arg : args) {
            message = message.concat(arg + " ");
        }

        player.sendMessage(ChatColor.LIGHT_PURPLE + "[" + sender.getName() + "] -> " + message);

        return true;
    }
}
