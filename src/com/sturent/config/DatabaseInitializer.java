package com.sturent.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    private static boolean initialized = false;

    public static synchronized void initialize(Connection conn) {
        if (initialized) return;

        try (Statement stmt = conn.createStatement()) {
            // 1. Cart table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS cart (" +
                "  cart_item_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  user_id INT NOT NULL," +
                "  item_id INT NOT NULL," +
                "  quantity INT NOT NULL DEFAULT 1," +
                "  unit_price DOUBLE NOT NULL" +
                ")"
            );

            // 2. Orders table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS orders (" +
                "  order_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  user_id INT NOT NULL," +
                "  total_amount DOUBLE NOT NULL," +
                "  status VARCHAR(50) DEFAULT 'PENDING'," +
                "  delivery_address TEXT," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // 3. Order Items table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS order_items (" +
                "  order_item_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  order_id INT NOT NULL," +
                "  item_id INT NOT NULL," +
                "  quantity INT NOT NULL," +
                "  unit_price DOUBLE NOT NULL" +
                ")"
            );

            // 4. Messages table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS messages (" +
                "  message_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  sender_id INT NOT NULL," +
                "  receiver_id INT NOT NULL," +
                "  content TEXT NOT NULL," +
                "  is_read BOOLEAN DEFAULT FALSE," +
                "  sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // 5. Reviews table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS reviews (" +
                "  review_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  reviewer_id INT NOT NULL," +
                "  seller_id INT NOT NULL," +
                "  item_id INT NOT NULL," +
                "  rating INT NOT NULL," +
                "  comment TEXT," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // 6. Notifications table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS notifications (" +
                "  notification_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  user_id INT NOT NULL," +
                "  title VARCHAR(255) NOT NULL," +
                "  message TEXT NOT NULL," +
                "  is_read BOOLEAN DEFAULT FALSE," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // 7. Users table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "  user_id VARCHAR(50) PRIMARY KEY," +
                "  name VARCHAR(100) NOT NULL," +
                "  email VARCHAR(150) NOT NULL UNIQUE," +
                "  password_hash VARCHAR(255) NOT NULL," +
                "  password_salt VARCHAR(255) NOT NULL," +
                "  phone VARCHAR(20) NOT NULL," +
                "  role VARCHAR(20) NOT NULL" +
                ")"
            );

            // 8. Items table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS items (" +
                "  item_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  user_id VARCHAR(50) NOT NULL," +
                "  title VARCHAR(255) NOT NULL," +
                "  type VARCHAR(50) NOT NULL," +
                "  sale_price DOUBLE DEFAULT 0," +
                "  rent_price DOUBLE DEFAULT 0," +
                "  rent_period VARCHAR(50)," +
                "  status VARCHAR(50) DEFAULT 'AVAILABLE'," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // 9. Complaints table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS complaints (" +
                "  complaint_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  user_id VARCHAR(50) NOT NULL," +
                "  item_id INT," +
                "  complaint_type VARCHAR(100) NOT NULL," +
                "  description TEXT," +
                "  status VARCHAR(50) DEFAULT 'PENDING'," +
                "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // 10. Item Images table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS item_images (" +
                "  image_id INT AUTO_INCREMENT PRIMARY KEY," +
                "  item_id INT NOT NULL," +
                "  image_url VARCHAR(500) NOT NULL," +
                "  is_primary BOOLEAN DEFAULT FALSE" +
                ")"
            );

            // 11. Seed sample cart item for user 1 if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM cart WHERE user_id = 1")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate(
                        "INSERT INTO cart (user_id, item_id, quantity, unit_price) " +
                        "VALUES (1, 101, 1, 11111.0)"
                    );
                }
            }

            // 12. Seed default admin and student accounts if missing
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE email = 'admin@sturent.com'")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    byte[] salt = com.sturent.util.PasswordUtil.generateSalt();
                    String hash = com.sturent.util.PasswordUtil.hashPassword("admin123", salt);
                    String saltStr = java.util.Base64.getEncoder().encodeToString(salt);
                    try (java.sql.PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO users (user_id, name, email, password_hash, password_salt, phone, role) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                        ps.setString(1, "admin1");
                        ps.setString(2, "StuRent Admin");
                        ps.setString(3, "admin@sturent.com");
                        ps.setString(4, hash);
                        ps.setString(5, saltStr);
                        ps.setString(6, "9876543210");
                        ps.setString(7, "ADMIN");
                        ps.executeUpdate();
                    }
                }
            } catch (Exception ignored) {}

            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE email = 'student@sturent.com'")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    byte[] salt = com.sturent.util.PasswordUtil.generateSalt();
                    String hash = com.sturent.util.PasswordUtil.hashPassword("student123", salt);
                    String saltStr = java.util.Base64.getEncoder().encodeToString(salt);
                    try (java.sql.PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO users (user_id, name, email, password_hash, password_salt, phone, role) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                        ps.setString(1, "student1");
                        ps.setString(2, "Campus Student");
                        ps.setString(3, "student@sturent.com");
                        ps.setString(4, hash);
                        ps.setString(5, saltStr);
                        ps.setString(6, "9876543211");
                        ps.setString(7, "STUDENT");
                        ps.executeUpdate();
                    }
                }
            } catch (Exception ignored) {}

            initialized = true;
        } catch (Exception ex) {
            System.err.println("Warning: Could not auto-initialize tables: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Connecting to database and initializing...");
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Successfully connected to Aiven MySQL!");
            initialize(conn);
            System.out.println("Database initialization completed.");
            try (Statement st = conn.createStatement()) {
                String[] tables = {"cart", "orders", "order_items", "messages", "reviews", "notifications"};
                for (String t : tables) {
                    try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + t)) {
                        if (rs.next()) {
                            System.out.println("  Table '" + t + "' row count: " + rs.getInt(1));
                        }
                    } catch (Exception e) {
                        System.err.println("  Table '" + t + "' check failed: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

