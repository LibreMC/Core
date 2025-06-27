package org.libremc.libreMC_Core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.libremc.libreMC_Core.Punishment;

abstract public class PunishmentEvent extends Event {
    protected static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    protected String punisher, punished, reason;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public String getPunished() {
        return punished;
    }

    public String getPunisher() {
        return punisher;
    }

    public String getReason() {
        return reason;
    }

}
