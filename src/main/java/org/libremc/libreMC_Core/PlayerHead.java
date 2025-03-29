package org.libremc.libreMC_Core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.libremc.libreMC_Core.extern.SkullCreator;

public class PlayerHead implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        if(event.getDamageSource().getCausingEntity() instanceof Player attacker){
            givePlayerHead(attacker, event.getEntity());
        }
    }

    public static void givePlayerHead(Player to, Player of){
        ItemStack item = SkullCreator.itemFromUuid(of.getUniqueId());
        to.getInventory().addItem(item);

    }
}
