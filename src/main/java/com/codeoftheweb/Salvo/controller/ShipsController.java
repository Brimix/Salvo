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

import java.util.*;

import static com.codeoftheweb.Salvo.util.Util.*;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static java.util.stream.Collectors.*;

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
        // Player authentication
        if(isGuest(authentication))
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);
        Player player = player_rep.findByEmail(authentication.getName()).orElse(null);
        if(player == null)
            return new ResponseEntity<>(makeMap("error", "Database error. Player not found."), HttpStatus.INTERNAL_SERVER_ERROR);

        // Get gamePlayer and check permissions
        GamePlayer gamePlayer = gp_rep.findById(gamePlayer_id).orElse(null);
        if(gamePlayer == null)
            return new ResponseEntity<>(makeMap("error", "Database error. GamePlayer not found."), HttpStatus.INTERNAL_SERVER_ERROR);
        if(player != gamePlayer.getPlayer())
            return new ResponseEntity<>(makeMap("error", "This is not your game!"), HttpStatus.UNAUTHORIZED);
        if(gamePlayer.getShips().size() == 5)
            return new ResponseEntity<>(makeMap("error", "All ships already placed."), HttpStatus.FORBIDDEN);

        // Validate the format of each received ship
        for(Ship ship : ships){
            String status = shipValidity(ship);
            if(status != "OK")
                return new ResponseEntity<>(makeMap("error", "Invalid ship! " + status), HttpStatus.FORBIDDEN);
        }

        // Validate the number of ships to place
        Set<Ship> newShips = gamePlayer.getShips(); newShips.addAll(ships);
        if(newShips.size() > 5)
            return new ResponseEntity<>(makeMap("error", "Too many ships!"), HttpStatus.FORBIDDEN);
        for(String currentType : shipTypes.keySet())
            if (newShips.stream().filter(ship -> (ship.getType().equals(currentType))).count() > 1)
                return new ResponseEntity<>(makeMap("error", "Too many " + currentType + "!"), HttpStatus.FORBIDDEN);

        // Check that ships don't overlap
        for(Ship ship1 : newShips)for(Ship ship2 : newShips)if(ship1 != ship2)
            if(overlapShips(ship1, ship2))
                return new ResponseEntity<>(makeMap("error", "Ships overlap!"), HttpStatus.FORBIDDEN);

        // All is fine!
        for(Ship ship : ships) gamePlayer.addShip(ship);
        ship_rep.saveAll(ships);
        return new ResponseEntity<>(makeMap("OK", "Ships correctly placed!"), HttpStatus.CREATED);
    }

    private String shipValidity(Ship ship){
        if(!shipTypes.keySet().contains(ship.getType()))
            return "Invalid type of ship.";
        if(shipTypes.get(ship.getType())  != ship.getLocations().size())
            return "Ship\'s \'length\' and \'type\' don\'t match!";

        for(String location : ship.getLocations())if(outOfBoundsLocation(location))
            return "Location out of bounds.";

        int length = shipTypes.get(ship.getType());
        Set<Character> row = getRows(ship);
        Set<Integer> column = getColumns(ship);

        if((row.size() > 1) && (column.size() > 1))
            return "Locations not aligned!";

        if(row.size() == 1){
            if(column.size() < length)
                return "Locations repeated!";
            if(max(column) - min(column) + 1 != length)
                return "Locations are not contiguous!";
        }
        else{
            if(row.size() < length)
                return "Locations repeated!";
            if(max(row) - min(row) + 1 != length)
                return "Locations are not contiguous!";
        }

        return "OK";
    }

    private boolean outOfBoundsLocation(String location){
        char row = location.charAt(0);
        int col = Integer.parseInt(location.substring(1));
        if(row < 'A' || 'J' < row) return true;
        if(col < 1 || 10 < col) return true;
        return false;
    }

    private boolean overlapShips(Ship ship1, Ship ship2){
        for(String location : ship1.getLocations())
            if(ship2.getLocations().contains(location))
                return true;
        return false;
    }

    private Set<Character> getRows(Ship ship){
        return ship.getLocations().stream().map(location -> location.charAt(0)).collect(toSet());}
    private Set<Integer> getColumns(Ship ship){
        return ship.getLocations().stream().map(location -> Integer.parseInt(location.substring(1))).collect(toSet());}
}
