package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Score;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScoreDTO {
    public static Map<String, Object> makeDTO(Score score){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", score.getId());
        dto.put("score", score.getScore());
        return dto;
    }
}
