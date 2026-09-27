package com.sturent.db;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CampusMartDBTest extends JFrame {
    private final JTextField hostField;
    private final JTextField portField;
    private final JTextField databaseField;
    private final JTextField userField;
    private final JPasswordField passwordField;
    private final JTextArea output;
    private final JButton testButton;

    public CampusMartDBTest() {
        super("CampusMart - Aiven MySQL Connection Test");

        hostField = new JTextField("mysql-178f6340-sturent.h.aivencloud.com");
        portField = new JTextField("17980");
        databaseField = new JTextField("defaultdb");
        userField = new JTextField("avnadmin");
        passwordField = new JPasswordField();
        output = new JTextArea();
        testButton = new JButton("Test Connection");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(content);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        addField(form, gbc, 0, "Host", hostField);
        addField(form, gbc, 1, "Port", portField);
        addField(form, gbc, 2, "Database", databaseField);
        addField(form, gbc, 3, "Username", userField);
        addField(form, gbc, 4, "Password", passwordField);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        testButton.addActionListener(this::onTest);
        form.add(testButton, gbc);

        content.add(form, BorderLayout.NORTH);

        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        content.add(new JScrollPane(output), BorderLayout.CENTER);

        output.setText("Ready. Enter your Aiven password and click Test Connection.\nMySQL Connector/J is bundled.\n");
    }

    private static void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel(label + ":"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(comp, gbc);
    }

    private void onTest(ActionEvent e) {
        testButton.setEnabled(false);
        output.setText("Testing connection...\n");
        new Thread(this::runTest, "db-test-thread").start();
    }

    private void runTest() {
        String host = hostField.getText().trim();
        String port = portField.getText().trim();
        String db = databaseField.getText().trim();
        String user = userField.getText().trim();
        String pass = new String(passwordField.getPassword());

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?sslMode=REQUIRED&connectTimeout=10000&socketTimeout=10000";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DriverManager.setLoginTimeout(15);
            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT 1 AS test_value, DATABASE() AS db_name, VERSION() AS version")) {
                if (rs.next()) {
                    int val = rs.getInt("test_value");
                    String dbName = rs.getString("db_name");
                    String version = rs.getString("version");
                    Driver driver = DriverManager.getDriver(url);
                    String result = "SUCCESS\n\nConnected to Aiven MySQL successfully.\n\n"
                            + "SELECT 1: " + val + "\n"
                            + "Database: " + dbName + "\n"
                            + "MySQL version: " + version + "\n\n"
                            + "JDBC driver: " + driver.getClass().getName();
                    SwingUtilities.invokeLater(() -> output.setText(result));
                }
            }
        } catch (Exception ex) {
            String err = "FAILED\n\n" + ex.getClass().getName() + ": " + ex.getMessage()
                    + "\n\nCheck host, port, database, username, password, internet access, and Aiven SSL settings.";
            SwingUtilities.invokeLater(() -> output.setText(err));
        } finally {
            SwingUtilities.invokeLater(() -> testButton.setEnabled(true));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CampusMartDBTest().setVisible(true));
    }
}
