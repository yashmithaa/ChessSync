package com.chess.model.pieces;

import com.chess.model.Board;
import com.chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(PieceColor color, int row, int column) {
        super(color, row, column);
        this.type = PieceType.QUEEN;
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        
        // All eight directions: horizontal, vertical, and diagonal
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // Rook-like moves
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // Bishop-like moves
        };
        
        for (int[] direction : directions) {
            int rowDir = direction[0];
            int colDir = direction[1];
            
            for (int i = 1; i < 8; i++) {
                int newRow = row + i * rowDir;
                int newCol = column + i * colDir;
                
                if (!isWithinBounds(newRow, newCol)) {
                    break;  // Out of board bounds
                }
                
                Piece targetPiece = board.getPiece(newRow, newCol);
                
                if (targetPiece == null) {
                    // Empty square, can move
                    validMoves.add(new Move(this, row, column, newRow, newCol));
                } else {
                    // Square is occupied
                    if (targetPiece.getColor() != this.getColor()) {
                        // Can capture opponent's piece
                        validMoves.add(new Move(this, row, column, newRow, newCol, targetPiece));
                    }
                    break;  // Cannot go further in this direction
                }
            }
        }
        
        return validMoves;
    }

    @Override
    public boolean isValidMove(Board board, int targetRow, int targetColumn) {
        int rowDiff = Math.abs(targetRow - row);
        int colDiff = Math.abs(targetColumn - column);
        
        // Queen moves like a rook or a bishop
        boolean isRookMove = (row == targetRow || column == targetColumn);
        boolean isBishopMove = (rowDiff == colDiff);
        
        if (!isRookMove && !isBishopMove) {
            return false;
        }
        
        // Determine direction
        int rowDir = Integer.compare(targetRow, row);
        int colDir = Integer.compare(targetColumn, column);
        
        // Check all squares in between for obstacles
        int checkRow = row + rowDir;
        int checkCol = column + colDir;
        
        while (checkRow != targetRow || checkCol != targetColumn) {
            if (board.getPiece(checkRow, checkCol) != null) {
                return false;  // Path is blocked
            }
            checkRow += rowDir;
            checkCol += colDir;
        }
        
        // Check destination square
        Piece targetPiece = board.getPiece(targetRow, targetColumn);
        return targetPiece == null || targetPiece.getColor() != this.getColor();
    }
}// Last modified during: feat: Create Piece abstract class and chess piece implementations [major]
