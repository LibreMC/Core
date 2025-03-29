package org.libremc.libreMC_Core.command;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.libremc.libreMC_Core.CombatTag;

public class SandworldCommand implements CommandExecutor {

    public static World sandworld = loadSandWorld();

    public static World loadSandWorld(){
        WorldCreator creator = new WorldCreator("sandworld");
        creator.type(WorldType.FLAT);
        creator = creator.generateStructures(false);

        World world = Bukkit.createWorld(creator);

        world.getWorldBorder().setSize(4096);
        world.getWorldBorder().setCenter(0, 0);

        // Disable any entities spawning
        world.setSpawnLimit(SpawnCategory.AMBIENT, 0);
        world.setSpawnLimit(SpawnCategory.ANIMAL, 0);
        world.setSpawnLimit(SpawnCategory.MONSTER, 0);

        world.loadChunk(0, 0);

        return world;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)){
            return false;
        }

        Player player = (Player)commandSender;

        if(CombatTag.isTagged(player)){
            player.sendMessage(CombatTag.MESSAGE);
            return true;
        }

        Location loc = new Location(sandworld, 0, sandworld.getHighestBlockYAt(0, 0), 0);

        player.teleport(loc);

        return true;
    }
}
