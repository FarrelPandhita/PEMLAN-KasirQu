package com.kasirqu.config;

/**
 * Holds database configuration properties loaded from .env.
 * Used by DatabaseConnection to construct the JDBC URL.
 *
 * All values are read once at class-load time via EnvLoader.
 */
public class DatabaseConfig {

    public static final String DB_HOST     = EnvLoader.get("DB_HOST",     "localhost");
    public static final String DB_PORT     = EnvLoader.get("DB_PORT",     "3306");
    public static final String DB_NAME     = EnvLoader.get("DB_NAME",     "db_kasir_dev");
    public static final String DB_USER     = EnvLoader.get("DB_USER",     "root");
    public static final String DB_PASSWORD = EnvLoader.get("DB_PASSWORD", "");

    /**
     * Builds the full JDBC URL from the config properties.
     *
     * @return jdbc:mysql://host:port/dbname
     */
    public static String getJdbcUrl() {
        return "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    }
}
