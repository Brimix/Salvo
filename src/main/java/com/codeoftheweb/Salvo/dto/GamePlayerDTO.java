package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.GamePlayer;

public class GamePlayerDTO {
    //~ Required from GameDTO.makeDTO
    public long id;
    public PlayerDTO player;

    public GamePlayerDTO(GamePlayer gamePlayer){
        this.id = gamePlayer.getId();
        this.player = new PlayerDTO(gamePlayer.getPlayer());
    }
}
