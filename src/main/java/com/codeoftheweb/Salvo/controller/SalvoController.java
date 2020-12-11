package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.model.Salvo;
import com.codeoftheweb.Salvo.repository.GamePlayerRepository;
import com.codeoftheweb.Salvo.repository.PlayerRepository;
import com.codeoftheweb.Salvo.repository.SalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.codeoftheweb.Salvo.util.Util.*;

@RestController
@RequestMapping("/api")
public class SalvoController {
    //~ Declaration of repositories
    @Autowired
    private PlayerRepository player_rep;
    @Autowired
    private GamePlayerRepository gp_rep;
    @Autowired
    private SalvoRepository salvo_rep;

    @RequestMapping(path = "games/players/{gamePlayer_id}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Object> fireSalvo(@PathVariable Long gamePlayer_id, @RequestBody Salvo salvo, Authentication authentication){
        // Player authentication
        if(isGuest(authentication))
            return new ResponseEntity<>(makeMap("error", "You are not logged in."), HttpStatus.UNAUTHORIZED);
        Player player = player_rep.findByEmail(authentication.getName()).orElse(null);
        if(player == null)
            return new ResponseEntity<>(makeMap("error", "Database error. Player not found."), HttpStatus.INTERNAL_SERVER_ERROR);

        // Get gamePlayers and check permissions
        GamePlayer gamePlayerMe = gp_rep.findById(gamePlayer_id).orElse(null);
        if(gamePlayerMe == null)
            return new ResponseEntity<>(makeMap("error", "Database error. GamePlayer not found."), HttpStatus.INTERNAL_SERVER_ERROR);
        if(player != gamePlayerMe.getPlayer())
            return new ResponseEntity<>(makeMap("error", "This is not your game!"), HttpStatus.UNAUTHORIZED);
        if(!getGameState(gamePlayerMe).equals("PLAY"))
            return new ResponseEntity<>(makeMap("error", "Playing is not allowed"), HttpStatus.UNAUTHORIZED);

        GamePlayer gamePlayerOpponent = getOpponent(gamePlayerMe);
        if(gamePlayerOpponent == null)
            return new ResponseEntity<>(makeMap("error", "You don\'t have a rival yet!"), HttpStatus.FORBIDDEN);

        // Validate the format of the received salvo
        String status = salvoValidity(salvo);
        if(status != "OK")
            return new ResponseEntity<>(makeMap("error", "Invalid salvo! " + status), HttpStatus.FORBIDDEN);

        //  Check that the salvo don't overlap with previous ones
        for(Salvo pastSalvo : gamePlayerMe.getSalvoes())
            if(overlapSalvoes(pastSalvo, salvo))
                return new ResponseEntity<>(makeMap("error", "Overlapping salvoes!"), HttpStatus.FORBIDDEN);

        // Validate turn
        int myTurn = gamePlayerMe.getSalvoes().size();
        int opponentTurn = gamePlayerOpponent.getSalvoes().size();
        if(myTurn > opponentTurn)
            return new ResponseEntity<>(makeMap("error", "Wait for your rival to finish this turn"), HttpStatus.FORBIDDEN);
        if(myTurn+1 < opponentTurn)
            return new ResponseEntity<>(makeMap("error", "Server problem. You are many turns behind your rival."), HttpStatus.INTERNAL_SERVER_ERROR);

        // All is fine
        salvo.setTurn(myTurn+1);
        gamePlayerMe.addSalvo(salvo);
        salvo_rep.save(salvo);
        return new ResponseEntity<>(makeMap("OK", "Your salvoes were fired!"), HttpStatus.CREATED);
    }

    private boolean overlapSalvoes(Salvo salvo1, Salvo salvo2){
        for(String location : salvo1.getLocations())
            if(salvo2.getLocations().contains(location))
                return true;
        return false;
    }

    private String salvoValidity(Salvo salvo){
        if(salvo.getLocations().size() > 5)
            return "Too many shots!";

        for(String location : salvo.getLocations())if(outOfBoundsLocation(location))
            return "Location out of bounds.";

        long dist = salvo.getLocations().stream().distinct().count();
        long total = salvo.getLocations().size();
        if(dist < total)
            return "Locations repeated!";

        return "OK";
    }
}