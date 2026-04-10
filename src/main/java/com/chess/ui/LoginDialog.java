package com.chess.ui;

import com.chess.model.Player;
import com.chess.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Optional;

public class LoginDialog extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private boolean loginSuccessful = false;

    private final AuthService authService;
    
    private static final Color PRIMARY_COLOR = new Color(41, 98, 255);
    private static final Color DARK_BG = new Color(20, 20, 30);
    private static final Color LIGHT_BG = new Color(250, 251, 255);
    private static final Color TEXT_COLOR = new Color(60, 60, 80);
    private static final Color LIGHT_TEXT = new Color(255, 255, 255);

    public LoginDialog(JFrame parent, AuthService authService) {
        super(parent, "Login", true);
        this.authService = authService;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        getContentPane().setBackground(LIGHT_BG);

        // Main container with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Header panel with gradient effect
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
        headerPanel.setBorder(new EmptyBorder(40, 20, 40, 20));
        headerPanel.setPreferredSize(new Dimension(450, 150));

        JLabel titleLabel = new JLabel("♟ Chess Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(LIGHT_TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Welcome back to your game");
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
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(LIGHT_BG);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        formPanel.add(usernameLabel);

        usernameField = createStyledTextField();
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(25));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        formPanel.add(passwordLabel);

        passwordField = createStyledPasswordField();
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));

        // Error label
        errorLabel = new JLabel();
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        errorLabel.setForeground(new Color(220, 53, 69));
        errorLabel.setVisible(false);
        errorLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        formPanel.add(errorLabel);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(LIGHT_BG);

        JButton loginButton = createStyledButton("Login", PRIMARY_COLOR, LIGHT_TEXT);
        JButton cancelButton = createStyledButton("Cancel", new Color(200, 200, 220), TEXT_COLOR);

        loginButton.addActionListener(e -> handleLogin());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(cancelButton);

        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalGlue());

        contentPanel.add(formPanel, BorderLayout.CENTER);
        return contentPanel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        field.setBackground(new Color(255, 255, 255));
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(PRIMARY_COLOR);
        field.setPreferredSize(new Dimension(0, 45));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Add focus border effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)));
            }
        });
        
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        field.setBackground(new Color(255, 255, 255));
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(PRIMARY_COLOR);
        field.setPreferredSize(new Dimension(0, 45));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Add focus border effect
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)));
            }
        });
        
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isArmed()) {
                    g2d.setColor(new Color(
                            Math.max(bgColor.getRed() - 20, 0),
                            Math.max(bgColor.getGreen() - 20, 0),
                            Math.max(bgColor.getBlue() - 20, 0)));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(
                            Math.min(bgColor.getRed() + 20, 255),
                            Math.min(bgColor.getGreen() + 20, 255),
                            Math.min(bgColor.getBlue() + 20, 255)));
                } else {
                    g2d.setColor(bgColor);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                if (!isEnabled()) {
                    g2d.setColor(new Color(150, 150, 150, 100));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                }
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(0, 45));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        Optional<Player> playerOpt = authService.authenticatePlayer(username, password);

        if (playerOpt.isPresent()) {
            loginSuccessful = true;
            dispose();
        } else {
            showError("Invalid username or password.");
            passwordField.setText("");
        }
    }

    private void showError(String message) {
        errorLabel.setText("✗ " + message);
        errorLabel.setVisible(true);
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
// Last modified during: feat: Add authentication UI components (LoginDialog, RegisterDialog, ForgotPasswordDialog) [minor]
