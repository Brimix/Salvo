package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.model.Ship;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import com.codeoftheweb.Salvo.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.codeoftheweb.Salvo.util.Util.isGuest;
import static com.codeoftheweb.Salvo.util.Util.makeMap;

@RestController
@RequestMapping("/api")
public class ShipsController {
    //~ Declaration of repositories
    @Autowired
    private PlayerRepository player_rep;
    @Autowired
    private GamePlayerRepository gp_rep;
    @Autowired
    private ShipRepository ship_rep;

    @RequestMapping(path = "games/players/{gamePlayer_id}/ships", method = RequestMethod.POST)
    public ResponseEntity<Object> placeShips(@PathVariable Long gamePlayer_id, @RequestBody List<Ship> ships, Authentication authentication){
        System.out.println("Got into ships!\n");
        if(isGuest(authentication))
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);
        Player player = player_rep.findByEmail(authentication.getName()).orElse(null);
        if(player == null)
            return new ResponseEntity<>(makeMap("error", "Database error. Player not found."), HttpStatus.INTERNAL_SERVER_ERROR);

        GamePlayer gamePlayer = gp_rep.findById(gamePlayer_id).orElse(null);
        if(gamePlayer == null)
            return new ResponseEntity<>(makeMap("error", "Database error. GamePlayer not found."), HttpStatus.INTERNAL_SERVER_ERROR);
        if(player != gamePlayer.getPlayer())
            return new ResponseEntity<>(makeMap("error", "This is not your game!"), HttpStatus.UNAUTHORIZED);

        if(gamePlayer.getShips().size() == 5)
            return new ResponseEntity<>(makeMap("error", "All ships already placed"), HttpStatus.FORBIDDEN);
        if(gamePlayer.getShips().size() + ships.size() > 5)
            return new ResponseEntity<>(makeMap("error", "Too many ships!"), HttpStatus.FORBIDDEN);

        for(Ship ship : ships) gamePlayer.addShip(ship);

        for(String currentType : List.of("carrier", "battleship", "submarine", "destroyer", "patrolboat"))
        if(gamePlayer.getShips().stream().filter(ship -> (ship.getType() == currentType)).count() > 1)
            return new ResponseEntity<>(makeMap("error", "Too many " + currentType + "!"), HttpStatus.FORBIDDEN);

        for(Ship ship : ships) ship_rep.save(ship);

        return new ResponseEntity<>(makeMap("OK", "Ships correctly placed!"), HttpStatus.CREATED);
    }
}
