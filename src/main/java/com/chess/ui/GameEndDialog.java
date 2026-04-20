package com.chess.ui;

import com.chess.model.pieces.PieceColor;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog showing the game end summary (checkmate, draw, or resignation).
 */
public class GameEndDialog extends JDialog {
    public enum GameEndReason {
        CHECKMATE("CHECKMATE"),
        DRAW("DRAW"),
        EXIT("USER EXIT");

        private final String displayName;

        GameEndReason(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public GameEndDialog(JFrame parent, PieceColor winner, int whitePawnsLost, int blackPawnsLost,
                          int totalMoves, GameEndReason reason) {
        super(parent, "Game Summary", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(parent);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(40, 40, 45));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("GAME OVER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 200, 100));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Result
        JLabel resultLabel = new JLabel();
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        if (winner != null) {
            resultLabel.setText((winner == PieceColor.WHITE ? "⚪ WHITE" : "⚫ BLACK") + " WINS");
            resultLabel.setForeground(winner == PieceColor.WHITE ? new Color(255, 255, 200) : new Color(150, 150, 150));
        } else {
            resultLabel.setText("GAME DRAWN");
            resultLabel.setForeground(new Color(200, 200, 200));
        }

        // Reason
        JLabel reasonLabel = new JLabel("Reason: " + reason.getDisplayName());
        reasonLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        reasonLabel.setForeground(new Color(180, 180, 180));
        reasonLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reasonLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Separator
        JLabel separatorLabel = new JLabel("─────────────────────");
        separatorLabel.setForeground(new Color(100, 100, 100));
        separatorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        separatorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        separatorLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(4, 1, 0, 10));
        statsPanel.setBackground(new Color(40, 40, 45));
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel movesLabel = createStatLabel("Total Moves: " + totalMoves);
        JLabel whiteLabel = createStatLabel("White Pawns Captured: " + whitePawnsLost);
        JLabel blackLabel = createStatLabel("Black Pawns Captured: " + blackPawnsLost);
        JLabel durationLabel = createStatLabel("Game Duration: " + calculateDuration());

        statsPanel.add(movesLabel);
        statsPanel.add(whiteLabel);
        statsPanel.add(blackLabel);
        statsPanel.add(durationLabel);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(40, 40, 45));

        JButton exitButton = new JButton("EXIT TO MENU");
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));
        exitButton.setBackground(new Color(100, 150, 200));
        exitButton.setForeground(new Color(255, 255, 255));
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(150, 35));
        exitButton.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        buttonPanel.add(exitButton);

        contentPanel.add(titleLabel);
        contentPanel.add(resultLabel);
        contentPanel.add(reasonLabel);
        contentPanel.add(separatorLabel);
        contentPanel.add(statsPanel);
        contentPanel.add(buttonPanel);

        setContentPane(contentPanel);
        setSize(400, 450);
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(new Color(200, 200, 200));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private String calculateDuration() {
        return "displayed in next update";
    }
}
