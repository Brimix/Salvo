package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.GameRepository;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.codeoftheweb.Salvo.util.Util.isGuest;
import static com.codeoftheweb.Salvo.util.Util.makeMap;

@RestController
@RequestMapping("/api")
public class GamesController {
    // Declaration of repositories
    @Autowired
    private PlayerRepository player_rep;
    @Autowired
    private GameRepository game_rep;
    @Autowired
    private GamePlayerRepository gp_rep;

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> Create(Authentication authentication) {
        if(isGuest(authentication))
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.FORBIDDEN);

        Player player = player_rep.findByEmail(authentication.getName());
        Game game = new Game();
        GamePlayer gamePlayer = new GamePlayer(player, game);

        game_rep.save(game);
        gp_rep.save(gamePlayer);
        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/game/{game_id}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> Join(@PathVariable Long game_id,  Authentication authentication) {
        if(isGuest(authentication))
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.FORBIDDEN);

        Player player = player_rep.findByEmail(authentication.getName());

        Game game = game_rep.findById(game_id).orElse(null);
        if(game == null)
            return new ResponseEntity<>(makeMap("error", "Game not found."), HttpStatus.FORBIDDEN);

        if (game.getPlayers().contains(player))
            return new ResponseEntity<>(makeMap("error", "You're already in the game!"), HttpStatus.FORBIDDEN);

        if(game.getPlayers().size() >= 2)
            return new ResponseEntity<>(makeMap("error", "Game is full!"), HttpStatus.FORBIDDEN);

        GamePlayer gamePlayer = new GamePlayer(player, game);
        if(gamePlayer == null)
            return new ResponseEntity<>(makeMap("error", "Couldn't create GamePlayer."), HttpStatus.FORBIDDEN);

        gp_rep.save(gamePlayer);
        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }
}
