package com.chess.model.pieces.decorator;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.pieces.King;
import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceColor;
import com.chess.model.pieces.Rook;
import java.util.ArrayList;
import java.util.List;

/**
 * Decorator Pattern:
 * Adds castling ability to a piece (typically a King).
 * Castling is a special two-piece move between King and Rook.
 */
public class CastlingDecorator extends PieceDecorator {

    public CastlingDecorator(Piece wrappedPiece) {
        super(wrappedPiece);
    }

    /**
     * Generate castling moves if the piece is a King and both King and Rook haven't moved
     */
    @Override
    protected List<Move> getSpecialMoves(Board board) {
        List<Move> castlingMoves = new ArrayList<>();

        // Only kings can castle
        if (wrappedPiece.getType().toString().equalsIgnoreCase("KING") && !wrappedPiece.hasMoved()) {
            int row = wrappedPiece.getRow();
            
            // Kingside castling (right rook)
            Piece rightRook = board.getPiece(row, 7);
            if (rightRook instanceof Rook && !rightRook.hasMoved() && 
                rightRook.getColor() == wrappedPiece.getColor()) {
                // Check if squares between king and rook are empty
                if (board.getPiece(row, 5) == null && board.getPiece(row, 6) == null) {
                    castlingMoves.add(new Move(wrappedPiece, row, 4, row, 6));
                }
            }
            
            // Queenside castling (left rook)
            Piece leftRook = board.getPiece(row, 0);
            if (leftRook instanceof Rook && !leftRook.hasMoved() && 
                leftRook.getColor() == wrappedPiece.getColor()) {
                // Check if squares between king and rook are empty
                if (board.getPiece(row, 1) == null && board.getPiece(row, 2) == null && 
                    board.getPiece(row, 3) == null) {
                    castlingMoves.add(new Move(wrappedPiece, row, 4, row, 2));
                }
            }
        }

        return castlingMoves;
    }
}
// Last modified during: feat: Implement Piece Decorator pattern for special moves [decorator]
