package org.libremc.libreMC_Core.command;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.libremc.libreMC_Core.Core;

import java.util.*;

public class IgnoreCommand implements CommandExecutor {

    /*
        Each player has a HashSet stored in their PDC, which contains
        the set of players that the player has ignored. This class
        handles the read and writes to the PDC through abstracted
        methods
     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.compareTo("ignorelist") == 0){
            Player player = (Player) sender;

            HashSet<String> ignore = getIgnoreList(player);

            if (ignore.isEmpty()) {
                sender.sendMessage("You are not ignoring any players.");
            } else {
                sender.sendMessage("Players you are ignoring:");
                for (String ignored_player : ignore) {
                    sender.sendMessage("- " + ignored_player);
                }
            }
        }

        if (args.length < 1) {
            sender.sendMessage("ignore: correct usage: /ignore <player>");
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage("Player is not online!");
            return true;
        }

        if (!IgnoreCommand.isIgnored((Player) sender, player.getName())) {
            IgnoreCommand.ignorePlayer((Player) sender, player.getName());
            sender.sendMessage("Ignored player " + "'" + player.getName() + "'");
        } else {
            IgnoreCommand.unignorePlayer((Player) sender, player.getName());
            sender.sendMessage("Unignored player " + "'" + player.getName() + "'");
        }

        return true;
    }

    public static synchronized HashSet<String> getIgnoreList(Player player) {
        NamespacedKey key = new NamespacedKey(Core.getInstance(), "ignore-key");
        PersistentDataContainer pdc = player.getPersistentDataContainer();

        if (pdc.has(key, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING))) {
            return new HashSet<>(pdc.get(key, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING)));
        } else {
            return new HashSet<>();
        }
    }

    public static synchronized boolean ignorePlayer(Player ignorer, String to_be_ignored) {
        NamespacedKey key = new NamespacedKey(Core.getInstance(), "ignore-key");
        PersistentDataContainer pdc = ignorer.getPersistentDataContainer();

        HashSet<String> ignore_list = getIgnoreList(ignorer);

        if (ignore_list.contains(to_be_ignored)) {
            return false;
        }

        ignore_list.add(to_be_ignored);
        pdc.set(key, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING), new ArrayList<>(ignore_list));

        return true;
    }

    public static synchronized boolean unignorePlayer(Player ignorer, String to_be_ignored) {
        NamespacedKey key = new NamespacedKey(Core.getInstance(), "ignore-key");
        PersistentDataContainer pdc = ignorer.getPersistentDataContainer();

        HashSet<String> ignore_list = getIgnoreList(ignorer);

        if (!ignore_list.contains(to_be_ignored)) {
            return false;
        }

        ignore_list.remove(to_be_ignored);
        pdc.set(key, PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING), new ArrayList<>(ignore_list));

        return true;
    }

    public static synchronized boolean isIgnored(Player player, String ignored) {
        HashSet<String> list = getIgnoreList(player);
        return list.contains(ignored);
    }
}
