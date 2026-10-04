package com.eiga.dao;
import com.eiga.model.Show;
import com.eiga.util.XmlUtil;
import org.w3c.dom.*;
import java.time.*;
import java.util.*;
public class ShowDAO {
    public List<Show> findAll() {
        List<Show> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument("shows.xml");
            NodeList nl = doc.getElementsByTagName("show");
            for(int i=0; i<nl.getLength(); i++) {
                if(nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nl.item(i);
                    Show s = new Show();
                    s.setShowId(XmlUtil.getElementValue(e, "showId"));
                    s.setMovieId(XmlUtil.getElementValue(e, "movieId"));
                    s.setTheatreId(XmlUtil.getElementValue(e, "theatreId"));
                    s.setScreenId(XmlUtil.getElementValue(e, "screenId"));
                    s.setStatus(XmlUtil.getElementValue(e, "status"));
                    String sd = XmlUtil.getElementValue(e, "showDate");
                    if(sd != null && !sd.isEmpty() && !sd.equals("null")) s.setShowDate(LocalDate.parse(sd));
                    String st = XmlUtil.getElementValue(e, "startTime");
                    if(st != null && !st.isEmpty() && !st.equals("null")) s.setStartTime(LocalTime.parse(st));
                    String et = XmlUtil.getElementValue(e, "endTime");
                    if(et != null && !et.isEmpty() && !et.equals("null")) s.setEndTime(LocalTime.parse(et));
                    list.add(s);
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
        return list;
    }
    public Show findById(String id) {
        for(Show s : findAll()) if(id.equals(s.getShowId())) return s;
        return null;
    }
    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name); child.setTextContent(value == null ? "" : value); parent.appendChild(child);
    }
    public void save(Show s) {
        try {
            Document doc = XmlUtil.loadDocument("shows.xml");
            NodeList nl = doc.getElementsByTagName("show");
            for(int i=0; i<nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if(s.getShowId().equals(XmlUtil.getElementValue(existing, "showId"))) { doc.getDocumentElement().removeChild(existing); break; }
            }
            Element root = doc.getDocumentElement(); Element n = doc.createElement("show");
            addChild(doc, n, "showId", s.getShowId());
            addChild(doc, n, "movieId", s.getMovieId());
            addChild(doc, n, "theatreId", s.getTheatreId());
            addChild(doc, n, "screenId", s.getScreenId());
            addChild(doc, n, "showDate", s.getShowDate() != null ? s.getShowDate().toString() : "");
            addChild(doc, n, "startTime", s.getStartTime() != null ? s.getStartTime().toString() : "");
            addChild(doc, n, "endTime", s.getEndTime() != null ? s.getEndTime().toString() : "");
            addChild(doc, n, "status", s.getStatus());
            root.appendChild(n); XmlUtil.saveDocument(doc, "shows.xml");
        } catch(Exception e) { e.printStackTrace(); }
    }
}
