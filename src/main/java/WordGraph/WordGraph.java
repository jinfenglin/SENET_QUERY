package WordGraph;

import javafx.util.Pair;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.*;


public class WordGraph {
    private DirectedGraph<Phrase, LabeledEdge> graph;
    private PhraseBase phraseBase;

    public WordGraph() throws Exception {
        graph = new DirectedPseudograph<Phrase, LabeledEdge>(
                new ClassBasedEdgeFactory<Phrase, LabeledEdge>(LabeledEdge.class));
        phraseBase = new PhraseBase();
        buildGraph();
    }

    public void buildGraph() {
        addPhraseToGraph(phraseBase.acronyms, Relationship.ACRONYM);
        addPhraseToGraph(phraseBase.contrasts, Relationship.CONTRAST);
        addPhraseToGraph(phraseBase.synonyms, Relationship.SYNONYM);
        addHypernym(phraseBase.hypernyms);
    }

    private void addPhraseToGraph(List<Pair<Phrase, Phrase>> wordPair, Relationship relationship) {
        for (Pair<Phrase, Phrase> p : wordPair) {
            Phrase p1 = p.getKey();
            Phrase p2 = p.getValue();
            graph.addVertex(p1);
            graph.addVertex(p2);
            addEdgeWithNoDup(p1, p2, new LabeledEdge(p1, p2, relationship));
            addEdgeWithNoDup(p2, p1, new LabeledEdge(p2, p1, relationship));
        }
    }

    private void addHypernym(Map<Phrase, List<Phrase>> hypernyms) {
        for (Phrase parent : hypernyms.keySet()) {
            List<Phrase> phrases = hypernyms.get(parent);
            if(parent.equals(new Phrase("design")))
                System.out.print("here");
            graph.addVertex(parent);
            for (Phrase p : phrases) {
                graph.addVertex(p);
                //addEdgeWithNoDup(p, parent, new WordGraph.LabeledEdge(p, parent, WordGraph.Relationship.HYPERNON));
                addEdgeWithNoDup(parent, p, new LabeledEdge(parent, p, Relationship.HYPERNYM));
            }

            for (int i = 0; i < phrases.size(); i++) {
                for (int j = 0; j < phrases.size(); j++) {
                    if (i == j)
                        continue;
                    Phrase p1 = phrases.get(i);
                    Phrase p2 = phrases.get(j);
                    addEdgeWithNoDup(p1, p2, new LabeledEdge(p1, p2, Relationship.CONTRAST));
                    addEdgeWithNoDup(p2, p1, new LabeledEdge(p2, p1, Relationship.CONTRAST));
                }
            }
        }
    }

    private boolean addEdgeWithNoDup(Phrase p1, Phrase p2, LabeledEdge<Phrase> edge) {
        if (p1.equals(p2))
            return false;
        if (!graph.containsEdge(edge)) {
            graph.addEdge(p1, p2, edge);
            return true;
        }
        return false;
    }

    public List<LabeledEdge> filterEdge(String word, Relationship relationship, boolean isIncomingEdge) {
        Set<LabeledEdge> edges;
        if (isIncomingEdge)
            edges = graph.incomingEdgesOf(new Phrase(word));
        else
            edges = graph.outgoingEdgesOf(new Phrase(word));

        List<LabeledEdge> res = new ArrayList<LabeledEdge>();
        for (LabeledEdge<Phrase> edge : edges) {
            if (edge.getRelationLabel().equals(relationship)) {
                res.add(edge);
            }
        }
        return res;
    }

    private List<String> getEdgeSources(List<LabeledEdge> edges) {
        List<String> res = new ArrayList<String>();
        for (LabeledEdge edge : edges) {
            res.add(edge.getV1().toString());
        }
        return res;
    }

    private List<String> getEdgeTarget(List<LabeledEdge> edges) {
        List<String> res = new ArrayList<String>();
        for (LabeledEdge edge : edges) {
            res.add(edge.getV2().toString());
        }
        return res;
    }

    public String getHypernym(String word) {
        List<LabeledEdge> edges = filterEdge(word, Relationship.HYPERNYM, true);
        return String.join(",", getEdgeSources(edges));
    }

    public String getContrast(String word) {
        List<LabeledEdge> edges = filterEdge(word, Relationship.CONTRAST, true);
        return String.join(",", getEdgeSources(edges));
    }

    public String getSynonym(String word) {
        List<LabeledEdge> edges = filterEdge(word, Relationship.SYNONYM, true);
        return String.join(",", getEdgeSources(edges));
    }

    public String getAcronym(String word) {
        List<LabeledEdge> edges = filterEdge(word, Relationship.ACRONYM, false);
        return String.join("", getEdgeTarget(edges));
    }

    public String getHypernon(String word) {
        List<LabeledEdge> edges = filterEdge(word, Relationship.HYPERNYM, false);
        return String.join(",", getEdgeTarget(edges));
    }

    private void ancestorDFS(String word, Set<String> ans) {
        List<LabeledEdge> edges = filterEdge(word, Relationship.HYPERNYM, true);
        List<String> parents = getEdgeSources(edges);
        for (String parent : parents) {
            if (!ans.contains(parent)) {
                ans.add(parent);
                ancestorDFS(parent, ans);
            }
        }
    }

    public Set<String> getAncestors(String word) {
        Set<String> ancestors = new HashSet<String>();
        ancestorDFS(word, ancestors);
        ancestors.remove(word);
        return ancestors;
    }

    public void printAncestors(String word) {
        System.out.println("Printing ancestors:");
        for(String parent: getAncestors(word)){
            System.out.println(parent);
        }
    }

    public void query(String word) {
        try {
            Set<LabeledEdge> edges = graph.edgesOf(new Phrase(word));
            System.out.println("===========================");
            System.out.println("Hypernon:" + getHypernon(word));
            System.out.println("Contrast:" + getContrast(word));
            System.out.println("Hypernym:" + getHypernym(word));
            System.out.println("Synonym:" + getSynonym(word));
            System.out.println("Acronym:" + getAcronym(word));
            System.out.println("===========================");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        WordGraph wg = new WordGraph();
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.print("Input the word you want to query about:");
            String line = input.nextLine();
            //wg.printAncestors(line);
            wg.query(line);
            //wg.query(line);
        }
    }
}
