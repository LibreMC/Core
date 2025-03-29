package org.libremc.libreMC_Core;

import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Punishment {
    public enum PunishmentType {
        BANNED,
        WARNED,
        MUTED
    }

    public static void addPunishment(UUID uuid, Punishment.PunishmentType type, long length_ms, String reason) throws SQLException {
        long time = System.currentTimeMillis();
        String query = "INSERT INTO punishment_table (uuid, punishment_type, date_issued, date_of_expiry, reason) VALUES(?, ?, ?, ?, ?);";

        try (PreparedStatement statement = Core.db.getStatement().getConnection().prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, type.toString());
            statement.setLong(3, time);
            statement.setLong(4, time + length_ms);
            statement.setString(5, reason);
            statement.execute();
        }
    }

    public static ResultSet getPunishments(UUID uuid) throws SQLException {
        String query = "SELECT punishment_id, punishment_type, date_issued, date_of_expiry, reason FROM punishment_table WHERE uuid = ?";
        ResultSet set;

        PreparedStatement statement = Core.db.getStatement().getConnection().prepareStatement(query);        statement.setString(1, uuid.toString());
        set = statement.executeQuery();

        return set;
    }

    // Deletes latest punishment of that type
    public static void removePunishment(UUID uuid, PunishmentType type) throws SQLException {
        String query = "DELETE FROM punishment_table WHERE date_issued = (SELECT MAX(date_issued) FROM punishment_table WHERE uuid = ? AND punishment_type = ?);";

        try (PreparedStatement statement = Core.db.getStatement().getConnection().prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, type.toString());
            statement.execute();
        }
    }

    // Deletes the specific punishment by ID
    public static void removePunishment(int punishment_id) throws SQLException {
        String query = "DELETE FROM punishment_table WHERE punishment_id = ?);";
        try (PreparedStatement statement = Core.db.getStatement().getConnection().prepareStatement(query)) {
            statement.setLong(1, punishment_id);
            statement.execute();
        }
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
