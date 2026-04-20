package com.chess.ui;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceColor;
import com.chess.model.pieces.PieceType;
import com.chess.util.MoveValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BoardPanel extends JPanel {
    private static final int TILE_SIZE = 80;
    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final Color LIGHT_TILE = new Color(235, 235, 205);
    private static final Color DARK_TILE = new Color(119, 149, 86);
    private static final Color HIGHLIGHT_COLOR = new Color(0, 255, 0);
    private static final Color SELECTED_COLOR = new Color(255, 255, 0);
    private static final Font[] PIECE_FONTS = {
        new Font("DejaVu Sans", Font.BOLD, 60),
        new Font("Noto Sans Symbols2", Font.BOLD, 60),
        new Font("Segoe UI Symbol", Font.BOLD, 60),
        new Font("Serif", Font.BOLD, 60)
    };

    private Board boardModel;
    private Point selectedTile = null;
    private List<Move> validMoves = null;
    private boolean isWhiteTurn = true;
    private Consumer<Move> moveListener;
    private List<Move> moveHistory;

    public BoardPanel() {
        setPreferredSize(new Dimension(TILE_SIZE * COLS, TILE_SIZE * ROWS));
        setBackground(new Color(40, 40, 45));
        this.boardModel = new Board();
        this.moveHistory = new ArrayList<>();
        initializeBoard();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    private void initializeBoard() {
        boardModel.setupInitialPosition();
    }

    private void handleMouseClick(MouseEvent e) {
        int col = e.getX() / TILE_SIZE;
        int row = e.getY() / TILE_SIZE;

        if (col < 0 || col >= COLS || row < 0 || row >= ROWS) {
            return;
        }

        if (selectedTile == null) {
            // No piece selected yet - try to select a piece
            Piece piece = boardModel.getPiece(row, col);
            if (piece != null && ((piece.getColor() == PieceColor.WHITE) == isWhiteTurn)) {
                selectedTile = new Point(col, row);
                // Get all moves and filter them to only legal moves
                List<Move> allMoves = piece.getValidMoves(boardModel);
                validMoves = new ArrayList<>();
                for (Move move : allMoves) {
                    if (MoveValidator.isLegalMove(boardModel, move)) {
                        validMoves.add(move);
                    }
                }
            }
        } else {
            // A piece is already selected - try to move it
            // Check if clicking on the same piece (deselect it)
            if (selectedTile.x == col && selectedTile.y == row) {
                // User clicked the same piece again - deselect it
                selectedTile = null;
                validMoves = null;
            } else {
                // User clicked a different square - try to move
                Piece selectedPiece = boardModel.getPiece(selectedTile.y, selectedTile.x);
                if (selectedPiece != null) {
                    for (Move move : validMoves) {
                        if (move.getTargetRow() == row && move.getTargetColumn() == col) {
                            // This move is guaranteed to be legal since it's in validMoves
                            boardModel.executeMove(move);
                            moveHistory.add(move);  // Add to shared history
                            isWhiteTurn = !isWhiteTurn;
                            selectedTile = null;
                            validMoves = null;
                            
                            // Notify the move listener
                            if (moveListener != null) {
                                moveListener.accept(move);
                            }
                            
                            repaint();
                            return;
                        }
                    }
                    // Move was not valid - check if clicking on own piece to reselect
                    Piece targetPiece = boardModel.getPiece(row, col);
                    if (targetPiece != null && targetPiece.getColor() == selectedPiece.getColor()) {
                        // Clicked on another piece of the same color - select it instead
                        selectedTile = new Point(col, row);
                        List<Move> allMoves = targetPiece.getValidMoves(boardModel);
                        validMoves = new ArrayList<>();
                        for (Move move : allMoves) {
                            if (MoveValidator.isLegalMove(boardModel, move)) {
                                validMoves.add(move);
                            }
                        }
                    } else {
                        // Invalid move target - deselect
                        selectedTile = null;
                        validMoves = null;
                    }
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBoard(g2d);
        highlightValidMoves(g2d);
        highlightSelectedTile(g2d);
        drawPieces(g2d);
        drawBoardLabels(g2d);
    }

    private void drawBoard(Graphics2D g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                boolean isLight = (row + col) % 2 == 0;
                g.setColor(isLight ? LIGHT_TILE : DARK_TILE);
                g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                // Draw subtle grid
                g.setColor(new Color(200, 200, 200, 20));
                g.setStroke(new BasicStroke(0.5f));
                g.drawRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void highlightSelectedTile(Graphics2D g) {
        if (selectedTile != null) {
            g.setColor(new Color(255, 255, 0, 150));
            g.fillRect(selectedTile.x * TILE_SIZE, selectedTile.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            // Border
            g.setColor(SELECTED_COLOR);
            g.setStroke(new BasicStroke(3));
            g.drawRect(selectedTile.x * TILE_SIZE + 1, selectedTile.y * TILE_SIZE + 1, TILE_SIZE - 2, TILE_SIZE - 2);
        }
    }

    private void highlightValidMoves(Graphics2D g) {
        if (validMoves != null) {
            for (Move move : validMoves) {
                int x = move.getToCol() * TILE_SIZE + TILE_SIZE / 2;
                int y = move.getToRow() * TILE_SIZE + TILE_SIZE / 2;

                // Draw circle for empty squares
                if (boardModel.getPiece(move.getToRow(), move.getToCol()) == null) {
                    g.setColor(new Color(0, 255, 0, 150));
                    g.fillOval(x - 12, y - 12, 24, 24);
                } else {
                    // Draw ring for capture squares
                    g.setColor(new Color(255, 100, 100, 150));
                    g.setStroke(new BasicStroke(3));
                    g.drawOval(x - 35, y - 35, 70, 70);
                }
            }
        }
    }

    private void drawBoardLabels(Graphics2D g) {
        g.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g.setColor(new Color(150, 150, 150, 200));

        // Column labels (a-h)
        for (int col = 0; col < COLS; col++) {
            char label = (char) ('a' + col);
            g.drawString(String.valueOf(label), col * TILE_SIZE + TILE_SIZE - 15, TILE_SIZE * ROWS + 15);
        }

        // Row labels (8-1)
        for (int row = 0; row < ROWS; row++) {
            String label = String.valueOf(8 - row);
            g.drawString(label, 5, (row + 1) * TILE_SIZE - 5);
        }
    }

    private void drawPieces(Graphics2D g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Piece piece = boardModel.getPiece(row, col);
                if (piece != null) {
                    String symbol = getUnicodeSymbol(piece);
                    Font drawFont = pickDisplayFont(symbol);
                    String renderedSymbol = symbol;

                    if (drawFont == null) {
                        drawFont = PIECE_FONTS[PIECE_FONTS.length - 1];
                        renderedSymbol = getFallbackSymbol(piece);
                    }

                    // Draw piece with shadow for better visibility
                    g.setColor(new Color(0, 0, 0, 100));
                    g.setFont(drawFont);
                    g.drawString(renderedSymbol, col * TILE_SIZE + 10, row * TILE_SIZE + 65);

                    // Draw actual piece
                    if (piece.getColor() == PieceColor.WHITE) {
                        g.setColor(new Color(255, 255, 255));
                    } else {
                        g.setColor(new Color(50, 50, 50));
                    }
                    g.drawString(renderedSymbol, col * TILE_SIZE + 8, row * TILE_SIZE + 63);
                }
            }
        }
    }

    private Font pickDisplayFont(String symbol) {
        for (Font font : PIECE_FONTS) {
            if (font.canDisplayUpTo(symbol) == -1) {
                return font;
            }
        }
        return null;
    }

    private String getFallbackSymbol(Piece piece) {
        return switch (piece.getType()) {
            case KING -> "K";
            case QUEEN -> "Q";
            case ROOK -> "R";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case PAWN -> "P";
        };
    }

    private String getUnicodeSymbol(Piece piece) {
        return switch (piece.getType()) {
            case ROOK -> piece.getColor() == PieceColor.WHITE ? "♖" : "♜";
            case KNIGHT -> piece.getColor() == PieceColor.WHITE ? "♘" : "♞";
            case BISHOP -> piece.getColor() == PieceColor.WHITE ? "♗" : "♝";
            case QUEEN -> piece.getColor() == PieceColor.WHITE ? "♕" : "♛";
            case KING -> piece.getColor() == PieceColor.WHITE ? "♔" : "♚";
            case PAWN -> piece.getColor() == PieceColor.WHITE ? "♙" : "♟";
            default -> "?";
        };
    }

    public void resetBoard() {
        selectedTile = null;
        validMoves = null;
        isWhiteTurn = true;
        boardModel.setupInitialPosition();
        moveHistory.clear();
        repaint();
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    /**
     * Set the shared move history (from GamePanel)
     */
    public void setMoveHistory(List<Move> history) {
        this.moveHistory = history;
    }

    /**
     * Set a listener to be notified when moves are made
     */
    public void setMoveListener(Consumer<Move> listener) {
        this.moveListener = listener;
    }

    /**
     * Get the board model
     */
    public Board getBoardModel() {
        return boardModel;
    }

    /**
     * Replay moves up to a specific index
     * @param moveIndex The index of the move to replay to (-1 for initial position)
     */
    public void replayMovesToIndex(int moveIndex) {
        // Reset the board to initial position
        boardModel.setupInitialPosition();
        isWhiteTurn = true;
        selectedTile = null;
        validMoves = null;

        // Replay all moves up to the given index
        for (int i = 0; i <= moveIndex && i < moveHistory.size(); i++) {
            Move move = moveHistory.get(i);
            boardModel.executeMove(move);
            isWhiteTurn = !isWhiteTurn;
        }

        repaint();
    }
}
