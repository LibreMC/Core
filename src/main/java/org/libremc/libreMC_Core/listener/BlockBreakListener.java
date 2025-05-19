package org.libremc.libreMC_Core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class BlockBreakListener implements Listener {
    @EventHandler
    void onBlockBreak(BlockBreakEvent event){
        if(event.getBlock().getType() != Material.STONE){
            return;
        }

        if(event.isCancelled()){
            return;
        }

        Random random = new Random();

        if(random.nextDouble() < 0.10){
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.CALCITE, 8));
        }

        if(random.nextDouble() < 0.10){
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.AMETHYST_SHARD, 8));
        }

    }
}
