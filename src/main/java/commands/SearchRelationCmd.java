package commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import result.SearchRelationRes;
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
    public SearchRelationRes execution(WordGraph wg, String[] args) {
        try {
            CommandLine cmdLine = parser.parse(options, args);
            List<String> remainArgs = cmdLine.getArgList(); //remmaining args are treated as words to be queried
            remainArgs.remove(0);
            String[] words = String.join(" ", remainArgs).split(",");
            //TODO check the word all in reuqired domain first before searching.
            List<List<String>> paths = wg.searchPath(words[0], words[1]);
            SearchRelationRes res = new SearchRelationRes(paths);
            return res;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
