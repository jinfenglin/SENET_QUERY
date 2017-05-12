package configures;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigureManger {
    protected static ConfigureManger cfMgr;
    private static String configSource = "src/main/resources/wordGraphConfig.conf";
    private DocumentBuilder builder;
    private Document document;
    public Map<String, DataSet> dataSets;

    private ConfigureManger() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        dataSets = new HashMap<>();
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
        NodeList dataNodes = document.getElementsByTagName("dataSet");
        for(int i=0;i<dataNodes.getLength();i++) {
            Node dataNode = dataNodes.item(i);
            DataSet ds = new DataSet(dataNode);
            dataSets.put(ds.type, ds);
        }
    }

    public String getConfigureSource() {
        return configSource;
    }

}
