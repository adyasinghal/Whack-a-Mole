package persistence;

import java.io.Serializable;

public class PlayerScore implements Serializable {
    private String name;
    private int score;

    public PlayerScore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
}
