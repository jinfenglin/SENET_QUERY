package Commands;

import WordGraph.WordGraph;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;


public abstract class Command {
    protected static CommandLineParser parser = new DefaultParser();

    abstract void execution(WordGraph wg, String[] args);
}
