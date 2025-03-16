package org.libremc.libreMC_Core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Punishment {
    public enum PunishmentType {
        BANNED,
        WARNED,
        MUTED
    }

    static int getValue(PunishmentType type){
        return type.ordinal();
    }

    PunishmentType type;
    long date;
    long expiry_date;
    String reason;

    public static void addPunishment(UUID uuid, Punishment.PunishmentType type, long length_ms, String reason) throws SQLException {
        long time = System.currentTimeMillis();
        String query = "INSERT INTO punishment_table (uuid, punishment_type, date_issued, date_of_expiry, reason) VALUES(?, ?, ?, ?, ?);";

        PreparedStatement statement = Core.db.getStatement().getConnection().prepareStatement(query);

        statement.setString(1, uuid.toString());
        statement.setString(2, type.toString());
        statement.setLong(3, time);
        statement.setLong(4, time + length_ms);
        statement.setString(5, reason);

        statement.execute();
        //statement.close();
    }

    public static ResultSet getPunishments(UUID uuid) throws SQLException {
        long time = System.currentTimeMillis();
        String query = "SELECT punishment_type, date_issued, date_of_expiry, reason FROM punishment_table WHERE uuid = ?";
        PreparedStatement statement = Core.db.getStatement().getConnection().prepareStatement(query);
        statement.setString(1, uuid.toString());
        ResultSet set = statement.executeQuery();
        //statement.close();
        return set;
    }

    public static void removePunishment(UUID uuid, PunishmentType type) throws SQLException {
        String query = "DELETE FROM punishment_table WHERE date_issued = (SELECT MAX(date_issued) FROM punishment_table WHERE uuid = ? AND punishment_type = ?);";
        PreparedStatement statement = Core.db.getStatement().getConnection().prepareStatement(query);
        statement.setString(1, uuid.toString());
        statement.setString(2, type.toString());
        statement.execute();
        //statement.close();
    }

    public static boolean isMuted(Player player) throws SQLException {
        ResultSet set = getPunishments(player.getUniqueId());
        long current_time = System.currentTimeMillis();

        while(set.next()){
            long date_of_expiry = set.getLong("date_of_expiry");
            String type = set.getString("punishment_type");

            if((type.compareTo(Punishment.PunishmentType.MUTED.name()) == 0) && date_of_expiry > current_time){
                set.close();
                return true;
            }
        }

        set.close();
        return false;
    }

    public static long parseDynamicTime(String timeStr) throws IllegalArgumentException {
        long total_millis = 0;
        String[] parts = timeStr.split(" ");

        for (String part : parts) {
            if (part.length() < 2) {
                throw new IllegalArgumentException("Invalid time format: " + part);
            }

            char unit = part.charAt(part.length() - 1);
            long value;
            try {
                value = Long.parseLong(part.substring(0, part.length() - 1));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number in time format: " + part);
            }

            switch (unit) {
                case 'd': // Days
                    total_millis += value * 24 * 60 * 60 * 1000;
                    break;
                case 'h': // Hours
                    total_millis += value * 60 * 60 * 1000;
                    break;
                case 'm': // Minutes
                    total_millis += value * 60 * 1000;
                    break;
                case 's': // Seconds
                    total_millis += value * 1000;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid time unit: " + unit);
            }
        }

        return total_millis;
    }

    public static String formatDuration(long millis) {
            long seconds = millis / 1000;
            long days = seconds / (24 * 3600);
            seconds %= 24 * 3600;
            long hours = seconds / 3600;
            seconds %= 3600;
            long minutes = seconds / 60;
            seconds %= 60;

            StringBuilder sb = new StringBuilder();
            if (days > 0) sb.append(days).append("d ");
            if (hours > 0) sb.append(hours).append("h ");
            if (minutes > 0) sb.append(minutes).append("m ");
            if (seconds > 0) sb.append(seconds).append("s ");
            return sb.toString().trim();
    }
}
