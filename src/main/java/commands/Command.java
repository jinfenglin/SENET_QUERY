package commands;

import result.Result;
import wordGraph.WordGraph;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;


public abstract class Command {
    protected static CommandLineParser parser = new DefaultParser();

    abstract Result execution(WordGraph wg, String[] args);
}
