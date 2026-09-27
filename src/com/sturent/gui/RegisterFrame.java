package com.sturent.gui;

import com.sturent.dao.UserDAO;
import com.sturent.model.User;
import com.sturent.service.AuthService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class RegisterFrame extends JFrame {

    private JTextField useridField, nameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private AuthService authService;

    public RegisterFrame() {
        authService = new AuthService(new UserDAO());

        setTitle("StuRent - Create Account");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        JLabel logo = new JLabel("StuRent");
        logo.setFont(new Font("SansSerif", Font.BOLD, 42));
        logo.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("Your Campus. Your Marketplace.");
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 19));
        tagline.setForeground(Color.WHITE);

        top.add(logo);
        top.add(Box.createVerticalStrut(8));
        top.add(tagline);

        content.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(20, 45, 20, 30));

        JLabel heading = new JLabel(
                "<html>Join Your Campus<br>Community</html>"
        );
        heading.setFont(new Font("SansSerif", Font.BOLD, 34));
        heading.setForeground(Color.WHITE);

        JLabel description = new JLabel(
                "<html>Create your StuRent account and start buying, "
                        + "renting, selling and connecting with students "
                        + "on campus.</html>"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 17));
        description.setForeground(Color.WHITE);
        description.setMaximumSize(new Dimension(420, 60));

        JLabel features = new JLabel(
                "FIND  •  RENT  •  SELL  •  CONNECT"
        );
        features.setFont(new Font("SansSerif", Font.BOLD, 14));
        features.setForeground(Color.WHITE);

        center.add(heading);
        center.add(Box.createVerticalStrut(20));
        center.add(description);
        center.add(Box.createVerticalStrut(30));
        center.add(features);

        content.add(center, BorderLayout.CENTER);

        JLabel bottom = new JLabel("A Smarter Campus Together");
        bottom.setFont(new Font("SansSerif", Font.BOLD, 13));
        bottom.setForeground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(20, 45, 30, 20));

        content.add(bottom, BorderLayout.SOUTH);

        panel.add(content);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 250, 249));

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(30, 45, 30, 45));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = createLabel("Create Your Account", 30, true);
        JLabel subtitle = createLabel(
                "Join StuRent and connect with your campus",
                14,
                false
        );

        card.add(title);
        card.add(Box.createVerticalStrut(7));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(22));

        useridField = addTextField(card, "College ID");
        nameField = addTextField(card, "Name");
        emailField = addTextField(card, "Email");
        phoneField = addTextField(card, "Phone (10 digits)");

        passwordField = addPasswordField(
                card,
                "Password (minimum 6 characters)"
        );

        confirmPasswordField =
                addPasswordField(card, "Confirm Password");

        card.add(Box.createVerticalStrut(18));

        JButton registerButton =
                new JButton("CREATE ACCOUNT");

        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        registerButton.setPreferredSize(new Dimension(350, 46));
        registerButton.setBackground(new Color(42, 137, 83));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);

        registerButton.addActionListener(e -> performRegistration());

        card.add(registerButton);
        card.add(Box.createVerticalStrut(18));

        JPanel loginPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 3, 0)
        );
        loginPanel.setOpaque(false);

        JLabel already = new JLabel("Already have an account?");
        JLabel login = new JLabel(" Login");

        already.setFont(new Font("SansSerif", Font.PLAIN, 12));
        login.setFont(new Font("SansSerif", Font.BOLD, 12));
        login.setForeground(new Color(42, 137, 83));
        login.setCursor(new Cursor(Cursor.HAND_CURSOR));

        login.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        loginPanel.add(already);
        loginPanel.add(login);

        card.add(loginPanel);
        card.add(Box.createVerticalStrut(15));

        JLabel footer =
                createLabel("StuRent • Campus Marketplace", 11, false);

        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setForeground(new Color(140, 145, 142));

        card.add(footer);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(25, 45, 25, 45);

        panel.add(card, gbc);

        return panel;
    }

    private JTextField addTextField(JPanel panel, String labelText) {
        JLabel label = createLabel(labelText, 12, true);
        JTextField field = createTextField();

        panel.add(label);
        panel.add(Box.createVerticalStrut(6));
        panel.add(field);
        panel.add(Box.createVerticalStrut(12));

        return field;
    }

    private JPasswordField addPasswordField(
            JPanel panel,
            String labelText
    ) {
        JLabel label = createLabel(labelText, 12, true);
        JPasswordField field = createPasswordField();

        panel.add(label);
        panel.add(Box.createVerticalStrut(6));
        panel.add(field);
        panel.add(Box.createVerticalStrut(12));

        return field;
    }

    private JLabel createLabel(
            String text,
            int size,
            boolean bold
    ) {
        JLabel label = new JLabel(text);
        label.setFont(
                new Font(
                        "SansSerif",
                        bold ? Font.BOLD : Font.PLAIN,
                        size
                )
        );
        label.setForeground(new Color(55, 65, 60));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setPreferredSize(new Dimension(350, 38));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 220, 215)
                        ),
                        new EmptyBorder(7, 10, 7, 10)
                )
        );

        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();

        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setPreferredSize(new Dimension(350, 38));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 220, 215)
                        ),
                        new EmptyBorder(7, 10, 7, 10)
                )
        );

        return field;
    }

    private void performRegistration() {
        String userid = useridField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password =
                new String(passwordField.getPassword());
        String confirmPassword =
                new String(confirmPasswordField.getPassword());

        if (userid.isBlank()
                || name.isBlank()
                || email.isBlank()
                || phone.isBlank()
                || password.isBlank()
                || confirmPassword.isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must be at least 6 characters long.",
                    "Invalid Password",
                    JOptionPane.WARNING_MESSAGE
            );
            passwordField.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Password Error",
                    JOptionPane.WARNING_MESSAGE
            );
            confirmPasswordField.requestFocus();
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Phone number must contain exactly 10 digits.",
                    "Invalid Phone Number",
                    JOptionPane.WARNING_MESSAGE
            );
            phoneField.requestFocus();
            return;
        }

        try {
            User user =
                    authService.prepareUserForRegistration(
                            userid,
                            name,
                            email,
                            password,
                            phone,
                            "STUDENT"
                    );

            if (user == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed. Please check your information.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (authService.registerUser(user)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration successful!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new LoginFrame().setVisible(true);
                dispose();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed. The College ID or email may already be registered.",
                        "Registration Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed. Please try again.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static class CampusPanel extends JPanel {
        private BufferedImage image;

        public CampusPanel() {
            try {
                InputStream stream =
                        getClass().getResourceAsStream("/campus.jpg");

                if (stream != null) {
                    image = ImageIO.read(stream);
                    stream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D graphics = (Graphics2D) g.create();

            if (image != null) {
                int width = getWidth();
                int height = getHeight();

                double imageRatio =
                        (double) image.getWidth() / image.getHeight();

                double panelRatio =
                        (double) width / height;

                int drawWidth;
                int drawHeight;

                if (imageRatio > panelRatio) {
                    drawHeight = height;
                    drawWidth = (int) (height * imageRatio);
                } else {
                    drawWidth = width;
                    drawHeight = (int) (width / imageRatio);
                }

                int x = (width - drawWidth) / 2;
                int y = (height - drawHeight) / 2;

                graphics.drawImage(
                        image,
                        x,
                        y,
                        drawWidth,
                        drawHeight,
                        null
                );

                graphics.setColor(new Color(0, 0, 0, 80));
                graphics.fillRect(0, 0, width, height);

            } else {
                graphics.setColor(new Color(45, 100, 65));
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
        SwingUtilities.invokeLater(() ->
                new RegisterFrame().setVisible(true)
        );
    }
}