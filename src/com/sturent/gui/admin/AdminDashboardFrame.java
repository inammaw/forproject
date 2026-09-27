package com.sturent.gui.admin;

import javax.swing.*;
import java.awt.*;
import com.sturent.Session;
import com.sturent.gui.LoginFrame;

public class AdminDashboardFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    private final JButton dashboardButton;
    private final JButton usersButton;
    private final JButton itemsButton;
    private final JButton complaintsButton;
    private final JButton reviewsButton;

    private final JLabel pageTitle;
    private final JLabel pageSubtitle;

    private static final Color SIDEBAR_COLOR =
            new Color(32, 36, 43);

    private static final Color SIDEBAR_TEXT =
            new Color(220, 224, 230);

    private static final Color ACTIVE_COLOR =
            new Color(45, 110, 85);

    private static final Color HOVER_COLOR =
            new Color(55, 61, 70);

    private static final Color BACKGROUND_COLOR =
            new Color(245, 247, 249);

    public AdminDashboardFrame() {

        setTitle("CampusMart - Admin Dashboard");

        setSize(1200, 750);

        setMinimumSize(
                new Dimension(1000, 650)
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLayout(
                new BorderLayout()
        );


        // =====================================================
        // SIDEBAR
        // =====================================================

        JPanel sidebar =
                new JPanel(
                        new BorderLayout()
                );

        sidebar.setPreferredSize(
                new Dimension(220, 750)
        );

        sidebar.setBackground(
                SIDEBAR_COLOR
        );


        // ---------------- SIDEBAR HEADER ----------------

        JPanel sidebarHeader =
                new JPanel();

        sidebarHeader.setOpaque(false);

        sidebarHeader.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        25,
                        25,
                        25
                )
        );

        sidebarHeader.setLayout(
                new BoxLayout(
                        sidebarHeader,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel applicationName =
                new JLabel("CampusMart");

        applicationName.setForeground(
                Color.WHITE
        );

        applicationName.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        22
                )
        );


        JLabel adminLabel =
                new JLabel("ADMIN PANEL");

        adminLabel.setForeground(
                new Color(170, 178, 188)
        );

        adminLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );


        sidebarHeader.add(
                applicationName
        );

        sidebarHeader.add(
                Box.createVerticalStrut(5)
        );

        sidebarHeader.add(
                adminLabel
        );


        sidebar.add(
                sidebarHeader,
                BorderLayout.NORTH
        );


        // ---------------- NAVIGATION ----------------

        JPanel navigationPanel =
                new JPanel();

        navigationPanel.setOpaque(false);

        navigationPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );

        navigationPanel.setLayout(
                new BoxLayout(
                        navigationPanel,
                        BoxLayout.Y_AXIS
                )
        );


        dashboardButton = createNavigationButton("Dashboard");
        usersButton = createNavigationButton("Users");
        itemsButton = createNavigationButton("Items");
        complaintsButton = createNavigationButton("Complaints");
        reviewsButton = createNavigationButton("Reviews");


        navigationPanel.add(dashboardButton);
        navigationPanel.add(Box.createVerticalStrut(8));

        navigationPanel.add(usersButton);
        navigationPanel.add(Box.createVerticalStrut(8));

        navigationPanel.add(itemsButton);
        navigationPanel.add(Box.createVerticalStrut(8));

        navigationPanel.add(complaintsButton);
        navigationPanel.add(Box.createVerticalStrut(8));

        navigationPanel.add(reviewsButton);


        sidebar.add(
                navigationPanel,
                BorderLayout.CENTER
        );


        // ---------------- LOGOUT ----------------

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setOpaque(false);

        footer.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        25,
                        15
                )
        );


        JButton logoutButton =
                new JButton("Logout");

        logoutButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        logoutButton.setForeground(
                new Color(235, 175, 175)
        );

        logoutButton.setBackground(
                SIDEBAR_COLOR
        );

        logoutButton.setFocusPainted(
                false
        );

        logoutButton.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        90,
                                        70,
                                        70
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                11,
                                17,
                                11,
                                17
                        )
                )
        );


        JPanel footerButtons = new JPanel(new GridLayout(2, 1, 0, 8));
        footerButtons.setOpaque(false);

        JButton marketButton = new JButton("Student Marketplace");
        marketButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        marketButton.setForeground(new Color(175, 235, 200));
        marketButton.setBackground(SIDEBAR_COLOR);
        marketButton.setFocusPainted(false);
        marketButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 90, 75)),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
        ));
        marketButton.addActionListener(e -> new com.sturent.ui.marketplace.DiscoverItemsFrame(1).setVisible(true));

        footerButtons.add(marketButton);
        footerButtons.add(logoutButton);

        footer.add(
                footerButtons,
                BorderLayout.CENTER
        );

        sidebar.add(
                footer,
                BorderLayout.SOUTH
        );


        add(
                sidebar,
                BorderLayout.WEST
        );


        // =====================================================
        // MAIN AREA
        // =====================================================

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                BACKGROUND_COLOR
        );


        // ---------------- HEADER ----------------

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                Color.WHITE
        );

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                new Color(
                                        225,
                                        228,
                                        232
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                20,
                                30,
                                20,
                                30
                        )
                )
        );


        JPanel titleContainer =
                new JPanel();

        titleContainer.setOpaque(false);

        titleContainer.setLayout(
                new BoxLayout(
                        titleContainer,
                        BoxLayout.Y_AXIS
                )
        );


        pageTitle =
                new JLabel(
                        "Admin Dashboard"
                );

        pageTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        pageTitle.setForeground(
                new Color(
                        35,
                        38,
                        42
                )
        );


        pageSubtitle =
                new JLabel(
                        "Overview of CampusMart"
                );

        pageSubtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        pageSubtitle.setForeground(
                new Color(
                        110,
                        116,
                        125
                )
        );


        titleContainer.add(
                pageTitle
        );

        titleContainer.add(
                Box.createVerticalStrut(5)
        );

        titleContainer.add(
                pageSubtitle
        );


        header.add(
                titleContainer,
                BorderLayout.WEST
        );


        mainPanel.add(
                header,
                BorderLayout.NORTH
        );


        // =====================================================
        // CONTENT
        // =====================================================

        cardLayout =
                new CardLayout();

        contentPanel =
                new JPanel(
                        cardLayout
                );

        contentPanel.setBackground(
                BACKGROUND_COLOR
        );

        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );


        AdminDashboardPanel dashboardPanel =
                new AdminDashboardPanel();

        AdminUserPanel userPanel =
                new AdminUserPanel();

        AdminItemPanel itemPanel =
                new AdminItemPanel();

        AdminComplaintPanel complaintPanel =
                new AdminComplaintPanel();

        AdminReviewPanel reviewPanel =
                new AdminReviewPanel();


        contentPanel.add(
                dashboardPanel,
                "dashboard"
        );

        contentPanel.add(
                userPanel,
                "users"
        );

        contentPanel.add(
                itemPanel,
                "items"
        );

        contentPanel.add(
                complaintPanel,
                "complaints"
        );

        contentPanel.add(
                reviewPanel,
                "reviews"
        );


        mainPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );


        add(
                mainPanel,
                BorderLayout.CENTER
        );


        // =====================================================
        // ACTIONS
        // =====================================================

        dashboardButton.addActionListener(
                e -> showPage(
                        "dashboard",
                        dashboardButton,
                        "Admin Dashboard",
                        "Overview of CampusMart"
                )
        );


        usersButton.addActionListener(
                e -> showPage(
                        "users",
                        usersButton,
                        "User Management",
                        "View and manage registered users"
                )
        );


        itemsButton.addActionListener(
                e -> showPage(
                        "items",
                        itemsButton,
                        "Item Management",
                        "View and manage marketplace items"
                )
        );
        complaintsButton.addActionListener(e -> showPage(
                "complaints",
                complaintsButton,
                "Complaints",
                "Review and manage user complaints"
        ));

        reviewsButton.addActionListener(e -> showPage(
                "reviews",
                reviewsButton,
                "Reviews",
                "View reviews submitted by users"
        ));


        logoutButton.addActionListener(
                e -> handleLogout()
        );


        // Initial page

        showPage(
                "dashboard",
                dashboardButton,
                "Admin Dashboard",
                "Overview of CampusMart"
        );
    }


    // =========================================================
    // NAVIGATION BUTTON
    // =========================================================

    private JButton createNavigationButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(
                SIDEBAR_TEXT
        );

        button.setBackground(
                SIDEBAR_COLOR
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFocusPainted(
                false
        );

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        13,
                        18,
                        13,
                        18
                )
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e
                    ) {

                        if (
                                button.getBackground()
                                        != ACTIVE_COLOR
                        ) {

                            button.setBackground(
                                    HOVER_COLOR
                            );
                        }
                    }


                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e
                    ) {

                        if (
                                button.getBackground()
                                        != ACTIVE_COLOR
                        ) {

                            button.setBackground(
                                    SIDEBAR_COLOR
                            );
                        }
                    }
                }
        );


        return button;
    }


    // =========================================================
    // PAGE SWITCHING
    // =========================================================

    private void showPage(
            String page,
            JButton activeButton,
            String title,
            String subtitle
    ) {

        cardLayout.show(
                contentPanel,
                page
        );


        resetButton(dashboardButton);
        resetButton(usersButton);
        resetButton(itemsButton);
        resetButton(complaintsButton);
        resetButton(reviewsButton);


        activeButton.setBackground(
                ACTIVE_COLOR
        );

        activeButton.setForeground(
                Color.WHITE
        );


        pageTitle.setText(
                title
        );

        pageSubtitle.setText(
                subtitle
        );
    }


    // =========================================================
    // RESET BUTTON
    // =========================================================

    private void resetButton(
            JButton button
    ) {

        button.setBackground(
                SIDEBAR_COLOR
        );

        button.setForeground(
                SIDEBAR_TEXT
        );
    }


    // =========================================================
    // LOGOUT
    // =========================================================
    private void handleLogout() {

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        // Clear the currently logged-in user
        Session.setCurrentUser(null);

        // Return to the login page
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);

        // Close the admin dashboard
        dispose();
    }

    // =========================================================
    // TESTING ENTRY POINT
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    AdminDashboardFrame frame =
                            new AdminDashboardFrame();

                    frame.setVisible(
                            true
                    );
                }
        );
    }
}