package phrases;

import org.tartarus.snowball.ext.englishStemmer;

import java.util.ArrayList;
import java.util.List;


public class Phrase {
    private String words;
    private String stem;
    private String type;
    private static englishStemmer stemmer;

    public Phrase(String words, String type) {
        this(words);
        this.type = type;
    }

    public Phrase(String words) {
        stemmer = new englishStemmer();
        this.words = words;
        cleanPhrase();
        createStemmedFormat();
    }

    private void createStemmedFormat() {
        String[] tokens = getTokens();
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < tokens.length; i++) {
            stemmer.setCurrent(tokens[i]);
            stemmer.stem();
            res.add(stemmer.getCurrent());
        }
        this.stem = String.join(" ", res);
    }

    private void cleanPhrase() {
        String[] tokens = getTokens();
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            token.trim();
            if (token.length() > 0)
                res.add(token);
        }
        this.words = String.join(" ", res);
    }

    public String getDataType() {
        return type;
    }

    public String getStem() {
        return stem;
    }

    public String[] getTokens() {
        String[] tokens = words.split(" ");
        return tokens;
    }

    @Override
    public String toString() {
        return words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phrase)) return false;

        Phrase phrase = (Phrase) o;

        return words != null ? stem.equals(phrase.stem) : phrase.stem == null;
    }

    @Override
    public int hashCode() {
        return stem != null ? stem.hashCode() : 0;
    }
}
