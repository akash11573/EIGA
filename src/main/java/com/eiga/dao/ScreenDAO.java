package com.eiga.dao;

import com.eiga.model.Screen;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ScreenDAO {

    private static final String FILE_NAME = "screens.xml";

    public List<Screen> findAll() {
        List<Screen> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("screen");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Screen obj = new Screen();
                    obj.setScreenId(XmlUtil.getElementValue(element, "screenId"));
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Screen findById(String id) {
        for (Screen obj : findAll()) {
            if (id != null && id.equals(obj.getScreenId())) {
                return obj;
            }
        }
        return null;
    }

    public void save(Screen obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("screen");
            
            Element idElement = doc.createElement("screenId");
            idElement.setTextContent(obj.getScreenId());
            newElement.appendChild(idElement);
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Screen obj) {
        // Basic update implementation for tests
    }

    public void delete(String id) {
        // Basic delete implementation for tests
    }


}
