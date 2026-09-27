package com.sturent.gui.admin;

import com.sturent.dao.AdminDAO;
import com.sturent.model.admin.AdminComplaint;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AdminComplaintPanel extends JPanel {

    private final AdminDAO adminDAO;

    private final JTextField searchField;
    private final JTable complaintTable;
    private final DefaultTableModel tableModel;

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

    private static final Color TABLE_HEADER_COLOR =
            new Color(235, 238, 241);


    public AdminComplaintPanel() {

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
        // SEARCH / ACTION BAR
        // =========================================================

        JPanel actionPanel =
                new JPanel(new BorderLayout(10, 10));

        actionPanel.setBackground(CARD_COLOR);

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
                        300,
                        36
                )
        );


        JButton searchButton =
                createButton("Search");

        JButton refreshButton =
                createButton("Refresh");


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
                BorderLayout.EAST
        );


        add(
                actionPanel,
                BorderLayout.NORTH
        );


        // =========================================================
        // TABLE
        // =========================================================

        tableModel =
                new DefaultTableModel(
                        new String[]{
                                "Complaint ID",
                                "User ID",
                                "Item ID",
                                "Type",
                                "Description",
                                "Status",
                                "Admin Response",
                                "Created At",
                                "Resolved At"
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


        complaintTable =
                new JTable(tableModel);

        complaintTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        complaintTable.setRowHeight(34);

        complaintTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        complaintTable.setForeground(
                TEXT_COLOR
        );

        complaintTable.setBackground(
                Color.WHITE
        );

        complaintTable.setGridColor(
                BORDER_COLOR
        );

        complaintTable.setShowVerticalLines(false);

        complaintTable.setSelectionBackground(
                new Color(220, 235, 228)
        );

        complaintTable.setSelectionForeground(
                TEXT_COLOR
        );


        // =========================================================
        // TABLE HEADER
        // =========================================================

        JTableHeader header =
                complaintTable.getTableHeader();

        header.setBackground(
                TABLE_HEADER_COLOR
        );

        header.setForeground(
                TEXT_COLOR
        );

        header.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        header.setPreferredSize(
                new Dimension(
                        header.getWidth(),
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


        complaintTable
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(centerRenderer);

        complaintTable
                .getColumnModel()
                .getColumn(1)
                .setCellRenderer(centerRenderer);

        complaintTable
                .getColumnModel()
                .getColumn(2)
                .setCellRenderer(centerRenderer);

        complaintTable
                .getColumnModel()
                .getColumn(5)
                .setCellRenderer(centerRenderer);


        // =========================================================
        // COLUMN WIDTHS
        // =========================================================

        complaintTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(90);

        complaintTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(90);

        complaintTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(80);

        complaintTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(170);

        complaintTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(260);

        complaintTable
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(100);

        complaintTable
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(220);

        complaintTable
                .getColumnModel()
                .getColumn(7)
                .setPreferredWidth(150);

        complaintTable
                .getColumnModel()
                .getColumn(8)
                .setPreferredWidth(150);


        JScrollPane scrollPane =
                new JScrollPane(
                        complaintTable
                );

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
                e -> searchComplaints()
        );

        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadComplaints();
                }
        );

        searchField.addActionListener(
                e -> searchComplaints()
        );


        // Double-click a complaint to manage it
        complaintTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (e.getClickCount() == 2) {
                            editSelectedComplaint();
                        }
                    }
                }
        );


        // =========================================================
        // LOAD
        // =========================================================

        loadComplaints();
    }


    // =============================================================
    // BUTTON
    // =============================================================

    private JButton createButton(
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
    // LOAD COMPLAINTS
    // =============================================================

    private void loadComplaints() {

        try {

            List<AdminComplaint> complaints =
                    adminDAO.getAllComplaints();

            displayComplaints(
                    complaints
            );

        } catch (Exception e) {

            showDatabaseError(e);
        }
    }


    // =============================================================
    // SEARCH
    // =============================================================

    private void searchComplaints() {

        String keyword =
                searchField
                        .getText()
                        .trim();


        try {

            if (keyword.isEmpty()) {

                loadComplaints();

                return;
            }


            List<AdminComplaint> complaints =
                    adminDAO.searchComplaints(
                            keyword
                    );


            displayComplaints(
                    complaints
            );


        } catch (Exception e) {

            showDatabaseError(e);
        }
    }


    // =============================================================
    // DISPLAY
    // =============================================================

    private void displayComplaints(
            List<AdminComplaint> complaints
    ) {

        tableModel.setRowCount(0);


        for (AdminComplaint complaint :
                complaints) {

            tableModel.addRow(
                    new Object[]{
                            complaint.getComplaintId(),
                            complaint.getUserId(),
                            complaint.getItemId(),
                            complaint.getComplaintType(),
                            complaint.getDescription(),
                            complaint.getStatus(),
                            complaint.getAdminResponse(),
                            complaint.getCreatedAt(),
                            complaint.getResolvedAt()
                    }
            );
        }
    }


    // =============================================================
    // EDIT COMPLAINT
    // =============================================================

    private void editSelectedComplaint() {

        int selectedRow =
                complaintTable.getSelectedRow();


        if (selectedRow == -1) {

            return;
        }


        int complaintId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );


        String currentStatus =
                tableModel
                        .getValueAt(
                                selectedRow,
                                5
                        )
                        .toString();


        Object[] statusOptions = {
                "PENDING",
                "REVIEWING",
                "RESOLVED",
                "REJECTED"
        };


        JComboBox<String> statusBox =
                new JComboBox<>(
                        (String[]) statusOptions
                );

        statusBox.setSelectedItem(
                currentStatus
        );


        JTextArea responseArea =
                new JTextArea(6, 35);

        responseArea.setLineWrap(true);

        responseArea.setWrapStyleWord(true);


        Object currentResponse =
                tableModel.getValueAt(
                        selectedRow,
                        6
                );

        if (currentResponse != null) {

            responseArea.setText(
                    currentResponse.toString()
            );
        }


        JScrollPane responseScrollPane =
                new JScrollPane(
                        responseArea
                );


        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.setBorder(
                new EmptyBorder(
                        5,
                        5,
                        5,
                        5
                )
        );


        JPanel statusPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        statusPanel.add(
                new JLabel("Status:")
        );

        statusPanel.add(statusBox);


        panel.add(
                statusPanel,
                BorderLayout.NORTH
        );

        panel.add(
                responseScrollPane,
                BorderLayout.CENTER
        );


        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Manage Complaint #" +
                                complaintId,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );


        if (result != JOptionPane.OK_OPTION) {

            return;
        }


        String newStatus =
                statusBox
                        .getSelectedItem()
                        .toString();


        String adminResponse =
                responseArea
                        .getText()
                        .trim();


        try {

            adminDAO.updateComplaint(
                    complaintId,
                    newStatus,
                    adminResponse
            );


            JOptionPane.showMessageDialog(
                    this,
                    "Complaint updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );


            loadComplaints();


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