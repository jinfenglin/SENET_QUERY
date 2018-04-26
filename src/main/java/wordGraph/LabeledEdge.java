package wordGraph;

import org.jgrapht.graph.DefaultEdge;

public class LabeledEdge<V> extends DefaultEdge {
    private V v1;
    private V v2;
    private Relationship relationLabel;
    private double score;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabeledEdge)) return false;

        LabeledEdge<?> that = (LabeledEdge<?>) o;

        if (getV1() != null ? !getV1().equals(that.getV1()) : that.getV1() != null) return false;
        if (getV2() != null ? !getV2().equals(that.getV2()) : that.getV2() != null) return false;
        return relationLabel == that.relationLabel;
    }

    @Override
    public int hashCode() {
        int result = getV1() != null ? getV1().hashCode() : 0;
        result = 31 * result + (getV2() != null ? getV2().hashCode() : 0);
        result = 31 * result + (relationLabel != null ? relationLabel.hashCode() : 0);
        return result;
    }

    public LabeledEdge(V v1, V v2, Relationship label, double score) {
        this.v1 = v1;
        this.v2 = v2;
        this.relationLabel = label;
        this.score = score;
    }

    public Relationship getRelationLabel() {
        return relationLabel;
    }

    public V getV1() {
        return v1;
    }

    public V getV2() {
        return v2;
    }

    public String toString() {
        return relationLabel.name();
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}

