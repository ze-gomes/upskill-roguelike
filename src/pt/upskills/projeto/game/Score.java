package pt.upskills.projeto.game;

public class Score implements Comparable<Score> {
    String name;
    int score;

    public Score(String name, int score){
        this.name = name;
        this.score = score;
    }


    // To sort scores in the array and calculate highscores
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
