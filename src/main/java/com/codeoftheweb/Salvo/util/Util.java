package com.codeoftheweb.Salvo.util;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Salvo;
import com.codeoftheweb.Salvo.model.Score;
import com.codeoftheweb.Salvo.model.Ship;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Util {
    public static boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    public static Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    public static String getGameState(GamePlayer me){
        GamePlayer opp = getOpponent(me);
        if(me.getShips().size() < 5) return "PLACESHIPS";
        if(opp == null || opp.getShips().size() < 5) return "WAITINGFOROPP";

        Score score = me.getPlayer().getScore(me.getGame());
//        if(score != null){
//            if(score.getScore() == 1.0D) return "WON";
//            if(score.getScore() == 0.5D) return "TIE";
//            if(score.getScore() == 0.0D) return "LOST";
//            assert(false); // If the code reaches this line, there's been a problem
//        }
        if(dead(me) || dead(opp)) {
            if(dead(me) && dead(opp)) return "TIE";
            if(dead(me)) return "LOST";
            if(dead(opp)) return "WON";
        }
        if(me.getTurn() > opp.getTurn()) return "WAIT";
        return "PLAY";
    }

    public static GamePlayer getOpponent(GamePlayer gamePlayer){
        return gamePlayer.getGame().getGamePlayers().stream()
                .filter(gp -> (gp != gamePlayer))
                .findAny().orElse(null);
    }

    public static Map<String, Integer> shipTypes = Stream.of(
        new Object[][]{
            {"carrier", 5},
            {"battleship", 4},
            {"submarine", 3},
            {"destroyer", 3},
            {"patrolboat", 2}
        }).collect(toMap(data -> (String)data[0], data -> (Integer)data[1]));

    public static boolean outOfBoundsLocation(String location){
        char row = location.charAt(0);
        int col = Integer.parseInt(location.substring(1));
        if(row < 'A' || 'J' < row) return true;
        if(col < 1 || 10 < col) return true;
        return false;
    }

    static boolean dead(GamePlayer gp1){
        GamePlayer gp2 = getOpponent(gp1);
        List<String> allLocations = new ArrayList<>();
        for(Salvo salvo : gp2.getSalvoes()) allLocations.addAll(salvo.getLocations());

        for(Ship ship : gp1.getShips())if(!allLocations.containsAll(ship.getLocations()))
            return false;
        return true;
    }
}
