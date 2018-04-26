package phrases;

import javafx.util.Pair;

/**
 * A phrase pair with score indicating their relevance
 */
public class PhrasePairWithScore {
    private Pair<Phrase, Phrase> pair;
    private double score;

    public PhrasePairWithScore(Pair<Phrase, Phrase> pair, double score) {
        this.pair = pair;
        this.score = score;
    }

    public Pair<Phrase, Phrase> getPair() {
        return pair;
    }

    public void setPair(Pair<Phrase, Phrase> pair) {
        this.pair = pair;
    }

    public double getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
