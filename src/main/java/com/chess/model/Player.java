package com.chess.model;

import com.chess.model.pieces.PieceColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String email;
    
    private int rating;
    
    private int gamesPlayed;
    
    private int gamesWon;
    
    @Transient
    private PieceColor color;
    
    public Player(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.rating = 1200; // Default rating
        this.gamesPlayed = 0;
        this.gamesWon = 0;
    }
}

// Last modified during: feat: Create Player entity and AuthService foundation [major]
