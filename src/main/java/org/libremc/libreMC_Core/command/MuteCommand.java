package org.libremc.libreMC_Core.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.libremc.libreMC_Core.Core;
import org.libremc.libreMC_Core.Punishment;

import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

public class MuteCommand implements CommandExecutor, Listener {
    @EventHandler
    void onPlayerChat(AsyncPlayerChatEvent event) throws SQLException {

        // For the ignore plugin. Go through all the recipients and check if they have the event player ignored. If so remove them from the recipient list
        Set<Player> set = event.getRecipients();
        set.removeIf(player -> IgnoreCommand.isIgnored(player, event.getPlayer().getName()));

        if(Punishment.isMuted(event.getPlayer())){
            event.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You are muted!");
            event.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("libremc_core.mute") && !sender.isOp()){
            sender.sendMessage("Yeah you cant do that pal");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("mute: correct usage: /mute <player> <reason> <time>");
            return true;
        }

        // Parse the player
        OfflinePlayer player = Core.getInstance().getServer().getOfflinePlayer(args[0]);
        if (!player.hasPlayedBefore() && !player.isOnline()) {
            sender.sendMessage("mute: player '" + args[0] + "' hasn't played before!");
            return true;
        }

        try {
            if(Punishment.isMuted((Player) player)){
                sender.sendMessage("Player is already muted!");
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 1; i < args.length - 1; i++) {
            reasonBuilder.append(args[i]).append(" ");
        }
        String reason = reasonBuilder.toString().trim();

        long mute_duration_millis;
        try {
            mute_duration_millis = Punishment.parseDynamicTime(args[args.length - 1]); // Last argument is the time
        } catch (IllegalArgumentException e) {
            sender.sendMessage("mute: invalid time format");
            return true;
        }

        try {
            Punishment.addPunishment(player.getUniqueId(), Punishment.PunishmentType.MUTED, mute_duration_millis, reason);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        sender.sendMessage("Muted player '" + player.getName() + "' for " + Punishment.formatDuration(mute_duration_millis) + ". Reason: " + reason);

        Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + player.getName() + " has been muted for " + Punishment.formatDuration(mute_duration_millis) + ". Reason: " + reason + ChatColor.RESET);

        return true;
    }
}
