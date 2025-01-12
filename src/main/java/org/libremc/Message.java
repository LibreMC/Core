package org.libremc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Message implements CommandExecutor {
    // Player,Person who messaged that player
    public static HashMap<String, String> player_reply = new HashMap<>();

    static HashMap<String, String> getReplyHashmap(){
        return player_reply;
    }

    static void setReplyHashmap(HashMap<String, String> hashmap){
        player_reply = hashmap;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args){
        if(args.length < 2){
            sender.sendMessage("msg: correct usage: /msg <player> <message>");
            return true;
        }

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            if (player.getName().equals(args[0])) {
                String message = "";
                for (int j = 1; j < args.length; j++) {
                    message = message.concat(args[j] + " ");
                }

                player.sendMessage(ChatColor.LIGHT_PURPLE + "[" + sender.getName() + "] -> " + message);

                player_reply.put(sender.getName(), player.getName());

                return true;
            }
        }

        sender.sendMessage("msg: Player '"+args[0]+"' is not online!");

        return true;
    }
}
