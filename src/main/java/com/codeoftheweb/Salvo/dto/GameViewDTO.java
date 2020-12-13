package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Salvo;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class GameViewDTO extends GameDTO{
    //~ DTO which creates the view that is shown to the player
    public List<ShipDTO> ships;
    public List<SalvoDTO> salvoes;

    public GameViewDTO(GamePlayer gamePlayer) {
        super(gamePlayer.getGame());
        this.ships = gamePlayer.getShips().stream()
                .map(s -> new ShipDTO(s))
                .collect(toList());

        Set<Salvo> allSalvoes = new HashSet<>();
        for(GamePlayer gp : gamePlayer.getGame().getGamePlayers())
            allSalvoes.addAll(gp.getSalvoes());
        this.salvoes = allSalvoes.stream()
                .map(s -> new SalvoDTO(s))
                .collect(toList());
    }
}
