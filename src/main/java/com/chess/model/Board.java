package com.chess.model;

import com.chess.model.pieces.*;
import com.chess.model.pieces.factory.PieceFactory;
import com.chess.model.pieces.factory.StandardPieceFactory;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

import java.io.Serializable;

@Getter
@Setter
public class Board implements Serializable {
    private static final long serialVersionUID = 1L;

    private Piece[][] squares;
    private Move lastMove;

    public Board() {
        squares = new Piece[8][8];
        initializeBoard(); // Initializes board when object is created
    }

    // Public method to match external calls (like in BoardPanel)
    public void setupInitialPosition() {
        initializeBoard();
    }

    private void initializeBoard() {
        // Clear existing board state
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = null;
            }
        }

        // Setup white pieces
        setupPieces(PieceColor.WHITE);

        // Setup black pieces
        setupPieces(PieceColor.BLACK);
    }

    private void setupPieces(PieceColor color) {
        int pawnRow = (color == PieceColor.WHITE) ? 6 : 1;
        int pieceRow = (color == PieceColor.WHITE) ? 7 : 0;
        PieceFactory factory = new StandardPieceFactory();

        // Pawns
        for (int col = 0; col < 8; col++) {
            squares[pawnRow][col] = factory.createPiece(PieceType.PAWN, color, pawnRow, col);
        }

        // Rooks
        squares[pieceRow][0] = factory.createPiece(PieceType.ROOK, color, pieceRow, 0);
        squares[pieceRow][7] = factory.createPiece(PieceType.ROOK, color, pieceRow, 7);

        // Knights
        squares[pieceRow][1] = factory.createPiece(PieceType.KNIGHT, color, pieceRow, 1);
        squares[pieceRow][6] = factory.createPiece(PieceType.KNIGHT, color, pieceRow, 6);

        // Bishops
        squares[pieceRow][2] = factory.createPiece(PieceType.BISHOP, color, pieceRow, 2);
        squares[pieceRow][5] = factory.createPiece(PieceType.BISHOP, color, pieceRow, 5);

        // Queen
        squares[pieceRow][3] = factory.createPiece(PieceType.QUEEN, color, pieceRow, 3);

        // King
        squares[pieceRow][4] = factory.createPiece(PieceType.KING, color, pieceRow, 4);
    }

    public Piece getPiece(int row, int column) {
        if (row >= 0 && row < 8 && column >= 0 && column < 8) {
            return squares[row][column];
        }
        return null;
    }

    public void setPiece(int row, int column, Piece piece) {
        if (row >= 0 && row < 8 && column >= 0 && column < 8) {
            squares[row][column] = piece;
            if (piece != null) {
                piece.setRow(row);
                piece.setColumn(column);
            }
        }
    }

    public void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Piece piece = getPiece(fromRow, fromCol);
        if (piece != null) {
            setPiece(toRow, toCol, piece);
            setPiece(fromRow, fromCol, null);
            piece.move(toRow, toCol);
        }
    }

    public void executeMove(Move move) {
        if (move.isCastling()) {
            executeCastling(move);
        } else {
            // Handle en passant capture
            if (move.isEnPassant()) {
                setPiece(move.getSourceRow(), move.getTargetColumn(), null);
            }

            movePiece(move.getSourceRow(), move.getSourceColumn(),
                      move.getTargetRow(), move.getTargetColumn());

            // Handle promotion
            if (move.isPromotion()) {
                PieceType promoType = move.getPromotionPieceType() != null ? move.getPromotionPieceType() : PieceType.QUEEN;
                PieceFactory factory = new StandardPieceFactory();
                Piece promotedPiece = factory.createPiece(promoType, move.getPiece().getColor(), move.getTargetRow(), move.getTargetColumn());
                promotedPiece.setHasMoved(true);
                setPiece(move.getTargetRow(), move.getTargetColumn(), promotedPiece);
            }
        }
        this.lastMove = move;
    }

    private void executeCastling(Move move) {
        int row = move.getSourceRow();
        int kingCol = move.getSourceColumn();
        int targetCol = move.getTargetColumn();

        // Move the king
        movePiece(row, kingCol, row, targetCol);

        // Move the rook
        int rookSourceCol = (targetCol > kingCol) ? 7 : 0;
        int rookTargetCol = (targetCol > kingCol) ? targetCol - 1 : targetCol + 1;

        movePiece(row, rookSourceCol, row, rookTargetCol);
    }

    public boolean isInCheck(PieceColor playerColor) {
        // Find the king
        int kingRow = -1, kingCol = -1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(row, col);
                if (piece != null && piece instanceof King && piece.getColor() == playerColor) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
        }
    
        // If king not found (shouldn't happen), return false
        if (kingRow == -1 || kingCol == -1) return false;
    
        // Check if any opponent piece attacks the king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(row, col);
                if (piece != null && piece.getColor() != playerColor) {
                    List<Move> moves = piece.getValidMoves(this);
                    for (Move move : moves) {
                        if (move.getTargetRow() == kingRow && move.getTargetColumn() == kingCol) {
                            return true; // King is under attack
                        }
                    }
                }
            }
        }
    
        return false;
    }
    
    

    private King findKing(PieceColor color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(row, col);
                if (piece instanceof King && piece.getColor() == color) {
                    return (King) piece;
                }
            }
        }
        return null;
    }
    public boolean isCheckmate(PieceColor color) {
        if (!isInCheck(color)) return false;
    
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(row, col);
                if (piece != null && piece.getColor() == color) {
                    List<Move> moves = piece.getValidMoves(this);
                    for (Move move : moves) {
                        Board clone = this.clone();
                        clone.executeMove(move);
                        if (!clone.isInCheck(color)) {
                            return false; // Found at least one move that saves the king
                        }
                    }
                }
            }
        }
        return true; // No valid move found to save king
    }
    

    // Clone method for validation purposes (DEEP copy to avoid piece state mutation)
    public Board clone() {
        Board clonedBoard = new Board();
        // Clear the initialized board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                clonedBoard.squares[row][col] = null;
            }
        }
        
        // Deep copy all pieces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = this.squares[row][col];
                if (piece != null) {
                    Piece clonedPiece = clonePiece(piece);
                    clonedBoard.squares[row][col] = clonedPiece;
                }
            }
        }
        clonedBoard.setLastMove(this.lastMove);
        return clonedBoard;
    }
    
    /**
     * Deep copy a piece with all its state
     */
    private Piece clonePiece(Piece piece) {
        Piece newPiece = null;
        PieceColor color = piece.getColor();
        int row = piece.getRow();
        int col = piece.getColumn();
        
        // Create a new instance based on piece type
        switch (piece.getType()) {
            case PAWN -> newPiece = new Pawn(color, row, col);
            case ROOK -> newPiece = new Rook(color, row, col);
            case KNIGHT -> newPiece = new Knight(color, row, col);
            case BISHOP -> newPiece = new Bishop(color, row, col);
            case QUEEN -> newPiece = new Queen(color, row, col);
            case KING -> newPiece = new King(color, row, col);
        }
        
        // Copy the hasMoved state
        if (newPiece != null && piece.hasMoved()) {
            newPiece.setHasMoved(true);
        }
        
        return newPiece;
    }
}
// Last modified during: feat: Implement Board model with move state management [minor]
