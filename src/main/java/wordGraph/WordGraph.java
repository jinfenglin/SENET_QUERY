package wordGraph;

import commands.CommandManger;
import phrases.Phrase;
import phrases.PhraseBase;
import javafx.util.Pair;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.*;
import java.util.stream.Collectors;


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
        addPhraseToGraph(phraseBase.related, Relationship.RELATED);
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

    public List<LabeledEdge> filterEdge(Phrase word, Relationship relationship, boolean isIncomingEdge) {
        Set<LabeledEdge> edges;
        if (isIncomingEdge)
            edges = graph.incomingEdgesOf(word);
        else
            edges = graph.outgoingEdgesOf(word);

        List<LabeledEdge> res = new ArrayList<LabeledEdge>();
        for (LabeledEdge<Phrase> edge : edges) {
            if (edge.getRelationLabel().equals(relationship)) {
                res.add(edge);
            }
        }
        return res;
    }

    private List<Phrase> getEdgeSources(List<LabeledEdge> edges) {
        List<Phrase> res = new ArrayList<Phrase>();
        for (LabeledEdge edge : edges) {
            res.add((Phrase) edge.getV1());
        }
        return res;
    }

    private List<Phrase> getEdgeTarget(List<LabeledEdge> edges) {
        List<Phrase> res = new ArrayList<Phrase>();
        for (LabeledEdge edge : edges) {
            res.add((Phrase) edge.getV2());
        }
        return res;
    }

    public String getHypernym(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.HYPERNYM, true);
        return String.join(",", getEdgeSources(edges).toString());
    }

    public String getContrast(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.CONTRAST, true);
        return String.join(",", getEdgeSources(edges).toString());
    }

    public String getSynonym(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.SYNONYM, true);
        return String.join(",", getEdgeSources(edges).toString());
    }

    public String getAcronym(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.ACRONYM, false);
        return String.join(",", getEdgeTarget(edges).toString());
    }

    public String getHyponymy(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.HYPERNYM, false);
        return String.join(",", getEdgeTarget(edges).toString());
    }

    public String getRelated(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.RELATED, false);
        return String.join(",", getEdgeTarget(edges).toString());
    }

    /**
     * Deep first search for a word. The search go through only one kind of relationship with one direction.
     *
     * @param config
     * @param ans
     */
    private void DFS(Phrase word, SearchConfig config, Set<String> ans) {
        List<Phrase> candidates = filterEdgeGetConnectedWords(word, config);
        for (Phrase candidate : candidates) {
            if (!ans.contains(candidate.toString())) {
                ans.add(candidate.toString());
                DFS(word, config, ans);
            }
        }
    }

    private List<Phrase> filterEdgeGetConnectedWords(Phrase word, SearchConfig config) {
        List<LabeledEdge> edges = filterEdge(word, config.relationship, config.getIncomingEdge);
        List<Phrase> connected;
        if (config.getIncomingEdge)
            connected = getEdgeSources(edges);
        else
            connected = getEdgeTarget(edges);

        return connected.stream().filter(x -> x.getDataType().equals(config.dataType) | config.dataType == null).collect(Collectors.toList());
    }

    private void DFSPath(Phrase source, Phrase target, SearchConfig config, Set<String> visited, List<Phrase> sol, List<List<String>> res) {
        if (sol.size() > 100)//avoid some time consuming search
            return;
        if (source.equals(target)) {
            List<String> tmp = new ArrayList<>();
            sol.stream().forEach(x -> tmp.add(x.toString()));
            res.add(tmp);
            return;
        }
        List<Phrase> candidates = filterEdgeGetConnectedWords(source, config);
        visited.add(source.toString());
        for (Phrase candidate : candidates) {
            if (visited.contains(candidate.toString()))
                continue;
            sol.add(candidate);
            DFSPath(candidate, target, config, visited, sol, res);
            sol.remove(candidate);
        }
    }

    public Set<String> search(String word, SearchConfig searchConfig) {
        Set<String> res = new HashSet<String>();
        Phrase wordPhrase = getPhrase(word);
        if (word == null)
            return res;
        DFS(wordPhrase, searchConfig, res);
        res.remove(word);
        return res;
    }

    public List<List<String>> searchPath(String source, String target, SearchConfig searchConfig) {
        List<List<String>> res = new ArrayList<>();
        Phrase sourcePhrase = getPhrase(source);
        Phrase targetPhrase = getPhrase(target);
        if (sourcePhrase == null | targetPhrase == null)
            return res;
        Set<String> visited = new HashSet<String>();
        visited.add(sourcePhrase.toString());
        DFSPath(sourcePhrase, targetPhrase, searchConfig, visited, new ArrayList<>(), res);
        return res;
    }

    public Phrase getPhrase(String word) {
        Phrase phrase = new Phrase(word);
        List<Phrase> candidates = new ArrayList<>();
        if (graph.vertexSet().contains(phrase)) {
            for (Phrase p : graph.vertexSet()) {
                if (p.equals(phrase))
                    candidates.add(p);
            }
        }
        for (Phrase p : candidates) {
            if (p.toString().equals(word))
                return p;
        }
        if (candidates.size() > 0)
            return candidates.get(0);
        return null;
    }


    public static void main(String[] args) throws Exception {
        WordGraph wg = new WordGraph();
        CommandManger cmdManger = new CommandManger(wg);
        cmdManger.run();
    }
}
