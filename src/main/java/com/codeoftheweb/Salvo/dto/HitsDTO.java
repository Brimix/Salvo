package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.GamePlayer;
import com.codeoftheweb.Salvo.model.Salvo;
import com.codeoftheweb.Salvo.model.Ship;

import java.util.*;

import static com.codeoftheweb.Salvo.util.Util.shipTypes;
import static java.util.stream.Collectors.toList;

public class HitsDTO {
    public static List<Map<String, Object>> makeDTO(GamePlayer gp1, GamePlayer gp2){
        // If there's less than 2 players in the game, show an empty list
        if(gp1 == null || gp2 == null)
            return new ArrayList<>();

        List<Map<String, Object>> hitsList = new ArrayList<>(); // List containing hit info for each opponent salvo

        Map<String, Integer> hitCountAccum = new LinkedHashMap<>(); // Counts number of accumulated hits for each shipType
        for(String shipType : shipTypes.keySet()) hitCountAccum.put(shipType, 0);

        // Obtain opponent salvos to an ordered-by-turn list
        List<Salvo> orderedSalvoes = gp2.getSalvoes().stream()
                .sorted(Comparator.comparingInt(Salvo::getTurn))
                .collect(toList());

        // For each of opponent salvoes (in order)
        for(Salvo salvo : orderedSalvoes){
            Map<String, Object>  hit = new LinkedHashMap<>(); // Here I'll save the info for this salvo

            hit.put("turn", salvo.getTurn()); // Save the current turn

            List<String> hitLocations = new ArrayList<>(); // List of salvo locations that hit some ship
            hit.put("hitLocations", hitLocations);

            Map<String, Integer> hitCount = new LinkedHashMap<>(); // Counts number of hits for each shipType
            hit.put("damages", hitCount);

            int missed = salvo.getLocations().size(); // Counts number of missed shots

            // For each of my ships
            for(Ship ship : gp1.getShips()) {
                List<String> hitLocationShip = hitsSalvoShip(salvo, ship); // Get list of locations hit by this salvo

                // Add this locations to main list
                hitLocations.addAll(hitLocationShip);

                // Count locations and add to 'hitCount'
                hitCount.put(ship.getType() + "Hits", hitLocationShip.size()); // (ex: "carrierHits: 2")

                // Count locations and accumulate them to 'hitCountAccum'
                hitCountAccum.replace(ship.getType(), hitCountAccum.get(ship.getType()) + hitLocationShip.size());

                // Substract number of hits from "missed" count
                missed -= hitLocationShip.size();
            }

            // For each type of ship, add the accumulated number of hits to "damages"
            for(String shipType : hitCountAccum.keySet()) hitCount.put(shipType, hitCountAccum.get(shipType));

            hit.put("missed", missed);
            hitsList.add(hit);
        }
        return hitsList;
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
