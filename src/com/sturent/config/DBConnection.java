package com.sturent.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class DBConnection {

    private static final String DEFAULT_HOST = "mysql-178f6340-sturent.h.aivencloud.com";
    private static final String DEFAULT_PORT = "17980";
    private static final String DEFAULT_DB = "defaultdb";
    private static final String DEFAULT_USER = "avnadmin";

    private static String host = DEFAULT_HOST;
    private static String port = DEFAULT_PORT;
    private static String database = DEFAULT_DB;
    private static String user = DEFAULT_USER;
    private static String password = null;
    private static boolean driverRegistered = false;

    static {
        ensureDriverRegistered();
        loadConfiguration();
    }

    public static synchronized void ensureDriverRegistered() {
        if (driverRegistered) return;

        // 1. Try standard Class.forName
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverRegistered = true;
            return;
        } catch (Throwable ignored) {
        }

        // 2. Fallback: dynamically load from known JAR locations
        String[] candidatePaths = {
                "lib/mysql-connector.jar",
                "src/com/sturent/CampusMart_Aiven_MySQL_Test_Java17.jar",
                "CampusMart_Aiven_MySQL_Test_Java17.jar"
        };

        for (String path : candidatePaths) {
            File jarFile = new File(path);
            if (jarFile.exists()) {
                try {
                    URL[] urls = new URL[]{jarFile.toURI().toURL()};
                    URLClassLoader loader = new URLClassLoader(urls, DBConnection.class.getClassLoader());
                    Class<?> clazz = Class.forName("com.mysql.cj.jdbc.Driver", true, loader);
                    Driver rawDriver = (Driver) clazz.getDeclaredConstructor().newInstance();
                    DriverManager.registerDriver(new DriverShim(rawDriver));
                    driverRegistered = true;
                    return;
                } catch (Throwable ignored) {
                }
            }
        }
    }

    public static synchronized void loadConfiguration() {
        // 1. Try to load from db.properties if present
        File propFile = new File("db.properties");
        if (propFile.exists()) {
            try (InputStream in = new FileInputStream(propFile)) {
                Properties props = new Properties();
                props.load(in);
                if (props.getProperty("db.host") != null && !props.getProperty("db.host").isBlank()) {
                    host = props.getProperty("db.host").trim();
                }
                if (props.getProperty("db.port") != null && !props.getProperty("db.port").isBlank()) {
                    port = props.getProperty("db.port").trim();
                }
                if (props.getProperty("db.name") != null && !props.getProperty("db.name").isBlank()) {
                    database = props.getProperty("db.name").trim();
                }
                if (props.getProperty("db.user") != null && !props.getProperty("db.user").isBlank()) {
                    user = props.getProperty("db.user").trim();
                }
                if (props.getProperty("db.password") != null && !props.getProperty("db.password").isBlank()) {
                    password = props.getProperty("db.password").trim();
                }
            } catch (Exception ignored) {
            }
        }

        // 2. Override from system properties or environment variables
        if (System.getProperty("db.password") != null && !System.getProperty("db.password").isBlank()) {
            password = System.getProperty("db.password").trim();
        } else if (System.getenv("db.password") != null && !System.getenv("db.password").isBlank()) {
            password = System.getenv("db.password").trim();
        } else if (System.getenv("DB_PASSWORD") != null && !System.getenv("DB_PASSWORD").isBlank()) {
            password = System.getenv("DB_PASSWORD").trim();
        }

        if (System.getProperty("db.user") != null && !System.getProperty("db.user").isBlank()) {
            user = System.getProperty("db.user").trim();
        } else if (System.getenv("DB_USER") != null && !System.getenv("DB_USER").isBlank()) {
            user = System.getenv("DB_USER").trim();
        }

        if (System.getProperty("db.host") != null && !System.getProperty("db.host").isBlank()) {
            host = System.getProperty("db.host").trim();
        } else if (System.getenv("DB_HOST") != null && !System.getenv("DB_HOST").isBlank()) {
            host = System.getenv("DB_HOST").trim();
        }
    }

    public static void setPassword(String pass) {
        password = pass;
    }

    public static void setHost(String h) {
        host = h;
    }

    public static void setUser(String u) {
        user = u;
    }

    public static void setDatabase(String db) {
        database = db;
    }

    public static String getUrl() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database + "?sslMode=REQUIRED";
    }

    public static String getUser() {
        return user;
    }

    public static Connection getConnection() throws SQLException {
        ensureDriverRegistered();

        if (password == null || password.trim().isEmpty()) {
            loadConfiguration();
        }
        if (password == null || password.trim().isEmpty()) {
            throw new SQLException(
                "Database password is not set! Please set 'db.password=YOUR_PASSWORD' in db.properties."
            );
        }

        Connection conn = DriverManager.getConnection(getUrl(), user, password);
        DatabaseInitializer.initialize(conn);
        return conn;
    }

    /**
     * Driver wrapper allowing dynamically loaded JDBC drivers to be registered with DriverManager
     */
    private static class DriverShim implements Driver {
        private final Driver driver;

        public DriverShim(Driver driver) {
            this.driver = driver;
        }

        @Override
        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        @Override
        public Connection connect(String url, Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        @Override
        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        @Override
        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        @Override
        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return driver.getParentLogger();
        }
    }
}
