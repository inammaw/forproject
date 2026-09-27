package com.sturent.gui;

import com.sturent.Session;
import com.sturent.dao.UserDAO;
import com.sturent.model.User;
import com.sturent.service.AuthService;
import com.sturent.gui.admin.AdminDashboardFrame;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private AuthService authService;

    public LoginFrame() {
        authService = new AuthService(new UserDAO());

        setTitle("StuRent - Login");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        add(createLeftPanel());
        add(createRightPanel());
    }

    private JPanel createLeftPanel() {
        CampusPanel panel = new CampusPanel();
        panel.setLayout(new BorderLayout());

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(45, 45, 20, 30));

        JLabel logo = createLabel(
                "StuRent",
                42,
                true,
                Color.WHITE
        );

        JLabel tagline = createLabel(
                "Your Campus. Your Marketplace.",
                19,
                false,
                Color.WHITE
        );

        top.add(logo);
        top.add(Box.createVerticalStrut(8));
        top.add(tagline);

        content.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(20, 45, 20, 30));

        JLabel heading = createLabel(
                "<html>A Smarter Campus<br>Together</html>",
                34,
                true,
                Color.WHITE
        );

        JLabel description = new JLabel(
                "<html>Find what you need. Rent what you want. "
                        + "Connect with students across your campus.</html>"
        );
        description.setFont(
                new Font("SansSerif", Font.PLAIN, 17)
        );
        description.setForeground(Color.WHITE);
        description.setMaximumSize(new Dimension(420, 60));

        JLabel features = createLabel(
                "FIND  •  RENT  •  CONNECT  •  SUSTAINABLE",
                14,
                true,
                Color.WHITE
        );

        center.add(heading);
        center.add(Box.createVerticalStrut(20));
        center.add(description);
        center.add(Box.createVerticalStrut(30));
        center.add(features);

        content.add(center, BorderLayout.CENTER);

        JLabel bottom = createLabel(
                "BUY  •  RENT  •  SELL  •  CONNECT",
                13,
                true,
                Color.WHITE
        );

        bottom.setBorder(
                new EmptyBorder(20, 45, 30, 20)
        );

        content.add(bottom, BorderLayout.SOUTH);

        panel.add(content);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 250, 249));

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(35, 45, 35, 45));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = createLabel(
                "Welcome Back!",
                32,
                true,
                new Color(35, 55, 45)
        );

        JLabel subtitle = createLabel(
                "Login to continue to StuRent",
                15,
                false,
                new Color(100, 110, 105)
        );

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(35));

        emailField = addTextField(card, "Email");
        passwordField = addPasswordField(card, "Password");

        JPanel options = new JPanel(
                new BorderLayout()
        );
        options.setOpaque(false);
        options.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 30)
        );

        JCheckBox showPassword =
                new JCheckBox("Show password");

        showPassword.setOpaque(false);
        showPassword.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('\u2022');
            }
        });

        JLabel forgotPassword =
                new JLabel("Forgot Password?");

        forgotPassword.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        forgotPassword.setForeground(
                new Color(42, 137, 83)
        );

        forgotPassword.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        forgotPassword.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e) {

                        JOptionPane.showMessageDialog(
                                LoginFrame.this,
                                "Password recovery will be added later.",
                                "Forgot Password",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
        );

        options.add(
                showPassword,
                BorderLayout.WEST
        );

        options.add(
                forgotPassword,
                BorderLayout.EAST
        );

        card.add(options);
        card.add(Box.createVerticalStrut(25));

        JButton loginButton =
                new JButton("LOGIN");

        loginButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        loginButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48)
        );

        loginButton.setPreferredSize(
                new Dimension(350, 48)
        );

        loginButton.setBackground(
                new Color(42, 137, 83)
        );

        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);

        loginButton.addActionListener(
                e -> performLogin()
        );

        card.add(loginButton);
        card.add(Box.createVerticalStrut(25));

        JPanel registerPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                3,
                                0
                        )
                );

        registerPanel.setOpaque(false);

        JLabel noAccount =
                new JLabel("Don't have an account?");

        noAccount.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        JLabel register =
                new JLabel(" Create New Account");

        register.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        register.setForeground(
                new Color(42, 137, 83)
        );

        register.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        register.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e) {

                        new RegisterFrame().setVisible(true);
                        dispose();
                    }
                }
        );

        registerPanel.add(noAccount);
        registerPanel.add(register);

        card.add(registerPanel);
        card.add(Box.createVerticalStrut(15));

        JPanel demoRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        demoRow.setOpaque(false);
        JButton previewBtn = new JButton("Explore Marketplace (Guest / Preview Hub)");
        previewBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        previewBtn.setForeground(new Color(42, 137, 83));
        previewBtn.setContentAreaFilled(false);
        previewBtn.setBorderPainted(false);
        previewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        previewBtn.addActionListener(e -> {
            new com.sturent.ui.CampusMartUIPreview().setVisible(true);
            dispose();
        });
        demoRow.add(previewBtn);
        card.add(demoRow);
        card.add(Box.createVerticalStrut(15));

        JLabel footer = createLabel(
                "StuRent • Campus Marketplace",
                11,
                false,
                new Color(140, 145, 142)
        );

        footer.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(footer);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.insets =
                new Insets(30, 45, 30, 45);

        panel.add(card, gbc);

        return panel;
    }

    private JTextField addTextField(
            JPanel panel,
            String labelText
    ) {
        JLabel label = createLabel(
                labelText,
                13,
                true,
                new Color(55, 65, 60)
        );

        JTextField field = createTextField();

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);
        panel.add(Box.createVerticalStrut(20));

        return field;
    }

    private JPasswordField addPasswordField(
            JPanel panel,
            String labelText
    ) {
        JLabel label = createLabel(
                labelText,
                13,
                true,
                new Color(55, 65, 60)
        );

        JPasswordField field =
                createPasswordField();

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);
        panel.add(Box.createVerticalStrut(10));

        return field;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();

        field.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );

        field.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );

        field.setPreferredSize(
                new Dimension(350, 45)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 220, 215)
                        ),
                        new EmptyBorder(
                                8, 12, 8, 12
                        )
                )
        );

        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field =
                new JPasswordField();

        field.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );

        field.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );

        field.setPreferredSize(
                new Dimension(350, 45)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 220, 215)
                        ),
                        new EmptyBorder(
                                8, 12, 8, 12
                        )
                )
        );

        return field;
    }

    private JLabel createLabel(
            String text,
            int size,
            boolean bold,
            Color color
    ) {
        JLabel label = new JLabel(text);

        label.setFont(
                new Font(
                        "SansSerif",
                        bold ? Font.BOLD : Font.PLAIN,
                        size
                )
        );

        label.setForeground(color);
        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    private void performLogin() {
        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (email.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both email and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            User user =
                    authService.login(
                            email,
                            password
                    );

            if (user == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            Session.setCurrentUser(user);

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                AdminDashboardFrame adminDashboard =
                        new AdminDashboardFrame();
                adminDashboard.setVisible(true);
                dispose();
            } else {
                int numericId = user.getNumericUserId();
                com.sturent.ui.marketplace.DiscoverItemsFrame marketplace =
                        new com.sturent.ui.marketplace.DiscoverItemsFrame(numericId);
                marketplace.setVisible(true);
                dispose();
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred during login.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static class CampusPanel extends JPanel {
        private BufferedImage image;

        public CampusPanel() {
            try {
                InputStream stream =
                        getClass().getResourceAsStream(
                                "/campus.jpg"
                        );

                if (stream != null) {
                    image = ImageIO.read(stream);
                    stream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {
            super.paintComponent(g);

            Graphics2D graphics =
                    (Graphics2D) g.create();

            if (image != null) {
                int width = getWidth();
                int height = getHeight();

                double imageRatio =
                        (double) image.getWidth()
                                / image.getHeight();

                double panelRatio =
                        (double) width / height;

                int drawWidth;
                int drawHeight;

                if (imageRatio > panelRatio) {
                    drawHeight = height;
                    drawWidth =
                            (int) (height * imageRatio);
                } else {
                    drawWidth = width;
                    drawHeight =
                            (int) (width / imageRatio);
                }

                int x =
                        (width - drawWidth) / 2;

                int y =
                        (height - drawHeight) / 2;

                graphics.drawImage(
                        image,
                        x,
                        y,
                        drawWidth,
                        drawHeight,
                        null
                );

                graphics.setColor(
                        new Color(0, 0, 0, 80)
                );

                graphics.fillRect(
                        0,
                        0,
                        width,
                        height
                );

            } else {
                graphics.setColor(
                        new Color(45, 100, 65)
                );

                graphics.fillRect(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );
            }

            graphics.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new LoginFrame().setVisible(true)
        );
    }
}
