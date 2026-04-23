package com.chess.model;

import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceType;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;

@Getter
@Setter
public class Move implements Serializable {
    private static final long serialVersionUID = 1L;

    private Piece piece;
    private int sourceRow;
    private int sourceColumn;
    private int targetRow;
    private int targetColumn;
    private Piece capturedPiece;
    private boolean isCastling;
    private boolean isEnPassant;
    private boolean isPromotion;
    private PieceType promotionPieceType;

    public Move(Piece piece, int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        this.piece = piece;
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
    }

    public Move(Piece piece, int sourceRow, int sourceColumn, int targetRow, int targetColumn, Piece capturedPiece) {
        this(piece, sourceRow, sourceColumn, targetRow, targetColumn);
        this.capturedPiece = capturedPiece;
    }

    public Move(Piece piece, int sourceRow, int sourceColumn, int targetRow, int targetColumn,
                Piece capturedPiece, boolean isCastling) {
        this(piece, sourceRow, sourceColumn, targetRow, targetColumn, capturedPiece);
        this.isCastling = isCastling;
    }

    // ADD THESE BELOW CONSTRUCTORS
    public int getToRow() {
        return targetRow;
    }

    public int getToCol() {
        return targetColumn;
    }

    @Override
    public String toString() {
        char fromFile = (char) ('a' + sourceColumn);
        int fromRank = 8 - sourceRow;
        char toFile = (char) ('a' + targetColumn);
        int toRank = 8 - targetRow;

        return String.format("%c%d-%c%d", fromFile, fromRank, toFile, toRank);
    }
}
// Last modified during: feat: Implement Board model with move state management [minor]
