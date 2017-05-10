package Commands;

import WordGraph.WordGraph;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import java.util.List;

public abstract class Command {
    private static CommandLineParser parser = new DefaultParser();
    abstract void execution(WordGraph wg, String args);
}
