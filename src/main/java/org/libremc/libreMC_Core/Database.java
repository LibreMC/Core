package org.libremc.libreMC_Core;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final Statement statement;

    private static final String SQL_INIT_QUERY = """
            CREATE TABLE IF NOT EXISTS punishment_table(
                punishment_id INTEGER PRIMARY KEY AUTOINCREMENT,
                uuid TEXT NOT NULL,
                punishment_type TEXT NOT NULL,
                date_issued INTEGER NOT NULL,
                date_of_expiry INTEGER NOT NULL,
                reason TEXT NOT NULL
            );
            """;

    public Database(String dbFileName) throws SQLException {
        String dbPath = new File(Core.getInstance().getDataFolder(), dbFileName).getAbsolutePath();
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        statement = connection.createStatement();
        statement.executeUpdate(SQL_INIT_QUERY);
    }

    public Statement getStatement() {
        return statement;
    }
}