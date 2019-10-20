package com.clarivate.scores.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document(collection = "scores")
public class Score implements Serializable {

    @Indexed
    @NotNull
    private int level;

    @NotNull
    private String userName;

    @NotNull
    private int score;

    public Score(int level, String userName, int score) {
        this.level = level;
        this.userName = userName;
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
