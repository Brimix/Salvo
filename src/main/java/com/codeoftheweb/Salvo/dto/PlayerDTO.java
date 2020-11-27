package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerDTO {
    public static Map<String, Object> makeDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("name", player.getName());
        dto.put("email", player.getEmail());
        return dto;
    }
}
