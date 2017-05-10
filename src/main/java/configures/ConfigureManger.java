package configures;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ConfigureManger {
    protected static ConfigureManger cfMgr;
    private static String configSource = "src/main/resources/wordGraphConfig.conf";
    private DocumentBuilder builder;
    private Document document;
    public String acronymPath, contrastPath, hyperPath, relatedPath, synonymPath, vocabularyPath;

    private ConfigureManger() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        updateConfigure();
    }

    public static ConfigureManger getConfigureManger() throws Exception {
        if (cfMgr == null) {
            cfMgr = new ConfigureManger();
        }
        return cfMgr;
    }

    public void setConfigureFileSource(String path) {
        configSource = path;
    }

    public void updateConfigure() throws Exception {
        document = builder.parse(new File(configSource));
        acronymPath = document.getElementsByTagName("acronym").item(0).getTextContent();
        contrastPath = document.getElementsByTagName("contrast").item(0).getTextContent();
        hyperPath = document.getElementsByTagName("hyper").item(0).getTextContent();
        synonymPath = document.getElementsByTagName("synonym").item(0).getTextContent();
        vocabularyPath = document.getElementsByTagName("vocabulary").item(0).getTextContent();

    }

    public String getConfigureSource() {
        return configSource;
    }

}
