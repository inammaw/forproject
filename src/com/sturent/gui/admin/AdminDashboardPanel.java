package com.sturent.gui.admin;

import com.sturent.dao.AdminDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminDashboardPanel extends JPanel {

    private final AdminDAO adminDAO;

    private final JLabel userCountLabel;
    private final JLabel itemCountLabel;
    private final JLabel availableItemCountLabel;

    private final JButton refreshButton;

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


    // ---------------- CONSTRUCTOR ----------------

    public AdminDashboardPanel() {

        adminDAO = new AdminDAO();

        setLayout(new BorderLayout(20, 20));
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
                new JPanel(new BorderLayout());

        topPanel.setOpaque(false);


        // ---------- TITLE ----------

        JPanel titlePanel = new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel titleLabel =
                new JLabel("Dashboard");

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        titleLabel.setForeground(TEXT_COLOR);


        JLabel subtitleLabel =
                new JLabel(
                        "Overview of your CampusMart platform"
                );

        subtitleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        subtitleLabel.setForeground(
                SECONDARY_TEXT
        );


        titlePanel.add(titleLabel);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitleLabel);


        topPanel.add(
                titlePanel,
                BorderLayout.WEST
        );


        // ---------- REFRESH BUTTON ----------

        refreshButton =
                new JButton("Refresh");

        refreshButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        refreshButton.setForeground(
                Color.WHITE
        );

        refreshButton.setBackground(
                ACCENT_COLOR
        );

        refreshButton.setFocusPainted(false);

        refreshButton.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        refreshButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        refreshButton.addActionListener(
                e -> loadData()
        );


        topPanel.add(
                refreshButton,
                BorderLayout.EAST
        );


        add(
                topPanel,
                BorderLayout.NORTH
        );


        // =========================================================
        // STATISTICS SECTION
        // =========================================================

        JPanel statisticsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                20,
                                20
                        )
                );

        statisticsPanel.setOpaque(false);


        // ---------- VALUE LABELS ----------

        userCountLabel =
                createValueLabel();

        itemCountLabel =
                createValueLabel();

        availableItemCountLabel =
                createValueLabel();


        // ---------- CARDS ----------

        statisticsPanel.add(
                createStatisticPanel(
                        "Total Users",
                        "Registered users",
                        userCountLabel
                )
        );


        statisticsPanel.add(
                createStatisticPanel(
                        "Total Items",
                        "Marketplace listings",
                        itemCountLabel
                )
        );


        statisticsPanel.add(
                createStatisticPanel(
                        "Available Items",
                        "Currently available",
                        availableItemCountLabel
                )
        );


        add(
                statisticsPanel,
                BorderLayout.CENTER
        );


        // =========================================================
        // LOAD DATABASE DATA
        // =========================================================

        loadData();
    }


    // =============================================================
    // VALUE LABEL
    // =============================================================

    private JLabel createValueLabel() {

        JLabel label =
                new JLabel(
                        "Loading...",
                        SwingConstants.CENTER
                );

        label.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        34
                )
        );

        label.setForeground(
                ACCENT_COLOR
        );

        return label;
    }


    // =============================================================
    // STATISTIC CARD
    // =============================================================

    private JPanel createStatisticPanel(
            String title,
            String description,
            JLabel valueLabel
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        card.setBackground(
                CARD_COLOR
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        BorderFactory.createEmptyBorder(
                                22,
                                20,
                                22,
                                20
                        )
                )
        );


        // ---------- CARD HEADER ----------

        JPanel cardHeader =
                new JPanel();

        cardHeader.setOpaque(false);

        cardHeader.setLayout(
                new BoxLayout(
                        cardHeader,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        titleLabel.setForeground(
                TEXT_COLOR
        );


        JLabel descriptionLabel =
                new JLabel(
                        description,
                        SwingConstants.CENTER
                );

        descriptionLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        descriptionLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        descriptionLabel.setForeground(
                SECONDARY_TEXT
        );


        cardHeader.add(titleLabel);

        cardHeader.add(
                Box.createVerticalStrut(4)
        );

        cardHeader.add(descriptionLabel);


        card.add(
                cardHeader,
                BorderLayout.NORTH
        );


        // ---------- VALUE ----------

        card.add(
                valueLabel,
                BorderLayout.CENTER
        );


        return card;
    }


    // =============================================================
    // LOAD DATA
    // =============================================================

    private void loadData() {

        refreshButton.setEnabled(false);

        try {

            int users =
                    adminDAO.getUserCount();

            int items =
                    adminDAO.getItemCount();

            int availableItems =
                    adminDAO.getAvailableItemCount();


            userCountLabel.setText(
                    String.valueOf(users)
            );

            itemCountLabel.setText(
                    String.valueOf(items)
            );

            availableItemCountLabel.setText(
                    String.valueOf(availableItems)
            );


        } catch (Exception e) {

            userCountLabel.setText("—");

            itemCountLabel.setText("—");

            availableItemCountLabel.setText("—");


            JOptionPane.showMessageDialog(
                    this,
                    "Could not load dashboard data.\n\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );


        } finally {

            refreshButton.setEnabled(true);
        }
    }
}