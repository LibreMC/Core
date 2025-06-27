package org.libremc.libreMC_Core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.libremc.libreMC_Core.Punishment;

public class BanEvent extends PunishmentEvent {
    private String length_formatted;

    public BanEvent(String punisher, String punished, String reason, String length_formatted){
        this.punisher = punisher;
        this.punished = punished;
        this.reason = reason;
        this.length_formatted = length_formatted;
    }

    public String getLengthFormatted() {
        return length_formatted;
    }
}
