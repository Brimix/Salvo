package com.codeoftheweb.Salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {
    //~ Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date created;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> scores;

    //~ Constructors
    public Game() {
        this.created = new Date();
        this.gamePlayers = new HashSet<GamePlayer>();
    }
    public Game(Date date) {
        this.created = date;
        this.gamePlayers = new HashSet<GamePlayer>();
    }

    public long getId() { return id; }
    public Date getCreated() { return created; }
    public void setCreated(Date created) { this.created = created; }
    public Set<GamePlayer> getGamePlayers() { return gamePlayers; }
    public void setGamePlayers(Set<GamePlayer> gamePlayers) { this.gamePlayers = gamePlayers; }

    //~ Methods
    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        gamePlayers.add(gamePlayer);
    }
    public void addScore(Score score){
        score.setGame(this);
        scores.add(score);
    }
    public List<Player> getPlayers() {
        return getGamePlayers().stream()
                .map(sub -> sub.getPlayer())
                .collect(toList());
    }
}