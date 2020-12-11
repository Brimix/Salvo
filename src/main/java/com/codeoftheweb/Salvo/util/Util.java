package com.codeoftheweb.Salvo.util;

import com.codeoftheweb.Salvo.model.GamePlayer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.LinkedHashMap;
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

    public static String getGameState(GamePlayer gamePlayer){
        if(gamePlayer.getShips().size() < 5) return "PLACESHIPS";
        return "PLAY";
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
}
