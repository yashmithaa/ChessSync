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
        
        int direction = (getColor() == PieceColor.BLACK) ? 1 : -1;
        int promotionRow = (getColor() == PieceColor.WHITE) ? 0 : 7;
        
        // One square forward
        int newRow = row + direction;
        if (isWithinBounds(newRow, column) && board.getPiece(newRow, column) == null) {
            Move m1 = new Move(this, row, column, newRow, column);
            if (newRow == promotionRow) {
                m1.setPromotion(true);
            }
            validMoves.add(m1);
            
            // Two squares forward from starting position
            if (!hasMoved()) {
                int doubleRow = row + 2 * direction;
                if (isWithinBounds(doubleRow, column) && board.getPiece(doubleRow, column) == null) {
                    validMoves.add(new Move(this, row, column, doubleRow, column));
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
                    Move m2 = new Move(this, row, column, newRow, newCol, targetPiece);
                    if (newRow == promotionRow) {
                        m2.setPromotion(true);
                    }
                    validMoves.add(m2);
                }
                
                // En passant
                if (targetPiece == null) {
                    Move lastMove = board.getLastMove();
                    if (lastMove != null && lastMove.getPiece().getType() == PieceType.PAWN && lastMove.getPiece().getColor() != this.getColor()) {
                        // Check if the last move was a double step by an adjacent pawn
                        if (Math.abs(lastMove.getSourceRow() - lastMove.getTargetRow()) == 2) {
                            if (lastMove.getTargetColumn() == newCol && lastMove.getTargetRow() == row) {
                                Move epMove = new Move(this, row, column, newRow, newCol, board.getPiece(row, newCol));
                                epMove.setEnPassant(true);
                                validMoves.add(epMove);
                            }
                        }
                    }
                }
            }
        }
        
        return validMoves;
    }

    @Override
    public boolean isValidMove(Board board, int targetRow, int targetColumn) {
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
            
            // En passant
            if (targetPiece == null) {
                Move lastMove = board.getLastMove();
                if (lastMove != null && lastMove.getPiece().getType() == PieceType.PAWN && lastMove.getPiece().getColor() != this.getColor()) {
                    if (Math.abs(lastMove.getSourceRow() - lastMove.getTargetRow()) == 2) {
                        if (lastMove.getTargetColumn() == targetColumn && lastMove.getTargetRow() == row) {
                            return true;
                        }
                    }
                }
            }
        }
        
        return false;
    }
}// Last modified during: feat: Create Piece abstract class and chess piece implementations [major]
