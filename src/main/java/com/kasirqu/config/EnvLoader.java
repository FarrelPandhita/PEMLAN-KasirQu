package com.kasirqu.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Loads environment variables from the .env file at project root.
 * Uses dotenv-java library to ensure credentials are never hardcoded.
 *
 * Usage:
 *   String dbHost = EnvLoader.get("DB_HOST");
 *
 * Setup:
 *   1. Copy .env.example → .env
 *   2. Fill in your local database credentials
 *   3. .env is git-ignored (never committed)
 */
public class EnvLoader {

    private static final Dotenv dotenv;

    static {
        dotenv = Dotenv.configure()
                .ignoreIfMissing()   // Don't crash if .env is absent (fallback to system env)
                .load();
    }

    /**
     * Gets an environment variable value.
     * Checks .env file first, then falls back to system environment variables.
     *
     * @param key the variable name (e.g. "DB_HOST")
     * @return the value, or null if not found
     */
    public static String get(String key) {
        return dotenv.get(key);
    }

    /**
     * Gets an environment variable with a default fallback.
     *
     * @param key          the variable name
     * @param defaultValue fallback if key is not found
     * @return the value or defaultValue
     */
    public static String get(String key, String defaultValue) {
        String value = dotenv.get(key);
        return value != null ? value : defaultValue;
    }
}
