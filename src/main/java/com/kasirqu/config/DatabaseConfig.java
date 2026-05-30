package com.kasirqu.config;

/**
 * Holds database configuration properties loaded EXCLUSIVELY from .env file.
 * No default values — .env MUST exist and contain all required keys.
 *
 * Setup:
 *   1. Copy .env.example → .env
 *   2. Fill in your local credentials
 *   3. If .env is missing, the app will crash with a clear error message.
 */
public class DatabaseConfig {

    public static final String DB_HOST;
    public static final String DB_PORT;
    public static final String DB_NAME;
    public static final String DB_USER;
    public static final String DB_PASSWORD;

    static {
        DB_HOST     = requireEnv("DB_HOST");
        DB_PORT     = requireEnv("DB_PORT");
        DB_NAME     = requireEnv("DB_NAME");
        DB_USER     = requireEnv("DB_USER");
        DB_PASSWORD = requireEnv("DB_PASSWORD");
    }

    /**
     * Reads a required environment variable. Fails fast if missing.
     */
    private static String requireEnv(String key) {
        String value = EnvLoader.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException(
                "[CONFIG ERROR] Variabel '" + key + "' tidak ditemukan. "
                + "Pastikan file .env sudah dibuat dari .env.example dan semua key terisi."
            );
        }
        return value;
    }

    /**
     * Builds the full JDBC URL from the config properties.
     *
     * @return jdbc:mysql://host:port/dbname
     */
    public static String getJdbcUrl() {
        return "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    }
}
