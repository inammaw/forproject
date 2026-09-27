package com.sturent.gui.admin;

import com.sturent.dao.AdminDAO;
import com.sturent.model.admin.AdminReview;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AdminReviewPanel extends JPanel {

    private final AdminDAO adminDAO;

    private final JTextField searchField;
    private final JTable reviewTable;
    private final DefaultTableModel tableModel;

    private static final Color BACKGROUND_COLOR =
            new Color(245, 247, 249);

    private static final Color CARD_COLOR =
            Color.WHITE;

    private static final Color TEXT_COLOR =
            new Color(35, 38, 42);

    private static final Color BORDER_COLOR =
            new Color(225, 228, 232);

    private static final Color TABLE_HEADER_COLOR =
            new Color(235, 238, 241);


    public AdminReviewPanel() {

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
                                "Review ID",
                                "Reviewer ID",
                                "Seller ID",
                                "Item ID",
                                "Rating",
                                "Comment",
                                "Created At"
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


        reviewTable =
                new JTable(tableModel);

        reviewTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        reviewTable.setRowHeight(34);

        reviewTable.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        reviewTable.setForeground(
                TEXT_COLOR
        );

        reviewTable.setBackground(
                Color.WHITE
        );

        reviewTable.setGridColor(
                BORDER_COLOR
        );

        reviewTable.setShowVerticalLines(false);

        reviewTable.setSelectionBackground(
                new Color(220, 235, 228)
        );

        reviewTable.setSelectionForeground(
                TEXT_COLOR
        );


        // =========================================================
        // TABLE HEADER
        // =========================================================

        JTableHeader header =
                reviewTable.getTableHeader();

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
        // CENTER ALIGNMENT
        // =========================================================

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        for (int i = 0; i <= 4; i++) {

            reviewTable
                    .getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(
                            centerRenderer
                    );
        }


        // =========================================================
        // COLUMN WIDTHS
        // =========================================================

        reviewTable
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(80);

        reviewTable
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(100);

        reviewTable
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(100);

        reviewTable
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(80);

        reviewTable
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(70);

        reviewTable
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(300);

        reviewTable
                .getColumnModel()
                .getColumn(6)
                .setPreferredWidth(160);


        JScrollPane scrollPane =
                new JScrollPane(
                        reviewTable
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
                e -> searchReviews()
        );

        refreshButton.addActionListener(
                e -> {
                    searchField.setText("");
                    loadReviews();
                }
        );

        searchField.addActionListener(
                e -> searchReviews()
        );


        // =========================================================
        // LOAD
        // =========================================================

        loadReviews();
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
    // LOAD REVIEWS
    // =============================================================

    private void loadReviews() {

        try {

            List<AdminReview> reviews =
                    adminDAO.getAllReviews();

            displayReviews(
                    reviews
            );

        } catch (Exception e) {

            showDatabaseError(e);
        }
    }


    // =============================================================
    // SEARCH REVIEWS
    // =============================================================

    private void searchReviews() {

        String keyword =
                searchField
                        .getText()
                        .trim();


        try {

            if (keyword.isEmpty()) {

                loadReviews();

                return;
            }


            List<AdminReview> reviews =
                    adminDAO.searchReviews(
                            keyword
                    );


            displayReviews(
                    reviews
            );


        } catch (Exception e) {

            showDatabaseError(e);
        }
    }


    // =============================================================
    // DISPLAY REVIEWS
    // =============================================================

    private void displayReviews(
            List<AdminReview> reviews
    ) {

        tableModel.setRowCount(0);


        for (AdminReview review :
                reviews) {

            tableModel.addRow(
                    new Object[]{
                            review.getReviewId(),
                            review.getReviewerId(),
                            review.getSellerId(),
                            review.getItemId(),
                            review.getRating(),
                            review.getComment(),
                            review.getCreatedAt()
                    }
            );
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