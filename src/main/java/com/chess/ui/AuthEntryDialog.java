package com.chess.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import com.chess.service.AuthService;

public class AuthEntryDialog extends JDialog {

    private boolean loginSuccessful = false;
    private AuthService authService;
    
    private static final Color PRIMARY_COLOR = new Color(41, 98, 255);
    private static final Color SECONDARY_COLOR = new Color(76, 175, 80);
    private static final Color LIGHT_BG = new Color(250, 251, 255);
    private static final Color TEXT_COLOR = new Color(60, 60, 80);
    private static final Color LIGHT_TEXT = new Color(255, 255, 255);

    public AuthEntryDialog(JFrame parent, AuthService authService) {
        super(parent, "Welcome to Chess", true);
        this.authService = authService;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        getContentPane().setBackground(LIGHT_BG);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 
                        getWidth(), getHeight(), new Color(30, 70, 230));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(50, 30, 50, 30));
        headerPanel.setPreferredSize(new Dimension(500, 180));

        JLabel titleLabel = new JLabel("♟ Online Chess");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(LIGHT_TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Enjoy the timeless game of strategy and mind");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(230, 230, 245));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(LIGHT_BG);

        // Login button
        JButton loginBtn = createStyledButton("Login", PRIMARY_COLOR, LIGHT_TEXT);
        loginBtn.addActionListener(e -> {
            LoginDialog loginDialog = new LoginDialog((JFrame) getParent(), authService);
            loginDialog.setVisible(true);
            if (loginDialog.isLoginSuccessful()) {
                loginSuccessful = true;
                dispose();
            }
        });
        buttonPanel.add(loginBtn);
        buttonPanel.add(Box.createVerticalStrut(15));

        // Register button
        JButton registerBtn = createStyledButton("Create Account", SECONDARY_COLOR, LIGHT_TEXT);
        registerBtn.addActionListener(e -> {
            RegisterDialog registerDialog = new RegisterDialog((JFrame) getParent(), authService);
            registerDialog.setVisible(true);
        });
        buttonPanel.add(registerBtn);
        buttonPanel.add(Box.createVerticalStrut(15));

        // Forgot password button
        JButton forgotBtn = createStyledButton("Forgot Password?", new Color(255, 152, 0), LIGHT_TEXT);
        forgotBtn.addActionListener(e -> {
            ForgotPasswordDialog forgotDialog = new ForgotPasswordDialog((JFrame) getParent(), authService);
            forgotDialog.setVisible(true);
        });
        buttonPanel.add(forgotBtn);
        buttonPanel.add(Box.createVerticalGlue());

        // Footer text
        JLabel footerLabel = new JLabel("© 2024 Online Chess. All rights reserved.");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(150, 150, 160));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(LIGHT_BG);
        footerPanel.add(footerLabel, BorderLayout.SOUTH);

        contentPanel.add(buttonPanel, BorderLayout.CENTER);
        contentPanel.add(footerPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isArmed()) {
                    g2d.setColor(new Color(
                            Math.max(bgColor.getRed() - 25, 0),
                            Math.max(bgColor.getGreen() - 25, 0),
                            Math.max(bgColor.getBlue() - 25, 0)));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(
                            Math.min(bgColor.getRed() + 20, 255),
                            Math.min(bgColor.getGreen() + 20, 255),
                            Math.min(bgColor.getBlue() + 20, 255)));
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                if (!isEnabled()) {
                    g2d.setColor(new Color(150, 150, 150, 100));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(0, 50));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
// Last modified during: feat: Add authentication UI components (LoginDialog, RegisterDialog, ForgotPasswordDialog) [minor]
