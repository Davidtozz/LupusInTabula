package it.unicam.cs.mpgc.rpg125950.lupusintabula.data;

import org.h2.tools.RunScript;
import org.h2.tools.Server;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {
    private static final String URL = "jdbc:h2:mem:lupusintabula;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Server server;

    private DatabaseManager() {
    }

    public static void bootstrap() throws SQLException, FileNotFoundException {
        if (server == null) {
            server = Server.createWebServer("-webPort", "8082", "-webAllowOthers").start();
            System.out.println("H2 Web Console started at: " + server.getURL());
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

        final String resourcePath = "/it/unicam/cs/mpgc/rpg125950/lupusintabula/init.sql";

        InputStream is = DatabaseManager.class.getResourceAsStream(resourcePath);

        if (is == null) {
            throw new FileNotFoundException("Resource not found on classpath: " + resourcePath);
        }

        // Wrap the stream in a Reader explicitly enforcing UTF-8 encoding
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            RunScript.execute(conn, reader);
        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            throw new RuntimeException("Error processing database init script", e);
        }
    }
}