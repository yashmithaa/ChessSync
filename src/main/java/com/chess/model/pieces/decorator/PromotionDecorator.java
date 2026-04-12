package com.chess.model.pieces.decorator;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceColor;
import com.chess.model.pieces.PieceType;
import java.util.ArrayList;
import java.util.List;

/**
 * Decorator Pattern:
 * Adds pawn promotion capability to a Pawn piece.
 * When a pawn reaches the opposite end of the board, it can be promoted to Queen, Rook, Bishop, or Knight.
 */
public class PromotionDecorator extends PieceDecorator {

    public PromotionDecorator(Piece wrappedPiece) {
        super(wrappedPiece);
    }

    /**
     * Generate promotion moves when a pawn reaches the final rank
     */
    @Override
    protected List<Move> getSpecialMoves(Board board) {
        List<Move> promotionMoves = new ArrayList<>();

        // Only pawns can be promoted
        if (wrappedPiece.getType() == PieceType.PAWN) {
            int promotionRow = (wrappedPiece.getColor() == PieceColor.WHITE) ? 0 : 7;

            // Check if pawn is on the promotion row
            if (wrappedPiece.getRow() == promotionRow) {
                // Pawn has reached the end, can be promoted
                // This is a simplified approach showing the concept
                promotionMoves.add(new Move(wrappedPiece, wrappedPiece.getRow(), wrappedPiece.getColumn(), 
                                           promotionRow, wrappedPiece.getColumn()));
            }
        }

        return promotionMoves;
    }
}
// Last modified during: feat: Implement Piece Decorator pattern for special moves [decorator]
