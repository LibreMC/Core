package org.libremc.libreMC_Core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Libremc implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args[0].compareTo("reload") == 0){
            if(sender.hasPermission("libremc.reload") || sender.isOp()){
                sender.sendMessage("[Core] Reloading");
                Core.getInstance().reload();
                sender.sendMessage("[Core] Done");
                return true;
            }
        }

        sender.sendMessage("LibreMC Core - developed by _coolbird");

        return true;
    }
}
