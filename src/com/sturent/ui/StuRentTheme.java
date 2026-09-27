package com.sturent.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * StuRent Global Design System & Theme
 * Matches the official StuRent campus marketplace aesthetic:
 * - Brand: "StuRent - Your Campus. Your Marketplace."
 * - Palette: Forest Green (#1b4332), Slate (#f1f5f9), Crisp White (#ffffff)
 * - Modern rounded cards, clean typography, badge indicators, and styled buttons.
 */
public class StuRentTheme {

    // Colors
    public static final Color PRIMARY_GREEN = new Color(27, 67, 50);       // #1b4332 Forest Green
    public static final Color PRIMARY_GREEN_HOVER = new Color(20, 54, 38); // #143626
    public static final Color PRIMARY_GREEN_LIGHT = new Color(45, 106, 79); // #2d6a4f
    
    public static final Color BG_CANVAS = new Color(241, 245, 249);        // #f1f5f9 Soft Slate
    public static final Color CARD_BG = Color.WHITE;
    public static final Color CARD_BORDER = new Color(226, 232, 240);      // #e2e8f0
    public static final Color CARD_BORDER_HOVER = new Color(203, 213, 225);

    public static final Color TEXT_DARK = new Color(15, 23, 42);          // #0f172a
    public static final Color TEXT_MUTED = new Color(100, 116, 139);      // #64748b
    public static final Color TEXT_LIGHT = new Color(148, 163, 184);

    public static final Color BADGE_GREEN_BG = new Color(220, 252, 231);  // #dcfce7
    public static final Color BADGE_GREEN_TEXT = new Color(22, 101, 52);  // #166534
    
    public static final Color BADGE_BLUE_BG = new Color(224, 242, 254);   // #e0f2fe
    public static final Color BADGE_BLUE_TEXT = new Color(3, 105, 161);   // #0369a1

    public static final Color BADGE_AMBER_BG = new Color(254, 243, 199);  // #fef3c7
    public static final Color BADGE_AMBER_TEXT = new Color(180, 83, 9);   // #b45309

    public static final Color DANGER_BG = new Color(254, 242, 242);        // #fef2f2
    public static final Color DANGER_BORDER = new Color(254, 202, 202);    // #fecaca
    public static final Color DANGER_TEXT = new Color(185, 28, 28);        // #b91c1c

    // Fonts
    public static final Font FONT_BRAND = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_TAGLINE = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_CARD_TITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_PRICE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD, 11);

    /**
     * Creates the top branding bar:
     * "StuRent"
     * "Your Campus. Your Marketplace."
     * Plus optional right-hand side action component (e.g. "+ Add Item" or "+ Back")
     */
    public static JPanel createHeader(JComponent rightAction) {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CARD_BORDER),
                new EmptyBorder(16, 24, 16, 24)
        ));

        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setOpaque(false);

        JLabel brandTitle = new JLabel("StuRent");
        brandTitle.setFont(FONT_BRAND);
        brandTitle.setForeground(TEXT_DARK);

        JLabel brandTagline = new JLabel("Your Campus. Your Marketplace.");
        brandTagline.setFont(FONT_TAGLINE);
        brandTagline.setForeground(TEXT_MUTED);

        brandPanel.add(brandTitle);
        brandPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        brandPanel.add(brandTagline);

        header.add(brandPanel, BorderLayout.WEST);
        if (rightAction != null) {
            header.add(rightAction, BorderLayout.EAST);
        }
        return header;
    }

    /**
     * Solid Forest Green Primary Button
     */
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? PRIMARY_GREEN_HOVER.darker() :
                        getModel().isRollover() ? PRIMARY_GREEN_LIGHT : PRIMARY_GREEN);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        return btn;
    }

    /**
     * Clean Outlined Secondary Button
     */
    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(248, 250, 252) : Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(CARD_BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(TEXT_DARK);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        return btn;
    }

    /**
     * Danger / Delete Button (Soft Red)
     */
    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(254, 226, 226) : DANGER_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(DANGER_BORDER);
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(DANGER_TEXT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        return btn;
    }

    /**
     * Modern Badge / Pill Tag (e.g. "SALE", "AVAILABLE", "DELIVERED")
     */
    public static JLabel createBadge(String text, Color bg, Color fg) {
        JLabel badge = new JLabel(" " + text + " ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(FONT_BADGE);
        badge.setForeground(fg);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        return badge;
    }

    /**
     * Status indicator with green dot + "AVAILABLE"
     */
    public static JPanel createStatusIndicator(String text, Color dotColor) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("\u2022");
        dot.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dot.setForeground(dotColor);
        JLabel label = new JLabel(text);
        label.setFont(FONT_BADGE);
        label.setForeground(dotColor);
        p.add(dot);
        p.add(label);
        return p;
    }

    /**
     * Card container with soft border and crisp background
     */
    public static JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(CARD_BORDER);
                g2.setStroke(new BasicStroke(1.0f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        return card;
    }

    /**
     * Text input field styled to match the search input in the screenshot
     */
    public static JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_REGULAR);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
}
