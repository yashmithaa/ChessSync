package com.chess.util;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.pieces.King;
import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceColor;

public class MoveValidator {
    
    // Validate if a move is legal (including checking for check)
    public static boolean isLegalMove(Board board, Move move) {
        // First check if the move is valid according to the piece's movement rules
        if (!move.getPiece().isValidMove(board, move.getTargetRow(), move.getTargetColumn())) {
            return false;
        }
        
        // Now check if the move would leave the king in check
        return !wouldBeInCheck(board, move);
    }
    
    // Check if making the move would leave or put the player's king in check
    private static boolean wouldBeInCheck(Board board, Move move) {
        // Clone board to simulate the move
        Board tempBoard = board.clone();
        
        // Execute the move on the cloned board
        tempBoard.executeMove(move);
        
        // Check if the player's king is in check after the move
        return tempBoard.isInCheck(move.getPiece().getColor());
    }
    
    // Check if a player is in checkmate
    public static boolean isCheckmate(Board board, PieceColor color) {
        // First, check if the king is in check
        if (!board.isInCheck(color)) {
            return false;
        }
        
        // Check if any legal move exists that would get out of check
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() == color) {
                    for (Move move : piece.getValidMoves(board)) {
                        if (isLegalMove(board, move)) {
                            return false; // Found a legal move, not checkmate
                        }
                    }
                }
            }
        }
        
        // No legal moves found to get out of check
        return true;
    }
    
    // Check if a player is in stalemate (not in check but no legal moves)
    public static boolean isStalemate(Board board, PieceColor color) {
        // If the king is in check, it's not stalemate
        if (board.isInCheck(color)) {
            return false;
        }
        
        // Check if any legal move exists
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() == color) {
                    for (Move move : piece.getValidMoves(board)) {
                        if (isLegalMove(board, move)) {
                            return false; // Found a legal move, not stalemate
                        }
                    }
                }
            }
        }
        
        // No legal moves found, but not in check
        return true;
    }
}// Last modified during: feat: Add REST API controllers and endpoints [minor]
