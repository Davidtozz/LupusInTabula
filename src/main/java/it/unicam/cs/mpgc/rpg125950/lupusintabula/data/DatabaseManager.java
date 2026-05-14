package it.unicam.cs.mpgc.rpg125950.lupusintabula.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {
    // Per la persistenza su file, cambiare la connectionString in: jdbc:h2:file:./data/lupusintabula
    private static final String URL = "jdbc:h2:mem:lupusintabula;DB_CLOSE_DELAY=-1"; // <- "-1" lascia il database aperto finche' l'app e' in esecuzione
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
