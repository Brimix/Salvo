package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Salvo;
import com.codeoftheweb.Salvo.model.Ship;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codeoftheweb.Salvo.util.Util.*;

public class FullGameViewDTO extends GameViewDTO{
    public String gameState;
    public Map<String, Object> hits;

    public FullGameViewDTO(GamePlayer gamePlayer) {
        super(gamePlayer);
        this.gameState = getGameState(gamePlayer);
        this.hits = new LinkedHashMap<>();
        if(getOpponent(gamePlayer) == null){
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
        }
        else{
            hits.put("self", gameHits(gamePlayer, getOpponent(gamePlayer)));
            hits.put("opponent", gameHits(getOpponent(gamePlayer), gamePlayer));
        }
    }

    private static List<Map<String, Object>> gameHits(GamePlayer gp1, GamePlayer gp2){
        List<Map<String, Object>> hitListGame = new ArrayList<>();
        Map<String, Integer> hitCountTotal = new LinkedHashMap<>();
        for(String shipType : shipTypes.keySet()) hitCountTotal.put(shipType, 0);

        for(Salvo salvo : gp2.getSalvoes()){
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
