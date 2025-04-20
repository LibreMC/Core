package org.libremc.libreMC_Core.command;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.libremc.libreMC_Core.CombatTag;
import org.libremc.libreMC_Core.extern.PlayerStorageManager;

public class SandworldCommand implements CommandExecutor {

    public static World sandworld = loadSandWorld();

    public static World loadSandWorld(){
        WorldCreator creator = new WorldCreator("sandworld");
        creator = creator.generateStructures(false);

        World world = Bukkit.createWorld(creator);

        world.getWorldBorder().setSize(500);
        world.getWorldBorder().setCenter(0, 0);

        // Disable any entities spawning
        world.setSpawnLimit(SpawnCategory.AMBIENT, 0);
        world.setSpawnLimit(SpawnCategory.ANIMAL, 0);
        world.setSpawnLimit(SpawnCategory.MONSTER, 0);

        world.setDifficulty(Difficulty.PEACEFUL);

        return world;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!commandSender.hasPermission("libremc_core.sandworld") && !commandSender.isOp()){
            commandSender.sendMessage("Yeah you cant do that pal");
            return true;
        }

        if(!(commandSender instanceof Player player)){
            return false;
        }

        if(CombatTag.isTagged(player)){
            player.sendMessage(CombatTag.MESSAGE);
            return true;
        }

        if(player.getWorld().getName().equalsIgnoreCase("sandworld")){
            player.sendMessage("Teleporting to overworld");
            player.teleport(PlayerStorageManager.getPlayerReturnLocation(player));
            return true;
        }

        PlayerStorageManager.setPlayerReturnLocation(player , player.getLocation());

        player.sendMessage("Teleporting to sandworld (to go back, use /sandworld again)");
        Location loc = new Location(sandworld, 0, sandworld.getHighestBlockYAt(0, 0) + 1, 0);
        player.teleport(loc);

        return true;
    }
}
