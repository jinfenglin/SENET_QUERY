package wordGraph;

import commands.CommandManger;
import phrases.Phrase;
import phrases.PhraseBase;
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
        addPhraseToGraph(phraseBase.vocabulary);
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

    private void addPhraseToGraph(List<Phrase> words) {
        for (Phrase phrase : words) {
            graph.addVertex(phrase);
        }
    }

    private void addHypernym(Map<Phrase, List<Phrase>> hypernyms) {
        for (Phrase parent : hypernyms.keySet()) {
            List<Phrase> phrases = hypernyms.get(parent);
            graph.addVertex(parent);
            for (Phrase p : phrases) {
                graph.addVertex(p);
                //addEdgeWithNoDup(p, parent, new wordGraph.LabeledEdge(p, parent, wordGraph.Relationship.HYPERNON));
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
            res.add(getPhrase(edge.getV1().toString()));
        }
        return res;
    }

    private List<String> getEdgeTarget(List<LabeledEdge> edges) {
        List<String> res = new ArrayList<String>();
        for (LabeledEdge edge : edges) {
            res.add(getPhrase(edge.getV2().toString()));
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
        return String.join(",", getEdgeTarget(edges));
    }

    public String getHyponymy(String word) {
        List<LabeledEdge> edges = filterEdge(word, Relationship.HYPERNYM, false);
        return String.join(",", getEdgeTarget(edges));
    }

    /**
     * Deep first search for a word. The search go through only one kind of relationship with one direction.
     *
     * @param config
     * @param ans
     */
    private void DFS(String word, SearchConfig config, Set<String> ans) {
        List<String> candidates = filterEdgeGetConnectedWords(word, config);
        for (String candidate : candidates) {
            if (!ans.contains(candidate)) {
                ans.add(candidate);
                DFS(word, config, ans);
            }
        }
    }

    private List<String> filterEdgeGetConnectedWords(String word, SearchConfig config) {
        List<LabeledEdge> edges = filterEdge(word, config.relationship, config.getIncomingEdge);
        List<String> connected;
        if (config.getIncomingEdge)
            connected = getEdgeSources(edges);
        else
            connected = getEdgeTarget(edges);
        return connected;
    }

    private void DFSPath(String source, String target, SearchConfig config, Set<String> visited, List<String> sol, List<List<String>> res) {
        source = getPhrase(source);
        target = getPhrase(target);
        if(sol.size()>100)//avoid some time consuming search
            return;
        if (source.equals(target)) {
            res.add(new ArrayList<String>(sol));
            return;
        }
        List<String> candidates = filterEdgeGetConnectedWords(source, config);
        visited.add(source);
        for (String candidate : candidates) {
            if (visited.contains(candidate))
                continue;
            sol.add(candidate);
            DFSPath(candidate, target, config, visited, sol, res);
            sol.remove(candidate);
        }
    }

    public Set<String> search(String word, SearchConfig searchConfig) {
        Set<String> res = new HashSet<String>();
        word = getPhrase(word);
        if (word.isEmpty())
            return res;
        DFS(word, searchConfig, res);
        res.remove(word);
        return res;
    }

    public List<List<String>> searchPath(String source, String target, SearchConfig searchConfig) {
        List<List<String>> res = new ArrayList<>();
        source = getPhrase(source);
        target = getPhrase(target);
        if (source.isEmpty() | target.isEmpty())
            return res;
        DFSPath(source, target, searchConfig, new HashSet<String>(), new ArrayList<>(), res);
        return res;
    }

    public String getPhrase(String word) {
        Phrase phrase = new Phrase(word);
        List<Phrase> candidates = new ArrayList<>();
        if (graph.vertexSet().contains(phrase)) {
            for (Phrase p : graph.vertexSet()) {
                if (p.equals(phrase))
                    candidates.add(p);
            }
        }
        for(Phrase p: candidates){
            if(p.toString().equals(word))
                return word;
        }
        if(candidates.size()>0)
            return candidates.get(0).toString();
        return "";
    }


    public static void main(String[] args) throws Exception {
        WordGraph wg = new WordGraph();
        CommandManger cmdManger = new CommandManger(wg);
        cmdManger.run();
    }
}
