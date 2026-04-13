package com.chess.ui;

import com.chess.model.GameMode;
import com.chess.model.Player;
import com.chess.service.PlayerService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameSetupDialog extends JDialog {
    private PlayerService playerService;
    private Player currentPlayer;
    
    private JComboBox<Player> opponentComboBox;
    private JComboBox<GameMode> gameModeComboBox;
    
    private JButton createButton;
    private JButton cancelButton;
    
    private boolean gameCreated = false;
    private Long selectedOpponentId;
    private GameMode selectedGameMode;
    
    public GameSetupDialog(JFrame parent, PlayerService playerService, Player currentPlayer) {
        super(parent, "New Game Setup", true);
        this.playerService = playerService;
        this.currentPlayer = currentPlayer;
        
        initializeComponents();
        loadPlayers();
        
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private void initializeComponents() {
        // Create layout
        setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Opponent selection
        JLabel opponentLabel = new JLabel("Select Opponent:");
        opponentComboBox = new JComboBox<>();
        
        // Game mode selection
        JLabel gameModeLabel = new JLabel("Game Mode:");
        gameModeComboBox = new JComboBox<>(GameMode.values());
        
        formPanel.add(opponentLabel);
        formPanel.add(opponentComboBox);
        formPanel.add(gameModeLabel);
        formPanel.add(gameModeComboBox);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        createButton = new JButton("Create Game");
        cancelButton = new JButton("Cancel");
        
        createButton.addActionListener(e -> createGame());
        cancelButton.addActionListener(e -> cancel());
        
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadPlayers() {
        List<Player> players = playerService.getAllPlayers();
        
        // Remove current player from the list
        players.removeIf(player -> player.getId().equals(currentPlayer.getId()));
        
        for (Player player : players) {
            opponentComboBox.addItem(player);
        }
    }
    
    private void createGame() {
        if (opponentComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an opponent.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get selected values
        Player opponent = (Player) opponentComboBox.getSelectedItem();
        selectedOpponentId = opponent.getId();
        selectedGameMode = (GameMode) gameModeComboBox.getSelectedItem();
        
        gameCreated = true;
        dispose();
    }
    
    private void cancel() {
        gameCreated = false;
        dispose();
    }
    
    public boolean isGameCreated() {
        return gameCreated;
    }
    
    public Long getSelectedOpponentId() {
        return selectedOpponentId;
    }
    
    public GameMode getSelectedGameMode() {
        return selectedGameMode;
    }
    
    // Custom renderer for player items in the combo box
    private class PlayerRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                     int index, boolean isSelected, 
                                                     boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (value instanceof Player) {
                Player player = (Player) value;
                setText(player.getUsername());
            }
            
            return this;
        }
    }
}// Last modified during: feat: Implement piece movement Strategy pattern through subclasses [strategy]
