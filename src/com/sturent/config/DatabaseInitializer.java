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

            // 7. Seed sample cart item for user 1 if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM cart WHERE user_id = 1")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.executeUpdate(
                        "INSERT INTO cart (user_id, item_id, quantity, unit_price) " +
                        "VALUES (1, 101, 1, 11111.0)"
                    );
                }
            }

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

