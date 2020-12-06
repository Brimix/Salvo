package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Game;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class GameDTO {
    //~ DTO which shows all info of a game
    public static Map<String, Object> makeDTO(Game game){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreated());
        dto.put("gamePlayers", game.getGamePlayers().stream()
                .map(gp -> GamePlayerDTO.makeDTO(gp))
                .collect(toList()));
        dto.put("scores", game.getScores().stream()
                .map(s -> ScoreDTO.makeDTO(s))
                .collect(toList()));
        return dto;
    }
}
