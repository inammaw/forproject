package com.sturent.gui.admin;

import com.sturent.dao.AdminDAO;
import com.sturent.model.admin.AdminUser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AdminUserPanel extends JPanel {

    private final AdminDAO adminDAO;

    private final JTextField searchField;
    private final JTable userTable;
    private final DefaultTableModel tableModel;

    // ---------------- COLORS ----------------

    private static final Color BACKGROUND_COLOR =
            new Color(245, 247, 249);

    private static final Color CARD_COLOR =
            Color.WHITE;

    private static final Color TEXT_COLOR =
            new Color(35, 38, 42);

    private static final Color SECONDARY_TEXT =
            new Color(110, 116, 125);

    private static final Color BORDER_COLOR =
            new Color(225, 228, 232);

    private static final Color ACCENT_COLOR =
            new Color(45, 110, 85);

    private static final Color TABLE_HEADER_COLOR =
            new Color(235, 238, 241);


    // =============================================================
    // CONSTRUCTOR
    // =============================================================

    public AdminUserPanel() {

        adminDAO = new AdminDAO();

        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND_COLOR);

        setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );


        // =========================================================
        // TOP SECTION
        // =========================================================

        JPanel topPanel =
                new JPanel(new BorderLayout(15, 15));

        topPanel.setOpaque(false);


        // ---------------- TITLE ----------------

                // =========================================================
        // SEARCH / ACTION BAR
        // =========================================================

        JPanel actionPanel =
                new JPanel(new BorderLayout(10, 10));

        actionPanel.setBackground(
                CARD_COLOR
        );

        actionPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                15,
                                12,
                                15
                        )
                )
        );


        // ---------------- SEARCH ----------------

        JPanel searchPanel =
                new JPanel(
                        new BorderLayout(8, 0)
                );

        searchPanel.setOpaque(false);


        JLabel searchLabel =
                new JLabel("Search:");

        searchLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        searchLabel.setForeground(
                TEXT_COLOR
        );


        searchField =
                new JTextField();

        searchField.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        searchField.setPreferredSize(
                new Dimension(
                        280,
                        36
                )
        );


        JButton searchButton =
                createNormalButton("Search");

        JButton refreshButton =
                createNormalButton("Refresh");


        searchPanel.add(
                searchLabel,
                BorderLayout.WEST
        );

        searchPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        searchPanel.add(
                searchButton,
                BorderLayout.EAST
        );


        actionPanel.add(
                searchPanel,
                BorderLayout.CENTER
        );


        // ---------------- DELETE ----------------

        JButton deleteButton =
                createDeleteButton("Delete User");


        actionPanel.add(
                deleteButton,
                BorderLayout.EAST
        );


        // ---------------- REFRESH ----------------

        JPanel refreshPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                0,
                                0
                        )
                );

        refreshPanel.setOpaque(false);

        refreshPanel.add(refreshButton);


        actionPanel.add(
                refreshPanel,
                BorderLayout.SOUTH
        );


        topPanel.add(
                actionPanel,
                BorderLayout.CENTER
        );


        add(
                topPanel,
                BorderLayout.NORTH
        );


        // =========================================================
        // TABLE
        // =========================================================

        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "User ID",
                                "Name",
                                "Email",
                                "Phone",
                                "Role"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };


        userTable =
                new JTable(tableModel);

        userTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        userTable.setRowHeight(34);

        userTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        userTable.setForeground(
                TEXT_COLOR
        );

        userTable.setBackground(
                Color.WHITE
        );

        userTable.setGridColor(
                BORDER_COLOR
        );

        userTable.setShowVerticalLines(false);

        userTable.setSelectionBackground(
                new Color(220, 235, 228)
        );

        userTable.setSelectionForeground(
                TEXT_COLOR
        );


        // =========================================================
        // TABLE HEADER
        // =========================================================

        JTableHeader tableHeader =
                userTable.getTableHeader();

        tableHeader.setBackground(
                TABLE_HEADER_COLOR
        );

        tableHeader.setForeground(
                TEXT_COLOR
        );

        tableHeader.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        tableHeader.setPreferredSize(
                new Dimension(
                        tableHeader.getWidth(),
                        38
                )
        );


        // =========================================================
        // COLUMN ALIGNMENT
        // =========================================================

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        userTable
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(centerRenderer);

        userTable
                .getColumnModel()
                .getColumn(4)
                .setCellRenderer(centerRenderer);


        // =========================================================
        // COLUMN WIDTHS
        // =========================================================

        userTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(100);

        userTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(150);

        userTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(230);

        userTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(130);

        userTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(100);


        // =========================================================
        // SCROLL PANE
        // =========================================================

        JScrollPane scrollPane =
                new JScrollPane(userTable);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        BORDER_COLOR
                )
        );

        scrollPane.getViewport().setBackground(
                Color.WHITE
        );


        add(
                scrollPane,
                BorderLayout.CENTER
        );


        // =========================================================
        // BUTTON ACTIONS
        // =========================================================

        searchButton.addActionListener(
                e -> searchUsers()
        );


        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadUsers();
                }
        );


        searchField.addActionListener(
                e -> searchUsers()
        );


        deleteButton.addActionListener(
                e -> deleteSelectedUser()
        );


        // =========================================================
        // LOAD USERS
        // =========================================================

        loadUsers();
    }


    // =============================================================
    // NORMAL BUTTON
    // =============================================================

    private JButton createNormalButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                TEXT_COLOR
        );

        button.setBackground(
                Color.WHITE
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                15,
                                8,
                                15
                        )
                )
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }


    // =============================================================
    // DELETE BUTTON
    // =============================================================

    private JButton createDeleteButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setForeground(
                new Color(150, 60, 60)
        );

        button.setBackground(
                Color.WHITE
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 180, 180)
                        ),
                        BorderFactory.createEmptyBorder(
                                8,
                                15,
                                8,
                                15
                        )
                )
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return button;
    }


    // =============================================================
    // LOAD ALL USERS
    // =============================================================

    private void loadUsers() {

        try {

            List<AdminUser> users =
                    adminDAO.getAllUsers();

            displayUsers(users);

        } catch (Exception e) {

            showDatabaseError(e);
        }
    }


    // =============================================================
    // SEARCH USERS
    // =============================================================

    private void searchUsers() {

        String keyword =
                searchField
                        .getText()
                        .trim();


        try {

            if (keyword.isEmpty()) {

                loadUsers();

                return;
            }


            List<AdminUser> users =
                    adminDAO.searchUsers(keyword);

            displayUsers(users);


        } catch (Exception e) {

            showDatabaseError(e);
        }
    }


    // =============================================================
    // DISPLAY USERS
    // =============================================================

    private void displayUsers(
            List<AdminUser> users
    ) {

        tableModel.setRowCount(0);


        for (AdminUser user : users) {

            tableModel.addRow(
                    new Object[]{
                            user.getUserId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPhone(),
                            user.getRole()
                    }
            );
        }
    }


    // =============================================================
    // DELETE SELECTED USER
    // =============================================================

    private void deleteSelectedUser() {

        int selectedRow =
                userTable.getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a user first.",
                    "No User Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        String userId =
                tableModel
                        .getValueAt(
                                selectedRow,
                                0
                        )
                        .toString();


        String userName =
                tableModel
                        .getValueAt(
                                selectedRow,
                                1
                        )
                        .toString();


        String role =
                tableModel
                        .getValueAt(
                                selectedRow,
                                4
                        )
                        .toString();


        // =========================================================
        // CONFIRMATION
        // =========================================================

        int choice =
                JOptionPane.showConfirmDialog(
                        this,

                        "Delete user '" + userName + "'?\n\n"
                                + "User ID: " + userId + "\n"
                                + "Role: " + role + "\n\n"
                                + "Their items and item images will also be deleted.",

                        "Confirm User Deletion",

                        JOptionPane.YES_NO_OPTION,

                        JOptionPane.WARNING_MESSAGE
                );


        if (choice != JOptionPane.YES_OPTION) {

            return;
        }


        // =========================================================
        // DELETE
        // =========================================================

        try {

            adminDAO.deleteUser(userId);


            JOptionPane.showMessageDialog(
                    this,
                    "User deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );


            loadUsers();


        } catch (Exception e) {

            showDatabaseError(e);
        }
    }


    // =============================================================
    // DATABASE ERROR
    // =============================================================

    private void showDatabaseError(
            Exception e
    ) {

        JOptionPane.showMessageDialog(
                this,

                "Database operation failed.\n\n"
                        + e.getMessage(),

                "Database Error",

                JOptionPane.ERROR_MESSAGE
        );
    }
}