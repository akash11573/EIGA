package com.eiga.dao;
import com.eiga.model.Screen;
import com.eiga.util.XmlUtil;
import org.w3c.dom.*;
import java.util.*;
public class ScreenDAO {
    public List<Screen> findAll() {
        List<Screen> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument("screens.xml");
            NodeList nl = doc.getElementsByTagName("screen");
            for(int i=0; i<nl.getLength(); i++) {
                if(nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nl.item(i);
                    Screen s = new Screen();
                    s.setScreenId(XmlUtil.getElementValue(e, "screenId"));
                    s.setTheatreId(XmlUtil.getElementValue(e, "theatreId"));
                    s.setName(XmlUtil.getElementValue(e, "name"));
                    s.setFormat(XmlUtil.getElementValue(e, "format"));
                    s.setProjection(XmlUtil.getElementValue(e, "projection"));
                    s.setSoundSystem(XmlUtil.getElementValue(e, "soundSystem"));
                    s.setStatus(XmlUtil.getElementValue(e, "status"));
                    String cap = XmlUtil.getElementValue(e, "capacity");
                    if(cap != null && !cap.isEmpty() && !cap.equals("null")) s.setCapacity(Integer.parseInt(cap));
                    list.add(s);
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return list;
    }
    public Screen findById(String id) {
        for(Screen s : findAll()) if(id.equals(s.getScreenId())) return s;
        return null;
    }
    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name); child.setTextContent(value == null ? "" : value); parent.appendChild(child);
    }
    public void save(Screen s) {
        try {
            Document doc = XmlUtil.loadDocument("screens.xml");
            NodeList nl = doc.getElementsByTagName("screen");
            for(int i=0; i<nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if(s.getScreenId().equals(XmlUtil.getElementValue(existing, "screenId"))) { doc.getDocumentElement().removeChild(existing); break; }
            }
            Element root = doc.getDocumentElement(); Element n = doc.createElement("screen");
            addChild(doc, n, "screenId", s.getScreenId());
            addChild(doc, n, "theatreId", s.getTheatreId());
            addChild(doc, n, "name", s.getName());
            addChild(doc, n, "capacity", String.valueOf(s.getCapacity()));
            addChild(doc, n, "format", s.getFormat());
            addChild(doc, n, "projection", s.getProjection());
            addChild(doc, n, "soundSystem", s.getSoundSystem());
            addChild(doc, n, "status", s.getStatus());
            root.appendChild(n); XmlUtil.saveDocument(doc, "screens.xml");
        } catch(Exception e) { e.printStackTrace(); }
    }
}
