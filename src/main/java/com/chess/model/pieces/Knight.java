package com.chess.model.pieces;

import com.chess.model.Board;
import com.chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    private static final int[][] KNIGHT_MOVES = {
        {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}
    };
    
    public Knight(PieceColor color, int row, int column) {
        super(color, row, column);
        this.type = PieceType.KNIGHT;
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        
        for (int[] move : KNIGHT_MOVES) {
            int newRow = row + move[0];
            int newCol = column + move[1];
            
            if (isWithinBounds(newRow, newCol)) {
                Piece targetPiece = board.getPiece(newRow, newCol);
                
                // Empty square or capture opponent's piece
                if (targetPiece == null || targetPiece.getColor() != this.getColor()) {
                    validMoves.add(new Move(this, row, column, newRow, newCol, targetPiece));
                }
            }
        }
        
        return validMoves;
    }

    @Override
    public boolean isValidMove(Board board, int targetRow, int targetColumn) {
        // Knights move in an L-shape: 2 squares in one direction and 1 square perpendicular
        int rowDiff = Math.abs(targetRow - row);
        int colDiff = Math.abs(targetColumn - column);
        
        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            Piece targetPiece = board.getPiece(targetRow, targetColumn);
            return targetPiece == null || targetPiece.getColor() != this.getColor();
        }
        
        return false;
    }
}// Last modified during: feat: Create Piece abstract class and chess piece implementations [major]
