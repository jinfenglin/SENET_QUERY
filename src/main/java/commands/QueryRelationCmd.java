package commands;


import org.apache.commons.cli.OptionBuilder;
import phrases.Phrase;
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

    public void execution(WordGraph wg, String[] args) {
        try {
            CommandLine cmdLine = parser.parse(options, args);
            List<String> remainArgs = cmdLine.getArgList(); //remmaining args are treated as words to be queried
            remainArgs.remove(0);
            String word = String.join(" ", remainArgs);
            int optionNum = cmdLine.getOptions().length;
            System.out.println("===========================");
            Phrase phrase = wg.getPhrase(word);
            if (phrase == null) {
                System.out.println("Unable to locate phrase:" + word);
            } else {
                String dValue = cmdLine.getOptionValue("d");
                if (dValue != null && !phrase.getDataType().equals(dValue)) {
                    System.out.println("Vertex found in graph, but not in the domain" + dValue);
                    return;
                } else if (dValue != null) {
                    optionNum--; //minus the parameter behind -d
                }
                System.out.println("Phrase:" + phrase.toString());
                System.out.println("--------------------------");
            }
            if (cmdLine.hasOption("H") | optionNum == 0)
                System.out.println("Hypernym:" + wg.getHypernym(word));
            if (cmdLine.hasOption("h") | optionNum == 0)
                System.out.println("Hypernon:" + wg.getHyponymy(word));
            if (cmdLine.hasOption("S") | optionNum == 0)
                System.out.println("Sibling/Contrast:" + wg.getContrast(word));
            if (cmdLine.hasOption("s") | optionNum == 0)
                System.out.println("Synonym:" + wg.getSynonym(word));
            if (cmdLine.hasOption("a") | optionNum == 0)
                System.out.println("Acronym:" + wg.getAcronym(word));
            if (cmdLine.hasOption('r') | optionNum == 0)
                System.out.println("Related:" + wg.getRelated(word));
            System.out.println("===========================");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
