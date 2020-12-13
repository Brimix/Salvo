package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Game;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class GameDTO {
    //~ DTO which shows all info of a game
    public long id;
    public Date created;
    public List<GamePlayerDTO> gamePlayers;
    public List<ScoreDTO> scores;

    public GameDTO(Game game){
        this.id = game.getId();
        this.created = game.getCreated();
        this.gamePlayers = game.getGamePlayers().stream()
                .map(gp -> new GamePlayerDTO(gp))
                .collect(toList());
        this.scores = game.getScores().stream()
                .map(s -> new ScoreDTO(s))
                .collect(toList());
    }
}
