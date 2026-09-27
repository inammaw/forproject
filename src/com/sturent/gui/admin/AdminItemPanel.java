package com.sturent.gui.admin;

import com.sturent.dao.AdminDAO;
import com.sturent.model.admin.AdminItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminItemPanel extends JPanel {

    private final AdminDAO adminDAO;

    private final JTextField searchField;
    private final JTable itemTable;
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

    public AdminItemPanel() {

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


        // ---------- TITLE ----------

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


        // ---------- SEARCH ----------

        JPanel searchPanel =
                new JPanel(new BorderLayout(8, 0));

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


        // ---------- DELETE BUTTON ----------

        JButton deleteButton =
                createDeleteButton("Delete Item");


        actionPanel.add(
                deleteButton,
                BorderLayout.EAST
        );


        // ---------- REFRESH ----------

        JPanel refreshPanel =
                new JPanel(new FlowLayout(
                        FlowLayout.RIGHT,
                        0,
                        0
                ));

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
                                "Item ID",
                                "Owner ID",
                                "Owner",
                                "Title",
                                "Listing Type",
                                "Sale Price",
                                "Rent Price",
                                "Rental Unit",
                                "Deposit",
                                "Status"
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


        itemTable =
                new JTable(tableModel);

        itemTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        itemTable.setRowHeight(32);

        itemTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        itemTable.setForeground(
                TEXT_COLOR
        );

        itemTable.setBackground(
                Color.WHITE
        );

        itemTable.setGridColor(
                BORDER_COLOR
        );

        itemTable.setShowVerticalLines(false);

        itemTable.setSelectionBackground(
                new Color(220, 235, 228)
        );

        itemTable.setSelectionForeground(
                TEXT_COLOR
        );


        // ---------- TABLE HEADER ----------

        JTableHeader tableHeader =
                itemTable.getTableHeader();

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


        // ---------- CENTER ALIGNMENT ----------

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        for (int i = 0; i < itemTable.getColumnCount(); i++) {

            if (i != 2 && i != 3) {

                itemTable
                        .getColumnModel()
                        .getColumn(i)
                        .setCellRenderer(
                                centerRenderer
                        );
            }
        }


        // ---------- COLUMN WIDTHS ----------

        itemTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(70);

        itemTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(90);

        itemTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(120);

        itemTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(180);

        itemTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(120);

        itemTable
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(90);

        itemTable
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(90);

        itemTable
                .getColumnModel()
                .getColumn(7)
                .setPreferredWidth(100);

        itemTable
                .getColumnModel()
                .getColumn(8)
                .setPreferredWidth(90);

        itemTable
                .getColumnModel()
                .getColumn(9)
                .setPreferredWidth(100);


        JScrollPane scrollPane =
                new JScrollPane(itemTable);

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
                e -> searchItems()
        );

        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadItems();
                }
        );

        searchField.addActionListener(
                e -> searchItems()
        );

        deleteButton.addActionListener(
                e -> deleteSelectedItem()
        );


        // =========================================================
        // LOAD DATA
        // =========================================================

        loadItems();
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
    // LOAD ITEMS
    // =============================================================

    private void loadItems() {

        try {

            List<AdminItem> items =
                    adminDAO.getAllItems();

            displayItems(items);

        } catch (Exception e) {

            showDatabaseError(e);
        }
    }


    // =============================================================
    // SEARCH ITEMS
    // =============================================================

    private void searchItems() {

        String keyword =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();


        try {

            if (keyword.isEmpty()) {

                loadItems();

                return;
            }


            List<AdminItem> allItems =
                    adminDAO.getAllItems();


            List<AdminItem> filteredItems =
                    new ArrayList<>();


            for (AdminItem item : allItems) {

                if (

                        String.valueOf(
                                        item.getItemId()
                                )
                                .contains(keyword)

                                ||

                                item.getUserId()
                                        .toLowerCase()
                                        .contains(keyword)

                                ||

                                item.getOwnerName()
                                        .toLowerCase()
                                        .contains(keyword)

                                ||

                                item.getTitle()
                                        .toLowerCase()
                                        .contains(keyword)

                                ||

                                item.getListingType()
                                        .toLowerCase()
                                        .contains(keyword)

                                ||

                                item.getStatus()
                                        .toLowerCase()
                                        .contains(keyword)

                ) {

                    filteredItems.add(item);
                }
            }


            displayItems(
                    filteredItems
            );


        } catch (Exception e) {

            showDatabaseError(e);
        }
    }


    // =============================================================
    // DISPLAY ITEMS
    // =============================================================

    private void displayItems(
            List<AdminItem> items
    ) {

        tableModel.setRowCount(0);


        for (AdminItem item : items) {

            tableModel.addRow(
                    new Object[]{
                            item.getItemId(),
                            item.getUserId(),
                            item.getOwnerName(),
                            item.getTitle(),
                            item.getListingType(),
                            item.getSalePrice(),
                            item.getRentPrice(),
                            item.getRentalUnit(),
                            item.getDeposit(),
                            item.getStatus()
                    }
            );
        }
    }


    // =============================================================
    // DELETE SELECTED ITEM
    // =============================================================

    private void deleteSelectedItem() {

        int selectedRow =
                itemTable.getSelectedRow();


        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an item first.",
                    "No Item Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        int itemId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );


        String title =
                tableModel
                        .getValueAt(
                                selectedRow,
                                3
                        )
                        .toString();


        int choice =
                JOptionPane.showConfirmDialog(
                        this,

                        "Delete item '" + title + "'?\n\n"
                                + "Item ID: " + itemId + "\n\n"
                                + "Its item images will also be deleted.",

                        "Confirm Item Deletion",

                        JOptionPane.YES_NO_OPTION,

                        JOptionPane.WARNING_MESSAGE
                );


        if (choice != JOptionPane.YES_OPTION) {

            return;
        }


        try {

            adminDAO.deleteItem(itemId);


            JOptionPane.showMessageDialog(
                    this,
                    "Item deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );


            loadItems();


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