package com.chess.ui;

import com.chess.model.Move;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Panel that displays move history in two separate columns for white and black moves.
 */
public class MoveHistoryPanel extends JPanel {
    private DefaultListModel<MoveEntry> whiteMovesModel;
    private DefaultListModel<MoveEntry> blackMovesModel;
    private JList<MoveEntry> whiteList;
    private JList<MoveEntry> blackList;
    private Consumer<Integer> moveNavigationListener;
    private List<Move> allMoves;

    public MoveHistoryPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 0));
        setBackground(new Color(50, 50, 55));

        // Initialize data models
        whiteMovesModel = new DefaultListModel<>();
        blackMovesModel = new DefaultListModel<>();
        allMoves = new ArrayList<>();

        // Create white moves list
        whiteList = new JList<>(whiteMovesModel);
        whiteList.setBackground(new Color(50, 50, 55));
        whiteList.setForeground(new Color(220, 220, 220));
        whiteList.setFont(new Font("Arial", Font.PLAIN, 12));
        whiteList.setCellRenderer(new MoveListRenderer());
        whiteList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && whiteList.getSelectedValue() != null) {
                MoveEntry entry = whiteList.getSelectedValue();
                blackList.clearSelection();
                if (moveNavigationListener != null) {
                    moveNavigationListener.accept(entry.getMoveIndex());
                }
            }
        });

        // Create black moves list
        blackList = new JList<>(blackMovesModel);
        blackList.setBackground(new Color(50, 50, 55));
        blackList.setForeground(new Color(220, 220, 220));
        blackList.setFont(new Font("Arial", Font.PLAIN, 12));
        blackList.setCellRenderer(new MoveListRenderer());
        blackList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && blackList.getSelectedValue() != null) {
                MoveEntry entry = blackList.getSelectedValue();
                whiteList.clearSelection();
                if (moveNavigationListener != null) {
                    moveNavigationListener.accept(entry.getMoveIndex());
                }
            }
        });

        // Create scroll panes
        JScrollPane whiteScroll = new JScrollPane(whiteList);
        whiteScroll.setBackground(new Color(50, 50, 55));
        whiteScroll.getViewport().setBackground(new Color(50, 50, 55));

        JScrollPane blackScroll = new JScrollPane(blackList);
        blackScroll.setBackground(new Color(50, 50, 55));
        blackScroll.getViewport().setBackground(new Color(50, 50, 55));

        // Create columns panel
        JPanel columnsPanel = new JPanel();
        columnsPanel.setLayout(new GridLayout(1, 2, 5, 0));
        columnsPanel.setBackground(new Color(50, 50, 55));
        columnsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Create white column with header
        JPanel whiteColumn = new JPanel();
        whiteColumn.setLayout(new BorderLayout());
        whiteColumn.setBackground(new Color(50, 50, 55));
        JLabel whiteHeader = new JLabel("White");
        whiteHeader.setForeground(new Color(200, 200, 100));
        whiteHeader.setFont(new Font("Arial", Font.BOLD, 12));
        whiteHeader.setHorizontalAlignment(JLabel.CENTER);
        whiteColumn.add(whiteHeader, BorderLayout.NORTH);
        whiteColumn.add(whiteScroll, BorderLayout.CENTER);

        // Create black column with header
        JPanel blackColumn = new JPanel();
        blackColumn.setLayout(new BorderLayout());
        blackColumn.setBackground(new Color(50, 50, 55));
        JLabel blackHeader = new JLabel("Black");
        blackHeader.setForeground(new Color(150, 150, 150));
        blackHeader.setFont(new Font("Arial", Font.BOLD, 12));
        blackHeader.setHorizontalAlignment(JLabel.CENTER);
        blackColumn.add(blackHeader, BorderLayout.NORTH);
        blackColumn.add(blackScroll, BorderLayout.CENTER);

        columnsPanel.add(whiteColumn);
        columnsPanel.add(blackColumn);

        // Create header
        JLabel headerLabel = new JLabel("Moves");
        headerLabel.setForeground(new Color(200, 200, 200));
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerLabel.setBackground(new Color(40, 40, 45));
        headerLabel.setOpaque(true);

        add(headerLabel, BorderLayout.NORTH);
        add(columnsPanel, BorderLayout.CENTER);
    }

    /**
     * Add a move to the history
     * @param move The move to add
     * @param moveIndex The index of the move
     * @param isWhiteMove True if the move is by white, false if by black
     */
    public void addMove(Move move, int moveIndex, boolean isWhiteMove) {
        String moveNotation = move.toString();
        MoveEntry entry = new MoveEntry(moveNotation, moveIndex);
        allMoves.add(move);

        if (isWhiteMove) {
            whiteMovesModel.addElement(entry);
        } else {
            blackMovesModel.addElement(entry);
        }
    }

    /**
     * Set the listener for move navigation
     */
    public void setMoveNavigationListener(Consumer<Integer> listener) {
        this.moveNavigationListener = listener;
    }

    /**
     * Clear all moves from the history
     */
    public void clearHistory() {
        whiteMovesModel.clear();
        blackMovesModel.clear();
        allMoves.clear();
    }

    /**
     * Inner class representing a move entry
     */
    public static class MoveEntry {
        private String notation;
        private int moveIndex;

        public MoveEntry(String notation, int moveIndex) {
            this.notation = notation;
            this.moveIndex = moveIndex;
        }

        public int getMoveIndex() {
            return moveIndex;
        }

        @Override
        public String toString() {
            return notation;
        }
    }

    /**
     * Custom cell renderer for move lists
     */
    private static class MoveListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (c instanceof JLabel) {
                JLabel label = (JLabel) c;
                if (isSelected) {
                    label.setBackground(new Color(100, 140, 180));
                    label.setForeground(new Color(255, 255, 255));  // White text for selected
                } else {
                    label.setBackground(new Color(50, 50, 55));
                    label.setForeground(new Color(220, 220, 220));
                }
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            }

            return c;
        }
    }
}
