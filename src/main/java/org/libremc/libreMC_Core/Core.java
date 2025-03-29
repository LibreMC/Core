package org.libremc.libreMC_Core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapCommonAPIListener;
import org.libremc.libreMC_Core.command.*;
import org.libremc.libreMC_Core.listener.DynmapListener;
import org.libremc.libreMC_Core.listener.JoinLeaveListener;
import org.libremc.libreMC_Core.listener.TownyListener;

import java.sql.SQLException;

import static org.libremc.libreMC_Core.EndPortalManager.startEndPortalManager;

public final class Core extends JavaPlugin implements CommandExecutor {

    public static String database_file = "punishments.db";

    static Core instance;

    public static Database db;

    public static Core getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("LibreMC Core: plugin started");

        this.getCommand("msg").setExecutor(new MessageCommand());
        this.getCommand("r").setExecutor(new ReplyCommand());
        this.getCommand("ban").setExecutor(new BanCommand());
        this.getCommand("unban").setExecutor(new UnbanCommand());
        this.getCommand("warn").setExecutor(new WarnCommand());
        this.getCommand("mute").setExecutor(new MuteCommand());
        this.getCommand("pstat").setExecutor(new PstatCommand());
        this.getCommand("unmute").setExecutor(new UnmuteCommand());
        this.getCommand("help").setExecutor(new HelpCommand());
        this.getCommand("ignore").setExecutor(new IgnoreCommand());
        this.getCommand("libremc").setExecutor(new Libremc());
        this.getCommand("endportal").setExecutor(new EndPortalManager());
        this.getCommand("map").setExecutor(new MapCommand());

        // PlayerJoin/LeaveEvent, join/leave messages
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        // PlayerChatEvent, checks if players are muted
        getServer().getPluginManager().registerEvents(new MuteCommand(), this);

        // PlayerMoveEvent, checks if players are beyond or at the world borders
        getServer().getPluginManager().registerEvents(new BorderWraparound(), this);

        // PlayerRespawnEvent, checks if a player is not in a town and random-teleports them
        getServer().getPluginManager().registerEvents(new RandomTP(), this);

        // TownyPlayerDamagePlayerEvent; PlayerDeathEvent, manages combat-tagging
        getServer().getPluginManager().registerEvents(new CombatTag(), this);

        // PlayerDeathEvent, gives out player heads to people who kill another
        getServer().getPluginManager().registerEvents(new PlayerHead(), this);

        // TownTogglePVPEvent; PlotTogglePvpEvent; TownPreClaimEvent, various checks regarding claiming and PVP
        getServer().getPluginManager().registerEvents(new TownyListener(), this);


        FileConfiguration file = Core.getInstance().getConfig();

        file.addDefault("portal.timestamp", 0);
        file.addDefault("portal.location.x", 0);
        file.addDefault("portal.location.y", 0);
        file.addDefault("portal.location.z", 0);

        saveConfig();

        DynmapCommonAPIListener.register(new DynmapListener());

        startEndPortalManager();

        try {
            db = new Database(database_file);
        } catch (SQLException e) {
            getLogger().warning("LibreMC Core: Failed to open SQL database:" + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            db.getStatement().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        getLogger().info("LibreMC Core: Cya");
    }

    public void reload(){
        onDisable();
        onEnable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args[0].equalsIgnoreCase("reload")){
            if(sender.hasPermission("libremc_core.reload") || sender.isOp()){
                sender.sendMessage("[Core]: Reloading");
                reload();
            }else{
                sender.sendMessage("[Core]: No permission!");
            }
        }



        return true;
    }
}
