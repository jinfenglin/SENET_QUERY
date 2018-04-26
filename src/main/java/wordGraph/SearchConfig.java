package wordGraph;

public enum SearchConfig {
    hyperSeach(Relationship.HYPERNYM, true),
    hyponSearch(Relationship.HYPERNYM, false),
    acrSearch(Relationship.ACRONYM, true),
    synSearch(Relationship.SYNONYM, true),
    conSearch(Relationship.CONTRAST, true),
    relatedSearch(Relationship.RELATED, true),
    anySearch(Relationship.ANY, true);

    public Relationship relationship;
    boolean getIncomingEdge;
    public String dataType;

     SearchConfig(Relationship relationship, Boolean getIncomingEdge, String dataType) {
        this(relationship, getIncomingEdge);
        this.dataType = dataType;
    }

    public SearchConfig setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    SearchConfig(Relationship relationship, Boolean getIncomingEdge) {
        this.relationship = relationship;
        this.getIncomingEdge = getIncomingEdge;
    }
}
