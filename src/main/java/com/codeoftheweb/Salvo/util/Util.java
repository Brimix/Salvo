package com.codeoftheweb.Salvo.util;

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

    public static Map<String, Integer> shipTypes = Stream.of(
        new Object[][]{
            {"carrier", 5},
            {"battleship", 4},
            {"submarine", 3},
            {"destroyer", 3},
            {"patrolboat", 2}
        }).collect(toMap(data -> (String)data[0], data -> (Integer)data[1]));
}
