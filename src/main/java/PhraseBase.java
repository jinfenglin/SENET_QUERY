import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PhraseBase {
    public List<Pair<Phrase, Phrase>> synonyms, contrasts, acronyms;
    public Map<Phrase, List<Phrase>> hypernyms;
    ConfigureManger cfMgr;

    public PhraseBase() throws Exception {
        cfMgr = ConfigureManger.getConfigureManger();
        synonyms = parseWordPairs(cfMgr.synonymPath);
        contrasts = parseWordPairs(cfMgr.contrastPath);
        acronyms = parseWordPairs(cfMgr.acronymPath);
        hypernyms = parseWordList(cfMgr.hyperPath);
    }

    private Map<Phrase, List<Phrase>> parseWordList(String filePath) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        Map<Phrase, List<Phrase>> res = new HashMap<Phrase, List<Phrase>>();
        String line;
        while ((line = bf.readLine()) != null) {
            String[] words = line.split(":");
            Phrase p1 = new Phrase(words[0]);
            List<Phrase> pList = new ArrayList<Phrase>();
            String[] wordP2 = words[1].split(",");
            for (int i = 0; i < wordP2.length; i++) {
                Phrase p = new Phrase(wordP2[i]);
                pList.add(p);
            }
            if(res.containsKey(p1)){
                    pList.addAll(res.get(p1));
            }
            res.put(p1, pList);

        }
        bf.close();
        return res;
    }

    private List<Pair<Phrase, Phrase>> parseWordPairs(String filePath) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        List<Pair<Phrase, Phrase>> res = new ArrayList<Pair<Phrase, Phrase>>();
        String line;
        while ((line = bf.readLine()) != null) {
            String[] words = line.split(",");
            Phrase p1 = new Phrase(words[0]);
            Phrase p2 = new Phrase(words[1]);
            Pair pair = new Pair(p1, p2);
            res.add(pair);
        }
        bf.close();
        return res;
    }


}
