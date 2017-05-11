package commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import wordGraph.SearchConfig;
import wordGraph.Util;
import wordGraph.WordGraph;

import java.util.List;

public class SearchRelationCmd extends Command {
    private Options options;

    public SearchRelationCmd() {
        options = new Options();
    }

    @Override
    void execution(WordGraph wg, String[] args) {
        try {
            CommandLine cmdLine = parser.parse(options, args);
            List<String> remainArgs = cmdLine.getArgList(); //remmaining args are treated as words to be queried
            remainArgs.remove(0);
            String[] words = String.join(" ", remainArgs).split(",");
            List<List<String>> res = wg.searchPath(words[0], words[1], SearchConfig.conSearch);
            Util.printListOfList(res);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}