package org.libremc.libreMC_Core.events;

public class WarnEvent extends PunishmentEvent{
    public WarnEvent(String punisher, String punished, String reason){
        this.punisher = punisher;
        this.punished = punished;
        this.reason = reason;
    }
}
