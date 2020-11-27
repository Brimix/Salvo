package com.codeoftheweb.Salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {
    //~ Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private int turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gameplayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "locations")
    private List<String> locations;

    //~ Constructors
    public Salvo(){}
    public Salvo(GamePlayer gamePlayer, int turn, List<String> locations) {
        gamePlayer.addSalvo(this);
        this.turn = turn;
        this.locations = locations;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn; }

    @JsonIgnore
    public GamePlayer getGamePlayer() { return gamePlayer; }
    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

    public List<String> getLocations() { return locations; }
    public void setLocations(List<String> locations) { this.locations = locations; }

    //~ Methods
}
