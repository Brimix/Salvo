package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Salvo;
import com.codeoftheweb.Salvo.model.Ship;
import com.codeoftheweb.Salvo.util.Util;

import java.util.*;

import static com.codeoftheweb.Salvo.util.Util.*;
import static java.util.stream.Collectors.toList;

public class GamePlayerDTO {
    //~ Required from GameDTO.makeDTO
    public static Map<String, Object> makeDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", PlayerDTO.makeDTO(gamePlayer.getPlayer()));
        return dto;
    }

    //~ DTO which creates the view that is shown to the player
    public static Map<String, Object> gameView(GamePlayer gamePlayer){
        Game game = gamePlayer.getGame();
        Map<String, Object> dto = GameDTO.makeDTO(game);
        dto.put("ships", gamePlayer.getShips().stream()
                .map(s -> ShipDTO.makeDTO(s))
                .collect(toList()));

        Set<Salvo> allSalvoes = new HashSet<>();
        for(GamePlayer gp : game.getGamePlayers())
            allSalvoes.addAll(gp.getSalvoes());
        dto.put("salvoes", allSalvoes.stream()
                .map(s -> SalvoDTO.makeDTO(s))
                .collect(toList()));
        return dto;
    }


    //~ Auxiliary Game View DTO for testing new Front End
    public static Map<String, Object> gameUltimateView(GamePlayer gamePlayer){
        Game game = gamePlayer.getGame();
        Map<String, Object> dto = GameDTO.makeDTO(game);
        Map<String, Object> Hitting = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getJoined());
//        dto.put("gameState", "Not yet implemented.");
        dto.put("gameState", getGameState(gamePlayer));
        dto.put("hits", Hitting);
        dto.put("gamePlayers", game.getGamePlayers().stream()
                                .map(gp -> GamePlayerDTO.makeDTO(gp))
                                .collect(toList()));

        dto.put("ships", gamePlayer.getShips().stream()
                .map(s -> ShipDTO.makeDTO(s))
                .collect(toList()));

        Set<Salvo> allSalvoes = new HashSet<>();
        for(GamePlayer gp : game.getGamePlayers()) allSalvoes.addAll(gp.getSalvoes());
        dto.put("salvoes", allSalvoes.stream()
                .sorted(Comparator.comparingInt(Salvo::getTurn))
                .map(s -> SalvoDTO.makeDTO(s))
                .collect(toList()));

        Hitting.put("self", HitsDTO.makeDTO(gamePlayer, getOpponent(gamePlayer)));
        Hitting.put("opponent", HitsDTO.makeDTO(getOpponent(gamePlayer), gamePlayer));
        return dto;
    }
}
