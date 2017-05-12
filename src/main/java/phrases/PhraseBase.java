package phrases;

import configures.ConfigureManger;
import configures.DataSet;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PhraseBase {
    public List<Pair<Phrase, Phrase>> synonyms, contrasts, acronyms, related;
    public Map<Phrase, List<Phrase>> hypernyms;
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

    private Map<Phrase, List<Phrase>> parseWordList(String filePath, String dataType) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        Map<Phrase, List<Phrase>> res = new HashMap<Phrase, List<Phrase>>();
        String line;
        while ((line = bf.readLine()) != null) {
            String[] words = line.split(":");
            Phrase p1 = new Phrase(words[0], dataType);
            List<Phrase> pList = new ArrayList<Phrase>();
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
        return res;
    }

    private List<Pair<Phrase, Phrase>> parseWordPairs(String filePath, String dataType) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        List<Pair<Phrase, Phrase>> res = new ArrayList<Pair<Phrase, Phrase>>();
        String line;
        while ((line = bf.readLine()) != null) {
            String[] words = line.split(",");
            Phrase p1 = new Phrase(words[0], dataType);
            Phrase p2 = new Phrase(words[1], dataType);
            Pair pair = new Pair(p1, p2);
            res.add(pair);
        }
        bf.close();
        return res;
    }


}
