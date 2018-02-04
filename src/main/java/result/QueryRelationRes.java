package result;

import phrases.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class QueryRelationRes extends Result {
    Phrase phrase;
    List<String> hypernym, hypernon, contrast, synonym, acronym, related;

    public QueryRelationRes(Phrase phrase) {
        super();
        this.phrase = phrase;
        hypernon = new ArrayList<>();
        hypernym = new ArrayList<>();
        contrast = new ArrayList<>();
        synonym = new ArrayList<>();
        acronym = new ArrayList<>();
        related = new ArrayList<>();
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    public List<String> getHypernym() {
        return hypernym;
    }

    public void setHypernym(List<String> hypernym) {
        this.hypernym = hypernym;
    }

    public List<String> getHypernon() {
        return hypernon;
    }

    public void setHypernon(List<String> hypernon) {
        this.hypernon = hypernon;
    }

    public List<String> getContrast() {
        return contrast;
    }

    public void setContrast(List<String> contrast) {
        this.contrast = contrast;
    }

    public List<String> getSynonym() {
        return synonym;
    }

    public void setSynonym(List<String> synonym) {
        this.synonym = synonym;
    }

    public List<String> getAcronym() {
        return acronym;
    }

    public void setAcronym(List<String> acronym) {
        this.acronym = acronym;
    }

    public List<String> getRelated() {
        return related;
    }

    public void setRelated(List<String> related) {
        this.related = related;
    }

    @Override
    public String toString() {
        StringJoiner str = new StringJoiner("\n");
        str.add("===========================");
        str.add(phrase.toString());
        str.add("--------------------------");
        str.add("Hypernym:" + getHypernym().toString());
        str.add("Hypernon" + getHypernon().toString());
        str.add("Sibling/Contrast" + getContrast());
        str.add("Synonym:" + getSynonym());
        str.add("Acronym:" + getAcronym());
        str.add("Related:" + getRelated());
        str.add("===========================");
        return str.toString();
    }
}
