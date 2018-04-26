package phrases;

/**
 *
 */
public class PhraseWithScore {
    Phrase phrase;
    double score;

    public PhraseWithScore(Phrase phrase, Double score) {
        this.phrase = phrase;
        this.score = score;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
