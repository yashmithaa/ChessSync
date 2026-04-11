package com.chess.model.pieces.factory;

import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceColor;
import com.chess.model.pieces.PieceType;

/**
 * Dependency Inversion Principle (DIP):
 * Interface for creating pieces. High-level modules depend on this abstraction,
 * not on concrete piece implementations (Pawn, Knight, Bishop, etc.).
 * 
 * This allows switching between different piece creation strategies without
 * modifying the Board class.
 * 
 * Benefits:
 * - Board class depends on abstraction (PieceFactory), not concrete pieces
 * - Can swap implementations (e.g., StandardPieceFactory, VariantPieceFactory)
 * - Follows DIP: depend on abstractions, not concretions
 */
public interface PieceFactory {
    /**
     * Create a piece of the specified type at the given position
     * 
     * @param type The type of piece to create
     * @param color The color of the piece
     * @param row The row position
     * @param column The column position
     * @return A new Piece instance
     */
    Piece createPiece(PieceType type, PieceColor color, int row, int column);
}
// Last modified during: feat: Add PieceFactory and SecurityProvider abstractions [DIP]
