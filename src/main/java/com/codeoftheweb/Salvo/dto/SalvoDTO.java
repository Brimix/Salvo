package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Salvo;
import java.util.List;

public class SalvoDTO {
    //~ Required from GameView DTO
    public int turn;
    public long player;
    public List<String> locations;

    public SalvoDTO(Salvo salvo){
        this.turn = salvo.getTurn();
        this.player = salvo.getGamePlayer().getPlayer().getId();
        this.locations = salvo.getLocations();
    }
}
