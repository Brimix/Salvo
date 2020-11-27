package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Salvo;

import java.util.LinkedHashMap;
import java.util.Map;

public class SalvoDTO {
    public static Map<String, Object> makeDTO(Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<>();
        // dto.put("id", salvo.getId());
        dto.put("turn", salvo.getTurn());
        dto.put("locations", salvo.getLocations());
        return dto;
    }
}
