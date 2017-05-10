package Commands;

import WordGraph.WordGraph;

public interface Command {
    void execution(WordGraph wg);
}
