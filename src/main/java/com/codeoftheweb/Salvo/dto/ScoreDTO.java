package com.codeoftheweb.Salvo.dto;

import com.codeoftheweb.Salvo.model.Score;
import java.util.Date;

public class ScoreDTO {
    //~ Required from GameDTO.makeDTO
    public long id;
    public double score;
    public long player;
    public long game;
    public Date finishDate;

    public ScoreDTO(Score score){
        this.id = score.getId();
        this.score = score.getScore();
        this.player = score.getPlayer().getId();
        this.game = score.getGame().getId();
        this.finishDate = score.getFinished();
    }
}
