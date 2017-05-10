package experiments;

import wordGraph.WordGraph;

import java.io.BufferedReader;
import java.io.FileReader;

public class WorldRelationExperiment {
    WordGraph wg;

    public WorldRelationExperiment() throws Exception {
        WordGraph wg = new WordGraph();
    }

    public void verifyRelaiton(String filePath) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = bf.readLine()) != null) {
            String[] words = line.split(",");
            String w1 = words[0];
            String w2 = words[1];
            String wr1 = wg.getPhrase(w1);
            String wr2 = wg.getPhrase(w2);
            if (wr1.isEmpty()) {
                System.out.println(wr1 + " is not in the graph.");
            } else if (wr2.isEmpty()) {
                System.out.println(wr2 + " is not in the graph.");
            } else {
                System.out.println("Both" + wr1 + " " + wr2 + "are in graph.");
            }
        }
        bf.close();
    }

    public static void main(String[] args) throws Exception {

    }
}
