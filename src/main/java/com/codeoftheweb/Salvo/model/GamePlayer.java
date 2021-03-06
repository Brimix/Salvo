package com.codeoftheweb.Salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

import static java.util.Collections.max;
import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {
    //~ Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date joined;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes;

    //~ Constructors
    public GamePlayer() {}
    public GamePlayer(Player player, Game game) {
        this.joined = new Date();
        player.addGamePlayer(this);
        game.addGamePlayer(this);
        this.ships = new LinkedHashSet<>();
        this.salvoes = new LinkedHashSet<>();
    }

    public long getId() { return id; }
    public Date getJoined() { return joined; }
    public void setJoined(Date joined) { this.joined = joined; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public Set<Ship> getShips() { return ships; }
    public void setShips(Set<Ship> ships) { this.ships = ships; }
    public Set<Salvo> getSalvoes() { return salvoes; }
    public void setSalvoes(Set<Salvo> salvos) { this.salvoes = salvos; }

    // ~ Methods
    public void addShip(Ship ship){
        ship.setGamePlayer(this);
        ships.add(ship);
    }
    public void addSalvo(Salvo salvo){
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }
    public Score getScore(double score){
        if(score < 0)
            return null;
        return new Score(score, player, game);
    }
    // Method designed for getGameState
    public int getTurn(){
        if(salvoes.size() == 0)
            return 0;
        return max(salvoes.stream().map(salvo -> salvo.getTurn()).collect(toList()));
    }
}
