package com.chess.model.pieces.factory;

import com.chess.model.pieces.*;
import com.chess.model.pieces.decorator.CastlingDecorator;
import com.chess.model.pieces.decorator.PromotionDecorator;

/**
 * Dependency Inversion Principle (DIP) + Decorator Pattern:
 * Enhanced implementation of PieceFactory that wraps pieces with decorators
 * to add special move capabilities (castling, promotion).
 * 
 * This shows how DIP enables flexible composition of behaviors.
 */
public class DecoratedPieceFactory implements PieceFactory {

    @Override
    public Piece createPiece(PieceType type, PieceColor color, int row, int column) {
        Piece piece = switch (type) {
            case PAWN -> new Pawn(color, row, column);
            case KNIGHT -> new Knight(color, row, column);
            case BISHOP -> new Bishop(color, row, column);
            case ROOK -> new Rook(color, row, column);
            case QUEEN -> new Queen(color, row, column);
            case KING -> new King(color, row, column);
        };

        // Enhance with special move decorators
        if (type == PieceType.PAWN) {
            piece = new PromotionDecorator(piece);
        } else if (type == PieceType.KING) {
            piece = new CastlingDecorator(piece);
        }

        return piece;
    }
}
// Last modified during: feat: Add PieceFactory and SecurityProvider abstractions [DIP]
