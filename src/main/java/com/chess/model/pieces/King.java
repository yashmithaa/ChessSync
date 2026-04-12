package com.chess.model.pieces;

import com.chess.model.Board;
import com.chess.model.Move;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(PieceColor color, int row, int column) {
        super(color, row, column);
        this.type = PieceType.KING;
    }

    @Override
    public List<Move> getValidMoves(Board board) {
        List<Move> validMoves = new ArrayList<>();

        // All 8 possible directions for king movement
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},          {0, 1},
            {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = column + dir[1];

            if (isWithinBounds(newRow, newCol)) {
                Piece targetPiece = board.getPiece(newRow, newCol);
                if (targetPiece == null || targetPiece.getColor() != this.getColor()) {
                    validMoves.add(new Move(this, row, column, newRow, newCol, targetPiece));
                }
            }
        }

        // Castling (simplified)
        if (!hasMoved()) {
            checkCastling(board, validMoves, 2);   // Kingside
            checkCastling(board, validMoves, -2);  // Queenside
        }

        return validMoves;
    }

    private void checkCastling(Board board, List<Move> validMoves, int direction) {
        int rookCol = (direction > 0) ? 7 : 0;
        Piece rookPiece = board.getPiece(row, rookCol);

        if (rookPiece instanceof Rook && !rookPiece.hasMoved() && rookPiece.getColor() == this.getColor()) {
            boolean pathClear = true;
            int start = Math.min(column, rookCol) + 1;
            int end = Math.max(column, rookCol);

            for (int col = start; col < end; col++) {
                if (board.getPiece(row, col) != null) {
                    pathClear = false;
                    break;
                }
            }

            if (pathClear) {
                // In full implementation, also check if passing squares are under attack
                int targetCol = column + direction;
                validMoves.add(new Move(this, row, column, row, targetCol, null, true));
            }
        }
    }

    @Override
    public boolean isValidMove(Board board, int targetRow, int targetColumn) {
        int rowDiff = Math.abs(targetRow - row);
        int colDiff = Math.abs(targetColumn - column);

        // Standard one-square king movement
        if (rowDiff <= 1 && colDiff <= 1) {
            Piece targetPiece = board.getPiece(targetRow, targetColumn);
            return targetPiece == null || targetPiece.getColor() != this.getColor();
        }

        // Castling (simplified)
        if (!hasMoved() && row == targetRow && Math.abs(targetColumn - column) == 2) {
            int rookCol = (targetColumn > column) ? 7 : 0;
            Piece rookPiece = board.getPiece(row, rookCol);

            if (rookPiece instanceof Rook && !rookPiece.hasMoved() && rookPiece.getColor() == this.getColor()) {
                boolean pathClear = true;
                int start = Math.min(column, rookCol) + 1;
                int end = Math.max(column, rookCol);

                for (int col = start; col < end; col++) {
                    if (board.getPiece(row, col) != null) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }
}
// Last modified during: feat: Create Piece abstract class and chess piece implementations [major]
