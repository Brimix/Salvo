package com.codeoftheweb.Salvo.controller;

import com.codeoftheweb.Salvo.model.Player;
import com.codeoftheweb.Salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.codeoftheweb.Salvo.util.Util.isGuest;
import static com.codeoftheweb.Salvo.util.Util.makeMap;

@RestController
@RequestMapping("/api")
public class PlayerController {
    // Declaration of repositories
    @Autowired
    private PlayerRepository player_rep;

    // Declaration of encoder
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password) {

        if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
            return new ResponseEntity<>(makeMap("error", "Missing data"), HttpStatus.FORBIDDEN);
        }

        if (player_rep.findByEmail(email).isPresent()) {
            return new ResponseEntity<>(makeMap("error", "Email already in use"), HttpStatus.FORBIDDEN);
        }

        player_rep.save(new Player(name, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
