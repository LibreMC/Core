package org.libremc.libreMC_Core.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.libremc.libreMC_Core.Punishment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MessageCommand implements CommandExecutor {
    // Player,Person who messaged that player
    public static HashMap<String, String> player_reply = new HashMap<>();

    static HashMap<String, String> getReplyHashmap(){
        return player_reply;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(args.length < 2){
            sender.sendMessage(label + ": correct usage: /" + label + " <player> <message>");
            return true;
        }

        try {
            if(Punishment.isMuted((Player)sender)){
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            if (player.getName().equals(args[0])) {

                if(IgnoreCommand.isIgnored(player, sender.getName())){
                    sender.sendMessage("You have been ignored by this person");
                    return true;
                }

                String message = "";
                for (int j = 1; j < args.length; j++) {
                    message = message.concat(args[j] + " ");
                }

                if (!((Player) sender).getPersistentDataContainer().getOrDefault(
                        new NamespacedKey("echochamber", "echochambered"),
                        PersistentDataType.BOOLEAN, false)
                ) {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "[" + sender.getName() + "] to [You] -> " + message);
                }
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "[You] to [" + player.getName() +  "] -> " + message);

                player_reply.put(player.getName(), sender.getName());

                return true;
            }
        }

        sender.sendMessage("msg: Player '"+args[0]+"' is not online!");

        return true;
    }
}
