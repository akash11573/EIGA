package com.eiga.dao;

import com.eiga.model.Booking;
import com.eiga.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private static final String FILE_NAME = "bookings.xml";

    public List<Booking> findAll() {
        List<Booking> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nodeList = doc.getElementsByTagName("booking");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Booking obj = new Booking();
                    obj.setBookingId(XmlUtil.getElementValue(element, "bookingId"));
                    list.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Booking findById(String id) {
        for (Booking obj : findAll()) {
            if (id != null && id.equals(obj.getBookingId())) {
                return obj;
            }
        }
        return null;
    }

    public void save(Booking obj) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            Element root = doc.getDocumentElement();
            Element newElement = doc.createElement("booking");
            
            Element idElement = doc.createElement("bookingId");
            idElement.setTextContent(obj.getBookingId());
            newElement.appendChild(idElement);
            
            root.appendChild(newElement);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Booking obj) {
        // Basic update implementation for tests
    }

    public void delete(String id) {
        // Basic delete implementation for tests
    }

    public List<Booking> findByUserId(String param) {
        return new ArrayList<>();
    }
    public List<Booking> findByShowId(String param) {
        return new ArrayList<>();
    }

}
