package com.eiga.dao;
import com.eiga.model.CheckIn;
import com.eiga.util.XmlUtil;
import org.w3c.dom.*;
import java.util.*;

public class CheckInDAO {
    private static final String FILE_NAME = "check_ins.xml";
    public List<CheckIn> findAll() {
        List<CheckIn> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("checkIn");
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nl.item(i);
                    CheckIn c = new CheckIn();
                    c.setCheckInId(XmlUtil.getElementValue(e, "checkInId"));
                    c.setBookingId(XmlUtil.getElementValue(e, "bookingId"));
                    c.setUserId(XmlUtil.getElementValue(e, "userId"));
                    c.setTheatreId(XmlUtil.getElementValue(e, "theatreId"));
                    c.setShowId(XmlUtil.getElementValue(e, "showId"));
                    c.setCheckedInAt(XmlUtil.getElementValue(e, "checkedInAt"));
                    c.setStaffId(XmlUtil.getElementValue(e, "staffId"));
                    c.setSource(XmlUtil.getElementValue(e, "source"));
                    c.setStatus(XmlUtil.getElementValue(e, "status"));
                    list.add(c);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name); child.setTextContent(value == null ? "" : value); parent.appendChild(child);
    }
    public void save(CheckIn c) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("checkIn");
            for (int i = 0; i < nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if (c.getCheckInId().equals(XmlUtil.getElementValue(existing, "checkInId"))) {
                    doc.getDocumentElement().removeChild(existing); break;
                }
            }
            Element root = doc.getDocumentElement();
            Element n = doc.createElement("checkIn");
            addChild(doc, n, "checkInId", c.getCheckInId());
            addChild(doc, n, "bookingId", c.getBookingId());
            addChild(doc, n, "userId", c.getUserId());
            addChild(doc, n, "theatreId", c.getTheatreId());
            addChild(doc, n, "showId", c.getShowId());
            addChild(doc, n, "checkedInAt", c.getCheckedInAt());
            addChild(doc, n, "staffId", c.getStaffId());
            addChild(doc, n, "source", c.getSource());
            addChild(doc, n, "status", c.getStatus());
            root.appendChild(n);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
