package com.chess.model;

import com.chess.model.pieces.PieceColor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "white_player_id")
    private Player whitePlayer;

    @ManyToOne
    @JoinColumn(name = "black_player_id")
    private Player blackPlayer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameMode gameMode;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private PieceColor winnerColor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status = GameStatus.NOT_STARTED;

    @Transient
    private Board board;

    @Transient
    private List<Move> moveHistory;

    @Transient
    private PieceColor currentTurn;

    @Column(columnDefinition = "TEXT")
    private String serializedMoves;

    public Game(Player whitePlayer, Player blackPlayer, GameMode gameMode) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.gameMode = gameMode;
        this.startTime = LocalDateTime.now();
        this.board = new Board();
        this.moveHistory = new ArrayList<>();
        this.currentTurn = PieceColor.WHITE;
        this.status = GameStatus.IN_PROGRESS;
    }

    public boolean makeMove(Move move) {
        if (move.getPiece().getColor() != currentTurn) {
            return false;
        }

        if (!move.getPiece().isValidMove(board, move.getTargetRow(), move.getTargetColumn())) {
            return false;
        }

        board.executeMove(move);
        moveHistory.add(move);
        currentTurn = (currentTurn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

        return true;
    }

    public void endGame(PieceColor winner) {
        this.endTime = LocalDateTime.now();
        this.winnerColor = winner;
        this.status = GameStatus.FINISHED;
    }

    public void markAsDraw() {
        this.endTime = LocalDateTime.now();
        this.winnerColor = null;
        this.status = GameStatus.DRAW;
    }

    public void serializeMoves() {
        if (moveHistory != null && !moveHistory.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Move move : moveHistory) {
                sb.append(move.toString()).append(";");
            }
            this.serializedMoves = sb.toString();
        }
    }

    public void deserializeMoves() {
        if (serializedMoves != null && !serializedMoves.isEmpty()) {
            String[] moveStrings = serializedMoves.split(";");
            moveHistory = new ArrayList<>();
            // TODO: Add logic to parse move strings into Move objects
        }
    }
}
// Last modified during: feat: Create Game entity, GameService, and GameRepository [major]
