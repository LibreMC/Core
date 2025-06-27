package org.libremc.libreMC_Core.events;

public class MuteEvent extends PunishmentEvent{
    private String length_formatted;

    public MuteEvent(String punisher, String punished, String reason, String length_formatted){
        this.punisher = punisher;
        this.punished = punished;
        this.reason = reason;
        this.length_formatted = length_formatted;
    }

    public String getLengthFormatted() {
        return length_formatted;
    }
}
