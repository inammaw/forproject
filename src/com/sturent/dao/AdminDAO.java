package com.sturent.dao;

import com.sturent.db.DBConnection;
import com.sturent.model.admin.AdminComplaint;
import com.sturent.model.admin.AdminItem;
import com.sturent.model.admin.AdminReview;
import com.sturent.model.admin.AdminUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // =========================================================
    // DASHBOARD
    // =========================================================

    public int getUserCount() throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM users";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }


    public int getItemCount() throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM items";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }


    public int getAvailableItemCount()
            throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM items " +
                        "WHERE status = 'AVAILABLE'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }


    // =========================================================
    // USERS
    // =========================================================

    public List<AdminUser> getAllUsers()
            throws SQLException {

        List<AdminUser> users =
                new ArrayList<>();

        String sql =
                "SELECT user_id, name, email, phone, role " +
                        "FROM users " +
                        "ORDER BY name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                AdminUser user =
                        new AdminUser(
                                resultSet.getString("user_id"),
                                resultSet.getString("name"),
                                resultSet.getString("email"),
                                resultSet.getString("phone"),
                                resultSet.getString("role")
                        );

                users.add(user);
            }
        }

        return users;
    }


    public List<AdminUser> searchUsers(
            String keyword
    ) throws SQLException {

        List<AdminUser> users =
                new ArrayList<>();

        String sql =
                "SELECT user_id, name, email, phone, role " +
                        "FROM users " +
                        "WHERE user_id LIKE ? " +
                        "OR name LIKE ? " +
                        "OR email LIKE ? " +
                        "ORDER BY name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            String searchPattern =
                    "%" + keyword + "%";

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    AdminUser user =
                            new AdminUser(
                                    resultSet.getString("user_id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("email"),
                                    resultSet.getString("phone"),
                                    resultSet.getString("role")
                            );

                    users.add(user);
                }
            }
        }

        return users;
    }


    // =========================================================
    // ITEMS
    // =========================================================

    public List<AdminItem> getAllItems()
            throws SQLException {

        List<AdminItem> items =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "i.item_id, " +
                        "i.user_id, " +
                        "u.name AS owner_name, " +
                        "i.title, " +
                        "i.listing_type, " +
                        "i.sale_price, " +
                        "i.rent_price, " +
                        "i.rental_unit, " +
                        "i.deposit, " +
                        "i.status " +
                        "FROM items i " +
                        "LEFT JOIN users u " +
                        "ON i.user_id = u.user_id " +
                        "ORDER BY i.item_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                AdminItem item =
                        new AdminItem(
                                resultSet.getInt("item_id"),
                                resultSet.getString("user_id"),
                                resultSet.getString("owner_name"),
                                resultSet.getString("title"),
                                resultSet.getString("listing_type"),
                                resultSet.getBigDecimal("sale_price"),
                                resultSet.getBigDecimal("rent_price"),
                                resultSet.getString("rental_unit"),
                                resultSet.getBigDecimal("deposit"),
                                resultSet.getString("status")
                        );

                items.add(item);
            }
        }

        return items;
    }


    public List<AdminItem> searchItems(
            String keyword
    ) throws SQLException {

        List<AdminItem> items =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "i.item_id, " +
                        "i.user_id, " +
                        "u.name AS owner_name, " +
                        "i.title, " +
                        "i.listing_type, " +
                        "i.sale_price, " +
                        "i.rent_price, " +
                        "i.rental_unit, " +
                        "i.deposit, " +
                        "i.status " +
                        "FROM items i " +
                        "LEFT JOIN users u " +
                        "ON i.user_id = u.user_id " +
                        "WHERE i.title LIKE ? " +
                        "OR i.user_id LIKE ? " +
                        "OR i.status LIKE ? " +
                        "OR i.listing_type LIKE ? " +
                        "ORDER BY i.item_id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            String pattern =
                    "%" + keyword + "%";

            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);
            statement.setString(4, pattern);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    items.add(
                            new AdminItem(
                                    resultSet.getInt("item_id"),
                                    resultSet.getString("user_id"),
                                    resultSet.getString("owner_name"),
                                    resultSet.getString("title"),
                                    resultSet.getString("listing_type"),
                                    resultSet.getBigDecimal("sale_price"),
                                    resultSet.getBigDecimal("rent_price"),
                                    resultSet.getString("rental_unit"),
                                    resultSet.getBigDecimal("deposit"),
                                    resultSet.getString("status")
                            )
                    );
                }
            }
        }

        return items;
    }


    // =========================================================
    // COMPLAINTS
    // =========================================================

    public List<AdminComplaint> getAllComplaints()
            throws SQLException {

        List<AdminComplaint> complaints =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "complaint_id, " +
                        "user_id, " +
                        "item_id, " +
                        "complaint_type, " +
                        "description, " +
                        "status, " +
                        "admin_response, " +
                        "created_at, " +
                        "resolved_at " +
                        "FROM complaints " +
                        "ORDER BY created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                AdminComplaint complaint =
                        new AdminComplaint(
                                resultSet.getInt("complaint_id"),
                                resultSet.getString("user_id"),
                                resultSet.getInt("item_id"),
                                resultSet.getString("complaint_type"),
                                resultSet.getString("description"),
                                resultSet.getString("status"),
                                resultSet.getString("admin_response"),
                                resultSet.getTimestamp("created_at"),
                                resultSet.getTimestamp("resolved_at")
                        );

                complaints.add(complaint);
            }
        }

        return complaints;
    }


    public List<AdminComplaint> searchComplaints(
            String keyword
    ) throws SQLException {

        List<AdminComplaint> complaints =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "complaint_id, " +
                        "user_id, " +
                        "item_id, " +
                        "complaint_type, " +
                        "description, " +
                        "status, " +
                        "admin_response, " +
                        "created_at, " +
                        "resolved_at " +
                        "FROM complaints " +
                        "WHERE CAST(complaint_id AS CHAR) LIKE ? " +
                        "OR user_id LIKE ? " +
                        "OR CAST(item_id AS CHAR) LIKE ? " +
                        "OR complaint_type LIKE ? " +
                        "OR description LIKE ? " +
                        "OR status LIKE ? " +
                        "ORDER BY created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            String pattern =
                    "%" + keyword + "%";

            for (int i = 1; i <= 6; i++) {
                statement.setString(i, pattern);
            }

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    complaints.add(
                            new AdminComplaint(
                                    resultSet.getInt("complaint_id"),
                                    resultSet.getString("user_id"),
                                    resultSet.getInt("item_id"),
                                    resultSet.getString("complaint_type"),
                                    resultSet.getString("description"),
                                    resultSet.getString("status"),
                                    resultSet.getString("admin_response"),
                                    resultSet.getTimestamp("created_at"),
                                    resultSet.getTimestamp("resolved_at")
                            )
                    );
                }
            }
        }

        return complaints;
    }


    public void updateComplaint(
            int complaintId,
            String status,
            String adminResponse
    ) throws SQLException {

        String sql =
                "UPDATE complaints " +
                        "SET status = ?, " +
                        "admin_response = ?, " +
                        "resolved_at = CASE " +
                        "WHEN ? = 'RESOLVED' " +
                        "THEN CURRENT_TIMESTAMP " +
                        "ELSE NULL END " +
                        "WHERE complaint_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setString(2, adminResponse);
            statement.setString(3, status);
            statement.setInt(4, complaintId);

            statement.executeUpdate();
        }
    }


    // =========================================================
    // REVIEWS
    // =========================================================

    public List<AdminReview> getAllReviews()
            throws SQLException {

        List<AdminReview> reviews =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "review_id, " +
                        "reviewer_id, " +
                        "seller_id, " +
                        "item_id, " +
                        "rating, " +
                        "comment, " +
                        "created_at " +
                        "FROM reviews " +
                        "ORDER BY created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                AdminReview review =
                        new AdminReview(
                                resultSet.getInt("review_id"),
                                resultSet.getInt("reviewer_id"),
                                resultSet.getInt("seller_id"),
                                resultSet.getInt("item_id"),
                                resultSet.getInt("rating"),
                                resultSet.getString("comment"),
                                resultSet.getTimestamp("created_at")
                        );

                reviews.add(review);
            }
        }

        return reviews;
    }


    public List<AdminReview> searchReviews(
            String keyword
    ) throws SQLException {

        List<AdminReview> reviews =
                new ArrayList<>();

        String sql =
                "SELECT " +
                        "review_id, " +
                        "reviewer_id, " +
                        "seller_id, " +
                        "item_id, " +
                        "rating, " +
                        "comment, " +
                        "created_at " +
                        "FROM reviews " +
                        "WHERE CAST(review_id AS CHAR) LIKE ? " +
                        "OR CAST(reviewer_id AS CHAR) LIKE ? " +
                        "OR CAST(seller_id AS CHAR) LIKE ? " +
                        "OR CAST(item_id AS CHAR) LIKE ? " +
                        "OR CAST(rating AS CHAR) LIKE ? " +
                        "OR comment LIKE ? " +
                        "ORDER BY created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            String pattern =
                    "%" + keyword + "%";

            for (int i = 1; i <= 6; i++) {
                statement.setString(i, pattern);
            }

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    reviews.add(
                            new AdminReview(
                                    resultSet.getInt("review_id"),
                                    resultSet.getInt("reviewer_id"),
                                    resultSet.getInt("seller_id"),
                                    resultSet.getInt("item_id"),
                                    resultSet.getInt("rating"),
                                    resultSet.getString("comment"),
                                    resultSet.getTimestamp("created_at")
                            )
                    );
                }
            }
        }

        return reviews;
    }


    // =========================================================
    // DELETE ITEM
    // =========================================================

    public void deleteItem(int itemId)
            throws SQLException {

        String deleteImages =
                "DELETE FROM item_images " +
                        "WHERE item_id = ?";

        String deleteItem =
                "DELETE FROM items " +
                        "WHERE item_id = ?";

        try (Connection connection =
                     DBConnection.getConnection()) {

            try {

                connection.setAutoCommit(false);

                // Delete images belonging to the item
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     deleteImages)) {

                    statement.setInt(1, itemId);
                    statement.executeUpdate();
                }

                // Delete the item
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     deleteItem)) {

                    statement.setInt(1, itemId);
                    statement.executeUpdate();
                }

                connection.commit();

            } catch (SQLException e) {

                connection.rollback();

                throw e;
            }
        }
    }


    // =========================================================
    // DELETE USER
    // =========================================================

    public void deleteUser(String userId)
            throws SQLException {

        String deleteImages =
                "DELETE FROM item_images " +
                        "WHERE item_id IN " +
                        "(SELECT item_id FROM items WHERE user_id = ?)";

        String deleteItems =
                "DELETE FROM items " +
                        "WHERE user_id = ?";

        String deleteUser =
                "DELETE FROM users " +
                        "WHERE user_id = ?";

        try (Connection connection =
                     DBConnection.getConnection()) {

            try {

                connection.setAutoCommit(false);

                // 1. Delete the user's item images
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     deleteImages)) {

                    statement.setString(1, userId);
                    statement.executeUpdate();
                }

                // 2. Delete the user's items
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     deleteItems)) {

                    statement.setString(1, userId);
                    statement.executeUpdate();
                }

                // 3. Delete the user
                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     deleteUser)) {

                    statement.setString(1, userId);
                    statement.executeUpdate();
                }

                connection.commit();

            } catch (SQLException e) {

                connection.rollback();

                throw e;
            }
        }
    }
}