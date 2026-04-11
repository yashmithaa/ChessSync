package com.chess.model.pieces.factory;

import com.chess.model.pieces.*;

/**
 * Dependency Inversion Principle (DIP):
 * Concrete implementation of PieceFactory for standard chess pieces.
 * Encapsulates the creation logic for all piece types.
 */
public class StandardPieceFactory implements PieceFactory {

    @Override
    public Piece createPiece(PieceType type, PieceColor color, int row, int column) {
        return switch (type) {
            case PAWN -> new Pawn(color, row, column);
            case KNIGHT -> new Knight(color, row, column);
            case BISHOP -> new Bishop(color, row, column);
            case ROOK -> new Rook(color, row, column);
            case QUEEN -> new Queen(color, row, column);
            case KING -> new King(color, row, column);
        };
    }
}
// Last modified during: feat: Add PieceFactory and SecurityProvider abstractions [DIP]
