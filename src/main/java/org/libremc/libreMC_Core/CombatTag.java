package org.libremc.libreMC_Core;

import com.palmergames.bukkit.towny.event.damage.TownyDamageEvent;
import com.palmergames.bukkit.towny.event.damage.TownyPlayerDamagePlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class CombatTag implements Listener {
    // Map of players that are tagged with task value
    static HashMap<Player, BukkitTask> tagged = new HashMap<>();

    // Maps victim to attacker
    static HashMap<Player, Player> last_attacker = new HashMap<>();

    final public static String MESSAGE = "You are combat-tagged!";

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        // Untag player so they won't be killed again for leaving after dying
        untagPlayer(event.getEntity());
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event){
        if(isTagged(event.getPlayer())){
            // If player is tagged and leaves, kill them
            if(last_attacker.get(event.getPlayer()) !=  null){
                // If the player has a last attacker then damage them so the attacker gets the head
                event.getPlayer().damage(1000, last_attacker.get(event.getPlayer()));
            }else{
                event.getPlayer().setHealth(0);
            }

        }
    }

    @EventHandler
    public void onDamage(TownyPlayerDamagePlayerEvent event){

        if(event.isCancelled()){
            return;
        }

        Player attacker = event.getAttackingPlayer();
        Player victim = event.getVictimPlayer();

        tagPlayer(attacker);
        tagPlayer(victim);

        last_attacker.put(victim, attacker);

    }

    public static void tagPlayer(Player player){
        if(!isTagged(player)){
            player.sendMessage("You have been combat-tagged!");
        }else{
            BukkitTask existingTask = tagged.get(player);
            if (existingTask != null) {
                existingTask.cancel();
            }
        }

        BukkitTask task = Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("LibreMC_Core"), () -> {
            tagged.remove(player);
            player.sendMessage("You are no longer combat-tagged");


        }, 20 * 5);

        tagged.put(player, task);
    }

    public static void untagPlayer(Player player){
        BukkitTask task = tagged.remove(player);
        if(task != null){
            task.cancel();
        }
    }

    public static boolean isTagged(Player player){
        return tagged.containsKey(player);
    }

}
