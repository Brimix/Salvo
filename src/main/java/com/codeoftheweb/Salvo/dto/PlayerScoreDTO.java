package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerScoreDTO {
    public long id;
    public String email;
    public Map<String, Object> score;

    public PlayerScoreDTO(Player player){
        this.id = player.getId();
        this.email = player.getEmail();
        this.score = buildScore(player);
    }

    private Map<String, Object> buildScore(Player player) {
        Map<String, Object> score = new LinkedHashMap<>();
        score.put("total", player.getTotal());
        score.put("won", player.getWins());
        score.put("tied", player.getDraws());
        score.put("lost", player.getLoses());
        return score;
    }
}
