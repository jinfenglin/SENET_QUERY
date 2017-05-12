package configures;


import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DataSet {
    public String acronymPath, contrastPath, hyperPath, relatedPath, synonymPath, vocabularyPath;
    String type;

    public DataSet(String acronymPath, String contrastPath, String hyperPath, String relatedPath, String synonymPath, String vocabularyPath, String type) {
        this.acronymPath = acronymPath;
        this.contrastPath = contrastPath;
        this.hyperPath = hyperPath;
        this.relatedPath = relatedPath;
        this.synonymPath = synonymPath;
        this.vocabularyPath = vocabularyPath;
        this.type = type;
    }

    public DataSet(Node dataNode) {
        Element enode = (Element) dataNode;
        type = enode.getAttribute("type");
        acronymPath = enode.getElementsByTagName("acronym").item(0).getTextContent();
        contrastPath = enode.getElementsByTagName("contrast").item(0).getTextContent();
        hyperPath = enode.getElementsByTagName("hyper").item(0).getTextContent();
        synonymPath = enode.getElementsByTagName("synonym").item(0).getTextContent();
        vocabularyPath = enode.getElementsByTagName("vocabulary").item(0).getTextContent();
        relatedPath = enode.getElementsByTagName("related").item(0).getTextContent();
    }
}
