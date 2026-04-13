package com.chess.model.pieces;

import com.chess.model.Board;
import com.chess.model.Move;
import java.util.List;

/**
 * Interface Segregation Principle (ISP):
 * Splits piece responsibilities - this interface only handles move validation.
 * Pieces that only need move validation don't need to implement move generation.
 */
public interface MoveValidator {
    /**
     * Check if a specific move is valid for this piece
     */
    boolean isValidMove(Board board, int targetRow, int targetColumn);
}
// Last modified during: feat: Implement MoveValidator and MoveProvider interfaces [ISP]
