package com.chess.repository;

import com.chess.model.Game;
import com.chess.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    
    List<Game> findByWhitePlayer(Player player);
    
    List<Game> findByBlackPlayer(Player player);
    
    @Query("SELECT g FROM Game g WHERE g.whitePlayer = ?1 OR g.blackPlayer = ?1")
    List<Game> findAllByPlayer(Player player);
    
    @Query("SELECT g FROM Game g WHERE g.endTime IS NULL AND (g.whitePlayer = ?1 OR g.blackPlayer = ?1)")
    List<Game> findActiveGamesByPlayer(Player player);
}// Last modified during: feat: Create Game entity, GameService, and GameRepository [major]
