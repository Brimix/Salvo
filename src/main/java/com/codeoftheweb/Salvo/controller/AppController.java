package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.*;
import com.codeoftheweb.Salvo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codeoftheweb.Salvo.util.Util.isGuest;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AppController {
    // Declaration of repositories
    @Autowired
    private PlayerRepository player_rep;
    @Autowired
    private GameRepository game_rep;
    @Autowired
    private GamePlayerRepository gp_rep;
    @Autowired
    private ShipRepository ship_rep;
    @Autowired
    private SalvoRepository salvo_rep;
    @Autowired
    private ScoreRepository score_rep;

    // Api to get JSON for each of the classes
    @RequestMapping("/players")
    public List<Map<String, Object>> getAllPlayers() {
        return player_rep.findAll().stream()
                .map(p -> PlayerDTO.makeDTO(p))
                .collect(toList());
    }

    @RequestMapping("/gamePlayers")
    public List<Map<String, Object>> getAllGamePlayers() {
        return gp_rep.findAll().stream()
                .map(gp -> GamePlayerDTO.makeDTO(gp))
                .collect(toList());
    }
    @RequestMapping("/ships")
    public List<Map<String, Object>> getAllShips() {
        return ship_rep.findAll().stream()
                .map(s -> ShipDTO.makeDTO(s))
                .collect(toList());
    }
    @RequestMapping("/salvoes")
    public List<Map<String, Object>> getAllSalvoes() {
        return salvo_rep.findAll().stream()
                .map(s -> SalvoDTO.makeDTO(s))
                .collect(toList());
    }
    @RequestMapping("/scores")
    public List<Map<String, Object>> getAllScores() {
        return score_rep.findAll().stream()
                .map(s -> ScoreDTO.makeDTO(s))
                .collect(toList());
    }

    // Game View for Task 3
//    @RequestMapping("/game_view/{gamePlayer_id}")
//    public Map<String, Object> getGameView(@PathVariable Long gamePlayer_id) {
//        GamePlayer gp = gp_rep.findById(gamePlayer_id).get();
//        return GamePlayerDTO.gameView(gp);
//    }

    // Game View for Task 4
//    @RequestMapping("/game_fullview/{gameplayer_id}")

    // Leaderboard for Task 5
    @RequestMapping("/leaderBoard")
    public List<Map<String, Object>> getLeaderboard() {
        return player_rep.findAll().stream().sorted(Comparator
                .comparingDouble(Player::getTotal)
                .reversed()
                )
                .map(p -> PlayerDTO.PlayerScoreDTO(p))
                .collect(toList());
    }

    public List<Map<String, Object>> getAllGames(){
        return game_rep.findAll().stream()
                .map(g -> GameDTO.makeDTO(g))
                .collect(toList());
    }
}
