package experiments;

import wordGraph.SearchConfig;
import wordGraph.Util;
import wordGraph.WordGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class WorldRelationExperiment {
    WordGraph wg;

    public WorldRelationExperiment() throws Exception {
        wg = new WordGraph();
    }

    public void verifyRelaiton(String filePath) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        String line;
        System.out.println("");
        while ((line = bf.readLine()) != null) {
            String[] words = line.split(",");
            String w1 = words[0];
            String w2 = words[1];
            String wr1 = wg.getPhrase(w1);
            String wr2 = wg.getPhrase(w2);
            boolean w1in = true, w2in = true;
            w1in = !wr1.isEmpty();
            w2in = !wr2.isEmpty();
            List<List<String>> hyperPaths = wg.searchPath(w1, w2, SearchConfig.hyperSeach);
            List<List<String>> synPaths = wg.searchPath(w1, w2, SearchConfig.synSearch);
            List<List<String>> hyponPaths = wg.searchPath(w1, w2, SearchConfig.hyponSearch);
            List<List<String>> sibPaths = wg.searchPath(w1, w2, SearchConfig.conSearch);
            String exp = String.format("%s,%s,%s,%s,%s,%s,%s,%s", w1, w2, w1in, w2in, hyperPaths, synPaths, hyponPaths, sibPaths);
            System.out.println(exp);

        }
        bf.close();
    }

    public static void main(String[] args) throws Exception {
        WorldRelationExperiment we = new WorldRelationExperiment();
        we.verifyRelaiton("src/main/resources/SemanticWebTerms/Agile-Table 1.csv");
    }
}
