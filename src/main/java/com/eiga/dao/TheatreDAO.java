package com.eiga.dao;

import com.eiga.model.Theatre;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class TheatreDAO {

    private static final String FILE_NAME = "theatres.xml";

    public List<Theatre> findAll() {
        List<Theatre> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("theatre");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Theatre obj = new Theatre();
                    obj.setTheatreId(XmlUtil.getElementValue(element, "theatreId"));
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Theatre findById(String id) {
        for (Theatre obj : findAll()) {
            if (id != null && id.equals(obj.getTheatreId())) {
                return obj;
            }
        }
        return null;
    }

    public void save(Theatre obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("theatre");
            
            Element idElement = doc.createElement("theatreId");
            idElement.setTextContent(obj.getTheatreId());
            newElement.appendChild(idElement);
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Theatre obj) {
        // Basic update implementation for tests
    }

    public void delete(String id) {
        // Basic delete implementation for tests
    }


}
