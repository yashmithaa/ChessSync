package com.chess.model.pieces;

import com.chess.model.Board;
import com.chess.model.Move;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract base class for chess pieces.
 * Implements both MoveProvider and MoveValidator interfaces to satisfy
 * Interface Segregation Principle (ISP) by explicitly declaring what
 * responsibilities each piece has.
 */
public abstract class Piece implements Serializable, MoveProvider, MoveValidator {
    private static final long serialVersionUID = 1L;

    protected PieceType type;
    protected PieceColor color;
    protected int row;
    protected int column;
    private boolean hasMoved = false;

    public Piece(PieceColor color, int row, int column) {
        this.color = color;
        this.row = row;
        this.column = column;
    }

    // Custom getter and setter for hasMoved
    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    // Method to get all valid moves for this piece on the given board
    public abstract List<Move> getValidMoves(Board board);

    // Method to check if a move is valid for this piece
    public abstract boolean isValidMove(Board board, int targetRow, int targetColumn);

    // Common utility method to check if a position is within board boundaries
    protected boolean isWithinBounds(int row, int column) {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

    // Update piece position after a move
    public void move(int targetRow, int targetColumn) {
        this.row = targetRow;
        this.column = targetColumn;
        this.hasMoved = true;
    }

    @Override
    public String toString() {
        return color.toString().charAt(0) + getType().toString().substring(0, 1);
    }

    // Getters and setters for other fields (optional if needed)
    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public PieceColor getColor() {
        return color;
    }

    public void setColor(PieceColor color) {
        this.color = color;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * DESIGN PATTERNS AND PRINCIPLES IMPLEMENTED:
     * 
     * 1. INTERFACE SEGREGATION PRINCIPLE (ISP):
     *    - This class implements both MoveProvider and MoveValidator interfaces
     *    - Clients can depend on just the interface they need (not all methods)
     *    - Example: A UI component checking validity only depends on MoveValidator
     *    - vs. A move generator only depends on MoveProvider
     *    - This reduces dependencies between components
     * 
     * 2. LISKOV SUBSTITUTION PRINCIPLE (LSP):
     *    - All concrete pieces (Pawn, Knight, Bishop, Rook, Queen, King) are substitutable
     *    - Each subclass faithfully implements the contract defined in Piece
     *    - move() method works the same way for all piece types
     *    - getValidMoves() and isValidMove() follow the same semantics
     *    - No subclass violates the assumptions of the parent class
     *    - Code using Piece references can safely use any concrete piece type
     * 
     * 3. DECORATOR PATTERN:
     *    - See com.chess.model.pieces.decorator.PieceDecorator
     *    - Pieces can be wrapped with decorators to add special moves
     *    - Examples: CastlingDecorator for kings, PromotionDecorator for pawns
     *    - Allows dynamic composition of behaviors without subclassing
     * 
     * 4. DEPENDENCY INVERSION PRINCIPLE (DIP):
     *    - See com.chess.model.pieces.factory.PieceFactory
     *    - Board depends on PieceFactory abstraction, not concrete pieces
     *    - Allows switching between different piece creation strategies
     *    - Example: StandardPieceFactory vs DecoratedPieceFactory
     */
}
// Last modified during: feat: Create Piece abstract class and chess piece implementations [major]
// Last modified during: feat: Apply Open/Closed Principle to Piece class hierarchy [OCP]
