package com.chess.model.pieces.decorator;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceColor;
import com.chess.model.pieces.MoveProvider;
import com.chess.model.pieces.MoveValidator;
import java.util.List;

/**
 * Decorator Pattern:
 * Base decorator class that wraps a Piece and allows adding special move behavior.
 * This allows composing special moves (castling, en passant, promotion) without
 * modifying concrete piece classes.
 */
public abstract class PieceDecorator extends Piece implements MoveProvider, MoveValidator {
    protected Piece wrappedPiece;

    public PieceDecorator(Piece wrappedPiece) {
        super(wrappedPiece.getColor(), wrappedPiece.getRow(), wrappedPiece.getColumn());
        this.wrappedPiece = wrappedPiece;
        this.type = wrappedPiece.getType();
    }

    /**
     * Add special moves to the base piece's valid moves
     */
    protected abstract List<Move> getSpecialMoves(Board board);

    /**
     * Get the base piece's valid moves and add special moves
     */
    @Override
    public List<Move> getValidMoves(Board board) {
        if (wrappedPiece instanceof MoveProvider) {
            List<Move> moves = ((MoveProvider) wrappedPiece).getValidMoves(board);
            moves.addAll(getSpecialMoves(board));
            return moves;
        }
        return getSpecialMoves(board);
    }

    /**
     * Delegate validation to wrapped piece
     */
    @Override
    public boolean isValidMove(Board board, int targetRow, int targetColumn) {
        if (wrappedPiece instanceof MoveValidator) {
            if (((MoveValidator) wrappedPiece).isValidMove(board, targetRow, targetColumn)) {
                return true;
            }
        }
        // Check special moves
        return getSpecialMoves(board).stream()
                .anyMatch(move -> move.getTargetRow() == targetRow && 
                                 move.getTargetColumn() == targetColumn);
    }

    @Override
    public void move(int targetRow, int targetColumn) {
        wrappedPiece.move(targetRow, targetColumn);
        super.move(targetRow, targetColumn);
    }

    @Override
    public boolean hasMoved() {
        return wrappedPiece.hasMoved();
    }

    @Override
    public void setHasMoved(boolean hasMoved) {
        wrappedPiece.setHasMoved(hasMoved);
    }
}
// Last modified during: feat: Implement Piece Decorator pattern for special moves [decorator]
