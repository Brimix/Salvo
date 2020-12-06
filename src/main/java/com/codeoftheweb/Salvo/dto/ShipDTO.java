package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Ship;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShipDTO {
    //~ Required from GameView DTO
    public static Map<String, Object> makeDTO(Ship ship){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", ship.getId());
        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocations());
        return dto;
    }
}
