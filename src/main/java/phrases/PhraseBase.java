package phrases;

import configures.ConfigureManger;
import configures.DataSet;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PhraseBase {
    public List<PhrasePairWithScore> synonyms, contrasts, acronyms, related;
    public Map<Phrase, List<Pair<Phrase, Double>>> hypernyms;
    public List<Phrase> vocabulary;
    ConfigureManger cfMgr;

    public PhraseBase() throws Exception {
        cfMgr = ConfigureManger.getConfigureManger();
        synonyms = new ArrayList<>();
        contrasts = new ArrayList<>();
        acronyms = new ArrayList<>();
        related = new ArrayList<>();
        hypernyms = new HashMap<>();
        vocabulary = new ArrayList<>();
        for (String dataType : cfMgr.dataSets.keySet()) {
            DataSet ds = cfMgr.dataSets.get(dataType);
            synonyms.addAll(parseWordPairs(ds.synonymPath, dataType));
            contrasts.addAll(parseWordPairs(ds.contrastPath, dataType));
            acronyms.addAll(parseWordPairs(ds.acronymPath, dataType));
            hypernyms.putAll(parseWordList(ds.hyperPath, dataType));
            vocabulary.addAll(parseWord(ds.vocabularyPath, dataType));
            related.addAll(parseWordPairs(ds.relatedPath, dataType));
            related.addAll(synonyms);
            related.addAll(contrasts);
            related.addAll(acronyms);
        }
    }

    private List<Phrase> parseWord(String filePath, String dataType) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        List<Phrase> vocList = new ArrayList<>();
        String line;
        while ((line = bf.readLine()) != null) {
            vocList.add(new Phrase(line, dataType));
        }
        bf.close();
        return vocList;
    }

    /**
     * Tempoary fix to add score to hypernym. Since we don't generate hypernym automatically right now. We just assign
     * score = 1 to them
     *
     * @param filePath
     * @param dataType
     * @return
     * @throws Exception
     */
    private Map<Phrase, List<Pair<Phrase, Double>>> parseWordList(String filePath, String dataType) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        Map<Phrase, List<Phrase>> res = new HashMap<>();
        String line;
        while ((line = bf.readLine()) != null) {
            String[] words = line.split(":");
            Phrase p1 = new Phrase(words[0], dataType);
            List<Phrase> pList = new ArrayList<>();
            String[] wordP2 = words[1].split(",");
            for (int i = 0; i < wordP2.length; i++) {
                Phrase p = new Phrase(wordP2[i], dataType);
                pList.add(p);
            }
            if (res.containsKey(p1)) {
                pList.addAll(res.get(p1));
            }
            res.put(p1, pList);

        }
        bf.close();
        Map<Phrase, List<Pair<Phrase, Double>>> tmp = new HashMap<>();
        for (Phrase p1 : res.keySet()) {
            List<Pair<Phrase, Double>> scorePair = new ArrayList<>();
            for (Phrase p2 : res.get(p1)) {
                scorePair.add(new Pair<>(p2, 1.0));
            }
            tmp.put(p1, scorePair);
        }
        return tmp;
    }

    private List<PhrasePairWithScore> parseWordPairs(String filePath, String dataType) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        List<PhrasePairWithScore> res = new ArrayList<>();
        String line;
        while ((line = bf.readLine()) != null) {
            String[] words = line.split(",");
            Phrase p1 = new Phrase(words[0], dataType);
            Phrase p2 = new Phrase(words[1], dataType);
            if (words.length > 3) {
                throw new Exception("Incorrect format of given document");
            }

            double score = 1.0;
            if (words.length == 3) {
                Double.valueOf(words[2]);
            }
            Pair pair = new Pair(p1, p2);
            PhrasePairWithScore pps = new PhrasePairWithScore(pair, score);
            res.add(pps);
        }
        bf.close();
        return res;
    }


}
