package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerDTO {
    // Required from GamePlayerDTO.makeDTO
    public static Map<String, Object> makeDTO(Player player){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("name", player.getName());
        dto.put("email", player.getEmail());
        return dto;
    }
    public static Map<String, Object> PlayerScoreDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> score = new LinkedHashMap<>();
        dto.put("id", player.getId());
        dto.put("email", player.getEmail());
        score.put("total", player.getTotal());
        score.put("won", player.getWins());
        score.put("tied", player.getDraws());
        score.put("lost", player.getLoses());
        dto.put("score", score);
        return dto;
    }
}
