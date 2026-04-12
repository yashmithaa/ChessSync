package com.chess.model.pieces;

import com.chess.model.Board;
import com.chess.model.Move;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(PieceColor color, int row, int column) {
        super(color, row, column);
        this.type = PieceType.PAWN;
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();
        
        // Direction: 1 for black (moving down), -1 for white (moving up)
        int direction = (getColor() == PieceColor.BLACK) ? 1 : -1;
        
        // One square forward
        int newRow = row + direction;
        if (isWithinBounds(newRow, column) && board.getPiece(newRow, column) == null) {
            validMoves.add(new Move(this, row, column, newRow, column));
            
            // Two squares forward from starting position
            if (!hasMoved()) {
                newRow = row + 2 * direction;
                if (isWithinBounds(newRow, column) && board.getPiece(newRow, column) == null &&
                    board.getPiece(row + direction, column) == null) {
                    validMoves.add(new Move(this, row, column, newRow, column));
                }
            }
        }
        
        // Capture diagonally
        for (int colOffset : new int[]{-1, 1}) {
            newRow = row + direction;
            int newCol = column + colOffset;
            
            if (isWithinBounds(newRow, newCol)) {
                Piece targetPiece = board.getPiece(newRow, newCol);
                if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                    validMoves.add(new Move(this, row, column, newRow, newCol, targetPiece));
                }
                
                // En passant (would need game state to track previous moves)
                // Simplified implementation for now
            }
        }
        
        return validMoves;
    }

    @Override
    public boolean isValidMove(Board board, int targetRow, int targetColumn) {
        // Direction: 1 for black (moving down), -1 for white (moving up)
        int direction = (getColor() == PieceColor.BLACK) ? 1 : -1;
        
        // Moving straight
        if (column == targetColumn) {
            // One square forward
            if (targetRow == row + direction && board.getPiece(targetRow, targetColumn) == null) {
                return true;
            }
            
            // Two squares forward from starting position
            if (!hasMoved() && targetRow == row + 2 * direction && 
                board.getPiece(row + direction, column) == null && 
                board.getPiece(targetRow, targetColumn) == null) {
                return true;
            }
        } 
        // Capturing diagonally
        else if (Math.abs(targetColumn - column) == 1 && targetRow == row + direction) {
            Piece targetPiece = board.getPiece(targetRow, targetColumn);
            if (targetPiece != null && targetPiece.getColor() != this.getColor()) {
                return true;
            }
            
            // En passant logic would go here
        }
        
        return false;
    }
}// Last modified during: feat: Create Piece abstract class and chess piece implementations [major]
