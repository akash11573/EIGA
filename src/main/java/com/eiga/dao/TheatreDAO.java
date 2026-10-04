package com.eiga.dao;
import com.eiga.model.Theatre;
import com.eiga.util.XmlUtil;
import org.w3c.dom.*;
import java.util.*;
public class TheatreDAO {
    public List<Theatre> findAll() {
        List<Theatre> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument("theatres.xml");
            NodeList nl = doc.getElementsByTagName("theatre");
            for(int i=0; i<nl.getLength(); i++) {
                if(nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nl.item(i);
                    Theatre t = new Theatre();
                    t.setTheatreId(XmlUtil.getElementValue(e, "theatreId"));
                    t.setName(XmlUtil.getElementValue(e, "name"));
                    t.setCity(XmlUtil.getElementValue(e, "city"));
                    t.setAddress(XmlUtil.getElementValue(e, "address"));
                    t.setStatus(XmlUtil.getElementValue(e, "status"));
                    list.add(t);
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return list;
    }
    public Theatre findById(String id) {
        for(Theatre t : findAll()) if(id.equals(t.getTheatreId())) return t;
        return null;
    }
    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name); child.setTextContent(value == null ? "" : value); parent.appendChild(child);
    }
    public void save(Theatre t) {
        try {
            Document doc = XmlUtil.loadDocument("theatres.xml");
            NodeList nl = doc.getElementsByTagName("theatre");
            for(int i=0; i<nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if(t.getTheatreId().equals(XmlUtil.getElementValue(existing, "theatreId"))) { doc.getDocumentElement().removeChild(existing); break; }
            }
            Element root = doc.getDocumentElement(); Element n = doc.createElement("theatre");
            addChild(doc, n, "theatreId", t.getTheatreId());
            addChild(doc, n, "name", t.getName());
            addChild(doc, n, "city", t.getCity());
            addChild(doc, n, "address", t.getAddress());
            addChild(doc, n, "status", t.getStatus());
            root.appendChild(n); XmlUtil.saveDocument(doc, "theatres.xml");
        } catch(Exception e) { e.printStackTrace(); }
    }
}
