package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Game;
import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Salvo;
import com.codeoftheweb.Salvo.model.Ship;
import com.codeoftheweb.Salvo.util.Util;

import java.util.*;

import static com.codeoftheweb.Salvo.util.Util.*;
import static java.util.stream.Collectors.toList;

public class GamePlayerDTO {
    //~ Required from GameDTO.makeDTO
    public static Map<String, Object> makeDTO(GamePlayer gamePlayer){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", PlayerDTO.makeDTO(gamePlayer.getPlayer()));
        return dto;
    }

    //~ DTO which creates the view that is shown to the player
    public static Map<String, Object> gameView(GamePlayer gamePlayer){
        Game game = gamePlayer.getGame();
        Map<String, Object> dto = GameDTO.makeDTO(game);
        dto.put("ships", gamePlayer.getShips().stream()
                .map(s -> ShipDTO.makeDTO(s))
                .collect(toList()));

        Set<Salvo> allSalvoes = new HashSet<>();
        for(GamePlayer gp : game.getGamePlayers())
            allSalvoes.addAll(gp.getSalvoes());
        dto.put("salvoes", allSalvoes.stream()
                .map(s -> SalvoDTO.makeDTO(s))
                .collect(toList()));
        return dto;
    }


    //~ Auxiliary Game View DTO for testing new Front End
    public static Map<String, Object> gameUltimateView(GamePlayer gamePlayer){
        Game game = gamePlayer.getGame();
        Map<String, Object> dto = GameDTO.makeDTO(game);
        Map<String, Object> Hitting = new LinkedHashMap<>();
        dto.put("id", gamePlayer.getId());
        dto.put("created", gamePlayer.getJoined());
//        dto.put("gameState", "Not yet implemented.");
        dto.put("gameState", getGameState(gamePlayer));
        dto.put("hits", Hitting);
        dto.put("gamePlayers", game.getGamePlayers().stream()
                                .map(gp -> GamePlayerDTO.makeDTO(gp))
                                .collect(toList()));

        dto.put("ships", gamePlayer.getShips().stream()
                .map(s -> ShipDTO.makeDTO(s))
                .collect(toList()));

        Set<Salvo> allSalvoes = new HashSet<>();
        for(GamePlayer gp : game.getGamePlayers()) allSalvoes.addAll(gp.getSalvoes());
        dto.put("salvoes", allSalvoes.stream()
                .map(s -> SalvoDTO.makeDTO(s))
                .collect(toList()));

        Hitting.put("self", gameHits(gamePlayer, getOpponent(gamePlayer)));
        Hitting.put("opponent", gameHits(getOpponent(gamePlayer), gamePlayer));
        return dto;
    }

    private static List<Map<String, Object>> gameHits(GamePlayer gp1, GamePlayer gp2){
        if(gp1 == null || gp2 == null)
            return new ArrayList<>();

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
