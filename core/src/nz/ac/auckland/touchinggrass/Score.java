package nz.ac.auckland.touchinggrass;

public class Score {

    public static int score;
    public Score() {

    }
    public static void incrementScore(int points) {
        Score.score += points;
    }
}
