import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServletConfigParser {

    Map<String, String> servletNameClassMapping;

    Map<String, String> servletUrlNameMapping;

    public ServletConfigParser() {
        servletNameClassMapping = new HashMap<>();
        servletUrlNameMapping = new HashMap<>();
    }

    private void parse() {
        File file  = new File("web.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
             builder = factory.newDocumentBuilder();
             Document document = builder.parse(file);
             NodeList servletInfo = document.getChildNodes();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
