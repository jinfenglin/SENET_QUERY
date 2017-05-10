import java.util.ArrayList;
import java.util.List;

public class Phrase {
    private String words;

    public Phrase(String words) {
        this.words = words;
        this.words = cleanPhrase();

    }


    private String cleanPhrase() {
        String[] tokens = getTokens();
        List<String> res = new ArrayList<String>();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            token.trim();
            if (token.length() > 0)
                res.add(token);
        }
        return String.join(" ", res);
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

        return words != null ? words.equals(phrase.words) : phrase.words == null;
    }

    @Override
    public int hashCode() {
        return words != null ? words.hashCode() : 0;
    }
}
