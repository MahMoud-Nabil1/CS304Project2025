package ScoreRelated;

// Helper class to store and sort scores
public class ScoreEntry implements Comparable<ScoreEntry> {
    String name;
    int score;

    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        // This logic sorts in Descending order (Highest to Lowest)
        return other.score - this.score;
    }

    @Override
    public String toString() {
        return name + ":" + score;
    }
}