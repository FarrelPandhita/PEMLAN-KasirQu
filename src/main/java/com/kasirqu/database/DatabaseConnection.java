package com.kasirqu.database;

import com.kasirqu.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton database connection manager.
 * Reads credentials from .env via DatabaseConfig (never hardcoded).
 *
 * Usage:
 *   Connection conn = DatabaseConnection.getConnection();
 *
 * Setup:
 *   1. Copy .env.example → .env at project root
 *   2. Fill in your local MySQL credentials
 */
public class DatabaseConnection {

    private static Connection connection;

    /**
     * Returns a singleton database connection.
     * Creates a new connection if none exists or the previous one was closed.
     *
     * @return the active Connection
     * @throws RuntimeException if connection cannot be established
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        DatabaseConfig.getJdbcUrl(),
                        DatabaseConfig.DB_USER,
                        DatabaseConfig.DB_PASSWORD
                );
                System.out.println("[DB] Connected to " + DatabaseConfig.DB_NAME
                        + " @ " + DatabaseConfig.DB_HOST + ":" + DatabaseConfig.DB_PORT);
            }
        } catch (SQLException e) {
            System.err.println("[DB] Connection failed: " + e.getMessage());
            throw new RuntimeException("Gagal terhubung ke database. "
                    + "Pastikan MySQL berjalan dan file .env sudah dikonfigurasi.", e);
        }
        return connection;
    }

    /**
     * Closes the active connection if open.
     * Call this on application shutdown.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Error closing connection: " + e.getMessage());
        }
    }
}