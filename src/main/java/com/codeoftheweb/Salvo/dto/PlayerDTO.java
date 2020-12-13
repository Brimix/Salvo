package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Player;

public class PlayerDTO {
    // Required from GamePlayerDTO.makeDTO
    public long id;
    public String name;
    public String email;

    public PlayerDTO(Player player){
        this.id = player.getId();
        this.name = player.getName();
        this.email = player.getEmail();
    }
}
