package com.chess.model.pieces;

import com.chess.model.Board;
import com.chess.model.Move;
import java.util.List;

/**
 * Interface Segregation Principle (ISP):
 * Splits piece responsibilities - this interface only handles move generation.
 * Pieces that can generate moves implement this interface separately.
 */
public interface MoveProvider {
    /**
     * Get all valid moves for this piece on the given board
     */
    List<Move> getValidMoves(Board board);
}
// Last modified during: feat: Implement MoveValidator and MoveProvider interfaces [ISP]
