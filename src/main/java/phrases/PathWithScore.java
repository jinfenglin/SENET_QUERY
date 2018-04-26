package phrases;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PathWithScore {
    List<String> path;
    double score;

    public PathWithScore(List<PhraseWithScore> path) {
        this.path = new ArrayList<>();
        this.score = calScore(path);
        for (PhraseWithScore ps : path) {
            this.path.add(ps.getPhrase().toString());
        }
    }

    private double calScore(List<PhraseWithScore> path) {
        double path_score = 1;
        for (PhraseWithScore ps : path) {
            path_score *= ps.score;
        }
        return path_score;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
