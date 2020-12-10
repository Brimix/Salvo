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
import java.util.Set;

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

        for(Ship ship : ships)
            for(String location : ship.getLocations())
                if(illegalLocation(location))
                    return new ResponseEntity<>(makeMap("error", "Invalid location!"), HttpStatus.UNAUTHORIZED);

        if(gamePlayer.getShips().size() == 5)
            return new ResponseEntity<>(makeMap("error", "All ships already placed."), HttpStatus.FORBIDDEN);

        Set<Ship> newShips = gamePlayer.getShips(); newShips.addAll(ships);
        if(newShips.size() > 5)
            return new ResponseEntity<>(makeMap("error", "Too many ships!"), HttpStatus.FORBIDDEN);

        for(String currentType : List.of("carrier", "battleship", "submarine", "destroyer", "patrolboat"))
            if(newShips.stream().filter(ship -> (ship.getType() == currentType)).count() > 1)
                return new ResponseEntity<>(makeMap("error", "Too many " + currentType + "!"), HttpStatus.FORBIDDEN);

        for(Ship ship1 : newShips)for(Ship ship2 : newShips) {
            if(ship1 == ship2) continue;
            if(overlapLocations(ship1, ship2))
                return new ResponseEntity<>(makeMap("error", "Ships overlap!"), HttpStatus.FORBIDDEN);
        }

        for(Ship ship : ships) gamePlayer.addShip(ship);
        ship_rep.saveAll(ships);
        return new ResponseEntity<>(makeMap("OK", "Ships correctly placed!"), HttpStatus.CREATED);
    }

    private boolean illegalLocation(String location){
        if(location.length() < 2) return true;
        char row = location.charAt(0);
        int col = Integer.parseInt(location.substring(1));
        if(row < 'A' || 'J' < row) return true;
        if(col < 1 || 10 < col) return true;
        return false;
    }

    private boolean overlapLocations(Ship ship1, Ship ship2){
        for(String location : ship1.getLocations())
            if(ship2.getLocations().contains(location))
                return true;
        return false;
    }
}
