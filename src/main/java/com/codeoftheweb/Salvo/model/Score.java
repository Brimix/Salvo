package com.codeoftheweb.Salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Score {
    //~ Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double score;
    private Date finished;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    //~ Constructors
    public Score(){}
    public Score(double score, Player player, Game game) {
        this.score = score;
        this.finished = new Date();
        player.addScore(this);
        game.addScore(this);
//        this.player = player;
//        this.game = game;
    }

    public long getId() { return id; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public Date getFinished() { return finished; }
    public void setFinished(Date finished) { this.finished = finished; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }

    //~ Methods
}
