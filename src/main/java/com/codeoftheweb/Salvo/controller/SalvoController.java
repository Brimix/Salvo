package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.repository.*;
import com.codeoftheweb.Salvo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {
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
    @RequestMapping("/games")
    public List<Map<String, Object>> getAllGames(){
        return game_rep.findAll().stream()
                .map(g -> GameDTO.makeDTO(g))
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
    @RequestMapping("/game_view/{gamePlayer_id}")
    public Map<String, Object> getGameView(@PathVariable Long gamePlayer_id) {
        GamePlayer gp_buscado = gp_rep.findById(gamePlayer_id).get();
        return GamePlayerDTO.gameView(gp_buscado);
    }

    // Game View for Task 4
    @RequestMapping("/game_fullview/{gameplayer_id}")
    public Map<String, Object> getGameFullView(@PathVariable Long gameplayer_id) {
        return GamePlayerDTO.gameFullView(gp_rep.findById(gameplayer_id).get());
    }

    // Leaderboard for Task 5
    @RequestMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderBoard() {
        return player_rep.findAll().stream()
                .map(p -> PlayerDTO.PlayerScoreDTO(p))
                .collect(toList());
    }
}
