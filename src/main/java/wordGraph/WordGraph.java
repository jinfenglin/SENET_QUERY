package wordGraph;

import commands.CommandManger;
import phrases.*;
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
        graph = new DirectedPseudograph<>(
                new ClassBasedEdgeFactory<>(LabeledEdge.class));
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

    private void addPhraseToGraph(List<PhrasePairWithScore> wordPair, Relationship relationship) {
        for (PhrasePairWithScore p : wordPair) {
            Phrase p1 = p.getPair().getKey();
            Phrase p2 = p.getPair().getValue();
            graph.addVertex(p1);
            graph.addVertex(p2);
            addEdgeWithNoDup(p1, p2, new LabeledEdge(p1, p2, relationship, p.getScore()));
            addEdgeWithNoDup(p2, p1, new LabeledEdge(p2, p1, relationship, p.getScore()));
        }
    }

    private void addPhraseToGraph(List<Phrase> words) {
        for (Phrase phrase : words) {
            graph.addVertex(phrase);
        }
    }

    private void addHypernym(Map<Phrase, List<Pair<Phrase, Double>>> hypernyms) {
        for (Phrase parent : hypernyms.keySet()) {
            List<Pair<Phrase, Double>> phrases = hypernyms.get(parent);
            graph.addVertex(parent);
            for (Pair<Phrase, Double> ps : phrases) {
                Phrase p = ps.getKey();
                double score = ps.getValue();
                graph.addVertex(p);
                //addEdgeWithNoDup(p, parent, new wordGraph.LabeledEdge(p, parent, wordGraph.Relationship.HYPERNON));
                addEdgeWithNoDup(parent, p, new LabeledEdge(parent, p, Relationship.HYPERNYM, score));
            }

            //Add siblings
            for (int i = 0; i < phrases.size(); i++) {
                for (int j = 0; j < phrases.size(); j++) {
                    if (i == j)
                        continue;
                    Phrase p1 = phrases.get(i).getKey();
                    Phrase p2 = phrases.get(j).getKey();
                    addEdgeWithNoDup(p1, p2, new LabeledEdge(p1, p2, Relationship.CONTRAST, 1.0));
                    addEdgeWithNoDup(p2, p1, new LabeledEdge(p2, p1, Relationship.CONTRAST, 1.0));
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
            if (edge.getRelationLabel().equals(relationship) || relationship == Relationship.ANY) {
                res.add(edge);
            }
        }
        return res;
    }

    private List<PhraseWithScore> getEdgeSources(List<LabeledEdge> edges) {
        List<PhraseWithScore> res = new ArrayList<>();
        for (LabeledEdge edge : edges) {
            res.add(new PhraseWithScore((Phrase) edge.getV1(), edge.getScore()));
        }
        return res;
    }

    private List<PhraseWithScore> getEdgeTarget(List<LabeledEdge> edges) {
        List<PhraseWithScore> res = new ArrayList<>();
        for (LabeledEdge edge : edges) {
            res.add(new PhraseWithScore((Phrase) edge.getV2(), edge.getScore()));
        }
        return res;
    }

    private List<String> edgeSourceToStrings(List<LabeledEdge> edges) {
        return getEdgeSources(edges).stream().map(p -> p.toString()).collect(Collectors.toList());
    }

    private List<String> edgeTargetToStrings(List<LabeledEdge> edges) {
        return getEdgeTarget(edges).stream().map(p -> p.toString()).collect(Collectors.toList());
    }

    public List<String> getHypernym(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.HYPERNYM, true);
        return edgeSourceToStrings(edges);
    }

    public List<String> getContrast(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.CONTRAST, true);
        return edgeSourceToStrings(edges);
    }

    public List<String> getSynonym(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.SYNONYM, true);
        return edgeSourceToStrings(edges);
    }

    public List<String> getAcronym(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.ACRONYM, false);
        return edgeTargetToStrings(edges);
    }

    public List<String> getHyponymy(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.HYPERNYM, false);
        return edgeTargetToStrings(edges);
    }

    public List<String> getRelated(String word) {
        List<LabeledEdge> edges = filterEdge(new Phrase(word), Relationship.RELATED, false);
        return edgeTargetToStrings(edges);
    }

//    /**
//     * Deep first search for a word. The search go through only one kind of relationship with one direction.
//     *
//     * @param config
//     * @param ans
//     */
//    private void DFS(Phrase word, SearchConfig config, Set<String> ans) {
//        List<Phrase> candidates = filterEdgeGetConnectedWords(word);
//        for (Phrase candidate : candidates) {
//            if (!ans.contains(candidate.toString())) {
//                ans.add(candidate.toString());
//                DFS(word, config, ans);
//            }
//        }
//    }

    /**
     * Return a list of adjacent nodes and the score indicating a relevance.
     *
     * @param word
     * @return
     */
    private List<PhraseWithScore> filterEdgeGetConnectedWords(Phrase word) {
        Set<LabeledEdge> inComingEdges = new HashSet<>();
        Set<LabeledEdge> outComingEdges = new HashSet<>();
        inComingEdges.addAll(graph.incomingEdgesOf(word));
        List<PhraseWithScore> connected = getEdgeSources(new ArrayList<>(inComingEdges));

        outComingEdges.addAll(graph.outgoingEdgesOf(word));
        connected.addAll(getEdgeTarget(new ArrayList<>(outComingEdges)));
        return connected;
    }

//    private void DFSPath(Phrase source, Phrase target, Set<String> visited, List<Phrase> sol, List<List<String>> res) {
//        if (sol.size() > 5)//avoid some time consuming search
//            return;
//        if (source.equals(target)) {
//            List<String> tmp = new ArrayList<>();
//            sol.stream().forEach(x -> tmp.add(x.toString()));
//            tmp.add(target.toString());
//            res.add(tmp);
//            return;
//        }
//        Set<Phrase> candidates = new HashSet<>(filterEdgeGetConnectedWords(source));
//        visited.add(source.toString());
//        sol.add(source);
//        for (Phrase candidate : candidates) {
//            if (visited.contains(candidate.toString()))
//                continue;
//            DFSPath(candidate, target, visited, sol, res);
//        }
//        sol.remove(source);
//        visited.remove(source.toString());
//    }

    private List<String> BFS(Phrase source, Phrase target) {
        List<PathWithScore> res = new ArrayList<>();
        List<PhraseWithScore> start = new ArrayList<>();
        int foundPathLen = Integer.MAX_VALUE;
        start.add(new PhraseWithScore(source, 1.0));
        List<List<PhraseWithScore>> searchQueue = new ArrayList<>();
        searchQueue.add(start);
        while (searchQueue.size() > 0) {
            List<PhraseWithScore> path = searchQueue.remove(0);
            if (path.size() > 10)
                return new ArrayList<>();
            Set<Phrase> visited = new HashSet<>();
            path.stream().forEach(x -> visited.add(x.getPhrase()));
            Phrase lastPhrase = path.get(path.size() - 1).getPhrase();
            Set<PhraseWithScore> candidates = new HashSet<>(filterEdgeGetConnectedWords(lastPhrase));
            for (PhraseWithScore candidate : candidates) {
                if (visited.contains(candidate.getPhrase())) {
                    continue;
                }
                List<PhraseWithScore> newPath = new ArrayList<>(path);
                newPath.add(candidate);
                searchQueue.add(newPath);
                if (candidate.getPhrase().equals(target)) {
                    newPath.add(candidate);
                    if (newPath.size() > foundPathLen)
                        break;
                    PathWithScore tmp = new PathWithScore(newPath);
                    foundPathLen = tmp.getPath().size();
                    res.add(tmp);
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Explore all the paths and give a score of the path
     *
     * @return
     */
    private List<Pair<List<String>, Double>> explore(Phrase source, int maxLen) {
        return null;
    }

//    public Set<String> search(String word, SearchConfig searchConfig) {
//        Set<String> res = new HashSet<String>();
//        Phrase wordPhrase = getPhrase(word);
//        if (word == null)
//            return res;
//        DFS(wordPhrase, searchConfig, res);
//        res.remove(word);
//        return res;
//    }

    public List<List<String>> searchPath(String source, String target) {
        List<List<String>> res = new ArrayList<>();
        Phrase sourcePhrase = getPhrase(source);
        Phrase targetPhrase = getPhrase(target);
        if (sourcePhrase == null | targetPhrase == null)
            return res;
        Set<String> visited = new HashSet<>();
//        DFSPath(sourcePhrase, targetPhrase, visited, new ArrayList<>(), res);
//        Collections.sort(res, new Comparator<List<String>>() {
//            @Override
//            public int compare(List<String> o1, List<String> o2) {
//                return o1.size() - o2.size();
//            }
//        });
        res.add(BFS(sourcePhrase, targetPhrase));
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
