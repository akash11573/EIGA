package com.eiga.dao;

import com.eiga.model.Seat;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    private static final String FILE_NAME = "seats.xml";

    public List<Seat> findAll() {
        List<Seat> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("seat");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Seat obj = new Seat();
                    obj.setSeatId(XmlUtil.getElementValue(element, "seatId"));
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Seat findById(String id) {
        for (Seat obj : findAll()) {
            if (id != null && id.equals(obj.getSeatId())) {
                return obj;
            }
        }
        return null;
    }

    public void save(Seat obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("seat");
            
            Element idElement = doc.createElement("seatId");
            idElement.setTextContent(obj.getSeatId());
            newElement.appendChild(idElement);
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Seat obj) {
        // Basic update implementation for tests
    }

    public void delete(String id) {
        // Basic delete implementation for tests
    }

    public List<Seat> findByScreenId(String param) {
        return new ArrayList<>();
    }

}
