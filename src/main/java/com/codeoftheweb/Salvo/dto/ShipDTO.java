package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Ship;
import java.util.List;

public class ShipDTO {
    //~ Required from GameView DTO
    public long id;
    public String type;
    public List<String> locations;

    public ShipDTO(Ship ship){
        this.id = ship.getId();
        this.type = ship.getType();
        this.locations = ship.getLocations();
    }
}
