package com.eiga.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XmlUtil {
    
    private static final String DATA_DIR = "data"; // Relative to project root / tomcat bin if not careful, but for this project we'll assume it's set correctly.
    // To ensure safety, we can use absolute path or a property, but for now we'll use a constant based on the expected path.
    // In a real web app, we'd use ServletContext.getRealPath, but this is a simple utility.
    
    public static File getDataFile(String filename) {
        // Find the absolute path to the project data directory
        // For development, assuming we run from EIGA/
        File file = new File("/Users/akashkumar/Desktop/EIGA/data", filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static synchronized Document loadDocument(String filename) throws Exception {
        File file = getDataFile(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        if (!file.exists()) {
            Document doc = dBuilder.newDocument();
            String rootElement = filename.substring(0, filename.lastIndexOf('.'));
            Element root = doc.createElement(rootElement);
            doc.appendChild(root);
            saveDocument(doc, filename);
            return doc;
        }
        return dBuilder.parse(file);
    }

    public static synchronized void saveDocument(Document doc, String filename) throws Exception {
        File file = getDataFile(filename);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
    
    public static String getElementValue(Element parent, String tagName) {
        if (parent.getElementsByTagName(tagName).getLength() > 0) {
            return parent.getElementsByTagName(tagName).item(0).getTextContent();
        }
        return null;
    }
}
