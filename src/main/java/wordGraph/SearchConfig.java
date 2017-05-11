package wordGraph;

public enum SearchConfig {
    hyperSeach(Relationship.HYPERNYM, true),
    hyponSearch(Relationship.HYPERNYM, false),
    acrSearch(Relationship.ACRONYM, false),
    synSearch(Relationship.SYNONYM, true),
    conSearch(Relationship.CONTRAST, true);

    public Relationship relationship;
    boolean getIncomingEdge;

    SearchConfig(Relationship relationship, Boolean getIncomingEdge) {
        this.relationship = relationship;
        this.getIncomingEdge = getIncomingEdge;
    }
}
