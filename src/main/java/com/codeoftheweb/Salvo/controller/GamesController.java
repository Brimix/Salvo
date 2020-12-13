package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.dto.GameDTO;
import com.codeoftheweb.Salvo.dto.GamePlayerDTO;
import com.codeoftheweb.Salvo.dto.PlayerDTO;
import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.GameRepository;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.codeoftheweb.Salvo.util.Util.isGuest;
import static com.codeoftheweb.Salvo.util.Util.makeMap;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class GamesController {
    //~ Declaration of repositories
    @Autowired
    private PlayerRepository player_rep;
    @Autowired
    private GameRepository game_rep;
    @Autowired
    private GamePlayerRepository gp_rep;

    //~ Method to retrieve games data for page view.
    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public Map<String, Object> getGamesData(Authentication authentication) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("player",
                !isGuest(authentication) ?
                new PlayerDTO(player_rep.findByEmail(authentication.getName()).get()) :
                "Guest");
        data.put("games", game_rep.findAll().stream()
                .map(g -> new GameDTO(g))
                .collect(toList()));
        return data;
    }

    //~ Method to create a new game
    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> create(Authentication authentication) {
        if(isGuest(authentication))
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);

        Player player = player_rep.findByEmail(authentication.getName()).orElse(null);
        if(player == null)
            return new ResponseEntity<>(makeMap("error", "Database error. Player not found."), HttpStatus.INTERNAL_SERVER_ERROR);

        Game game = new Game();
        GamePlayer gamePlayer = new GamePlayer(player, game);
        game_rep.save(game);
        gp_rep.save(gamePlayer);
        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }

    //~ Method to join an existing game
    @RequestMapping(path = "/game/{game_id}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> join(@PathVariable Long game_id,  Authentication authentication) {
        if(isGuest(authentication))
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);

        Player player = player_rep.findByEmail(authentication.getName()).orElse(null);
        if(player == null)
            return new ResponseEntity<>(makeMap("error", "Database error. Player not found."), HttpStatus.INTERNAL_SERVER_ERROR);

        Game game = game_rep.findById(game_id).orElse(null);
        if(game == null)
            return new ResponseEntity<>(makeMap("error", "Game not found."), HttpStatus.FORBIDDEN);

        if (game.getPlayers().contains(player))
            return new ResponseEntity<>(makeMap("error", "You are already in the game!"), HttpStatus.FORBIDDEN);

        if(game.getPlayers().size() >= 2)
            return new ResponseEntity<>(makeMap("error", "Game is full!"), HttpStatus.FORBIDDEN);

        GamePlayer gamePlayer = new GamePlayer(player, game);
        if(gamePlayer == null)
            return new ResponseEntity<>(makeMap("error", "Database error. Couldn't create GamePlayer."), HttpStatus.INTERNAL_SERVER_ERROR);

        gp_rep.save(gamePlayer);
        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }
}
