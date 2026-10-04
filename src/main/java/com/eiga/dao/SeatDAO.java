package com.eiga.dao;
import com.eiga.model.Seat;
import com.eiga.util.XmlUtil;
import org.w3c.dom.*;
import java.util.*;
public class SeatDAO {
    public List<Seat> findAll() {
        List<Seat> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument("seats.xml");
            NodeList nl = doc.getElementsByTagName("seat");
            for(int i=0; i<nl.getLength(); i++) {
                if(nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nl.item(i);
                    Seat s = new Seat();
                    s.setSeatId(XmlUtil.getElementValue(e, "seatId"));
                    s.setScreenId(XmlUtil.getElementValue(e, "screenId"));
                    s.setRow(XmlUtil.getElementValue(e, "row"));
                    s.setCategory(XmlUtil.getElementValue(e, "category"));
                    s.setStatus(XmlUtil.getElementValue(e, "status"));
                    String num = XmlUtil.getElementValue(e, "number");
                    if(num != null && !num.isEmpty() && !num.equals("null")) s.setNumber(Integer.parseInt(num));
                    String pr = XmlUtil.getElementValue(e, "price");
                    if(pr != null && !pr.isEmpty() && !pr.equals("null")) s.setPrice(Double.parseDouble(pr));
                    list.add(s);
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return list;
    }
    public Seat findById(String id) {
        for(Seat s : findAll()) if(id.equals(s.getSeatId())) return s;
        return null;
    }
    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name); child.setTextContent(value == null ? "" : value); parent.appendChild(child);
    }
    public void save(Seat s) {
        try {
            Document doc = XmlUtil.loadDocument("seats.xml");
            NodeList nl = doc.getElementsByTagName("seat");
            for(int i=0; i<nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if(s.getSeatId().equals(XmlUtil.getElementValue(existing, "seatId"))) { doc.getDocumentElement().removeChild(existing); break; }
            }
            Element root = doc.getDocumentElement(); Element n = doc.createElement("seat");
            addChild(doc, n, "seatId", s.getSeatId());
            addChild(doc, n, "screenId", s.getScreenId());
            addChild(doc, n, "row", s.getRow());
            addChild(doc, n, "number", String.valueOf(s.getNumber()));
            addChild(doc, n, "category", s.getCategory());
            addChild(doc, n, "price", String.valueOf(s.getPrice()));
            addChild(doc, n, "status", s.getStatus());
            root.appendChild(n); XmlUtil.saveDocument(doc, "seats.xml");
        } catch(Exception e) { e.printStackTrace(); }
    }
}
