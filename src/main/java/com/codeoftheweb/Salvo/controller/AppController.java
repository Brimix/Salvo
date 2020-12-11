package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.*;
import com.codeoftheweb.Salvo.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.codeoftheweb.Salvo.util.Util.isGuest;
import static com.codeoftheweb.Salvo.util.Util.makeMap;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AppController {
    //~ Declaration of repositories
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

    // Methods to get JSON for each of the classes
    @RequestMapping("/playersInfo")
    public List<Map<String, Object>> getAllPlayers() {
        return player_rep.findAll().stream()
                .map(p -> PlayerDTO.makeDTO(p))
                .collect(toList());
    }
    @RequestMapping("/gamesInfo")
    public List<Map<String, Object>> getAllGames(){
        return game_rep.findAll().stream()
                .map(g -> GameDTO.makeDTO(g))
                .collect(toList());
    }
    @RequestMapping("/gamePlayersInfo")
    public List<Map<String, Object>> getAllGamePlayers() {
        return gp_rep.findAll().stream()
                .map(gp -> GamePlayerDTO.makeDTO(gp))
                .collect(toList());
    }
    @RequestMapping("/shipsInfo")
    public List<Map<String, Object>> getAllShips() {
        return ship_rep.findAll().stream()
                .map(s -> ShipDTO.makeDTO(s))
                .collect(toList());
    }
    @RequestMapping("/salvoesInfo")
    public List<Map<String, Object>> getAllSalvoes() {
        return salvo_rep.findAll().stream()
                .map(s -> SalvoDTO.makeDTO(s))
                .collect(toList());
    }
    @RequestMapping("/scoresInfo")
    public List<Map<String, Object>> getAllScores() {
        return score_rep.findAll().stream()
                .map(s -> ScoreDTO.makeDTO(s))
                .collect(toList());
    }

    //~ Method to retrieve scores data to create the Leaderboard
    @RequestMapping("/leaderBoard")
    public List<Map<String, Object>> getLeaderBoard() {
        return player_rep.findAll().stream().sorted(Comparator
                .comparingDouble(Player::getTotal)
                .reversed()
                )
                .map(p -> PlayerDTO.PlayerScoreDTO(p))
                .collect(toList());
    }

    //~ Method to retrieve the game data which is shown to a player
//    @RequestMapping(path = "/game_view/{gamePlayer_id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getGameView(@PathVariable Long gamePlayer_id, Authentication authentication) {
        if(isGuest(authentication))
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);
        Player player = player_rep.findByEmail(authentication.getName()).orElse(null);
        if(player == null)
            return new ResponseEntity<>(makeMap("error", "Database error. Player not found."), HttpStatus.INTERNAL_SERVER_ERROR);

        GamePlayer gamePlayer = gp_rep.findById(gamePlayer_id).orElse(null);
        if(gamePlayer == null)
            return new ResponseEntity<>(makeMap("error", "GamePlayer not found."), HttpStatus.FORBIDDEN);
        if(player != gamePlayer.getPlayer())
            return new ResponseEntity<>(makeMap("error", "This is not your game!"), HttpStatus.UNAUTHORIZED);;

        return new ResponseEntity<>(GamePlayerDTO.gameView(gamePlayer), HttpStatus.ACCEPTED);
    }

    //~ Auxiliary Game View for testing new Front End
    @RequestMapping(path = "/game_view/{gameplayer_id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getGameUltimateView(@PathVariable Long gameplayer_id, Authentication authentication) {
        if(isGuest(authentication))
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);
        Player player = player_rep.findByEmail(authentication.getName()).orElse(null);
        if(player == null)
            return new ResponseEntity<>(makeMap("error", "Database error. Player not found."), HttpStatus.INTERNAL_SERVER_ERROR);

        GamePlayer gamePlayer = gp_rep.findById(gameplayer_id).orElse(null);
        if(gamePlayer == null)
            return new ResponseEntity<>(makeMap("error", "Game not found."), HttpStatus.FORBIDDEN);
        if(player != gamePlayer.getPlayer())
            return new ResponseEntity<>(makeMap("error", "This is not your game!"), HttpStatus.UNAUTHORIZED);;


        return new ResponseEntity<>(GamePlayerDTO.gameUltimateView(gamePlayer), HttpStatus.ACCEPTED);
    }
}
