package pt.upskills.projeto.game;

import java.util.Comparator;

public class Score implements Comparable<Score> {
    String name;
    int score;

    public Score(String name, int score){
        this.name = name;
        this.score = score;
    }


    @Override
    public int compareTo(Score s) {
        if (this.score > s.score){
            return -1;
        } else if (this.score == s.score){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "name:" + name +
                "score:" + score;
    }
}
