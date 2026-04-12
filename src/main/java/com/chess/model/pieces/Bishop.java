package com.chess.model.pieces;

import com.chess.model.Board;
import com.chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(PieceColor color, int row, int column) {
        super(color, row, column);
        this.type = PieceType.BISHOP;
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        
        // Diagonal directions: up-left, up-right, down-left, down-right
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        
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
        
        // Check if move is diagonal
        if (rowDiff == colDiff) {
            // Determine direction
            int rowDir = (targetRow > row) ? 1 : -1;
            int colDir = (targetColumn > column) ? 1 : -1;
            
            // Check all squares in between for obstacles
            for (int i = 1; i < rowDiff; i++) {
                int checkRow = row + i * rowDir;
                int checkCol = column + i * colDir;
                
                if (board.getPiece(checkRow, checkCol) != null) {
                    return false;  // Path is blocked
                }
            }
            
            // Check destination square
            Piece targetPiece = board.getPiece(targetRow, targetColumn);
            return targetPiece == null || targetPiece.getColor() != this.getColor();
        }
        
        return false;
    }
}// Last modified during: feat: Create Piece abstract class and chess piece implementations [major]
