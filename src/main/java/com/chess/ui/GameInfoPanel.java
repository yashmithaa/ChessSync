package com.chess.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel displaying game information: current turn and captured pieces.
 */
public class GameInfoPanel extends JPanel {
    private JLabel turnLabel;
    private JLabel whitePawnsLabel;
    private JLabel blackPawnsLabel;
    private JLabel checkLabel;
    private JButton exitButton;
    private boolean isWhiteTurn;
    private int whitePawnsLost;
    private int blackPawnsLost;
    private boolean inCheck;
    private Runnable exitListener;

    public GameInfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(40, 40, 45));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setPreferredSize(new Dimension(0, 280));

        // Turn indicator
        turnLabel = new JLabel();
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Check indicator
        checkLabel = new JLabel();
        checkLabel.setFont(new Font("Arial", Font.BOLD, 12));
        checkLabel.setForeground(new Color(255, 100, 100));
        checkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        checkLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        checkLabel.setVisible(false);

        // Separator
        JLabel separatorLabel = new JLabel("─────────────");
        separatorLabel.setForeground(new Color(100, 100, 100));
        separatorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        separatorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        separatorLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // White pawns lost
        whitePawnsLabel = new JLabel();
        whitePawnsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        whitePawnsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        whitePawnsLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Black pawns lost
        blackPawnsLabel = new JLabel();
        blackPawnsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        blackPawnsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        blackPawnsLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Title
        JLabel titleLabel = new JLabel("Captured Pawns");
        titleLabel.setForeground(new Color(150, 150, 150));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Exit button
        exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));
        exitButton.setBackground(new Color(200, 50, 50));
        exitButton.setForeground(new Color(255, 255, 255));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(100, 30));
        exitButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> onExitClicked());

        add(turnLabel);
        add(checkLabel);
        add(separatorLabel);
        add(titleLabel);
        add(whitePawnsLabel);
        add(blackPawnsLabel);
        add(Box.createVerticalGlue());
        add(exitButton);

        // Initialize
        this.isWhiteTurn = true;
        this.whitePawnsLost = 0;
        this.blackPawnsLost = 0;
        this.inCheck = false;
        updateDisplay();
    }

    /**
     * Update the turn display
     */
    public void setWhiteTurn(boolean isWhiteTurn) {
        this.isWhiteTurn = isWhiteTurn;
        updateDisplay();
    }

    /**
     * Update pawn count
     */
    public void setPawnsLost(int whiteLost, int blackLost) {
        this.whitePawnsLost = whiteLost;
        this.blackPawnsLost = blackLost;
        updateDisplay();
    }

    /**
     * Increment white pawns lost
     */
    public void addWhitePawnLost() {
        whitePawnsLost++;
        updateDisplay();
    }

    /**
     * Increment black pawns lost
     */
    public void addBlackPawnLost() {
        blackPawnsLost++;
        updateDisplay();
    }

    private void updateDisplay() {
        // Update turn label
        Color turnColor = isWhiteTurn ? new Color(255, 255, 200) : new Color(100, 100, 100);
        turnLabel.setForeground(turnColor);
        turnLabel.setText(isWhiteTurn ? "⚪ WHITE TO MOVE" : "⚫ BLACK TO MOVE");

        // Update pawn counts
        whitePawnsLabel.setForeground(new Color(220, 220, 220));
        blackPawnsLabel.setForeground(new Color(220, 220, 220));
        
        whitePawnsLabel.setText("White: " + whitePawnsLost + " ♟");
        blackPawnsLabel.setText("Black: " + blackPawnsLost + " ♙");
    }

    /**
     * Called when exit button is clicked
     */
    private void onExitClicked() {
        if (exitListener != null) {
            exitListener.run();
        }
    }
    
    /**
     * Set the exit listener to be called when exit is clicked
     */
    public void setExitListener(Runnable listener) {
        this.exitListener = listener;
    }
    
    /**
     * Set whether the current player is in check
     */
    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
        if (inCheck) {
            checkLabel.setText("⚠ CHECK!");
            checkLabel.setVisible(true);
        } else {
            checkLabel.setVisible(false);
        }
    }
}
