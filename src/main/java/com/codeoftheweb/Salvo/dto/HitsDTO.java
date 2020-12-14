package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Salvo;
import com.codeoftheweb.Salvo.model.Ship;

import java.util.*;

import static com.codeoftheweb.Salvo.util.Util.shipTypes;
import static java.util.stream.Collectors.toList;

public class HitsDTO {
    public static List<Map<String, Object>> makeDTO(GamePlayer gp1, GamePlayer gp2){
        if(gp1 == null || gp2 == null)
            return new ArrayList<>();

        List<Map<String, Object>> hitListGame = new ArrayList<>();
        Map<String, Integer> hitCountTotal = new LinkedHashMap<>();
        for(String shipType : shipTypes.keySet()) hitCountTotal.put(shipType, 0);

        List<Salvo> orderedSalvoes = gp2.getSalvoes().stream()
                .sorted(Comparator.comparingInt(Salvo::getTurn))
                .collect(toList());
        for(Salvo salvo : orderedSalvoes){
            Map<String, Object>  hit = new LinkedHashMap<>();
            List<String> totalHits = new ArrayList<>();
            Map<String, Integer> hitCount = new LinkedHashMap<>();

            hit.put("turn", salvo.getTurn());
            hit.put("hitLocations", totalHits);
            hit.put("damages", hitCount);

            int missed = salvo.getLocations().size();
            for(Ship ship : gp1.getShips()) {
                List<String> hitList = hitsSalvoShip(salvo, ship);
                totalHits.addAll(hitList);
                hitCount.put(ship.getType() + "Hits", hitList.size());
                hitCountTotal.replace(ship.getType(), hitCountTotal.get(ship.getType()) + hitList.size());
                missed -= hitList.size();
            }
            for(String key : hitCountTotal.keySet()) hitCount.put(key, hitCountTotal.get(key));
            hit.put("missed", missed);
            hitListGame.add(hit);
        }
        return hitListGame;
    }
    private static List<String> hitsSalvoShip(Salvo salvo, Ship ship){
        List<String> list = new ArrayList<>();
        for(String loc : salvo.getLocations()){
            if(ship.getLocations().contains(loc))
                list.add(loc);
        }
        return list;
    }
}
