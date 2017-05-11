package commands;


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
    }

    public void execution(WordGraph wg, String[] args) {
        try {
            CommandLine cmdLine = parser.parse(options, args);
            List<String> remainArgs = cmdLine.getArgList(); //remmaining args are treated as words to be queried
            remainArgs.remove(0);
            String word = String.join(" ", remainArgs);
            int optionNum = cmdLine.getOptions().length;
            System.out.println("===========================");
            String phraseRealFormat = wg.getPhrase(word);
            if (phraseRealFormat.isEmpty()) {
                System.out.println("Unable to locate phrase:" + word);
            } else {
                System.out.println("Phrase:" + wg.getPhrase(word));
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
            System.out.println("===========================");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
