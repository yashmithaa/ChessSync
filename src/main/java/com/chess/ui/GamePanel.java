package com.chess.ui;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceColor;
import com.chess.model.pieces.PieceType;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game panel that combines the chess board with move history.
 * Provides a complete game interface with board on left and move history on right.
 */
public class GamePanel extends JPanel {
    private BoardPanel boardPanel;
    private MoveHistoryPanel moveHistoryPanel;
    private GameInfoPanel gameInfoPanel;
    private List<Move> moveHistory;
    private int currentMoveIndex;
    private int whitePawnsLost;
    private int blackPawnsLost;
    private boolean gameEnded;
    private PieceColor winner;

    public GamePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 45));
        
        this.moveHistory = new ArrayList<>();
        this.currentMoveIndex = -1;
        this.whitePawnsLost = 0;
        this.blackPawnsLost = 0;
        this.gameEnded = false;
        this.winner = null;

        // Create the board panel
        boardPanel = new BoardPanel();
        boardPanel.setMoveListener(this::onMoveMade);
        boardPanel.setMoveHistory(moveHistory);  // Share move history

        // Create the move history panel
        moveHistoryPanel = new MoveHistoryPanel();
        moveHistoryPanel.setMoveNavigationListener(this::onMoveSelected);

        // Create the game info panel
        gameInfoPanel = new GameInfoPanel();

        // Create right side panel with game info and move history
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(new Color(40, 40, 45));
        rightPanel.add(gameInfoPanel, BorderLayout.NORTH);
        rightPanel.add(moveHistoryPanel, BorderLayout.CENTER);

        // Add panels to layout
        add(boardPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        
        // Set exit listener on game info panel
        gameInfoPanel.setExitListener(this::onGameExit);
    }

    /**
     * Called when a move is made on the board
     */
    private void onMoveMade(Move move) {
        // Remove any moves after the current position (if we were replaying)
        while (moveHistory.size() > currentMoveIndex + 1) {
            moveHistory.remove(moveHistory.size() - 1);
        }

        // Track captured pawns by checking the captured piece's color
        if (move.getCapturedPiece() != null) {
            if (move.getCapturedPiece().getType() == PieceType.PAWN) {
                // Check the color of the captured piece
                if (move.getCapturedPiece().getColor() == PieceColor.WHITE) {
                    whitePawnsLost++;
                    gameInfoPanel.addWhitePawnLost();
                } else {
                    blackPawnsLost++;
                    gameInfoPanel.addBlackPawnLost();
                }
            }
        }

        moveHistory.add(move);
        currentMoveIndex++;
        
        // Determine if this is a white or black move based on move index
        // Move 0 is white, move 1 is black, move 2 is white, etc.
        boolean isWhiteMove = (currentMoveIndex % 2 == 0);
        moveHistoryPanel.addMove(move, currentMoveIndex, isWhiteMove);

        // Update turn indicator in game info panel
        gameInfoPanel.setWhiteTurn(boardPanel.isWhiteTurn());
        
        // Check for check and checkmate
        checkGameState();
    }
    
    /**
     * Check the current game state for check and checkmate
     */
    private void checkGameState() {
        if (gameEnded) return;
        
        Board board = boardPanel.getBoardModel();
        PieceColor currentPlayer = boardPanel.isWhiteTurn() ? PieceColor.WHITE : PieceColor.BLACK;
        
        // Check if current player is in checkmate
        if (board.isCheckmate(currentPlayer)) {
            gameEnded = true;
            winner = currentPlayer == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
            SwingUtilities.invokeLater(() -> showGameEndDialog(GameEndDialog.GameEndReason.CHECKMATE));
        }
        // Check if current player is in check
        else if (board.isInCheck(currentPlayer)) {
            // Show check indicator
            gameInfoPanel.setInCheck(true);
        } else {
            gameInfoPanel.setInCheck(false);
        }
    }

    /**
     * Called when user clicks on a move in the history panel
     */
    private void onMoveSelected(int moveIndex) {
        if (moveIndex < -1 || moveIndex >= moveHistory.size()) {
            return;
        }

        currentMoveIndex = moveIndex;
        boardPanel.replayMovesToIndex(moveIndex);
        
        // Recalculate captured pawns based on replayed position
        recalculateCapturedPawns(moveIndex);
        
        // Update turn indicator
        gameInfoPanel.setWhiteTurn(boardPanel.isWhiteTurn());
    }

    /**
     * Recalculate captured pawns based on the current position
     */
    private void recalculateCapturedPawns(int moveIndex) {
        whitePawnsLost = 0;
        blackPawnsLost = 0;

        // Count captured pawns up to the given move index
        for (int i = 0; i <= moveIndex && i < moveHistory.size(); i++) {
            Move move = moveHistory.get(i);
            if (move.getCapturedPiece() != null && move.getCapturedPiece().getType() == PieceType.PAWN) {
                // Check the color of the captured piece
                if (move.getCapturedPiece().getColor() == PieceColor.WHITE) {
                    whitePawnsLost++;
                } else {
                    blackPawnsLost++;
                }
            }
        }

        gameInfoPanel.setPawnsLost(whitePawnsLost, blackPawnsLost);
    }

    public Board getBoardModel() {
        return boardPanel.getBoardModel();
    }

    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }

    public int getCurrentMoveIndex() {
        return currentMoveIndex;
    }
    
    /**
     * Called when the game is exited
     */
    private void onGameExit() {
        if (!gameEnded) {
            gameEnded = true;
            showGameEndDialog(GameEndDialog.GameEndReason.EXIT);
        }
    }
    
    /**
     * Show the game end summary dialog
     */
    private void showGameEndDialog(GameEndDialog.GameEndReason reason) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        GameEndDialog dialog = new GameEndDialog(
            topFrame,
            winner,
            whitePawnsLost,
            blackPawnsLost,
            moveHistory.size(),
            reason
        );
        dialog.setVisible(true);
    }
}
