package wordGraph;

public class SearchConfig {
    public String word;
    public Relationship relationship;
    boolean getIncomingEdge, getEdgeSource;

    public SearchConfig(String word, Relationship relationship, Boolean getIncomingEdge, Boolean getEdgeSource) {
        this.word = word;
        this.relationship = relationship;
        this.getIncomingEdge = getIncomingEdge;
        this.getEdgeSource = getEdgeSource;
    }
}
