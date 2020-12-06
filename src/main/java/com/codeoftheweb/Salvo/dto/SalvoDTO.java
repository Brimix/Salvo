package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Salvo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SalvoDTO {
    //~ Required from GameView DTO
    public static Map<String, Object> makeDTO(Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<>();
        // dto.put("id", salvo.getId());
        dto.put("turn", salvo.getTurn());
        dto.put("player", salvo.getGamePlayer().getPlayer().getId());
        dto.put("locations", salvo.getLocations());
        return dto;
    }

    //~ Auxiliary Salvo DTO for testing new Front End
//    public static Map<String, Object> porongaDTO(Salvo salvo){
//        Map<String, Object> pacote = new LinkedHashMap<>();
//        Map<String, Object> shipData = new LinkedHashMap<>();
//        shipData.put("carrier", 0);
//        shipData.put("battleship", 0);
//        shipData.put("submarine", 0);
//        shipData.put("destroyer", 0);
//        shipData.put("patrolboat", 0);
//        shipData.put("carrierHits", 0);
//        shipData.put("battleshipHits", 0);
//        shipData.put("submarineHits", 0);
//        shipData.put("destroyerHits", 0);
//        shipData.put("patrolboatHits", 0);
//        shipData.put("missed", 5);
//
//        pacote.put("turn", salvo.getTurn());
//        pacote.put("hitLocations", salvo.getLocations());
//        pacote.put("damages", shipData);
//
//        return pacote;
//    }
}
