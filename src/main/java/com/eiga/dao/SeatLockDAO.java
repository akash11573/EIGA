package com.eiga.dao;
import com.eiga.model.SeatLock;
import com.eiga.util.XmlUtil;
import org.w3c.dom.*;
import java.time.LocalDateTime;
import java.util.*;

public class SeatLockDAO {
    private static final String FILE_NAME = "seat_locks.xml";
    public List<SeatLock> findAll() {
        List<SeatLock> list = new ArrayList<>();
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("seatLock");
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) nl.item(i);
                    SeatLock l = new SeatLock();
                    l.setLockId(XmlUtil.getElementValue(e, "lockId"));
                    l.setShowId(XmlUtil.getElementValue(e, "showId"));
                    l.setSeatId(XmlUtil.getElementValue(e, "seatId"));
                    l.setUserId(XmlUtil.getElementValue(e, "userId"));
                    l.setStatus(XmlUtil.getElementValue(e, "status"));
                    String la = XmlUtil.getElementValue(e, "lockedAt");
                    if (la != null && !la.isEmpty()) l.setLockedAt(LocalDateTime.parse(la));
                    String ea = XmlUtil.getElementValue(e, "expiresAt");
                    if (ea != null && !ea.isEmpty()) l.setExpiresAt(LocalDateTime.parse(ea));
                    list.add(l);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    private void addChild(Document doc, Element parent, String name, String value) {
        Element child = doc.createElement(name); child.setTextContent(value == null ? "" : value); parent.appendChild(child);
    }
    
    public void save(SeatLock l) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("seatLock");
            for (int i = 0; i < nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if (l.getLockId().equals(XmlUtil.getElementValue(existing, "lockId"))) {
                    doc.getDocumentElement().removeChild(existing); break;
                }
            }
            Element root = doc.getDocumentElement();
            Element n = doc.createElement("seatLock");
            addChild(doc, n, "lockId", l.getLockId());
            addChild(doc, n, "showId", l.getShowId());
            addChild(doc, n, "seatId", l.getSeatId());
            addChild(doc, n, "userId", l.getUserId());
            addChild(doc, n, "status", l.getStatus());
            addChild(doc, n, "lockedAt", l.getLockedAt() != null ? l.getLockedAt().toString() : "");
            addChild(doc, n, "expiresAt", l.getExpiresAt() != null ? l.getExpiresAt().toString() : "");
            root.appendChild(n);
            XmlUtil.saveDocument(doc, FILE_NAME);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    public void delete(String lockId) {
        try {
            Document doc = XmlUtil.loadDocument(FILE_NAME);
            NodeList nl = doc.getElementsByTagName("seatLock");
            for (int i = 0; i < nl.getLength(); i++) {
                Element existing = (Element) nl.item(i);
                if (lockId.equals(XmlUtil.getElementValue(existing, "lockId"))) {
                    doc.getDocumentElement().removeChild(existing); 
                    XmlUtil.saveDocument(doc, FILE_NAME);
                    break;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
