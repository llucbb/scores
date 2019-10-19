package com.clarivate.scores.model;

import org.springframework.data.annotation.Id;

public class HighestScore {

    public static final String USER_ID = "userid";

    //@JsonProperty(USER_ID)
    @Id
    private String userName;

    private int score;

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

    @Override
    public String toString() {
        return "HighestScore{" +
                "userName='" + userName + '\'' +
                ", score=" + score +
                '}';
    }
}
