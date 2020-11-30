package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Salvo;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class GamePlayerDTO {
    // Required from GameDTO.makeDTO
    public static Map<String, Object> makeDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", PlayerDTO.makeDTO(gamePlayer.getPlayer())  );
        return dto;
    }

    // Game View DTO for Task 3
    public static Map<String, Object> gameView(GamePlayer gamePlayer){
        Map<String, Object> dto = GameDTO.makeDTO(gamePlayer.getGame());
        dto.put("ships", gamePlayer.getShips().stream()
                .map(s -> ShipDTO.makeDTO(s))
                .collect(toList()));
        return dto;
    }

    // Game View DTO for Task 4
    public static Map<String, Object> gameFullView(GamePlayer gamePlayer){
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
}
