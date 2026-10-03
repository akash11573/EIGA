package com.eiga.dao;

import com.eiga.model.Show;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ShowDAO {

    private static final String FILE_NAME = "shows.xml";

    public List<Show> findAll() {
        List<Show> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("show");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Show obj = new Show();
                    obj.setShowId(XmlUtil.getElementValue(element, "showId"));
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Show findById(String id) {
        for (Show obj : findAll()) {
            if (id != null && id.equals(obj.getShowId())) {
                return obj;
            }
        }
        return null;
    }

    public void save(Show obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("show");
            
            Element idElement = doc.createElement("showId");
            idElement.setTextContent(obj.getShowId());
            newElement.appendChild(idElement);
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Show obj) {
        // Basic update implementation for tests
    }

    public void delete(String id) {
        // Basic delete implementation for tests
    }

    public List<Show> findByTheatreId(String param) {
        return new ArrayList<>();
    }
    public List<Show> findByScreenId(String param) {
        return new ArrayList<>();
    }
    public List<Show> findByMovieId(String param) {
        return new ArrayList<>();
    }

}
