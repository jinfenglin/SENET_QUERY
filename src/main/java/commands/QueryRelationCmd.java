package commands;


import org.apache.commons.cli.OptionBuilder;
import phrases.Phrase;
import result.QueryRelationRes;
import wordGraph.WordGraph;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.util.List;

public class QueryRelationCmd extends Command {
    private Options options;

    public QueryRelationCmd() {
        options = new Options();
        options.addOption("h", "hyponymy", false, "Show hyponymy in query result");
        options.addOption("H", "hypernymy", false, "Show hypernym in query result");
        options.addOption("S", "sibling", false, "Show sibling/contrast in query result");
        options.addOption("s", "synonym", false, "Show synonym in query result");
        options.addOption("a", "acronym", false, "Show acronym in query result");
        options.addOption("r", "related", false, "Show related terms in query result");
        options.addOption("d", "data-set", true, "Search only in given domain");
    }

    public QueryRelationRes execution(WordGraph wg, String[] args) {
        try {
            CommandLine cmdLine = parser.parse(options, args);
            List<String> remainArgs = cmdLine.getArgList(); //remmaining args are treated as words to be queried
            remainArgs.remove(0);
            String word = String.join(" ", remainArgs);
            int optionNum = cmdLine.getOptions().length;

            Phrase phrase = wg.getPhrase(word);
            QueryRelationRes res = new QueryRelationRes(phrase);
            if (phrase == null) {
                System.out.println("Unable to locate phrase:" + word);
                return null;
            } else {
                String dValue = cmdLine.getOptionValue("d");
                if (dValue != null && !phrase.getDataType().equals(dValue)) {
                    System.out.println("Vertex found in graph, but not in the domain" + dValue);
                    return null;
                } else if (dValue != null) {
                    optionNum--; //minus the parameter behind -d
                }
            }
            if (cmdLine.hasOption("H") | optionNum == 0)
                res.setHypernym(wg.getHypernym(word));
            if (cmdLine.hasOption("h") | optionNum == 0)
                res.setHypernon(wg.getHyponymy(word));
            if (cmdLine.hasOption("S") | optionNum == 0)
                res.setContrast(wg.getContrast(word));
            if (cmdLine.hasOption("s") | optionNum == 0)
                res.setSynonym(wg.getSynonym(word));
            if (cmdLine.hasOption("a") | optionNum == 0)
                res.setAcronym(wg.getAcronym(word));
            if (cmdLine.hasOption('r') | optionNum == 0)
                res.setRelated(wg.getRelated(word));
            return res;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
