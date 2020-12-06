package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Score;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScoreDTO {
    //~ Required from GameDTO.makeDTO
    public static Map<String, Object> makeDTO(Score score){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", score.getId());
        dto.put("score", score.getScore());
        dto.put("player", score.getPlayer().getId());
        dto.put("game", score.getGame().getId());
        dto.put("finishDate", score.getFinished());
        return dto;
    }
}
