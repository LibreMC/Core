package org.libremc.libreMC_Core.extern;

/* Stolen from the Sanctuary plugin */
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.libremc.libreMC_Core.Core;

import javax.annotation.Nullable;
import java.util.List;

public class PlayerStorageManager {

    private final static String PLAYER_RETURN_KEY = "sandworld-player-return-key";

    public static @Nullable Location getPlayerReturnLocation(Player player){
        NamespacedKey key = new NamespacedKey(Core.getInstance(), PLAYER_RETURN_KEY);
        PersistentDataContainer container = player.getPersistentDataContainer();

        if(container.has(key)){
            // x, y, z
            List<Integer> coords = container.get(key, PersistentDataType.LIST.integers());
            return new Location(Bukkit.getWorld("world"), coords.get(0), coords.get(1), coords.get(2));
        }

        return null;

    }

    public static void setPlayerReturnLocation(Player player, Location loc){
        NamespacedKey key = new NamespacedKey(Core.getInstance(), PLAYER_RETURN_KEY);
        PersistentDataContainer container = player.getPersistentDataContainer();

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        container.set(key, PersistentDataType.LIST.integers(), List.of(x, y, z));
    }
}
